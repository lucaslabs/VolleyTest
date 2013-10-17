package com.lmn.volleytest.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lmn.volleytest.R;
import com.lmn.volleytest.util.Constants;

/**
 * Activity that executes a POST request to the Echo Server (httpbin.org) and
 * show the response.
 * 
 * @author Lucas Nobile
 */
public class PostJsonProfileActivity extends RequestActivity<String> {

	public static final String TAG = GetJsonProfileActivity.class
			.getSimpleName();

	private TextView tvActivityTitle, tvName, tvDomain;

	@Override
	protected void onCreateImpl(Bundle savedInstanceState) {
		setContentView(R.layout.activity_post_profile);

		tvActivityTitle = (TextView) findViewById(R.id.tvActivityTitle);
		tvActivityTitle.setText(getString(R.string.activity_post_profile_json));

		tvName = (TextView) findViewById(R.id.tvName);
		tvDomain = (TextView) findViewById(R.id.tvDomain);

		// Execute the request
		performRequest();
	}

	@Override
	protected Request<?> createRequest() {
		StringRequest request = new StringRequest(Request.Method.POST,
				Constants.EchoServer.POST, this, this) {

			// POST parameters
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("name", "Lucas Nobile");
				params.put("domain", "https://github.com/lucaslabs");

				return params;
			}
		};

		return request;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.d(TAG, "GET error: " + error.getMessage());
		mProgressDialog.dismiss();
	}

	@Override
	public void onResponse(String response) {
		mProgressDialog.dismiss();

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			JSONObject form = jsonObject.getJSONObject("form");

			tvName.setText(getString(R.string.profile_name,
					form.getString(Constants.ProfileField.NAME)));
			tvDomain.setText(getString(R.string.profile_domain,
					form.getString(Constants.ProfileField.DOMAIN)));

		} catch (JSONException e) {
			Log.d(TAG, "Error parsing response: " + e.getMessage());
			e.printStackTrace();
		}

	}

}