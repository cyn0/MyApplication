package in.co.icici.myapplication.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

import in.co.icici.myapplication.Constants;
import in.co.icici.myapplication.MainActivity;
import in.co.icici.myapplication.R;

import static android.content.ContentValues.TAG;

/**
 * Created by paln on 3/4/2017.
 */

public class MyNotification {
	private static String TAG = "NOTIFICATION";
	public static void showPaybillNotification(final Context context, final JSONObject responseObject) {
		try {
			if (responseObject.has(Constants.SMSG_KEY_AMOUNT) && responseObject.has(Constants.SMSG_KEY_BILLER) && responseObject.has(Constants.SMSG_KEY_NICKNAME)) {
				int amount = (int) responseObject.get(Constants.SMSG_KEY_AMOUNT);
				String billerName = (String) responseObject.get(Constants.SMSG_KEY_BILLER);
				String nickName = (String) responseObject.get(Constants.SMSG_KEY_NICKNAME);
				Log.d(TAG, "Biller: " + billerName + "Nickname: " + nickName + " Amt:" + amount);

				final String title = "Pay " + billerName + " bill";
				final String text = "Tap to pay " + nickName + " Rs." + amount;
				Intent intent = new Intent(context, MainActivity.class);
				intent.putExtra(Constants.INTENT_MESSAGE, responseObject.toString());
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

				showNotification(context, contentIntent, title, text);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void showNotification(final Context context, final PendingIntent pendingIntent, final String title, final String text) {


		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(context)
						.setSmallIcon(R.drawable.ic_menu_gallery)
						.setContentTitle(title)
						.setContentText(text);
		mBuilder.setContentIntent(pendingIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());

	}
}
