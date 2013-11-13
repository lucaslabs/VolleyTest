package com.lmn.volleytest.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lmn.volleytest.activity.DynamicPhotoListActivity;
import com.lmn.volleytest.util.Constants;

/**
 * {@link BroadcastReceiver} that gets notified once the device boot is
 * completed.
 * <p>
 * The idea is to synchronizing your polls with other polling apps to reduce how
 * often the update rate occurs.
 * 
 * @author Lucas Nobile
 */
public class MyBootReceiver extends BroadcastReceiver {

	private static final String TAG = MyBootReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		// Avoid NPE --> constants first!
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			Log.d(TAG, "Device boot completed.");

			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			int alarmType = AlarmManager.ELAPSED_REALTIME; // or
															// AlarmManager.RTC

			long firstPoll = Constants.ScheduleUpdate.TRIGGER_AT_TIME;

			Intent i = new Intent(DynamicPhotoListActivity.ACTION_UPDATE_DATA);

			PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

			// AlarmManager.setInexactRepeat will fire the intents at a regular
			// intervals simultaneously, but not at exactly specified times.
			am.setInexactRepeating(alarmType, firstPoll,
					Constants.ScheduleUpdate.INTERVAL, pi);
		}
	}
}