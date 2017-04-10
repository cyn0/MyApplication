package in.co.icici.myapplication.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by paln on 9/4/2017.
 */

public class SQLDatabaseHandler extends SQLiteOpenHelper {
	private static String TAG = "DATABASE";
	private static final int DATABASE_VERSION = 2;

	private static final String DATABASE_NAME = "myDB";
	private static final String TABLE_PAYMENTS = "payments";

	private static final String KEY_ID = "id";
	private static final String KEY_MSG = "name";

	private static SQLDatabaseHandler mDatabaseHandler;

	public SQLDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static SQLDatabaseHandler getSharedInstance(final Context context) {
		if(mDatabaseHandler == null) {
			mDatabaseHandler = new SQLDatabaseHandler(context);
		}
		return  mDatabaseHandler;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PAYMENTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_MSG + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);

		// Create tables again
		onCreate(db);
	}

	public void addPayment(final String msg) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MSG, msg);

		// Inserting Row
		db.insert(TABLE_PAYMENTS, null, values);
		db.close(); // Closing database connection
	}

	public ArrayList<Pair<Integer, String>> getAllPayments() {
		ArrayList<Pair<Integer, String>> data = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PAYMENTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		Log.d(TAG, "DONE");
		if (cursor.moveToFirst()) {
			do {
				Integer id = Integer.parseInt(cursor.getString(0));
				String msg = cursor.getString(1);
				Log.d(TAG, id + " : " + msg);
				data.add(Pair.create(id, msg));

			} while (cursor.moveToNext());
		}

		return data;
	}

	public void deletePayment(final Integer id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PAYMENTS, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}
}