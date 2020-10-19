package com.freemarketfx.example.rte.client.model.query;

import java.util.Date;

public class GetBusinessHoursResponse {

	private boolean isBusinessHour;

	private String closingIn;
	private Date closingAt;

	private String openingIn;
	private Date openingAt;

	public boolean isBusinessHour() {
		return isBusinessHour;
	}

	public void setBusinessHour(boolean isBusinessHour) {
		this.isBusinessHour = isBusinessHour;
	}

	public String getClosingIn() {
		return closingIn;
	}

	public void setClosingIn(String closingIn) {
		this.closingIn = closingIn;
	}

	public Date getClosingAt() {
		return closingAt;
	}

	public void setClosingAt(Date closingAt) {
		this.closingAt = closingAt;
	}

	public String getOpeningIn() {
		return openingIn;
	}

	public void setOpeningIn(String openingIn) {
		this.openingIn = openingIn;
	}

	public Date getOpeningAt() {
		return openingAt;
	}

	public void setOpeningAt(Date openingAt) {
		this.openingAt = openingAt;
	}
}
