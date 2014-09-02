package com.hustunique.adapters;

import com.example.studentrecord.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyWeeksBaseAdapter extends BaseAdapter{

	private Context mcontext;
	private String[] mweeks;
	private LayoutInflater minflater;
	
	public MyWeeksBaseAdapter(Context context,String[] weeks){
		this.mcontext=context;
		this.mweeks=weeks;
		this.minflater=LayoutInflater.from(mcontext);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mweeks.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mweeks[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mholder;
		if(arg1==null){
			mholder=new ViewHolder();
			arg1=minflater.inflate(R.layout.weekdate_item_layout,null);
			mholder.weekstext=(TextView)arg1.findViewById(R.id.weekdate);
			arg1.setTag(mholder);
		}else{
			mholder=(ViewHolder)arg1.getTag();
		}
		
		mholder.weekstext.setText(mweeks[arg0]);
		
		return arg1;
	}
	
	class ViewHolder{
		TextView weekstext;
	}
}
