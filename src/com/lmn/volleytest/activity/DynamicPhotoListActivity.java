package com.lmn.volleytest.activity;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
<<<<<<< HEAD
=======
import com.lmn.volleytest.receiver.MyAlarmReceiver;
import com.lmn.volleytest.receiver.MyAlarmReceiver.OnScheduleUpdateListener;
import com.lmn.volleytest.util.Constants;
>>>>>>> Add project file.
import com.lmn.volleytest.util.request.GsonRequest;

/**
 * Executes a request to get photos from Picasa dynammically.
 * <p>
 * Implements the "Endless" List UI design pattern :). See
 * {@link EndlessScrollListener}.
 * 
 * @author Lucas Nobile
 */
public class DynamicPhotoListActivity extends RequestActivity<PicasaResponse>
		implements OnScheduleUpdateListener {

	private static final String TAG = DynamicPhotoListActivity.class
			.getSimpleName();
	private static final int RESULT_PER_PAGE = 20;
	private ListView mListView;
	private PicasaPhotoAdapter mAdapter;
	private boolean mHasData = false;
	private boolean mErrorOcurred = false;
	private ArrayList<PicasaPhoto> mEntries = new ArrayList<PicasaPhoto>();

	private BroadcastReceiver mAlarmReceiver;
	private boolean isRequestPending;
	private boolean needToScroll;

	public static final String ACTION_UPDATE_DATA = "com.lmn.volleytest.action.UPDATE_DATA";

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
			if (!isRequestPending) {
				performRequest();
				isRequestPending = true;
			}
		}

		// Register the receiver
		mAlarmReceiver = new MyAlarmReceiver(this);
		IntentFilter intentFilter = new IntentFilter(ACTION_UPDATE_DATA);
		this.registerReceiver(mAlarmReceiver, intentFilter);

		// After receiver registering, we can schedule the update.
		scheduleAlarmReceiver();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(mAlarmReceiver);
	}

	// Schedule AlarmManager to invoke MyAlarmReceiver and cancel any existing
	// current PendingIntent. We do this because we *also* invoke the receiver
	// from a BOOT_COMPLETED receiver so that we make sure the service runs
	// either when app is installed/started, or when device boots.
	private void scheduleAlarmReceiver() {
		AlarmManager alarmMgr = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);

		Intent i = new Intent(DynamicPhotoListActivity.ACTION_UPDATE_DATA);

		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0); // PendingIntent.FLAG_CANCEL_CURRENT

		// Use inexact repeating which is easier on battery (system can phase
		// events and not wake at exact times)
		alarmMgr.setInexactRepeating(AlarmManager.RTC,
				Constants.ScheduleUpdate.TRIGGER_AT_TIME,
				Constants.ScheduleUpdate.INTERVAL, pi);
	}

	@Override
	public void onScheduleUpdate() {
		if (!isRequestPending) {
			performRequest();
			isRequestPending = true;
			needToScroll = true;
		}
	}

	@Override
	protected Request<PicasaResponse> createRequest() {
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
		Log.d(TAG, "GET success!: " + response.toString());

		mProgressDialog.dismiss();
		mAdapter.addFeeds(response);

		isRequestPending = false;

		if (needToScroll) {
			int position = mEntries.size() - RESULT_PER_PAGE;
			mListView.setSelection(position);
			needToScroll = false;
		}
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
				if (!isRequestPending) {
					loading = true;
					performRequest();
					isRequestPending = true;
				}

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
