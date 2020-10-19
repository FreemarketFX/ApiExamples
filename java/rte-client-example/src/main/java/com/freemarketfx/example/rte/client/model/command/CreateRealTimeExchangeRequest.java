package com.freemarketfx.example.rte.client.model.command;

import java.math.BigDecimal;

public class CreateRealTimeExchangeRequest {

	private int accountId;

	private String sourceCurrencyCode;

	private String targetCurrencyCode;

	private BigDecimal goal;

	private String goalType;

	public CreateRealTimeExchangeRequest() {
	}

	public CreateRealTimeExchangeRequest(int accountId, String sourceCurrencyCode, String targetCurrencyCode,
			BigDecimal goal, String goalType) {
		this.accountId = accountId;
		this.sourceCurrencyCode = sourceCurrencyCode;
		this.targetCurrencyCode = targetCurrencyCode;
		this.goal = goal;
		this.goalType = goalType;
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

	public BigDecimal getGoal() {
		return goal;
	}

	public void setGoal(BigDecimal goal) {
		this.goal = goal;
	}

	public String getGoalType() {
		return goalType;
	}

	public void setGoalType(String goalType) {
		this.goalType = goalType;
	}
}