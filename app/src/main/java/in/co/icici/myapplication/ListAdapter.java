package in.co.icici.myapplication;

/**
 * Created by paln on 9/4/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<Pair<Integer, String>> data;

	private static LayoutInflater inflater=null;
//	public ImageLoader imageLoader;

	public ListAdapter(Activity a,  ArrayList<Pair<Integer, String>> d) {
		activity = a;
		data=d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		imageLoader=new ImageLoader(activity.getApplicationContext());

	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.list_row, null);
		}
		Log.d("adapter", "getview");
		TextView title = (TextView)convertView.findViewById(R.id.title);
		TextView artist = (TextView)convertView.findViewById(R.id.artist);
		TextView amt = (TextView)convertView.findViewById(R.id.amount);
		ImageView thumb_image=(ImageView)convertView.findViewById(R.id.list_image);

		Pair<Integer, String> pair = data.get(position);
		convertView.setTag(pair);

		try {
			JSONObject jsonObject = new JSONObject(pair.second);
			String nickname = jsonObject.getString(Constants.SMSG_KEY_NICKNAME);
			String biller = jsonObject.getString(Constants.SMSG_KEY_BILLER);
			String amount = "₹ " + (int)jsonObject.get(Constants.SMSG_KEY_AMOUNT);

			title.setText(nickname.toUpperCase());
			artist.setText(biller);
			amt.setText(amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
}