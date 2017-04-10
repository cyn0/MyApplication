package in.co.icici.myapplication.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.co.icici.myapplication.Constants;
import in.co.icici.myapplication.HttpRequestHandler.Httphandler;
import in.co.icici.myapplication.HttpRequestHandler.Httphandler.HttpDataListener;

/**
 * Created by paln on 5/4/2017.
 */

public class Authorisation {
	public interface AuthorisationListener {
		void onAuthorisationSuccess();
		void onAuthorisationFail();
	}
	public static void setAuthToken(final String user, final String  password, final AuthorisationListener authorisationListener) {
		HttpDataListener httpDataListener = new HttpDataListener() {
			@Override
			public void onDataAvailable(String response) {
				try{
					//lot of assumptions :(
					JSONArray jsonArray = new JSONArray(response);
					JSONObject jsonObject = (JSONObject)jsonArray.get(0);
					String token = jsonObject.getString("token");
					if(TextUtils.isEmpty(token)) {
						authorisationListener.onAuthorisationFail();
					} else {
						Constants.authToken = token;
						authorisationListener.onAuthorisationSuccess();
					}
				}catch (JSONException e){
					e.printStackTrace();
				}

			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
				authorisationListener.onAuthorisationFail();
			}
		};

		Httphandler.getSharedInstance().getAuthToken(user, password, httpDataListener);
	}
}
