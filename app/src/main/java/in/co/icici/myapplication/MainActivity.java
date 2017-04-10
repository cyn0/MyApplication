package in.co.icici.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import in.co.icici.myapplication.SQL.SQLDatabaseHandler;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	FrameLayout mFrameLayout;
	FrameLayout mProgressLayout;
	FragmentManager mFragmentManager;

	boolean doubleBackToExitPressedOnce = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mFragmentManager = getSupportFragmentManager();

//		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//		fab.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//						.setAction("Action", null).show();
//			}
//		});

		setNavigationDrawer();
		doInit();
		setUI();

	}

	private void setUI() {
		mProgressLayout = (FrameLayout) findViewById(R.id.progress_overlay);
		mFrameLayout = (FrameLayout) findViewById(R.id.container);
	}

	private void doInit() {

		final Bundle extras = getIntent().getExtras();

		if(extras != null) {
			String screenName = extras.getString(Constants.INTENT_FRAGMENT_NAME);
			String msg = (String) extras.get(Constants.INTENT_MESSAGE);
			if(screenName.equals(PaymentFragment.class.getName())) {

				PaymentFragment paymentFragment = PaymentFragment.newInstance(msg, false, null);
				mFragmentManager
						.beginTransaction()
						.replace(R.id.container, paymentFragment)
						.commit();
				return;
			}
		}

		mFragmentManager
				.beginTransaction()
				.add(R.id.container, WelcomeFragment.newInstance())
				.commit();
	}

	public void showHideProgressOverlay(final boolean show) {
		mProgressLayout.setVisibility(show ? View.VISIBLE : View.GONE);

	}

	private void handleSavedPaymentsClicked() {
		final ArrayList<Pair<Integer,String>> payments = SQLDatabaseHandler.getSharedInstance(getApplicationContext()).getAllPayments();

		if(payments.size() == 0) {
			Snackbar.make(mFrameLayout, "No saved payments found", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
		}

		mFragmentManager
				.beginTransaction()
				.replace(R.id.container, SavedPaymentsFragment.newInstance())
				.commit();

	}

	private void setNavigationDrawer(){
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		Fragment fragment = mFragmentManager.findFragmentById(R.id.container);

		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);

		} else if (fragment instanceof WelcomeFragment){

			if (doubleBackToExitPressedOnce) {
				super.onBackPressed();
				return;
			}

			this.doubleBackToExitPressedOnce = true;
			Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					doubleBackToExitPressedOnce=false;
				}
			}, 2000);
		} else {
			mFragmentManager
					.beginTransaction()
					.replace(R.id.container, WelcomeFragment.newInstance())
					.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_saved_payments) {
			handleSavedPaymentsClicked();
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
