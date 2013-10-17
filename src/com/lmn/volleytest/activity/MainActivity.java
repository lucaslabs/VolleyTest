package com.lmn.volleytest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lmn.volleytest.R;

/**
 * Activity with buttons to show the Volley capabilities.
 * 
 * @author Lucas Nobile
 */
public class MainActivity extends Activity {

	public static final String TAG = MainActivity.class.getSimpleName();

	private Button btnGetJsonProfile;
	private Button btnGetGsonProfile;
	private Button btnGetProfilePicture;

	private Button btnPostJsonProfile;

	private Button btnShowDynamicPhotoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get Profile in JSON Object
		btnGetJsonProfile = (Button) findViewById(R.id.btnGetJsonProfile);
		btnGetJsonProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						GetJsonProfileActivity.class);
				startActivity(intent);
			}
		});

		// Get Profile using Gson
		btnGetGsonProfile = (Button) findViewById(R.id.btnGetGsonProfile);
		btnGetGsonProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						GetGsonProfileActivity.class);
				startActivity(intent);
			}
		});

		// Get Profile picture
		btnGetProfilePicture = (Button) findViewById(R.id.btnGetProfilePicture);
		btnGetProfilePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						GetProfilePictureActivity.class);
				startActivity(intent);
			}
		});

		// Post Profile in JSON format
		btnPostJsonProfile = (Button) findViewById(R.id.btnPostJsonProfile);
		btnPostJsonProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						PostJsonProfileActivity.class);
				startActivity(intent);
			}
		});

		// Show photos from Picasa dynamically
		btnShowDynamicPhotoList = (Button) findViewById(R.id.btnShowDynamicPhotoList);
		btnShowDynamicPhotoList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						DynamicPhotoListActivity.class);
				startActivity(intent);
			}
		});
	}

}
