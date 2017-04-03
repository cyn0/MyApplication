package in.co.icici.myapplication.HttpRequestHandler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

/**
 * Created by paln on 28/11/2015.
 */
public class Httphandler {
	//Context context;

	private String TAG = "Http error";
	private static Httphandler mInstance;
	
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

}
