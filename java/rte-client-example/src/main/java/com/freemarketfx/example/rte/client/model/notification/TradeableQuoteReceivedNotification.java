package com.freemarketfx.example.rte.client.model.notification;

import java.math.BigDecimal;
import java.util.Date;

public class TradeableQuoteReceivedNotification {

	private int tradeableQuoteId;
	private int tradeableQuoteRequestId;

	private String sourceCurrencyCode;
	private String targetCurrencyCode;

	private BigDecimal rate;

	private BigDecimal send;
	private BigDecimal receive;
	private BigDecimal sourceFees;
	private BigDecimal targetFees;

	private Date validUntil;

	private Date createdDate;

	public int getTradeableQuoteId() {
		return tradeableQuoteId;
	}

	public void setTradeableQuoteId(int tradeableQuoteId) {
		this.tradeableQuoteId = tradeableQuoteId;
	}

	public int getTradeableQuoteRequestId() {
		return tradeableQuoteRequestId;
	}

	public void setTradeableQuoteRequestId(int tradeableQuoteRequestId) {
		this.tradeableQuoteRequestId = tradeableQuoteRequestId;
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

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getSend() {
		return send;
	}

	public void setSend(BigDecimal send) {
		this.send = send;
	}

	public BigDecimal getReceive() {
		return receive;
	}

	public void setReceive(BigDecimal receive) {
		this.receive = receive;
	}

	public BigDecimal getSourceFees() {
		return sourceFees;
	}

	public void setSourceFees(BigDecimal sourceFees) {
		this.sourceFees = sourceFees;
	}

	public BigDecimal getTargetFees() {
		return targetFees;
	}

	public void setTargetFees(BigDecimal targetFees) {
		this.targetFees = targetFees;
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}