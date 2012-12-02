package com.sol2;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ToggleButton;

/**
 * Simple activity that launches the Layar application and opens the specified
 * layer.
 * 
 * @author Ronald van der Lingen (ronald@layar.com)
 */
public class Launcher extends Activity implements OnClickListener {

	private static final String MARKET_URL = (getSdkVersion() > 5) ? "market://details?id=com.layar"
			: "market://search?q=pname:com.layar";

	private LocationManager locationManager;
	private Location location;
	private boolean flagAdd = false;
	private String[] mItems = { "atmskyiv",
			"gasstationskyiv" };
	private Context context;
	public String mod;
	private ListView mListitems;
	private ToggleButton tb1, tb2;

	private ItemsAdapter mAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(featureId)
		setContentView(R.layout.main);
		tb1 = (ToggleButton) findViewById(R.id.homeButton);
		tb2 =(ToggleButton) findViewById(R.id.addButton);
		tb1.toggle();
		tb1.setOnClickListener(this);
		tb2.setOnClickListener(this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		location = locationManager.getLastKnownLocation("network");

		mListitems = (ListView) findViewById(R.id.listItems2);

		mAdapter = new ItemsAdapter(Launcher.this, R.layout.list_items, mItems);
		mListitems.setAdapter(mAdapter);
		mListitems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// String selectedValue = (String)
				// getListAdapter().getItem(position);
				if (flagAdd) {
					mod = mItems[arg2];
					Intent foo = new Intent(getApplicationContext(), AddPoints.class);
					foo.putExtra("key", mod);
					startActivity(foo);
				}
				else
				{
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("layar://" + mItems[arg2]));
				startActivity(intent);
				}
			}
		});
//		// ActionBar gets initiated
//		ActionBar actionbar = getActionBar();
//		// //Tell the ActionBar we want to use Tabs.
//		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//		// initiating both tabs and set text to it.
//		ActionBar.Tab PlayerTab = actionbar.newTab().setText("Fragment A");
//		ActionBar.Tab StationsTab = actionbar.newTab().setText("Fragment B");
//
//		// //create the two fragments we want to use for display content
//		Fragment PlayerFragment = new AFragment();
//		Fragment StationsFragment = new BFragment();
//
//		// set the Tab listener. Now we can listen for clicks.
//		PlayerTab.setTabListener(new MyTabsListener(PlayerFragment));
//		StationsTab.setTabListener(new MyTabsListener(StationsFragment));
//
//		// add the two tabs to the actionbar
//		actionbar.addTab(PlayerTab);
//		actionbar.addTab(StationsTab);
		openLayar();
	}

	
	
	@Override
	public void onClick(View v) {

		if (v.getId() == tb1.getId()){
			flagAdd = false;
			tb2.toggle();
			
		}else if (v.getId() == tb2.getId()){
			flagAdd = true;
			tb1.toggle();
		}
		
	}

	private void openLayar() {
		if (!isLayarInstalled()) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setMessage(R.string.layar_not_available);

			if (isMarketAvailable()) {
				dialog.setPositiveButton(R.string.layar_market,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(Intent.ACTION_VIEW,
										Uri.parse(MARKET_URL));
								startActivity(intent);
								finish();
							}
						});
			}

			dialog.setNegativeButton(R.string.layar_cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});

			dialog.show();
		}
	}

	private boolean isLayarInstalled() {
		try {
			getPackageManager().getApplicationInfo("com.layar",
					PackageManager.GET_META_DATA);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	private HashMap<String, String> parseJSONmenu(String json) {
		HashMap<String, String> menuMap = new HashMap<String, String>();
		JSONObject item;
		try {
			JSONObject jObj = new JSONObject(json);
			jObj.length();
			JSONArray jMenu = jObj.getJSONArray("menu");
			for (int i = 0; i < jObj.length(); i++) {
				item = jMenu.getJSONObject(i);
				menuMap.put(item.getString("Name"), item.getString("URL"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return menuMap;
	}

	private boolean isMarketAvailable() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL));
		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, 0);
		return list.size() > 0;
	}

	private static final int getSdkVersion() {
		try {
			return Integer.parseInt(Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	public float getLatitude() {
		if (location != null)
			return (float) (location.getLatitude());
		return 0;
	}

	public float getLongitude() {
		if (location != null)
			return (float) (location.getLongitude());
		return 0;
	}

//	class MyTabsListener implements ActionBar.TabListener {
//		public Fragment fragment;
//
//		public MyTabsListener(Fragment fragment) {
//			this.fragment = fragment;
//		}
//
//		@Override
//		public void onTabReselected(Tab tab, FragmentTransaction ft) {
//			Toast.makeText(Launcher.this, "Reselected!", Toast.LENGTH_LONG)
//					.show();
//		}
//
//		@Override
//		public void onTabSelected(Tab tab, FragmentTransaction ft) {
//			ft.replace(R.id.fragment_container, fragment);
//		}
//
//		@Override
//		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//			ft.remove(fragment);
//		}
//	}
//
//	public class BFragment extends Fragment {
//
//		private ListView listitems;
//
//		private ItemsAdapter adapter;
//		String mod;
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			// Inflate the layout for this fragment
//			// listitems = (ListView) getView().findViewById(R.id.listItemsTab);
//			//
//			// adapter = new ItemsAdapter(Launcher.this, R.layout.list_items,
//			// mItems);
//			// listitems.setAdapter(adapter);
//			// listitems.setOnItemClickListener(new OnItemClickListener() {
//			//
//			// @Override
//			// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//			// long arg3) {
//			//
//			// mod = mItems[arg2];
//			//
//			// }
//			// });
//			flagAdd = true;
//			return inflater.inflate(R.layout.main, container, false);
//		}
//
//
//	}
//	public class AFragment extends Fragment {
//
//		@Override
//		public View onCreateView(LayoutInflater inflater,
//				ViewGroup container, Bundle savedInstanceState) {
//			flagAdd = false;
//			return inflater.inflate(R.layout.main, container, false);
//		}

//	}
}