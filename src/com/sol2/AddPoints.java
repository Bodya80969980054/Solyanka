package com.sol2;

import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AddPoints extends Activity{
	TextView txtTitle, txtDescription, txtLon, txtLat;
	String Title, Description, Lon, Lat,Email,forms;
	Button button;
	AddMenuAsynk as = new AddMenuAsynk(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addpoint);
		txtDescription = (TextView) findViewById(R.id.txtdisk);
		txtTitle = (TextView) findViewById(R.id.txttitle);
		txtLon = (TextView) findViewById(R.id.txtLon);
		txtLat = (TextView) findViewById(R.id.txtLat);
		button = (Button) findViewById(R.id.btnSend);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Title = (String) txtTitle.getText().toString();
				Description = (String) txtDescription.getText().toString();
				Lon = (String) txtLon.getText().toString();
				Lat = (String) txtLat.getText().toString();
				  Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
				  Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
				  for (Account account :accounts)
				  {
					  if (account.name.contains("@")){
						  Email = account.name;
						 // Toast.makeText(AddPoints.this,""+ Email, Toast.LENGTH_SHORT).show();
					  break;
					  }
				  }
				 forms="http://uani.me/Gisapi/SetPOI?userid="+Email+"&layerName="+getIntent().getExtras().getString("key")+"&lon="+Lon+"&lat="+Lat+"&title="+Title+"&description="+Description;
				 Log.i("req",forms);
				 as.execute(forms);
				 finish();
				 // Toast.makeText(AddPoints.this, forms, Toast.LENGTH_SHORT).show();
			}
			
		});
		

//		 /Gisapi/SetPOI?userid=&layerName=atmskyiv&lon=50.12201&lat=40.123010&title=Tittttle&description=Dessscription
	}
	public class AddMenuAsynk extends AsyncTask<String, Void, Void>{
		  
		  @Override
		  protected Void doInBackground(String... params) {
		   DefaultHttpClient mHttpClient = new DefaultHttpClient();
		   String uri = params[0];
		   HttpGet dhttpget = new HttpGet(uri);
		   try{        
		       HttpResponse dresponse = mHttpClient.execute(dhttpget);
		   }catch (Exception e){
		    e.printStackTrace();             
		   }
		   return null;
		  }
		 }
}
