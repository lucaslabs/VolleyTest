package com.lmn.volleytest.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lmn.volleytest.R;
import com.lmn.volleytest.util.Constants;

/**
 * Activity that executes a request to get user profile data in JSON Object
 * format and then shows this data.
 * 
 * @author Lucas Nobile
 */
public class GetJsonProfileActivity extends RequestActivity<JSONObject> {

	public static final String TAG = GetJsonProfileActivity.class.getSimpleName();

	private TextView tvActivityTitle, tvId, tvName, tvFirstName, tvLastName, tvUsername,
			tvGender;

	@Override
	protected void onCreateImpl(Bundle savedInstanceState) {
		setContentView(R.layout.activity_get_profile);

		tvActivityTitle = (TextView) findViewById(R.id.tvActivityTitle);
		tvActivityTitle.setText(getString(R.string.activity_get_profile_json));
		
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
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
				Constants.Facebook.USER_PROFILE, null, this, this);
		return request;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.d(TAG, "GET error: " + error.getMessage());
		mProgressDialog.dismiss();
	}

	@Override
	public void onResponse(JSONObject response) {
		mProgressDialog.dismiss();

		try {
			tvId.setText(getString(R.string.profile_id,
					response.getString(Constants.ProfileField.ID)));
			tvName.setText(getString(R.string.profile_name,
					response.getString(Constants.ProfileField.NAME)));
			tvFirstName.setText(getString(R.string.profile_first_name,
					response.getString(Constants.ProfileField.FIRST_NAME)));
			tvLastName.setText(getString(R.string.profile_last_name,
					response.getString(Constants.ProfileField.LAST_NAME)));
			tvUsername.setText(getString(R.string.profile_username,
					response.getString(Constants.ProfileField.USERNAME)));
			tvGender.setText(getString(R.string.profile_gender,
					response.getString(Constants.ProfileField.GENDER)));
		} catch (JSONException e) {
			Log.d(TAG, "Error parsing response: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
