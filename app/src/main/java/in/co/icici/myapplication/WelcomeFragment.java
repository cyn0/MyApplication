package in.co.icici.myapplication;

/**
 * Created by paln on 9/4/2017.
 */


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeFragment extends Fragment {
	// Store instance variables
	private String title;
	private int page;

	private String TAG = "WelcomeFragment";
	Activity mActivity;

	public static WelcomeFragment newInstance() {
		WelcomeFragment fragment = new WelcomeFragment();
		Bundle args = new Bundle();
//		args.putInt("someInt", page);
//		args.putString("someTitle", title);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page = getArguments().getInt("someInt", 0);
		title = getArguments().getString("someTitle");

		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.welcome_fragment, container, false);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}


}
