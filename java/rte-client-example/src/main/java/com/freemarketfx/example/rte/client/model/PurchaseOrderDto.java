package com.freemarketfx.example.rte.client.model;

public class PurchaseOrderDto {

	private int id;

	private String status;

	private String statusNarrative;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusNarrative() {
		return statusNarrative;
	}

	public void setStatusNarrative(String statusNarrative) {
		this.statusNarrative = statusNarrative;
	}
}