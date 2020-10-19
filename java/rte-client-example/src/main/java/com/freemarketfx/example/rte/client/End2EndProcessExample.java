package com.freemarketfx.example.rte.client;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import com.freemarketfx.example.rte.client.model.ExchangeDto;
import com.freemarketfx.example.rte.client.model.MarketDataQuoteSubscriptionDto;
import com.freemarketfx.example.rte.client.model.PurchaseOrderDto;
import com.freemarketfx.example.rte.client.model.TradeableQuoteRequestDto;
import com.freemarketfx.example.rte.client.model.command.CreateMarketDataQuoteSubscriptionRequest;
import com.freemarketfx.example.rte.client.model.command.CreatePurchaseOrderFromTradeableQuoteRequest;
import com.freemarketfx.example.rte.client.model.command.CreateRealTimeExchangeRequest;
import com.freemarketfx.example.rte.client.model.command.CreateTradeableQuoteRequestForExchangeRequest;
import com.freemarketfx.example.rte.client.model.notification.MarketDataQuoteReceivedNotification;
import com.freemarketfx.example.rte.client.model.notification.MarketDataQuoteSubscriptionExpiredNotification;
import com.freemarketfx.example.rte.client.model.notification.MarketDataQuoteSubscriptionFailedNotification;
import com.freemarketfx.example.rte.client.model.notification.PurchaseOrderCompletedNotification;
import com.freemarketfx.example.rte.client.model.notification.PurchaseOrderFailedNotification;
import com.freemarketfx.example.rte.client.model.notification.TradeableQuoteReceivedNotification;
import com.freemarketfx.example.rte.client.model.notification.TradeableQuoteRequestExpiredNotification;
import com.freemarketfx.example.rte.client.model.notification.TradeableQuoteRequestFailedNotification;
import com.freemarketfx.example.rte.client.model.query.GetBusinessHoursResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import io.reactivex.Single;
import okhttp3.Protocol;

public class End2EndProcessExample {

	private String identityBaseUrl;
	private String apiV1BaseUrl;
	private String apiV2BaseUrl;

	private String clientId;
	private String clientSecret;
	private String username;
	private String password;
	private int accountId;

	private HubConnection hubConnection;
	private Gson gson = GsonFactory.newDefaultGson();
	private Gson apiV1Gson = GsonFactory.newApiV1Gson();
	private String accessToken;

	public static void main(String[] args) throws Exception {
		new End2EndProcessExample().run(args);
	}

	public void run(String[] args) throws Exception {

		if (args.length < 5) {
			System.out.println("Usage: End2EndProcessExample clientId clientSecret username password accountId");
			System.exit(1);
		}

		clientId = args[0];
		clientSecret = args[1];
		username = args[2];
		password = args[3];
		accountId = Integer.parseInt(args[4]);

		identityBaseUrl = System.getProperty("freemarket.identityBaseUrl", "https://identity-sandbox.wearefreemarket.com");
		apiV1BaseUrl = System.getProperty("freemarket.apiV1BaseUrl", "https://sandbox.wearefreemarket.com/api");
		apiV2BaseUrl = System.getProperty("freemarket.apiV2BaseUrl", "https://api-sandbox.wearefreemarket.com");

		try {

			initAccessToken();

			checkBusinessHours();

			initHubConnection();

			testMarketData();

			testRealTimeExchange();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			cleanup();
		}
	}

	private void initAccessToken() throws Exception {

		System.out.println("Getting access token");

		JsonObject json = gson.fromJson(
			Request.post(identityBaseUrl + "/connect/token")
				.bodyForm(
					new BasicNameValuePair("grant_type", "password"),
					new BasicNameValuePair("scope", "freemarketFXAPI"),
					new BasicNameValuePair("client_id", clientId),
					new BasicNameValuePair("client_secret", clientSecret),
					new BasicNameValuePair("username", username),
					new BasicNameValuePair("password", password))
				.execute()
				.returnContent().asString(), 
			JsonObject.class);

		accessToken = json.get("access_token").getAsString();
	}

	private void checkBusinessHours() throws Exception {

		System.out.println("Checking business hours");

		GetBusinessHoursResponse response = gson.fromJson(
			Request.get(apiV2BaseUrl + "/v2/BusinessHours")
				.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.execute()
				.returnContent().asString(), 
			GetBusinessHoursResponse.class);

		if (!response.isBusinessHour()) {

			System.out.println("It's not business hours");

			System.exit(1);
		}
	}

	private void initHubConnection() throws Exception {

		System.out.println("Connecting to SignalR");

		hubConnection = HubConnectionBuilder
			.create(apiV2BaseUrl + "/v2/RealTimeFXHub")
			// Use an existing access token
			.withAccessTokenProvider(Single.just(accessToken))
			.setHttpClientBuilderCallback((builder) -> {
				// OkHttp defaults to HTTP 2.0 which on the default included 3.x version uses non daemon threads that are
				// not cleaned up on hub shutdown causing the app to keep running after main finishes running. For details,
				// check out: https://github.com/square/okhttp/issues/4029
				builder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
			})
			.build();

		hubConnection.start().blockingAwait();
	}

	private void testMarketData() throws Exception {

		CountDownLatch latch = new CountDownLatch(3);

		// Register listeners

		HubNotificationHelper.on(hubConnection, (notification) -> {

			System.out.println(String.format("Market data subscription failed: %s", notification.getNarrative()));

			System.exit(1);

		}, MarketDataQuoteSubscriptionFailedNotification.class);

		HubNotificationHelper.on(hubConnection, (notification) -> {

			System.out.println("Market data subscription expired");

			System.exit(1);

		}, MarketDataQuoteSubscriptionExpiredNotification.class);

		HubNotificationHelper.on(hubConnection, (notification) -> {

			System.out.println(String.format("Market data %s/%s: forward: %s, reverse: %s",
				notification.getSourceCurrencyCode(), notification.getTargetCurrencyCode(),
				notification.getForwardRate(), notification.getReverseRate()));

			latch.countDown();

		}, MarketDataQuoteReceivedNotification.class);

		// Create a market data subscription

		System.out.println("Creating market data subscription");

		MarketDataQuoteSubscriptionDto subscription = new HubInvocationHelper(hubConnection)
			.invoke(MarketDataQuoteSubscriptionDto.class, new CreateMarketDataQuoteSubscriptionRequest(accountId, "GBP", "EUR"));

		System.out.println(String.format("Market data subscription created: %s", subscription.getId()));

		// Wait for a couple of quotes

		latch.await();

		// Cancel the subscription

		System.out.println("Cancelling market data subscription");

		new HubInvocationHelper(hubConnection)
			.invoke("CancelMarketDataQuoteSubscription", subscription.getId());

		System.out.println("Market data subscription cancelled");
	}

	private void testRealTimeExchange() throws Exception {

		// Create a new real time exchange

		System.out.println("Creating a real time exchange");

		ExchangeDto exchange = apiV1Gson.fromJson(
			Request.post(apiV1BaseUrl + "/v1/Exchanges/CreateRealTime")
				.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.bodyString(apiV1Gson.toJson(
					new CreateRealTimeExchangeRequest(accountId, "GBP", "EUR", new BigDecimal(123.00), "Buy")),
					ContentType.APPLICATION_JSON)
				.execute()
				.returnContent().asString(), 
			ExchangeDto.class);

		System.out.println(String.format("RTE exchange created: %s", exchange.getId()));

		AtomicReference<Integer> quoteId = new AtomicReference<Integer>();
		CountDownLatch quoteLatch = new CountDownLatch(3);

		// Register tradeable quote listeners

		HubNotificationHelper.on(hubConnection, (notification) -> {

			System.out.println(String.format("Tradeable quote request failed: %s", notification.getNarrative()));

			System.exit(1);

		}, TradeableQuoteRequestFailedNotification.class);

		HubNotificationHelper.on(hubConnection, (notification) -> {

			System.out.println("Tradeable quote request expired");

			System.exit(1);

		}, TradeableQuoteRequestExpiredNotification.class);

		HubNotificationHelper.on(hubConnection, (notification) -> {

			System.out.println(String.format("Tradeable quote %s/%s: rate: %s, send: %s, receive: %s",
				notification.getSourceCurrencyCode(), notification.getTargetCurrencyCode(),
				notification.getRate(), notification.getSend(), notification.getReceive()));

			quoteId.set(notification.getTradeableQuoteId());
			quoteLatch.countDown();

		}, TradeableQuoteReceivedNotification.class);

		// Create a tradeable quote request

		System.out.println("Creating tradeable quote request");

		TradeableQuoteRequestDto quoteRequest = new HubInvocationHelper(hubConnection)
			.invoke(TradeableQuoteRequestDto.class, new CreateTradeableQuoteRequestForExchangeRequest(exchange.getId()));

		System.out.println(String.format("Tradeable quote request created: %s", quoteRequest.getId()));

		// Wait for a couple of quotes

		quoteLatch.await();

		CountDownLatch orderLatch = new CountDownLatch(1);

		// Register purchase order listeners

		HubNotificationHelper.on(hubConnection, (notification) -> {

			System.out.println(String.format("Purchase order failed: %s", notification.getNarrative()));

			System.exit(1);

		}, PurchaseOrderFailedNotification.class);

		HubNotificationHelper.on(hubConnection, (notification) -> {

			System.out.println("Purchase order completed");

			orderLatch.countDown();

		}, PurchaseOrderCompletedNotification.class);

		// Create a purchase order from the last received quote

		System.out.println("Creating purchase order");

		PurchaseOrderDto order = new HubInvocationHelper(hubConnection)
			.invoke(PurchaseOrderDto.class, new CreatePurchaseOrderFromTradeableQuoteRequest(quoteId.get()));

		System.out.println(String.format("Purchase order created: %s, status: %s", order.getId(), order.getStatus()));

		// Wait for completion

		orderLatch.await();

		// Check the order status

		order = gson.fromJson(
			Request.get(apiV2BaseUrl + "/v2/PurchaseOrders/" + order.getId())
				.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.execute()
				.returnContent().asString(),
			PurchaseOrderDto.class);

		System.out.println(String.format("Purchase order status: %s", order.getStatus()));
	}

	private void cleanup() {

		System.out.println("Cleaning up");

		try {
			if (hubConnection != null) {
				hubConnection.close();
			}
		} catch (Exception e) {
		}
	}
}
