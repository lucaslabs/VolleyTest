package com.lmn.volleytest.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.lmn.volleytest.R;
import com.lmn.volleytest.VolleyTestApp;
import com.lmn.volleytest.model.FacebookProfile;
import com.lmn.volleytest.util.Constants;
import com.lmn.volleytest.util.request.GsonRequest;

/**
 * Activity that executes a request to get user profile and picture using Gson
 * and then shows this data.
 * 
 * @author Lucas Nobile
 */
public class GetProfilePictureActivity extends RequestActivity<FacebookProfile> {

	public static final String TAG = GetProfilePictureActivity.class
			.getSimpleName();

	private TextView tvActivityTitle, tvName;
	private NetworkImageView ivPicture;

	private ImageLoader mImageLoader;

	@Override
	protected void onCreateImpl(Bundle savedInstanceState) {
		setContentView(R.layout.activity_get_profile_picture);

		// Create the image loader
		mImageLoader = VolleyTestApp.IMAGE_LOADER_MANAGER.getImageLoader();

		tvActivityTitle = (TextView) findViewById(R.id.tvActivityTitle);
		tvActivityTitle
				.setText(getString(R.string.activity_get_profile_picture));

		tvName = (TextView) findViewById(R.id.tvName);

		ivPicture = (NetworkImageView) findViewById(R.id.ivPicture);

		// Execute the request
		performRequest();

		getProfilePicture();
	}

	/**
	 * Executes a request to get the profile picture.
	 */
	private void getProfilePicture() {
		// TODO Set a placeholder image
		ivPicture.setImageUrl(Constants.Facebook.USER_PICTURE, mImageLoader);
	}

	@Override
	protected Request<?> createRequest() {
		GsonRequest<FacebookProfile> request = new GsonRequest<FacebookProfile>(
				Request.Method.GET, Constants.Facebook.USER_PROFILE,
				FacebookProfile.class, this, this);
		return request;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.d(TAG, "GET error: " + error.getMessage());
		mProgressDialog.dismiss();
	}

	@Override
	public void onResponse(FacebookProfile response) {
		mProgressDialog.dismiss();

		tvName.setText(getString(R.string.profile_name, response.getName()));
	}
}
