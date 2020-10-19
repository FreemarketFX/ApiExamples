package com.freemarketfx.example.rte.client.model.notification;

public class MarketDataQuoteSubscriptionFailedNotification {

	private int marketDataQuoteSubscriptionId;

	private String narrative;

	public int getMarketDataQuoteSubscriptionId() {
		return marketDataQuoteSubscriptionId;
	}

	public void setMarketDataQuoteSubscriptionId(int marketDataQuoteSubscriptionId) {
		this.marketDataQuoteSubscriptionId = marketDataQuoteSubscriptionId;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}
}