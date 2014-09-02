package com.hustunique.Views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.studentrecord.R;
import com.hustunique.adapters.MyTypeAdapter;

public class Mygridview extends LinearLayout{
	
	private Context mcontext;
	GridView mtypes;
	private ArrayList<Map<String,String>> list;
	
	public Mygridview(Context context,ArrayList<Map<String,String>> list) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mcontext=context;
		this.list=list;
		mtypes=(GridView) LayoutInflater.from(mcontext).inflate(R.layout.type_selections,null);
		MyTypeAdapter adapter=new MyTypeAdapter(this.mcontext, list);
		mtypes.setAdapter(adapter);
		
		this.addView(mtypes);
	}
	
	public Mygridview(Context context,AttributeSet attri,ArrayList<Map<String,String>> list){
		super(context,attri);
		this.mcontext=context;
		this.list=list;
		mtypes=(GridView) LayoutInflater.from(mcontext).inflate(R.layout.type_selections,null);
		
		mtypes=(GridView)findViewById(R.id.type_selections);
		MyTypeAdapter adapter=new MyTypeAdapter(this.mcontext, list);
		mtypes.setAdapter(adapter);
		this.addView(mtypes);
	}
	
	public	GridView getGridView(){
		if(mtypes!=null)
			return mtypes;
		return null;
	}
	
}
