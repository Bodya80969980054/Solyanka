package com.sol2;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Simple activity that launches the Layar application and opens the specified
 * layer.
 * 
 * @author Ronald van der Lingen (ronald@layar.com)
 */
public class Launcher extends Activity {

	private static final String MARKET_URL = (getSdkVersion() > 5) ? "market://details?id=com.layar"
			: "market://search?q=pname:com.layar";
	private ListView mListitems;
	private String[] mItems = { "atmskyiv", "drugstoreskyiv", "shopskyiv", "gasstationskyiv"  };
	private ItemsAdapter mAdapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Intent intent = new Intent();
//		intent.setAction(Intent.ACTION_VIEW);
		double latitude = 50.042206;
		double longitude = 30.17000;
		Uri uri = Uri.parse("layar://custom.layar.nl/login.php?lon=30.933156943889342&lat=50.373196116536235");
//		intent.setData(Uri.parse("layar://examplelayer/?action=refresh&CUSTOM_SLIDER_2=540&radius=3500"));
		intent.setData(uri);
		startActivity(intent);
		mListitems = (ListView) findViewById(R.id.listItems);
		mAdapter = new ItemsAdapter(Launcher.this, R.layout.list_items, mItems);
		mListitems.setAdapter(mAdapter);
		mListitems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				String selectedValue = (String) getListAdapter().getItem(position);
				Toast.makeText(Launcher.this, mItems[arg2],Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("layar://" + mItems[arg2]));
				startActivity(intent);
				
			}
		});
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
		} else {
			// Intent intent = new Intent(Intent.ACTION_VIEW,
			// Uri.parse("layar://" + getString(R.string.layer_name)));
			// startActivity(intent);
			// finish();
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
}