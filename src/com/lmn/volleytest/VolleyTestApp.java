package com.lmn.volleytest;

import android.app.Application;

import com.lmn.volleytest.util.image.ImageLoaderManager;
import com.lmn.volleytest.util.request.RequestManager;

/**
 * Main entry point of the app.
 * 
 * @author Lucas Nobile
 */
public class VolleyTestApp extends Application {

	public static final String TAG = VolleyTestApp.class.getSimpleName();

	// Handle requests
	public static RequestManager REQUEST_MANAGER;

	// Handle image cache
	public static ImageLoaderManager IMAGE_LOADER_MANAGER;

	@Override
	public void onCreate() {
		super.onCreate();

		REQUEST_MANAGER = new RequestManager(this);
		IMAGE_LOADER_MANAGER = new ImageLoaderManager(this,
				REQUEST_MANAGER.getRequestQueue());
	}
}
