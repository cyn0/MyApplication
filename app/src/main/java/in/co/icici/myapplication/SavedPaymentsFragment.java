package in.co.icici.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import in.co.icici.myapplication.SQL.SQLDatabaseHandler;

/**
 * Created by paln on 9/4/2017.
 */

public class SavedPaymentsFragment extends Fragment {

	ListView mListView;

	FragmentManager mFragmentManager;

	public static SavedPaymentsFragment newInstance() {
		SavedPaymentsFragment fragment = new SavedPaymentsFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getActivity().getSupportFragmentManager();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.saved_payments_fragment, container, false);
		mListView = (ListView) view.findViewById(R.id.list);

		final ArrayList<Pair<Integer,String>> payments = SQLDatabaseHandler.getSharedInstance(getContext()).getAllPayments();
		ListAdapter adapter = new ListAdapter(getActivity(), payments);
		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PaymentFragment paymentFragment = PaymentFragment.newInstance(payments.get(position).second, true, payments.get(position).first);
				mFragmentManager
						.beginTransaction()
						.replace(R.id.container, paymentFragment)
						.commit();

			}
		});

		return view;
	}

}

