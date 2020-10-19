package com.freemarketfx.example.rte.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.signalr.Action1;
import com.microsoft.signalr.HubConnection;

public final class HubNotificationHelper {

	private static Gson gson = GsonFactory.newDefaultGson();

	private HubNotificationHelper() {
	}

	public static <T> void on(HubConnection hubConnection, Action1<T> callback, Class<T> notificationClass) {
		String target = notificationClass.getSimpleName().replaceFirst("Notification$", "");
		HubNotificationHelper.on(hubConnection, target, callback, notificationClass);
	}

	public static <T> void on(HubConnection hubConnection, String target, Action1<T> callback, Class<T> notificationClass) {
		hubConnection.on(target, (param) -> {
			T notification = gson.fromJson(param.get("body"), notificationClass);
			callback.invoke(notification);
		}, JsonObject.class);
	}
}
