package in.co.icici.myapplication.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

import java.util.Date;

import in.co.icici.myapplication.Constants;
import in.co.icici.myapplication.LoginActivity;
import in.co.icici.myapplication.PaymentFragment;
import in.co.icici.myapplication.R;

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
				Intent intent = new Intent(context, LoginActivity.class);
				intent.putExtra(Constants.INTENT_HAS_MESSAGE, true);
				intent.putExtra(Constants.INTENT_MESSAGE, responseObject.toString());
				intent.putExtra(Constants.INTENT_FRAGMENT_NAME, PaymentFragment.class.getName());

				int requestCode = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
				PendingIntent contentIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

				showNotification(context, contentIntent, title, text);
				Log.d(TAG, responseObject.toString());

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
