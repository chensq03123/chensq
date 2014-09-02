package com.hustunique.Views;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.studentrecord.R;
import com.hustunique.adapters.MyBaseAdapter;
import com.hustunique.adapters.MyTypeAdapter;

public class MyViewPager extends LinearLayout{

	private Context mcontext;
	ListView expenslist;
	private ArrayList<Map<String,String>> list;
	private MyBaseAdapter adapter;
	
	public MyViewPager(Context context,ArrayList<Map<String,String>> list) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mcontext=context;
		this.list=list;
		expenslist=(ListView) LayoutInflater.from(mcontext).inflate(R.layout.hist_list_layout,null);
		adapter=new MyBaseAdapter(this.mcontext, list);
		expenslist.setAdapter(adapter);
		
		this.addView(expenslist);
	}
	
	
	public MyViewPager(Context context,AttributeSet attr,ArrayList<Map<String,String>> list){
		super(context,attr);
		this.mcontext=context;
		this.list=list;
		expenslist=(ListView) LayoutInflater.from(mcontext).inflate(R.layout.hist_list_layout,null);
		adapter=new MyBaseAdapter(this.mcontext, list);
		expenslist.setAdapter(adapter);
		
		this.addView(expenslist);
	}
	
	public ListView getListView(){
		if(expenslist!=null)
			return expenslist;
		return null;
	}
	
	public MyBaseAdapter getadapter(){
		if(adapter!=null)
			return adapter;
		return null;
	}
}
