package com.freemarketfx.example.rte.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubException;

public class HubInvocationHelper {

	private Gson gson = GsonFactory.newDefaultGson();
	private HubConnection hubConnection;

	public HubInvocationHelper(HubConnection hubConnection) {
		this.hubConnection = hubConnection;
	}

	public <T> T invoke(Class<T> responseClass, Object request) {
		String method = request.getClass().getSimpleName().replaceFirst("Request$", "");
		return invoke(responseClass, method, request);
	}

	public <T> T invoke(Class<T> responseClass, String method, Object request) {

		JsonObject argument = new JsonObject();
		argument.add("body", gson.toJsonTree(request));

		try {

			JsonObject invocationReponse = hubConnection.invoke(JsonObject.class, method, argument).blockingGet();

			ensureSuccessStatus(invocationReponse);

			T response = gson.fromJson(invocationReponse.get("body"), responseClass);
			return response;

		} catch (HubException e) {
			throw new HubInvocationException("Error invoking hub method", e);
		}
	}

	public void invoke(String method, Object request) {

		JsonObject argument = new JsonObject();
		argument.add("body", gson.toJsonTree(request));

		try {

			JsonObject invocationReponse = hubConnection.invoke(JsonObject.class, method, argument).blockingGet();
			ensureSuccessStatus(invocationReponse);

		} catch (HubException e) {
			throw new HubInvocationException("Error invoking hub method", e);
		}
	}

	private void ensureSuccessStatus(JsonObject invocationReponse) {

		int status = invocationReponse.get("status").getAsInt();

		if (200 <= status && status < 300) {
			return;
		}

		throw new HubInvocationException("Unsuccessfull status: " + status + ", body: " + invocationReponse.get("body"));
	}
}
