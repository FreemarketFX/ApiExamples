package com.freemarketfx.example.rte.client;

public class HubInvocationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HubInvocationException(String message) {
		super(message);
	}

	public HubInvocationException(String message, Throwable cause) {
		super(message, cause);
	}
}
