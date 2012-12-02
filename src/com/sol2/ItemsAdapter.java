package com.sol2;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore.Action;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemsAdapter extends ArrayAdapter<String> {
	private Context mContext;
	private int mIdResource;
	private String [] mAllItems;

	public ItemsAdapter(Context context, int resource, String[] objects) 
	{
		super(context, resource, objects);
		mContext = context;
		mIdResource = resource;
		mAllItems = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Holder holder = null;
		
		if (view == null)
		{
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			view = inflater.inflate(mIdResource, parent, false);
			holder = new Holder();
			holder.imageView = (ImageView)view.findViewById(R.id.imageItems);
			holder.textView = (TextView)view.findViewById(R.id.nameItems);
			view.setTag(holder);
		}
		else
		{
			holder = (Holder)view.getTag();
		}
		String string = mAllItems[position];
		holder.textView.setText(string);
		switch (position) {
		case 0:
			holder.imageView.setImageResource(R.drawable.atmskyiv);
			break;
		case 1:
			holder.imageView.setImageResource(R.drawable.drugstoreskyiv);
			break;
		case 2:
			holder.imageView.setImageResource(R.drawable.shopskyiv);
			break;
		case 3:
			holder.imageView.setImageResource(R.drawable.gasstationskyiv);
			break;
		default:
			holder.imageView.setImageResource(R.drawable.icon);
			break;
		}
		
		return view;
	}

	static class Holder {
		ImageView imageView;
		TextView textView;
	}
}
