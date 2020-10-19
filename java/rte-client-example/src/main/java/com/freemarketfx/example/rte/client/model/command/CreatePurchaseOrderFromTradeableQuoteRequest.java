package com.freemarketfx.example.rte.client.model.command;

public class CreatePurchaseOrderFromTradeableQuoteRequest {

	private int tradeableQuoteId;

	public CreatePurchaseOrderFromTradeableQuoteRequest() {
	}

	public CreatePurchaseOrderFromTradeableQuoteRequest(int tradeableQuoteId) {
		this.tradeableQuoteId = tradeableQuoteId;
	}

	public int getTradeableQuoteId() {
		return tradeableQuoteId;
	}

	public void setTradeableQuoteId(int tradeableQuoteId) {
		this.tradeableQuoteId = tradeableQuoteId;
	}
}