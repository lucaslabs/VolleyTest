package com.lmn.volleytest.receiver;

import com.lmn.volleytest.activity.DynamicPhotoListActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This {@link BroadcastReceiver} executes itself by interval times defined in
 * {@link MyBootReceiver}.
 * 
 * @author Lucas Nobile
 */
public class MyAlarmReceiver extends BroadcastReceiver {

	/**
	 * Lister to handle schedule update.
	 */
	public interface OnScheduleUpdateListener {
		public void onScheduleUpdate();
	}

	private OnScheduleUpdateListener mListener;

	public MyAlarmReceiver(OnScheduleUpdateListener listener) {
		mListener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (DynamicPhotoListActivity.ACTION_UPDATE_DATA.equals(intent
				.getAction())) {
			mListener.onScheduleUpdate();
		}
	}
}