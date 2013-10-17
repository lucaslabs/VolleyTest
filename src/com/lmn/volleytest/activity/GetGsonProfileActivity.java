package com.lmn.volleytest.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.lmn.volleytest.R;
import com.lmn.volleytest.model.FacebookProfile;
import com.lmn.volleytest.util.Constants;
import com.lmn.volleytest.utils.request.GsonRequest;

/**
 * Activity that executes a request to get user profile using Gson and then
 * shows this data.
 * 
 * @author Lucas Nobile
 */
public class GetGsonProfileActivity extends RequestActivity<FacebookProfile> {

	public static final String TAG = GetGsonProfileActivity.class
			.getSimpleName();

	private TextView tvActivityTitle, tvId, tvName, tvFirstName, tvLastName,
			tvUsername, tvGender;

	@Override
	protected void onCreateImpl(Bundle savedInstanceState) {
		setContentView(R.layout.activity_get_profile);

		tvActivityTitle = (TextView) findViewById(R.id.tvActivityTitle);
		tvActivityTitle.setText(getString(R.string.activity_get_profile_gson));

		tvId = (TextView) findViewById(R.id.tvId);
		tvName = (TextView) findViewById(R.id.tvName);
		tvFirstName = (TextView) findViewById(R.id.tvFirstName);
		tvLastName = (TextView) findViewById(R.id.tvLastName);
		tvUsername = (TextView) findViewById(R.id.tvUsername);
		tvGender = (TextView) findViewById(R.id.tvGender);

		// Execute the request
		performRequest();
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

		tvId.setText(getString(R.string.profile_id, response.getId()));
		tvName.setText(getString(R.string.profile_name, response.getName()));
		tvFirstName.setText(getString(R.string.profile_first_name,
				response.getFirstName()));
		tvLastName.setText(getString(R.string.profile_last_name,
				response.getLastName()));
		tvUsername.setText(getString(R.string.profile_username,
				response.getUsername()));
		tvGender.setText(getString(R.string.profile_gender,
				response.getGender()));
	}
}
