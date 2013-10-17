package com.lmn.volleytest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.lmn.volleytest.R;
import com.lmn.volleytest.VolleyTestApp;

/**
 * Activity that executes a request.
 * 
 * @author Lucas Nobile
 * @param <T>
 */
public abstract class RequestActivity<T> extends Activity implements
		Response.Listener<T>, Response.ErrorListener {

	public static final String TAG = RequestActivity.class.getSimpleName();

	/**
	 * Loading progress dialog.
	 */
	protected ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onCreateImpl(savedInstanceState);
	}

	/**
	 * Creates the child activity view.
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void onCreateImpl(Bundle savedInstanceState);

	/**
	 * Creates a new request.
	 * 
	 * @return the request
	 */
	protected abstract Request<?> createRequest();

	public void performRequest() {
		mProgressDialog = ProgressDialog.show(this,
				getString(R.string.progress_dialog_title),
				getString(R.string.progress_dialog_message));

		Request<?> request = createRequest();

		VolleyTestApp.REQUEST_MANAGER.addToRequestQueue(request, TAG);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		VolleyTestApp.REQUEST_MANAGER.cancelPendingRequests(TAG);
	}
}
