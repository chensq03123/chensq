package com.hustunique.adapters;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studentrecord.R;

public class MyTypeAdapter extends BaseAdapter
{
	
	private Context mcontext;
	private LayoutInflater minflater;
	private ArrayList<Map<String,String>> list;
	
	public MyTypeAdapter(Context context,ArrayList<Map<String,String>> list){
		this.mcontext=context;
		this.list=list;
		this.minflater=LayoutInflater.from(mcontext);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(arg1==null){
			holder=new ViewHolder();
			arg1=minflater.inflate(R.layout.typeselecting_gridview_item,null);
			holder.type_ico=(ImageView)arg1.findViewById(R.id.type_grid_ico);
			holder.type_name=(TextView)arg1.findViewById(R.id.type_grid_name);
			arg1.setTag(holder);
		}else{
			holder=(ViewHolder)arg1.getTag();
		}
		
		holder.type_ico.setImageResource(Integer.valueOf(list.get(arg0).get("drawid")));
		holder.type_name.setText(list.get(arg0).get("typename"));
		return arg1;
	}
	
	class ViewHolder{
		ImageView type_ico;
		TextView type_name;
	}
}
