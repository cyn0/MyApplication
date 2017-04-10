package in.co.icici.myapplication;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import in.co.icici.myapplication.HttpRequestHandler.Httphandler;
import in.co.icici.myapplication.HttpRequestHandler.Httphandler.HttpDataListener;
import in.co.icici.myapplication.SQL.SQLDatabaseHandler;

import static android.view.View.GONE;
import static in.co.icici.myapplication.R.id.msgTextAmount;
import static in.co.icici.myapplication.R.id.msgTextNickname;

/**
 * Created by paln on 9/4/2017.
 */

public class PaymentFragment extends Fragment {

	private final String TAG = "PaymentFragment";

	Button mMakePaymentButton;
	Button mSaveForLaterButton;
	TextView mNicknameTextView;
	TextView mAmountTextView;
	View mFragmentView;

	int mAmount;
	String mBiller;
	String mNickname;
	boolean mSavedPayment;
	Integer mId;

	public JSONObject mMsgObject;

	FragmentManager mFragmentManager;

	public static PaymentFragment newInstance(final String msg, final boolean savedPayment, final Integer id) {
		PaymentFragment fragment = new PaymentFragment();
		Bundle args = new Bundle();
		args.putString(Constants.INTENT_MESSAGE, msg);
		args.putBoolean(Constants.INTENT_SAVED_MESSAGE, savedPayment);
		if(id != null) {
			args.putInt(Constants.INTENT_MSG_ID, id);
		}
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getActivity().getSupportFragmentManager();
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.payment_fragment, container, false);

		mFragmentView = view;

		mNicknameTextView = (TextView)view.findViewById(msgTextNickname);
		mAmountTextView = (TextView) view.findViewById(msgTextAmount);
		mMakePaymentButton = (Button) view.findViewById(R.id.makePayment);
		mSaveForLaterButton = (Button) view.findViewById(R.id.saveForLater);

		mMakePaymentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				handleMakePaymentClicked();
			}
		});

		mSaveForLaterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				handlePayLaterClicked();
			}
		});

		((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Pay " + mBiller + " Bill");
		String msgText = "Pay " + mNickname.toUpperCase();
		String amount = "₹ " + mAmount;

		mNicknameTextView.setText(msgText);
		mAmountTextView.setText(amount);

		if(mSavedPayment) {
			mSaveForLaterButton.setVisibility(GONE);
		}
		return view;
	}

	private void init() {
		String msg = getArguments().getString(Constants.INTENT_MESSAGE);
		mSavedPayment = getArguments().getBoolean(Constants.INTENT_SAVED_MESSAGE, false);
		try{
			mMsgObject = new JSONObject(msg);
			Log.d(TAG, mMsgObject.toString());

			mAmount = (int)mMsgObject.get(Constants.SMSG_KEY_AMOUNT);
			mBiller = mMsgObject.getString(Constants.SMSG_KEY_BILLER);
			mNickname = mMsgObject.getString(Constants.SMSG_KEY_NICKNAME);

			if(mSavedPayment) {
				mId = getArguments().getInt(Constants.INTENT_MSG_ID);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleMakePaymentClicked() {
		((MainActivity)getActivity()).showHideProgressOverlay(true);

		HttpDataListener dataListener = new HttpDataListener() {
			@Override
			public void onDataAvailable(String response) {
				JSONArray result = Httphandler.getSharedInstance().handleICICServerResponse(response, this);

				//till here success
				handlePaymentSuccess();
			}

			@Override
			public void onError(Exception e) {

			}
		};
		Httphandler.getSharedInstance().makeBillerPayment(mMsgObject, dataListener);
	}

	private void handlePaymentSuccess(){
		((MainActivity)getActivity()).showHideProgressOverlay(false);

		PaymentSuccessFragment paymentSuccessFragment = PaymentSuccessFragment.newInstance(mMsgObject);
		mFragmentManager
			.beginTransaction()
			.replace(R.id.container, paymentSuccessFragment)
			.commit();

		if(mSavedPayment) {
			SQLDatabaseHandler.getSharedInstance(getContext()).deletePayment(mId);
		}
	}

	private void handlePayLaterClicked() {

		Snackbar.make(mFragmentView, "You can find the bill in Saved Payments", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show();
		mSaveForLaterButton.setVisibility(GONE);
	}
}
