package com.freemarketfx.example.rte.client.model.command;

public class CreateTradeableQuoteRequestForExchangeRequest {

	private int exchangeId;

	public CreateTradeableQuoteRequestForExchangeRequest() {
	}

	public CreateTradeableQuoteRequestForExchangeRequest(int exchangeId) {
		this.exchangeId = exchangeId;
	}

	public int getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
	}
}