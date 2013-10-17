package com.lmn.volleytest.utils.image;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

/**
 * Wrapper of {@link com.android.volley.toolbox.ImageLoader} to handle image
 * loading and cache in memory.
 * 
 * @author Lucas Nobile
 */
public class ImageLoaderManager {
	private static final String TAG = ImageLoaderManager.class.getSimpleName();

	private ImageLoader mImageLoader;

	public ImageLoaderManager(Context context, RequestQueue requestQueue) {
		int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		mImageLoader = new ImageLoader(requestQueue, new BitmapLruImageCache(
				cacheSize));
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}
}
