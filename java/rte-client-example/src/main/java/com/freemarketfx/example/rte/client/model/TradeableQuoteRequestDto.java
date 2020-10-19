package com.freemarketfx.example.rte.client.model;

import java.util.Date;

public class TradeableQuoteRequestDto {

	private int id;

	private Date expireDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
}