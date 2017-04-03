package in.co.icici.myapplication.SMS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import in.co.icici.myapplication.Constants;
import in.co.icici.myapplication.HttpRequestHandler.Httphandler;
import in.co.icici.myapplication.HttpRequestHandler.Httphandler.HttpDataListener;
import in.co.icici.myapplication.util.MyNotification;

import org.json.JSONObject;

/**
 * Created by paln on 2/4/2017.
 */

public class SmsListener extends BroadcastReceiver {

	private SharedPreferences preferences;
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "SMSBroadcastReceiver";
	private Context mContext;
	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;

		Log.i(TAG, "Intent recieved: " + intent.getAction());

		if(intent.getAction().equals(SMS_RECEIVED)){
			Bundle bundle = intent.getExtras();
			if (bundle != null){
				handleSMSMessage(bundle);
			}

		}
	}

	private void handleSMSMessage(final Bundle bundle) {
		SmsMessage[] msgs = null;
		JSONObject smsObject = null;
		try{
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for(int i=0; i<msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				smsObject = createJSON(msgs[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		if(smsObject != null) {
			HttpDataListener httpDataListener = new HttpDataListener() {
				@Override
				public void onDataAvailable(String response) {
					handleServerResponse(response);
				}

				@Override
				public void onError(Exception e) {

				}
			};
			Httphandler.getSharedInstance().uploadSMS(smsObject, httpDataListener);
		}
	}

	private void handleServerResponse(final String response) {
		try {
			final JSONObject responseObject = new JSONObject(response);
			MyNotification.showPaybillNotification(mContext, responseObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private JSONObject createJSON(SmsMessage msg) throws Exception{
		JSONObject jsonObject = null;
		String msg_from = msg.getOriginatingAddress();
		String msgBody = msg.getMessageBody();
		Log.d(TAG, "Message recieved: " + msg_from + " : " +msgBody);

		jsonObject = new JSONObject();
		jsonObject.put(Constants.KEY_TYPE, Constants.VALUE_SMS);
		jsonObject.put(Constants.KEY_MESSAGE, msgBody);
		jsonObject.put(Constants.KEY_ORIGIN, msg_from);

		return jsonObject;
	}
}