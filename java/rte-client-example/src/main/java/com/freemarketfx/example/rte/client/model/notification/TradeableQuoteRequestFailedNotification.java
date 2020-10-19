package com.freemarketfx.example.rte.client.model.notification;

public class TradeableQuoteRequestFailedNotification {

	private int tradeableQuoteRequestId;

	private String narrative;

	public int getTradeableQuoteRequestId() {
		return tradeableQuoteRequestId;
	}

	public void setTradeableQuoteRequestId(int tradeableQuoteRequestId) {
		this.tradeableQuoteRequestId = tradeableQuoteRequestId;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}
}