package com.freemarketfx.example.rte.client.model.notification;

import java.math.BigDecimal;
import java.util.Date;

public class MarketDataQuoteReceivedNotification {

	private int marketDataQuoteSubscriptionId;

	private String sourceCurrencyCode;
	private String targetCurrencyCode;

	private BigDecimal forwardRate;
	private BigDecimal reverseRate;

	private Date timeStamp;

	public int getMarketDataQuoteSubscriptionId() {
		return marketDataQuoteSubscriptionId;
	}

	public void setMarketDataQuoteSubscriptionId(int marketDataQuoteSubscriptionId) {
		this.marketDataQuoteSubscriptionId = marketDataQuoteSubscriptionId;
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

	public BigDecimal getForwardRate() {
		return forwardRate;
	}

	public void setForwardRate(BigDecimal forwardRate) {
		this.forwardRate = forwardRate;
	}

	public BigDecimal getReverseRate() {
		return reverseRate;
	}

	public void setReverseRate(BigDecimal reverseRate) {
		this.reverseRate = reverseRate;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
}