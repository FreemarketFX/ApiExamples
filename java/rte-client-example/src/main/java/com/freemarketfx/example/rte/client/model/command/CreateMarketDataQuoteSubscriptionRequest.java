package com.freemarketfx.example.rte.client.model.command;

public class CreateMarketDataQuoteSubscriptionRequest {

	private int accountId;

	private String sourceCurrencyCode;

	private String targetCurrencyCode;

	public CreateMarketDataQuoteSubscriptionRequest() {
	}

	public CreateMarketDataQuoteSubscriptionRequest(int accountId, String sourceCurrencyCode,
			String targetCurrencyCode) {
		this.accountId = accountId;
		this.sourceCurrencyCode = sourceCurrencyCode;
		this.targetCurrencyCode = targetCurrencyCode;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getSourceCurrencyCode() {
		return sourceCurrencyCode;
	}

	public void setSourceCurrencyCode(String sourceCurrencyCode) {
		this.sourceCurrencyCode = sourceCurrencyCode;
	}

	public String getTargetCurrencyCode() {
		return targetCurrencyCode;
	}

	public void setTargetCurrencyCode(String targetCurrencyCode) {
		this.targetCurrencyCode = targetCurrencyCode;
	}
}