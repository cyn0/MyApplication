package in.co.icici.myapplication.HttpRequestHandler;

import android.text.TextUtils;

import org.json.JSONObject;

import in.co.icici.myapplication.Constants;

public class MockServer {
	private static MockServer mSharedInstance;
	JSONObject responseObject;

	//constants
	final String ACT = "act";
	final String ELECTRICITY = "electricity";

//	String[] INTERNET_SERVICE_PROVIDERS =
	public static MockServer getSharedInstance(){
		if(mSharedInstance == null) {
			mSharedInstance = new MockServer();
		}
		return mSharedInstance;
	}

	public JSONObject parseSMS(final JSONObject requestObject){
		responseObject = new JSONObject();
		try {
			String origin = requestObject.getString(Constants.KEY_ORIGIN);
			String msg = requestObject.getString(Constants.KEY_MESSAGE);

			//remove spl characters
			origin = origin.replaceAll("[\\-\\+\\.\\^:,]","");
			origin = origin.toLowerCase();
			msg = msg.replaceAll("[\\-\\+\\.\\^:,]","");
			msg = msg.toLowerCase();

			boolean foundAmount = tryParsingAmount(msg);
			tryParsingBiller(origin, msg, foundAmount);

//			long currentTime = System.nanoTime();
//			responseObject.put(Constants.SMSG_KEY_MSG_ID, currentTime);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseObject;
	}

	private void tryParsingBiller(String origin, String msg, final boolean foundAmount) throws Exception{
		//TO-Do. redo all the logic
		try {
			if(origin.contains(ACT) || msg.contains(ACT)) {
				responseObject.put(Constants.SMSG_KEY_NICKNAME, ACT);
				responseObject.put(Constants.SMSG_KEY_BILLER, Constants.VALUE_INTERNET);
				responseObject.put(Constants.SMSG_KEY_VALID, true);

			} else if(msg.contains("electricity")) {
				responseObject.put(Constants.SMSG_KEY_NICKNAME, "EB");
				responseObject.put(Constants.SMSG_KEY_BILLER, Constants.VALUE_ELECTRICITY);
				responseObject.put(Constants.SMSG_KEY_VALID, true);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean tryParsingAmount(final String msg) throws Exception{
		int canBeAmount = -1;
		String[] tokens = msg.split(" ");
		for(int i=0; i<tokens.length; i++) {
			String token = tokens[i];

			//I know its repetitive but its more readable

			//handle rs.1000 or 1000rs.
			if(token.contains("rs")) {
				token = token.replace("rs", "");

				if(TextUtils.isEmpty(token)) {
					continue;
				}

				if(TextUtils.isDigitsOnly(token)) {
					canBeAmount = Integer.parseInt(token);
					responseObject.put(Constants.SMSG_KEY_AMOUNT, canBeAmount);
					return true;
				}
			}

			//handle 1000 rs or Rs 1000
			if(TextUtils.isDigitsOnly(token)) {
				canBeAmount = Integer.parseInt(token);

				if(canBeAmount <0) {
					continue;
				}

				//look for 'rs' before and after.
				if(i > 0 && tokens[i-1].equals("rs")){
					responseObject.put(Constants.SMSG_KEY_AMOUNT, canBeAmount);
					return true;
				}

				if(i<tokens.length && tokens[i+1].equals("rs")) {
					responseObject.put(Constants.SMSG_KEY_AMOUNT, canBeAmount);
					return true;
				}
			}

			//what if 'rs' is not present in the msg ?!
		}

		return false;
	}
}

/*
ACT msg:
origin: DZ-ACTGRP
Dear customer your ACT fibernet bill is due for payment bill amount Rs 1494 Please visit www.actcorp.in and
pay before 15th oct'16 to avoid late fee charges.

Electricity
origin: TANGED
Electricity charges Rs.1130 for SC.No. 094yyyyyyy is due on 12/04/2017. Pay online at YYYYY. Save Electricity
 */