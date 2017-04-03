package in.co.icici.myapplication;

import static android.R.attr.value;

/**
 * Created by paln on 2/4/2017.
 */

public class Constants {
	public static String authToken = "";
	public static String CustId = "33335001";

	//App constants
	public static String INTENT_MESSAGE = "message";

	//msg request key
	public static String KEY_HEADER = "HEADER";
	public static String KEY_ORIGIN = "origin";
	public static String KEY_MESSAGE = "message";
	public static String KEY_TYPE = "type";

	//msg value
	public static String VALUE_SMS = "sms";

	//Response
	public static String SMSG_KEY_TYPE = "type";
	public static String SMSG_KEY_BILLER = "biller";
	public static String SMSG_KEY_NICKNAME = "nickname";
	public static String SMSG_KEY_AMOUNT = "amount";

	//billers
	public static String VALUE_ELECTRICITY = "electricity";
	public static String VALUE_INTERNET = "internet";
	public static String VALUE_MOBILE = "mobile";
	public static String VALUE_LPG = "lpg";

	enum Billers {
		ELECTRICITY("electricity"),
		MOBILE("mobile"),
		INTERNET("internet"),
		LPG("lpg");
		public String value;
		Billers(final String value){
			this.value = value;
		}
	}
}
