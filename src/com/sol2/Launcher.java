package com.sol2;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

/**
 * Simple activity that launches the Layar application and opens the specified
 * layer. 
 * 
 * @author Ronald van der Lingen (ronald@layar.com)
 */
public class Launcher extends Activity {
  
  private static final String MARKET_URL = (getSdkVersion() > 5) ?
      "market://details?id=com.layar" : "market://search?q=pname:com.layar";
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    if (! isLayarInstalled()) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setMessage(R.string.layar_not_available);
      
      if (isMarketAvailable()) {
        dialog.setPositiveButton(R.string.layar_market, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL));
            startActivity(intent);
            finish();
          }
        });
      }
      
      dialog.setNegativeButton(R.string.layar_cancel, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          finish();
        }
      });
      
      dialog.show();
    } else {
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("layar://" + getString(R.string.layer_name)));
      startActivity(intent);
      finish();
    }
  }
  
  private boolean isLayarInstalled() {
    try {
      getPackageManager().getApplicationInfo("com.layar", PackageManager.GET_META_DATA);
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