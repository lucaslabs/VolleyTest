package com.lmn.volleytest.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.lmn.volleytest.R;
import com.lmn.volleytest.VolleyTestApp;
import com.lmn.volleytest.adapter.PicasaPhotoAdapter;
import com.lmn.volleytest.model.picasa.PicasaPhoto;
import com.lmn.volleytest.model.picasa.PicasaResponse;
import com.lmn.volleytest.util.request.GsonRequest;

/**
 * Executes a request to get photos from Picasa dynammically.
 * <p>
 * Implements the "Endless" List UI design pattern :). See
 * {@link EndlessScrollListener}.
 * 
 * @author Lucas Nobile
 */
public class DynamicPhotoListActivity extends RequestActivity<PicasaResponse> {

	private static final String TAG = DynamicPhotoListActivity.class
			.getSimpleName();
	private static final int RESULT_PER_PAGE = 20;
	private ListView mListView;
	private PicasaPhotoAdapter mAdapter;
	private boolean mHasData = false;
	private boolean mErrorOcurred = false;
	private ArrayList<PicasaPhoto> mEntries = new ArrayList<PicasaPhoto>();

	@Override
	protected void onCreateImpl(Bundle savedInstanceState) {
		setContentView(R.layout.activity_dynamic_list);

		setTitle(R.string.activity_dynamic_photos);

		mListView = (ListView) findViewById(R.id.lvPicasaPhotos);
		mAdapter = new PicasaPhotoAdapter(this, 0, mEntries,
				VolleyTestApp.IMAGE_LOADER_MANAGER.getImageLoader());

		mListView.setAdapter(mAdapter);

		// Scroll listener
		mListView.setOnScrollListener(new EndlessScrollListener());

	}

	@Override
	public void onResume() {
		super.onResume();

		if (!mHasData && !mErrorOcurred) {

			// Execute the request
			performRequest();
		}
	}

	@Override
	protected Request createRequest() {
		String photoOf = "android"; // search criteria
		int thumbSize = 160;
		int startIndex = 1 + mEntries.size();

		// TODO Use Uri.Builder
		String url = "https://picasaweb.google.com/data/feed/api/all?q="
				+ photoOf + "&thumbsize=" + thumbSize + "&max-results="
				+ RESULT_PER_PAGE + "&start-index=" + startIndex + "&alt=json";

		GsonRequest<PicasaResponse> request = new GsonRequest<PicasaResponse>(
				Request.Method.GET, url, PicasaResponse.class, this, this);

		return request;
	}

	@Override
	public void onResponse(PicasaResponse response) {
		mProgressDialog.dismiss();
		mAdapter.addFeeds(response);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.d(TAG, "GET error: " + error.getMessage());
		mProgressDialog.dismiss();
		mErrorOcurred = true;
	}

	/**
	 * Detects when user is closer to the end of the current page and starts
	 * loading the next page so the user will not have to wait (that much) for
	 * the next entries.
	 */
	public class EndlessScrollListener implements AbsListView.OnScrollListener {
		// How many entries earlier to start loading next page
		private int visibleThreshold = 5;
		private int currentPage = 0;
		private int previousTotal = 0;
		private boolean loading = true;

		public EndlessScrollListener() {
		}

		public EndlessScrollListener(int visibleThreshold) {
			this.visibleThreshold = visibleThreshold;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (loading) {
				if (totalItemCount > previousTotal) {
					loading = false;
					previousTotal = totalItemCount;
					currentPage++;
				}
			}
			if (!loading
					&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				// I load the next page of gigs using a background task,
				// but you can call any function here.
				performRequest();
				loading = true;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		public int getCurrentPage() {
			return currentPage;
		}
	}

}
