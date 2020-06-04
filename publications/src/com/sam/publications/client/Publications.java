package com.sam.publications.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Publications implements EntryPoint {
	public void onModuleLoad() {
		PHP.Call("getPublicationList.php", true);
		
		new Timer() {
			public void run() {
				if (PHP.phpOutput != null) {
					cancel();
					GeneratePublicationList.Run();
				}
			}
		}.scheduleRepeating(100);
	}
}
