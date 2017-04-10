package in.co.icici.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by paln on 10/4/2017.
 */


public class PaymentSuccessFragment extends Fragment {

	private static JSONObject mMsgObject;

	public static PaymentSuccessFragment newInstance(final JSONObject jsonObject) {
		PaymentSuccessFragment fragment = new PaymentSuccessFragment();
		mMsgObject = jsonObject;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.payment_sucess_fragment, container, false);
		try {
			String nickName = mMsgObject.getString(Constants.SMSG_KEY_NICKNAME);
			int amount = (int) mMsgObject.get(Constants.SMSG_KEY_AMOUNT);

			String text = "Success! Paid Rs." + amount + " to " + nickName;

			TextView textView = (TextView) view.findViewById(R.id.successText);
			textView.setText(text);

		} catch (Exception e ){

		}
		return view;
	}

}

