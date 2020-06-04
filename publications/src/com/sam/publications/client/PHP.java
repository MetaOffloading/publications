package com.sam.publications.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public class PHP {
	static String phpOutput = null;

	public static String Call(String url, final boolean registerResponse) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Window.alert("Could not connect to server");
				}

				public void onResponseReceived(Request request, Response response) {
					if (registerResponse) {
						phpOutput = response.getText();
					}
				}
			});
		} catch (RequestException e) {
			Window.alert("no connection to server");
		}

		return (phpOutput);
	}
}