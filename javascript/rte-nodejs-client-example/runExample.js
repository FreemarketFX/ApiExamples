const fetch = require('node-fetch');
const signalR = require('@microsoft/signalr');
const config = require('./config.json');

(async () => {
    await runExample();
})().catch(e => {
   console.error(e);
});

async function runExample(){

    const accessToken = await getAccessToken();

    const isBusinessHoursRepsonse = await isBusinessHours(accessToken);

    if (!isBusinessHoursRepsonse.isBusinessHour) {
        console.error(`It's not business hours`);
    }

    const hubConnection = await initHubConnection(accessToken);

    await requestMarketData(hubConnection);

    await createRealTimeExchange(hubConnection, accessToken);
}

async function getAccessToken() {

    console.log('Getting access token');

    const response = await fetch(`${config.identityProviderBaseUrl}/connect/token`, {
         method: 'POST', 
         body: 'grant_type=password'+
            '&scope=freemarketFXAPI'+
            `&client_id=${config.clientId}`+
            `&client_secret=${config.clientSecret}`+ 
            `&username=${config.username}`+
            `&password=${config.password}`, 
         headers: {'Content-Type': 'application/x-www-form-urlencoded'} 
        });

    return (await response.json()).access_token;
}

async function isBusinessHours(accessToken) {

    console.log('Checking business hours');

    const response = await fetch(`${config.apiV2BaseUrl}/v2/BusinessHours`, {
         headers: {'Authorization': `Bearer ${accessToken}`} 
        });


    return await response.json();
}

async function initHubConnection(accessToken) {

    console.log("Connecting to SignalR");

    const hubConnection = new signalR.HubConnectionBuilder()
    .withUrl(`${config.apiV2BaseUrl}/v2/RealTimeFXHub`, { accessTokenFactory: () => accessToken })
    .build();

    await hubConnection.start()

    return hubConnection;
}

async function requestMarketData(hubConnection){

    // Register listeners

    hubConnection.on("MarketDataQuoteSubscriptionFailed", (notification) => {
        console.error(`Market data subscription failed: ${notification.Narrative}`);
        process.exit();
    });

    hubConnection.on("MarketDataQuoteSubscriptionExpired", () => {
        console.error(`Market data subscription expired`);
        process.exit();
    });

    hubConnection.on("MarketDataQuoteReceived", (notification) => {
        console.log(`Market data ${notification.body.sourceCurrencyCode}/${notification.body.targetCurrencyCode}`+
        `: forward: ${notification.body.forwardRate}, reverse: ${notification.body.reverseRate}`);
    });

    // Create a market data subscription

    console.log("Creating market data subscription");

    const marketDataQuoteSubscription = await hubConnection.invoke.apply(hubConnection, ["CreateMarketDataQuoteSubscription",
	{
		body: {
			"AccountId": config.accountId,
	        "SourceCurrencyCode": "GBP",
            "TargetCurrencyCode": "EUR"
	    }
	}])

    const subscriptionId = marketDataQuoteSubscription.body.id;
    console.log(`Market data subscription created: ${subscriptionId}`);

    // Wait for a couple of quotes and than cancel the subscribtion

    return new Promise((resolve, reject) =>{
        setTimeout(async()=>{

            // Cancel the subscription
            console.log('Cancelling market data subscription');
    
            await hubConnection.invoke.apply(hubConnection, ["CancelMarketDataQuoteSubscription", {
                body: subscriptionId
            }]);
    
            console.log('Market data subscription cancelled');
    
            resolve();
    
        }, 10000);
    }); 
}

async function createRealTimeExchange(hubConnection, accessToken){

    // Create a new real time exchange

    console.log('Creating a real time exchange');

    const response = await fetch(`${config.apiV1BaseUrl}/v1/Exchanges/CreateRealTime`, {
        method: 'POST', 
        body: `accountId=${config.accountId}`+
           `&sourceCurrencyCode=GBP`+ 
           `&targetCurrencyCode=EUR`+
           `&goal=123`+
           `&goalType=Buy`,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': `Bearer ${accessToken}`
        } 
       });

    const exchange = await response.json();

    if(!exchange.Id){
        console.error(`Creating a real time exchange failed: ${JSON.stringify(exchange)}`);
        process.exit();
    }

    console.log(`RTE exchange created: ${exchange.Id}`);

    let quoteId = 0;

    // Register tradeable quote listeners

    hubConnection.on("TradeableQuoteRequestFailed", (notification) => {
        console.error(`Tradeable quote request failed: ${notification.narrative}`);
        process.exit();
    });

    hubConnection.on("TradeableQuoteRequestExpired", (notification) => {
        console.error(`Tradeable quote request expired`);
        process.exit();
    });

    hubConnection.on("TradeableQuoteReceived", (notification) => {

        const quote = notification.body;
        console.info(`Tradeable quote ${quote.sourceCurrencyCode}/${quote.targetCurrencyCode}:`+ 
        `rate: ${quote.rate}, send: ${quote.send}, receive: ${quote.receive}`);

        quoteId = quote.tradeableQuoteId;

        if(!quoteId){
            console.error(`Error in receving tradeable quote: ${JSON.stringify(notification)}`);
            process.exit();
        }
    });

    // Register purchase order listeners

    hubConnection.on("PurchaseOrderFailed", (notification) => {
        console.error(`Purchase order failed: ${notification.narrative}`);
        process.exit();
    });

    hubConnection.on("PurchaseOrderCompleted", (notification) => {
        console.log(`Purchase order completed`);
        process.exit();
    });

    // Create a tradeable quote request

    console.info("Creating tradeable quote request");

    const tradeableQuote = await hubConnection.invoke.apply(hubConnection, [
        "CreateTradeableQuoteRequestForExchange", 
        {
            body: { 
                ExchangeId:  exchange.Id 
            } 
        }
    ]);

    console.info(`Tradeable quote request created: ${tradeableQuote.body.id}`);

    // Wait for a couple of quotes

    return new Promise((resolve, reject) =>{

        setTimeout(async()=>{

            // Create a purchase order from the last received quote

            console.log("Creating purchase order");

            const order = await hubConnection.invoke.apply(hubConnection, [
                "CreatePurchaseOrderFromTradeableQuote", { 
                    body:{
                        tradeableQuoteId: quoteId
                    }
                }
            ]);

            if(!order.body.id){
                console.error(`Error in CreatePurchaseOrderFromTradeableQuote: ${JSON.stringify(order)}`);
                process.exit();
            }

            console.log(`Purchase order created: ${order.body.id}, status: ${order.body.status}`);

            // Wait for completion
            setTimeout(async()=>{

                // Check the order status

                const resp = await fetch(`${config.apiV2BaseUrl}/v2/PurchaseOrders/${order.body.id}`, {
                    headers: {
                        'Authorization': `Bearer ${accessToken}`
                    } 
                });
            
                const purchaseOrder = await resp.json();

                if(!purchaseOrder.status){
                    console.error(`Error in checking the order status: ${JSON.stringify(purchaseOrder)}`);
                    process.exit();
                }

                console.log(`Purchase order status: ${purchaseOrder.status}`);

                resolve();
            }, 10000);

        }, 10000);
    });
}
