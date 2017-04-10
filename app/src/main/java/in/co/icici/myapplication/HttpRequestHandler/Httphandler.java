package in.co.icici.myapplication.HttpRequestHandler;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.co.icici.myapplication.Constants;

/**
 * Created by paln on 28/11/2015.
 */
public class Httphandler {
	//Context context;

	private String TAG = "Http error";
	private static Httphandler mInstance;

	private final String BILLER_SERVER = "https://biller.mybluemix.net";
	private final String PAY_BILLER_URI = "/biller/icicibank/billpay";
	public interface HttpDataListener{
		public void onDataAvailable(String response);
		
		public void onError(Exception e);
	}
	
	public static Httphandler getSharedInstance(){
		if(mInstance == null) {
			mInstance = new Httphandler();
		}

		return mInstance;
	}
	
	//https://corporateapiprojectwar.mybluemix.net/corporate_banking/mybank/authenticate_client?client_id=jananidamodaran@gmail.com&password=BU5IHUOV
	public void getAuthToken(final String clientId, final String password,final HttpDataListener dataListener) {
		final String url = "https://corporateapiprojectwar.mybluemix.net/corporate_banking/mybank/authenticate_client?client_id=" + clientId + "&password=" + password;
		new AsyncHttpTask(dataListener).execute(url, "GET");
	}

	public void uploadSMS(final JSONObject smsObject, final HttpDataListener dataListener){
//		final String url = SERVER_BASE_URL + POST_OFFERED_RIDE;
//        new AsyncHttpTask(dataListener).execute(url, "POST", mOfferRide.toJSON().toString());
		JSONObject responseObject = MockServer.getSharedInstance().parseSMS(smsObject);
		dataListener.onDataAvailable(responseObject.toString());
	}

	//https://biller.mybluemix.net/biller/icicibank/billpay
	// ?client_id=jananidamodaran@gmail.com&token=d8356d1ad1c4&custid=33335001&nickname=act&amount=10000
	public void makeBillerPayment(final JSONObject jsonObject, final HttpDataListener dataListener){
		String nickname = "";
		int amount = 0;

		try {
			nickname = jsonObject.getString(Constants.SMSG_KEY_NICKNAME);
			amount = (int) jsonObject.get(Constants.SMSG_KEY_AMOUNT);

		} catch (Exception e ) {
			e.printStackTrace();
		}
		final Uri builtUri = Uri.parse(BILLER_SERVER + PAY_BILLER_URI)
				.buildUpon()
				.appendQueryParameter("client_id", Constants.emailId)
				.appendQueryParameter("token", Constants.authToken)
				.appendQueryParameter("custid", Constants.custId)
				.appendQueryParameter("nickname", nickname)
				.appendQueryParameter("amount", "" + amount)
				.build();

		new AsyncHttpTask(dataListener).execute(builtUri.toString(), "GET");
	}
	private class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

		String response;
		HttpDataListener dataListener;
		AsyncHttpTask(HttpDataListener dataListener){
			this.dataListener = dataListener;
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			InputStream inputStream = null;
			HttpURLConnection urlConnection = null;
			Integer result = 0;
			try {
                /* forming th java.net.URL object */
				URL url = new URL(params[0]);
				String httpMethod = params[1];
				urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
				urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
				urlConnection.setRequestProperty("Accept", "application/json");

				urlConnection.setRequestMethod(httpMethod);
                
				if(httpMethod.equals("POST")){
	                byte[] outputInBytes = params[2].getBytes("UTF-8");
	                OutputStream os = urlConnection.getOutputStream();
	                os.write( outputInBytes );
	                os.close();
				}
				
				Log.d("Request url", url.toString());
                int statusCode = urlConnection.getResponseCode();

                if (statusCode >= 200 && statusCode < 300) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    response = convertInputStreamToString(inputStream);
                    Log.d("response", response);
                    
                    result = 1; 
                } else {
                	Log.e(TAG, "Response code not ok");
                }
                
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				dataListener.onDataAvailable(response);
			} else {
				dataListener.onError(new Exception("Status code not OK."));
			}
		}
	}

	private String convertInputStreamToString(InputStream inputStream) throws IOException {

		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));

		String line = "";
		String result = "";

		while((line = bufferedReader.readLine()) != null){
			result += line;
		}

            /* Close Stream */
		if(null!=inputStream){
			inputStream.close();
		}

		return result;
	}

	public JSONArray handleICICServerResponse(final String serverResponse, final HttpDataListener dataListener) {
		//purpose
		// in the server response, first object in the array is the status code result is the required data :( :(

		JSONArray parsedResult = new JSONArray();
		try {
			JSONArray jsonArray = new JSONArray(serverResponse);
			JSONObject statusObject = (JSONObject) jsonArray.get(0);
			int status = statusObject.getInt("code");

			if(status == 200) {
				for(int i=1; i<jsonArray.length(); i++) {
					parsedResult.put(jsonArray.getJSONObject(i));
				}
			} else {
				dataListener.onError( new Exception("server request failed") );
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return  parsedResult;
	}
}
