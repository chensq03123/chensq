package com.hustunique.studentrecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentrecord.R;
import com.hustunique.Views.Mygridview;
import com.hustunique.adapters.MyViewPagerAdapter;
import com.hustunique.tools.DBhelper;
import com.hustunique.tools.DataConsts;
import com.hustunique.tools.Myrecord;

public class AddrecordActivity extends Activity{

	private ArrayList<Map<String,String>> list;
	private TextView key_one,key_two,key_three,key_four;
	private TextView key_five,key_six,key_seven,key_eight;
	private TextView key_night,key_cancel,key_zero,key_dot;
	private TextView key_ok,recording_date,current_counting,key_add,key_more;
	private EditText notes;
	private StringBuilder mstrbuilder;
	float Count=0;
	float temp=0;
	float tempcount;
	int currposition=0;
	boolean Isadding;
	final Calendar cd=Calendar.getInstance();
	private ViewPager mPager;
	private ArrayList<View> viewlist;
	private MyViewPagerAdapter madapter;
	private Myrecord myrecord;
	private ImageView backimg;
	private	LinearLayout dotlayout;
	ArrayList<ImageView> imglist;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.addrecord_layout);
		InitWedgets();
		backimg=(ImageView)findViewById(R.id.back);
		backimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddrecordActivity.this.finish();
			}
		});
		imglist.get(0).setBackgroundResource(R.drawable.selected);
		recording_date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Date date=new Date();
				cd.setTime(date);
				new DatePickerDialog(AddrecordActivity.this,new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker picker, int year, int month, int day) {
						// TODO Auto-generated method stub
						recording_date.setText(year+"年"+(month+1)+"月"+day+"日");
						myrecord.setDate(year, month+1, day,DataConsts.CaculateWeekDay(year, month+1, day));
						}
				}, cd.get(Calendar.YEAR),cd.get(Calendar.MONTH),cd.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void InitWedgets(){
		mstrbuilder=new StringBuilder();
		key_one=(TextView)findViewById(R.id.block_one);
		key_one.setOnClickListener(new onClickListener());
		key_two=(TextView)findViewById(R.id.block_two);
		key_two.setOnClickListener(new onClickListener());
		key_three=(TextView)findViewById(R.id.block_three);
		key_three.setOnClickListener(new onClickListener());
		key_four=(TextView)findViewById(R.id.block_four);
		key_four.setOnClickListener(new onClickListener());
		key_five=(TextView)findViewById(R.id.block_five);
		key_five.setOnClickListener(new onClickListener());
		key_six=(TextView)findViewById(R.id.block_six);
		key_six.setOnClickListener(new onClickListener());
		key_seven=(TextView)findViewById(R.id.block_seven);
		key_seven.setOnClickListener(new onClickListener());
		key_eight=(TextView)findViewById(R.id.block_eight);
		key_eight.setOnClickListener(new onClickListener());
		key_night=(TextView)findViewById(R.id.block_night);
		key_night.setOnClickListener(new onClickListener());
		key_cancel=(TextView)findViewById(R.id.block_cancel);
		key_cancel.setOnClickListener(new onClickListener());
		key_zero=(TextView)findViewById(R.id.block_zero);
		key_zero.setOnClickListener(new onClickListener());
		key_dot=(TextView)findViewById(R.id.block_dot);
		key_dot.setOnClickListener(new onClickListener());
		key_ok=(TextView)findViewById(R.id.block_ok);
		key_ok.setOnClickListener(new onClickListener());
		key_add=(TextView)findViewById(R.id.block_add);
		key_add.setOnClickListener(new onClickListener());
		key_more=(TextView)findViewById(R.id.block_more);
		key_more.setOnClickListener(new onClickListener());
		notes=(EditText)findViewById(R.id.recording_noting);
		
		
		FrameLayout titlebar=(FrameLayout)findViewById(R.id.titlebar);
		SharedPreferences sh=getSharedPreferences("SRSETTINGS",0);
		if(!sh.getBoolean("SRISOVERUSE",false))
			titlebar.setBackgroundResource(R.drawable.bgbar);
		else
			titlebar.setBackgroundResource(R.drawable.overusetitlebar);
		
		/////////////////////////////////Record instance initializing/////////////////////////
		myrecord=new Myrecord();
		
		recording_date=(TextView)findViewById(R.id.recording_date);
		current_counting=(TextView)findViewById(R.id.recording_count);
		
		list=DataConsts.getList(AddrecordActivity.this);
		ImageView typeselected=(ImageView) findViewById(R.id.recording_type_ico);
		typeselected.setImageResource(Integer.valueOf(list.get(0).get("drawid")));
		this.myrecord.setType(list.get(0).get("typename"));
		this.myrecord.setDate(cd.get(Calendar.YEAR),cd.get(Calendar.MONTH)+1,cd.get(Calendar.DAY_OF_MONTH),DataConsts.CaculateWeekDay(cd.get(Calendar.YEAR), cd.get(Calendar.MONTH)+1, cd.get(Calendar.DAY_OF_MONTH)));
		this.myrecord.setCount(0);
		
		
		viewlist=new ArrayList<View>();
		imglist=new ArrayList<ImageView>();
		dotlayout=(LinearLayout)findViewById(R.id.dot_layout);
		mPager=(ViewPager)findViewById(R.id.vpager);
		////////////////////Datepicker Initializing/////////////////////////////////
		Date date=new Date();
		cd.setTime(date);
		recording_date.setText(cd.get(Calendar.YEAR)+"年"+(cd.get(Calendar.MONTH)+1)+"月"+cd.get(Calendar.DAY_OF_MONTH)+"日");	
		Isadding=true;
		///////////////////Viewpager Initializing////////////////////////////////////////////
		InitViewpager();
	}
	
	private void InitViewpager(){
		list=DataConsts.getList(AddrecordActivity.this);
		for(int i=0;i<list.size()/8+1;i++){
			if(i!=list.size()/8){
			ArrayList<Map<String, String>> sublist=new ArrayList<Map<String,String>>();
			for(int j=8*i;j<8*i+8;j++){
				sublist.add(list.get(j));
			}
			Mygridview view=new Mygridview(AddrecordActivity.this,sublist);
			view.getGridView().setOnItemClickListener(new Onitemclicklistener());
			viewlist.add(view);
			}else{
				ArrayList<Map<String, String>> sublist=new ArrayList<Map<String,String>>();
				for(int j=8*i;j<list.size();j++){
					sublist.add(list.get(j));
				}
				Mygridview view=new Mygridview(AddrecordActivity.this,sublist);
				view.getGridView().setOnItemClickListener(new Onitemclicklistener());
				viewlist.add(view);
			}
		}
		//////////////////////////////////////////////////////////////////////////////
		
		madapter=new MyViewPagerAdapter(viewlist);
		mPager.setAdapter(madapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currposition=arg0;
				for(int i=0;i<imglist.size();i++)
					if(i!=currposition)
					imglist.get(i).setBackgroundResource(R.drawable.normal);
					else
						imglist.get(i).setBackgroundResource(R.drawable.selected);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		for(int j=0;j<viewlist.size();j++){
			imglist.add(new ImageView(AddrecordActivity.this));
		}
		if(list.size()>8){
		LinearLayout.LayoutParams paras=(LayoutParams) dotlayout.getLayoutParams();
		paras.setMargins(10,0,10,0);
		for(int i=0;i<imglist.size();i++){
			imglist.get(i).setLayoutParams(paras);
			imglist.get(i).setBackgroundResource(R.drawable.normal);
			dotlayout.addView(imglist.get(i));
			}
		}
	}
	
	private class Onitemclicklistener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if(8*currposition+arg2==list.size()-1){
				Addingtype();
			}else{
			ImageView typeselected=(ImageView)findViewById(R.id.recording_type_ico);
			typeselected.setImageResource(Integer.valueOf(list.get(8*currposition+arg2).get("drawid")));
			myrecord.setType(list.get(8*currposition+arg2).get("typename"));
		}
		}
		
		private void Addingtype(){
			Builder bd=new AlertDialog.Builder(AddrecordActivity.this);
			LinearLayout layout=(LinearLayout)LayoutInflater.from(AddrecordActivity.this).inflate(R.layout.addtype_layout,null);
			TextView btn=(TextView)layout.findViewById(R.id.comfirm);
			TextView cancel=(TextView)layout.findViewById(R.id.cancel);
			final EditText typetext=(EditText)layout.findViewById(R.id.typetext);
			bd.setView(layout);
			bd.setTitle("添加分类");
			final AlertDialog dialog=bd.create();
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SharedPreferences Mytype=getSharedPreferences("Mytypes",0);
					int count=Mytype.getInt("SRTYPESCOUNT",10);
					Editor ed=Mytype.edit();
					String str=typetext.getText().toString().trim();
					if(str.compareTo("")==0){
						Toast.makeText(AddrecordActivity.this,"还没输入分类哦！",2000).show();
					}else{
					ed.putString("typeico"+count,String.valueOf(R.drawable.specifiedico));
					ed.putString("typename"+count,str);
					ed.putInt("SRTYPESCOUNT",count+1);
					ed.commit();
					dotlayout.removeAllViews();
					imglist.clear();
					viewlist.clear();
					InitViewpager();
					mPager.setCurrentItem(viewlist.size()-1);
					dialog.dismiss();
					}
				}
			});
			cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}
	
	private class onClickListener implements OnClickListener{

		@SuppressLint({ "ShowToast", "NewApi" })
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch(arg0.getId()){
			case R.id.block_one:
			case R.id.block_two:
			case R.id.block_three:
			case R.id.block_four:
			case R.id.block_five:
			case R.id.block_six:
			case R.id.block_seven:
			case R.id.block_eight:
			case R.id.block_night:
			case R.id.block_zero:
			case R.id.block_dot:{
				TextView tv=(TextView)arg0;
				String text=tv.getText().toString().trim();
				if(Isadding)
				{
					 if(mstrbuilder.toString()=="0.0"){
							mstrbuilder.delete(0,mstrbuilder.length());
							mstrbuilder.append("");
						}
					if(mstrbuilder.toString().contains(".")&&text.contains("."))
						Toast.makeText(AddrecordActivity.this,"输入非法", 2000).show();
					else{
						if(mstrbuilder.toString().compareTo("")!=0&&Float.parseFloat(mstrbuilder.toString())>9999)
							Toast.makeText(AddrecordActivity.this,"输入数值过大",1000).show();
						else{
							mstrbuilder.append(text);
							current_counting.setText(mstrbuilder.toString());
						}
					}
				}
				break;
			}
			case R.id.block_add:{
				//current_counting.setText("0.0");
				key_ok.setText("=");
				if(mstrbuilder.toString().isEmpty())
					temp=0;
				else
					temp=Float.parseFloat(mstrbuilder.toString());
				Count+=temp;
				//Toast.makeText(AddrecordActivity.this,String.valueOf(temp),2000).show();
				current_counting.setText(String.valueOf(Math.round(Count*100)/100f));
				mstrbuilder.delete(0,mstrbuilder.length());
				Isadding=true;
				break;
			}
			case R.id.block_ok:{
				TextView tv=(TextView)arg0;
				if(tv.getText()=="="){
					//temp=Float.parseFloat(mstrbuilder.toString());
					float i=0;
					if(mstrbuilder.toString()!="")
						i=Float.parseFloat(mstrbuilder.toString());
					Count+=i;
					myrecord.setCount(Count);
					tempcount=0;
					mstrbuilder.delete(0,mstrbuilder.length());
					current_counting.setText(String.valueOf(Math.round(Count*100)/100f));
					tv.setText("OK");
					Isadding=false;
				}else if(tv.getText().toString().compareTo("OK")==0){
					if(!mstrbuilder.toString().isEmpty()){
						myrecord.setCount(Float.parseFloat(mstrbuilder.toString()));
						myrecord.setNotes(notes.getText().toString().trim());
						DBhelper.insert(myrecord);
						//Intent intent=new Intent(AddrecordActivity.this,MainActivity.class);
						//startActivity(intent);
						Intent intent=new Intent(MainActivity.AddAction);
						AddrecordActivity.this.sendBroadcast(intent);
						AddrecordActivity.this.finish();
						overridePendingTransition(R.anim.push_in_up, R.anim.push_out_up);
					}else if(myrecord.getCount()==0&&mstrbuilder.toString().isEmpty()){
							Toast.makeText(AddrecordActivity.this,"还没添加记录呢！",1000).show();
					}else if(myrecord.getCount()!=0){
						myrecord.setNotes(notes.getText().toString().trim());
						DBhelper.insert(myrecord);
						//Intent intent=new Intent(AddrecordActivity.this,MainActivity.class);
						//startActivity(intent);
						Intent intent=new Intent(MainActivity.AddAction);
						AddrecordActivity.this.sendBroadcast(intent);
						//AddrecordActivity.this.finish();
						finish();
						overridePendingTransition(R.anim.push_in_up, R.anim.push_out_up);
						
					}
				}
				break;
			}
			case R.id.block_cancel:{
				TextView tv=(TextView)findViewById(R.id.block_ok);
				if(Isadding)
				{
					if(mstrbuilder.toString().trim().isEmpty());
					else{
						mstrbuilder.delete(mstrbuilder.length()-1,mstrbuilder.length());
					}
					current_counting.setText(mstrbuilder.toString());
				}else if(tv.getText().toString().compareTo("OK")==0){
					myrecord.setCount(0);
					mstrbuilder.delete(0,mstrbuilder.length());
					current_counting.setText("0.0");
					Isadding=true;
				}
				break;
			}
			case R.id.block_more:{
				if(myrecord.getCount()==0&&mstrbuilder.toString().isEmpty()){
					Toast.makeText(AddrecordActivity.this,"还没添加记录呢！",1000).show();
				}else if(!mstrbuilder.toString().isEmpty()){
					myrecord.setCount(Float.parseFloat(mstrbuilder.toString()));
					myrecord.setNotes(notes.getText().toString().trim());
					DBhelper.insert(myrecord);
					mstrbuilder.delete(0,mstrbuilder.length());
					current_counting.setText("0.0");
					notes.setText("");
					}else{
					myrecord.setNotes(notes.getText().toString().trim());
					DBhelper.insert(myrecord);
					mstrbuilder.delete(0,mstrbuilder.length());
					myrecord.setCount(0);
					current_counting.setText("0.0");
					Count=0;temp=0;
					notes.setText("");
					Isadding=true;
				}
			}
		}
		}
		
	}
	
}
