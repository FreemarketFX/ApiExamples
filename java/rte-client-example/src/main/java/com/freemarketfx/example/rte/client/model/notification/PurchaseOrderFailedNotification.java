package com.freemarketfx.example.rte.client.model.notification;

public class PurchaseOrderFailedNotification {

	private int purchaseOrderId;

	private String narrative;

	public int getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(int purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}
}