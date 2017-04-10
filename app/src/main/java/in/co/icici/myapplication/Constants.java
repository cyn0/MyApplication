package in.co.icici.myapplication;

/**
 * Created by paln on 2/4/2017.
 */

public class Constants {
	//client constants
	public static String authToken = "";
	public static String custId = "33335001";
	public static String emailId = "jananidamodaran@gmail.com";
	public static String passwd = "BU5IHUOV";
	//App constants
	public static String INTENT_MESSAGE = "message";
	public static String INTENT_HAS_MESSAGE = "has_message";
	public static String INTENT_SAVED_MESSAGE = "has_message";
	public static String INTENT_MSG_ID = "msg_id";
	public static String INTENT_FRAGMENT_NAME = "frag_name";

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
	public static String SMSG_KEY_VALID= "valid";
	public static String SMSG_KEY_MSG_ID= "valid";

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
