package com.freemarketfx.example.rte.client;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class GsonFactory {

	private GsonFactory() {
	}

	public static Gson newDefaultGson() {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	}

	public static Gson newApiV1Gson() {
		return new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	}
}
