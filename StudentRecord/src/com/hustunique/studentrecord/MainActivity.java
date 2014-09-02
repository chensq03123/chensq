package com.hustunique.studentrecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentrecord.R;
import com.hustunique.Views.MyCircle;
import com.hustunique.Views.MyViewPager;
import com.hustunique.Views.StickyLayout;
import com.hustunique.adapters.MyBaseAdapter;
import com.hustunique.adapters.MyViewPagerAdapter;
import com.hustunique.tools.DBhelper;
import com.hustunique.tools.DataConsts;

public class MainActivity extends Activity implements  StickyLayout.OnGiveUpTouchEventListener{

	private MyBaseAdapter adapter;
	private RelativeLayout progresslayout;
	private FrameLayout titlebar; 
	private ListView expenslist;
	private ArrayList<Map<String,String>> list;
	private ImageView add_btn,settingbtn,nextmonth,lastmonth;
	private TextView expanse,currentmonth,norecordimg;
	private TextView remaintext,remaindays,spenttext;
	private MyCircle circle;
	private ViewPager mviewpager;
	private ArrayList<View> viewlist;
	private boolean isfirstinitial;
	private SharedPreferences sh;
	private int[] dates;
	private int postpage;
	private int curryear,currmonth;
	private float currtotalexpanse,totalexpanse=0;
	private float remain=0;
	private int buget;
	int remainave;
	private long savedtime;
	private Toast toast;
	private boolean isoveruse=false;
	boolean pageviewtag;
	private BroadcastReceiver mreceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if(arg1.getAction().compareTo(Action)==0)
				Init();
			else if(arg1.getAction().compareTo(AddAction)==0){
				Init();
				if(dates[0]==curryear&&dates[1]==currmonth){
					adapter.Updatadata(list);
					adapter.notifyDataSetChanged();
				}
			}
		}
	};
	public static final String Action="COM.HUSTUNIQUE.UPDATEDATA";
	public static final String AddAction="COM.HUSTUNIQUE.ADDRECORD";
	private StickyLayout stlayout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.testmain);	
		////////////////////////REGISTE BROADCASTRECIEVER/////////////
		IntentFilter filter=new IntentFilter();
		filter.addAction(Action);
		filter.addAction(AddAction);
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(mreceiver, filter);
		///////////////////////Create database&table if is first run////////////////////////////////////
		
		
//////////////////////////////Initializing ViewPager///////////////////////////////////////////////////////
		ArrayList<Map<String,String>> fakelist=new ArrayList<Map<String,String>>();
		viewlist=new ArrayList<View>();
		for(int i=0;i<50;i++){
			MyViewPager vpr=new MyViewPager(MainActivity.this,fakelist);
			viewlist.add(vpr);
		}
		mviewpager=(ViewPager)findViewById(R.id.hist_list_viewpager);
		MyViewPagerAdapter madapter=new MyViewPagerAdapter(viewlist);
		mviewpager.setAdapter(madapter);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		//LinearLayout headerview=(LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.myheaderview,null);
		RelativeLayout headerview=(RelativeLayout)findViewById(R.id.progress_layout);
		LinearLayout headerview2=(LinearLayout)findViewById(R.id.headerviewsecond);
		norecordimg=(TextView)findViewById(R.id.norecord_img);
		progresslayout=(RelativeLayout)headerview.findViewById(R.id.progress_layout);
		titlebar=(FrameLayout)findViewById(R.id.titlebar);
		spenttext=(TextView)findViewById(R.id.spenttext);
		expanse=(TextView)headerview.findViewById(R.id.expanse);
		remaintext=(TextView)headerview2.findViewById(R.id.remain);
		remaindays=(TextView)headerview.findViewById(R.id.daysremian);
		
		settingbtn=(ImageView)findViewById(R.id.setting);
		settingbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,SettingActivity.class);
				startActivity(intent);
			}
		});
		/////////////////////////////////////////////////////////////////////////////////////
		//ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
		//for(int i=0;i<1000;i++){
		//	HashMap<String,String> map=new HashMap<String, String>();
		//	map.put("num",String.valueOf(i));
		//	map.put("drawid",String.valueOf(DataConsts.Mytypeico[i%9]));
		//	map.put("typename",DataConsts.Mytypetext[i%9]);
		//	list.add(map);
		//}
		/////////////////////////////////////////////////////////////////////////////////////
		add_btn=(ImageView)findViewById(R.id.add_btn);
		nextmonth=(ImageView)headerview2.findViewById(R.id.nextmonth);
		lastmonth=(ImageView)headerview2.findViewById(R.id.lastmonth);
		currentmonth=(TextView)headerview2.findViewById(R.id.date_month);
		circle=(MyCircle)headerview.findViewById(R.id.progress);
		dates=new int[3];
		Date d=new Date();
		Calendar cd=Calendar.getInstance();
		cd.setTime(d);
		curryear=dates[0]=cd.get(Calendar.YEAR);
		currmonth=dates[1]=cd.get(Calendar.MONTH)+1;
		dates[2]=cd.get(Calendar.DAY_OF_MONTH);
		if(dates[1]<10)
			currentmonth.setText("0"+String.valueOf(dates[1]));
		else
			currentmonth.setText(String.valueOf(dates[1]));
		Init();
		
		//adapter=new MyBaseAdapter(MainActivity.this,list);
		//expenslist=(ListView)findViewById(R.id.historylist);
		stlayout=(StickyLayout)findViewById(R.id.sticky_layout);
		//expenslist.addHeaderView(headerview);
		stlayout.setOnGiveUpTouchEventListener(this);
		//expenslist.addHeaderView(headerview2);
		//expenslist.setAdapter(adapter);
		mviewpager.setCurrentItem(25);
		postpage=25;
		expenslist=((MyViewPager) viewlist.get(25)).getListView();
		adapter=((MyViewPager)(viewlist.get(25))).getadapter();
		adapter.Updatadata(list);
		adapter.notifyDataSetChanged();	
		mviewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@SuppressLint("ResourceAsColor")
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				int pageflag=0;
				pageflag=arg0-postpage;
				postpage=arg0;
				if(pageflag!=0){
				dates=DataConsts.MonthJudgement(dates, pageflag);
				String sql="select * from Recordtwo where year="+dates[0]+" and month="+dates[1]+" order by day desc";
				list=DBhelper.query(sql, null);
				expenslist=((MyViewPager) viewlist.get(arg0)).getListView();
				adapter=((MyViewPager)(viewlist.get(arg0))).getadapter();
				adapter.Updatadata(list);
				adapter.notifyDataSetChanged();
				pageflag=0;
				expenslist.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						if(!isfirstinitial){
							Intent intent=new Intent(MainActivity.this,RecordDetailActivity.class);
							int[] data={Integer.valueOf(list.get(arg2).get("year")),Integer.valueOf(list.get(arg2).get("month")),Integer.valueOf(list.get(arg2).get("day"))};
							intent.putExtra("Tags", data);
							startActivity(intent);
						}
					}
				});

				expenslist.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
							final int Tag=arg2;
							if(!isfirstinitial){
							Builder bd=new AlertDialog.Builder(MainActivity.this);
							bd.setTitle("操作");
							LinearLayout mview=(LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.deletedialog,null);
							TextView delete=(TextView)mview.findViewById(R.id.deletetext);
							bd.setView(mview);
							final AlertDialog dialog=bd.create();
							delete.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									String sql="delete from Recordtwo where id="+(Integer.parseInt(list.get(Tag).get("id")));
									list.remove(Tag);
									if(list.size()==0)
										if((currmonth==dates[1]&&curryear==dates[0])){
											expanse.setText("尚无记录");
											spenttext.setVisibility(View.INVISIBLE);
										}
									DBhelper.delete(sql);
									adapter.Updatadata(list);
									adapter.notifyDataSetInvalidated();
									DataConsts.Broadcastsending(MainActivity.this);
									dialog.dismiss();
								}
							});
							Vibrator vib = (Vibrator)MainActivity.this.getSystemService(Service.VIBRATOR_SERVICE);  
//				          vibrator.vibrate(1000);//只震动一秒，一次  
				            long[] pattern = {0,50};  
				            //两个参数，一个是自定义震动模式，  
				            //数组中数字的含义依次是静止的时长，震动时长，静止时长，震动时长。。。时长的单位是毫秒  
				            //第二个是“是否反复震动”,-1 不重复震动  
				            //第二个参数必须小于pattern的长度，不然会抛ArrayIndexOutOfBoundsException  
				            vib.vibrate(pattern, 1);
							dialog.show();
							}
						return false;
					}
				});
				expenslist.setOnScrollListener(new OnScrollListener() {
					
					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1) {
						// TODO Auto-generated method stub
						if(arg1==OnScrollListener.SCROLL_STATE_FLING){
							add_btn.setAlpha(1f);
						}
						else if(arg1==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
							add_btn.setAlpha(0.3f);
						}
					}
					
					@Override
					public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
						// TODO Auto-generated method stub
						
					}
				});
				//adapter.Updatadata(list);
				//adapter.notifyDataSetInvalidated();
				if(dates[1]<10)
					currentmonth.setText("0"+String.valueOf(dates[1]));
				else
					currentmonth.setText(String.valueOf(dates[1]));
				if((currmonth==dates[1]&&curryear==dates[0])){
					if(isoveruse){
						isoveruse=true;
						remaintext.setText("亲，你超支了");
						remaintext.setTextColor(Color.RED);
					}else{
						remaintext.setText("剩下日均可用"+(int)(remainave)+"元");
						remaintext.setTextColor(Color.rgb(0xaa,0xaa,0xaa));
					}
				}else{
					int expanses=0;
					for(int i=0;i<list.size();i++)
						expanses+=Float.parseFloat(list.get(i).get("expanse"));
					remaintext.setText("总共支出 : "+String.valueOf(expanses)+"元");
					remaintext.setTextColor(Color.rgb(0xaa,0xaa,0xaa));
				}
				if(list.size()==0){
					if((currmonth==dates[1]&&curryear==dates[0])){
					expanse.setText("尚无记录");
					spenttext.setVisibility(View.INVISIBLE);}
					norecordimg.setVisibility(View.VISIBLE);
				}else{
					norecordimg.setVisibility(View.GONE);
					spenttext.setVisibility(View.VISIBLE);
				}
				}
				
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
		expenslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(!isfirstinitial){
					Intent intent=new Intent(MainActivity.this,RecordDetailActivity.class);
					int[] data={Integer.valueOf(list.get(arg2).get("year")),Integer.valueOf(list.get(arg2).get("month")),Integer.valueOf(list.get(arg2).get("day"))};
					intent.putExtra("Tags", data);
					startActivity(intent);
				}
				
			}
		});
		expenslist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
					final int Tag=arg2;
					if(!isfirstinitial){
					Builder bd=new AlertDialog.Builder(MainActivity.this);
					bd.setTitle("操作");
					LinearLayout mview=(LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.deletedialog,null);
					TextView delete=(TextView)mview.findViewById(R.id.deletetext);
					bd.setView(mview);
					final AlertDialog dialog=bd.create();
					delete.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							String sql="delete from Recordtwo where id="+(Integer.parseInt(list.get(Tag).get("id")));
							list.remove(Tag);
							if(list.size()==0)
							if((currmonth==dates[1]&&curryear==dates[0])){
								expanse.setText("无记录");
								spenttext.setVisibility(View.INVISIBLE);
							}
							DBhelper.delete(sql);
							adapter.Updatadata(list);
							adapter.notifyDataSetInvalidated();
							DataConsts.Broadcastsending(MainActivity.this);
							dialog.dismiss();
						}
					});
					Vibrator vib = (Vibrator)MainActivity.this.getSystemService(Service.VIBRATOR_SERVICE);  
//		          vibrator.vibrate(1000);//只震动一秒，一次  
		            long[] pattern = {0,50};  
		            //两个参数，一个是自定义震动模式，  
		            //数组中数字的含义依次是静止的时长，震动时长，静止时长，震动时长。。。时长的单位是毫秒  
		            //第二个是“是否反复震动”,-1 不重复震动  
		            //第二个参数必须小于pattern的长度，不然会抛ArrayIndexOutOfBoundsException  
		            vib.vibrate(pattern, 1);
					dialog.show();
					}
				return false;
			
			}
		});
		expenslist.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				if(arg1==OnScrollListener.SCROLL_STATE_FLING){
					add_btn.setAlpha(1f);
				}
				else if(arg1==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
					add_btn.setAlpha(0.3f);
				}
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		add_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,AddrecordActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_in_up,R.anim.push_out_up);
			}
		});

		nextmonth.setOnClickListener(new MyOnClickListener());
		lastmonth.setOnClickListener(new MyOnClickListener());
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mreceiver);
	}


	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		long currenttime;
		if(keyCode==KeyEvent.KEYCODE_BACK){
			
				currenttime=System.currentTimeMillis();
				if(currenttime-savedtime<=2000){
					toast.cancel();
					this.finish();
					
				}else{
				savedtime=currenttime;
				toast=Toast.makeText(MainActivity.this,"连续两次返回退出",2000);
				toast.show();
				}
			}
		return false;
	}
	
	@SuppressLint("ResourceAsColor")
	private void Init(){
		
		totalexpanse=0;
		String sql="select * from Recordtwo "+"where month="+currmonth+" and year="+curryear+" order by day desc" ;
		list=DBhelper.query(sql, null);
		for(int i=0;i<list.size();i++)
			totalexpanse+=Float.parseFloat(list.get(i).get("expanse"));
		//if((currmonth==dates[1]&&curryear==dates[0]))
			currtotalexpanse=totalexpanse;
		sh=getSharedPreferences("SRSETTINGS",0);
		buget=sh.getInt("SRBUGET",1000);
		remain=buget-totalexpanse;
		isfirstinitial=sh.getBoolean("Isfirstinitial",true);
		if(isfirstinitial){
			Map<String, String> map1=new HashMap<String, String>();
			Map<String, String> map2=new HashMap<String, String>();
			map1.put("id","-1");
			map1.put("type","欢迎使用");
			map1.put("expanse","0");
			map1.put("notes","");
			map1.put("year",String.valueOf(curryear));
			map1.put("month",String.valueOf(currmonth));
			map1.put("day",String.valueOf(dates[2]));
			map1.put("week",String.valueOf(DataConsts.CaculateWeekDay(curryear, currmonth,dates[2])));
			map2.put("id","-1");
			map2.put("type","学僧记账");
			map2.put("expanse","0");
			map2.put("notes","");
			map2.put("year",String.valueOf(curryear));
			map2.put("month",String.valueOf(currmonth));
			map2.put("day",String.valueOf(dates[2]));
			map2.put("week",String.valueOf(DataConsts.CaculateWeekDay(curryear, currmonth,dates[2])));
			list.add(map1);
			list.add(map2);
			sh.edit().putBoolean("Isfirstinitial",false).commit();
			}
		if(list.size()==0){
			norecordimg.setVisibility(View.VISIBLE);
		}else{
			norecordimg.setVisibility(View.GONE);
		}
		if(remain<0){
			progresslayout.setBackgroundResource(R.drawable.overusebgn);
			titlebar.setBackgroundResource(R.drawable.overusetitlebar);
			nextmonth.setImageResource(R.drawable.rightselector_overuse);
			lastmonth.setImageResource(R.drawable.leftselector_overuse);
			add_btn.setImageResource(R.drawable.overusebtn);
			isoveruse=true;
			sh.edit().putBoolean("SRISOVERUSE", true).commit();
			if(dates[0]==curryear&&dates[1]==currmonth){
			remaintext.setText("亲，你超支了");
			remaintext.setTextColor(Color.RED);}
		}else{
			nextmonth.setImageResource(R.drawable.rightselector);
			lastmonth.setImageResource(R.drawable.leftselector);
			progresslayout.setBackgroundResource(R.drawable.progressbackground);
			titlebar.setBackgroundResource(R.drawable.bgbar);
			add_btn.setImageResource(R.drawable.icon_add);
			isoveruse=false;
			int days=DataConsts.Daysofmont(curryear,currmonth);
			remainave=(int) (days-dates[2]!=0?(int) (remain/(days-dates[2])):remain);
			if(dates[0]==curryear&&dates[1]==currmonth){
			remaindays.setText("本月剩余"+String.valueOf(days-dates[2])+"天");
			sh.edit().putBoolean("SRISOVERUSE",false).commit();
			remaintext.setText("剩下日均可用"+(int)(remainave)+"元");
			remaintext.setTextColor(Color.rgb(0xaa,0xaa,0xaa));}
		}
		//remaintext.setText("剩余预算 "+String.valueOf(buget-totalexpanse)+"元");
		//circle.setParas(totalexpanse/buget, isoveruse);
		new MytextviewTask().execute();
		//new MyprogressTask().execute();
	}

	class MytextviewTask extends AsyncTask<Void,Integer,Void>{

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			expanse.setText("￥"+String.valueOf(currtotalexpanse));
			circle.setParas(currtotalexpanse/buget, isoveruse);
				if(currtotalexpanse==0){
					spenttext.setVisibility(View.INVISIBLE);;
					expanse.setText("尚无记录");
				}else{spenttext.setVisibility(View.VISIBLE);}
			}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
				int step=1;
				step=(int)currtotalexpanse/1000+1;
				try {
				for(int i=0;i<=currtotalexpanse;i+=step){
					if(i<currtotalexpanse/8){
						Thread.sleep(3);
					}
					else{
						Thread.sleep(1);
					}
					publishProgress(i);
				}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if(expanse!=null)
			expanse.setText("￥"+String.valueOf(values[0]));
			circle.setParas(values[0]/(float)buget, isoveruse);
		}
		
	}

	class MyOnClickListener implements OnClickListener{
		@SuppressLint("ResourceAsColor")
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int flag=0;
			switch(arg0.getId()){
			case R.id.lastmonth:flag=-1;break;
			case R.id.nextmonth:flag=1;break;
			}
			dates=DataConsts.MonthJudgement(dates, flag);
			String sql="select * from Recordtwo where year="+dates[0]+" and month="+dates[1]+" order by day desc";
			list=DBhelper.query(sql, null);
			adapter.Updatadata(list);
			adapter.notifyDataSetInvalidated();
			if(dates[1]<10)
				currentmonth.setText("0"+String.valueOf(dates[1]));
			else
				currentmonth.setText(String.valueOf(dates[1]));
			if((currmonth==dates[1]&&curryear==dates[0])){
				if(isoveruse){
					isoveruse=true;
					remaintext.setText("亲，你超支了");
					remaintext.setTextColor(Color.RED);
				}else{
					remaintext.setText("剩下日均可用"+(int)(remainave)+"元");
					remaintext.setTextColor(Color.rgb(0xaa,0xaa,0xaa));
				}
			}else{
				int expanses=0;
				for(int i=0;i<list.size();i++)
					expanses+=Float.parseFloat(list.get(i).get("expanse"));
				remaintext.setText("总共支出 : "+String.valueOf(expanses)+"元");
				remaintext.setTextColor(Color.rgb(0xaa,0xaa,0xaa));
			}
			
			if(list.size()==0){
				if((currmonth==dates[1]&&curryear==dates[0])){
				expanse.setText("尚无记录");
				spenttext.setVisibility(View.INVISIBLE);}
				norecordimg.setVisibility(View.VISIBLE);
			}else{
				norecordimg.setVisibility(View.GONE);
				spenttext.setVisibility(View.VISIBLE);
			}}
	}
	
	
	@Override
	public boolean giveUpTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(list.size()==0)
			return true;
		else if (expenslist.getFirstVisiblePosition() ==0) {  
	        View view = expenslist.getChildAt(0);  
	        if (view != null && view.getTop() >= 0) {  
	            return true;  
	        }  
		}
		return false;
	}

}

		/*package com.hustunique.studentrecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentrecord.R;
import com.hustunique.Views.MyCircle;
import com.hustunique.Views.MyListView;
import com.hustunique.Views.StickyLayout;
import com.hustunique.adapters.MyBaseAdapter;
import com.hustunique.tools.DBhelper;
import com.hustunique.tools.DataConsts;

public class MainActivity extends Activity implements  StickyLayout.OnGiveUpTouchEventListener{

	private MyBaseAdapter adapter;
	private RelativeLayout progresslayout,touchlayout;
	private FrameLayout titlebar; 
	private MyListView expenslist;
	private ArrayList<Map<String,String>> list;
	private ImageView add_btn,settingbtn,nextmonth,lastmonth,norecordimg;
	private TextView expanse,currentmonth;
	private TextView remaintext,tips,remaindays;
	ListView expandableListView;
	private MyCircle circle;
	private SharedPreferences sh;
	private int[] dates;
	private float totalexpanse=0;
	private float remain=0;
	private int buget;
	private int currmonth,curryear;
	private long savedtime;
	private Toast toast;
	private boolean isoveruse=false;
	private BroadcastReceiver mreceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if(arg1.getAction().compareTo(Action)==0)
			Init();
			else if(arg1.getAction().compareTo(AddAction)==0){
				Init();
				adapter.Updatadata(list);
				adapter.notifyDataSetChanged();
			}
		}
	};
	public static final String Action="COM.HUSTUNIQUE.UPDATEDATA";
	public static final String AddAction="COM.HUSTUNIQUE.ADDRECORD";
	private StickyLayout stlayout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.testmain);	
		////////////////////////REGISTE BROADCASTRECIEVER/////////////
		IntentFilter filter=new IntentFilter();
		filter.addAction(Action);
		filter.addAction(AddAction);
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(mreceiver, filter);
		///////////////////////Create database&table if is first run////////////////////////////////////
		DBhelper.path=this.getFilesDir().toString();
		sh=this.getSharedPreferences("Isfirstrun",0);
		boolean isfirstfun=sh.getBoolean("Isfirstrun",true);
		if(isfirstfun){
			DBhelper.createTable();
			DataConsts.InitData(MainActivity.this);
			sh.edit().putBoolean("Isfirstrun",false).commit();
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////
		//LinearLayout headerview=(LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.myheaderview,null);
		RelativeLayout headerview=(RelativeLayout)findViewById(R.id.progress_layout);
		LinearLayout headerview2=(LinearLayout)findViewById(R.id.headerviewsecond);
		norecordimg=(ImageView)findViewById(R.id.norecord_img);
		//LinearLayout headerview2=(LinearLayout)LayoutInflater.from(MainActivity.this).inflate(R.layout.myheaderviewsencond,null);
		progresslayout=(RelativeLayout)headerview.findViewById(R.id.progress_layout);
		touchlayout=(RelativeLayout)findViewById(R.id.touchlayout);
		titlebar=(FrameLayout)findViewById(R.id.titlebar);
		expanse=(TextView)headerview.findViewById(R.id.expanse);
		tips=(TextView)headerview.findViewById(R.id.tips);
		remaintext=(TextView)headerview2.findViewById(R.id.remain);
		remaindays=(TextView)headerview.findViewById(R.id.daysremian);
		
		settingbtn=(ImageView)findViewById(R.id.setting);
		settingbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,SettingActivity.class);
				startActivity(intent);
			}
		});
		/////////////////////////////////////////////////////////////////////////////////////
		//ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
		//for(int i=0;i<1000;i++){
		//	HashMap<String,String> map=new HashMap<String, String>();
		//	map.put("num",String.valueOf(i));
		//	map.put("drawid",String.valueOf(DataConsts.Mytypeico[i%9]));
		//	map.put("typename",DataConsts.Mytypetext[i%9]);
		//	list.add(map);
		//}
		/////////////////////////////////////////////////////////////////////////////////////
		add_btn=(ImageView)findViewById(R.id.add_btn);
		nextmonth=(ImageView)headerview2.findViewById(R.id.nextmonth);
		lastmonth=(ImageView)headerview2.findViewById(R.id.lastmonth);
		currentmonth=(TextView)headerview2.findViewById(R.id.date_month);
		circle=(MyCircle)headerview.findViewById(R.id.progress);
		dates=new int[3];
		Date d=new Date();
		Calendar cd=Calendar.getInstance();
		cd.setTime(d);
		curryear=dates[0]=cd.get(Calendar.YEAR);
		currmonth=dates[1]=cd.get(Calendar.MONTH)+1;
		dates[2]=cd.get(Calendar.DAY_OF_MONTH);
		if(dates[1]<10)
			currentmonth.setText("0"+String.valueOf(dates[1]));
		else
			currentmonth.setText(String.valueOf(dates[1]));
		Init();
		adapter=new MyBaseAdapter(MainActivity.this,list);
		expenslist=(MyListView)findViewById(R.id.historylist);
		stlayout=(StickyLayout)findViewById(R.id.sticky_layout);
		//expenslist.addHeaderView(headerview);
		 expandableListView=(ListView)findViewById(R.id.expandablelist);
		  ArrayList<String> slist=new ArrayList<String>();
	        for(int i=0;i<50;i++)
	        	slist.add("item"+i);
	        ArrayAdapter<String> sadapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,slist);
	       // adapter = new MyexpandableListAdapter(this);
	        expandableListView.setAdapter(sadapter);
		stlayout.setOnGiveUpTouchEventListener(this);
		//expenslist.addHeaderView(headerview2);
		expenslist.setAdapter(adapter);
		if(list.size()==0){
			
			norecordimg.setVisibility(View.VISIBLE);
		}else{
			norecordimg.setVisibility(View.GONE);
		}
		expenslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
			
					Intent intent=new Intent(MainActivity.this,RecordDetailActivity.class);
					int[] data={Integer.valueOf(list.get(arg2).get("year")),Integer.valueOf(list.get(arg2).get("month")),Integer.valueOf(list.get(arg2).get("day"))};
					intent.putExtra("Tags", data);
					startActivity(intent);
				
			}
		});
		expenslist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int Tag=arg2;
					Builder bd=new AlertDialog.Builder(MainActivity.this);
					bd.setTitle("操作");
					LinearLayout mview=(LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.deletedialog,null);
					TextView delete=(TextView)mview.findViewById(R.id.deletetext);
					bd.setView(mview);
					final AlertDialog dialog=bd.create();
					delete.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							String sql="delete from Recordtwo where id="+(Integer.parseInt(list.get(Tag).get("id")));
							list.remove(Tag);
							DBhelper.delete(sql);
							adapter.Updatadata(list);
							adapter.notifyDataSetInvalidated();
							DataConsts.Broadcastsending(MainActivity.this);
							dialog.dismiss();
						}
					});
					Vibrator vib = (Vibrator)MainActivity.this.getSystemService(Service.VIBRATOR_SERVICE);  
//		          vibrator.vibrate(1000);//只震动一秒，一次  
		            long[] pattern = {0,50};  
		            //两个参数，一个是自定义震动模式，  
		            //数组中数字的含义依次是静止的时长，震动时长，静止时长，震动时长。。。时长的单位是毫秒  
		            //第二个是“是否反复震动”,-1 不重复震动  
		            //第二个参数必须小于pattern的长度，不然会抛ArrayIndexOutOfBoundsException  
		            vib.vibrate(pattern, 1);
					dialog.show();
				return false;
			}
		});
	/*	expenslist.setOnTouchListener(new OnTouchListener() {
			
			float downx,downy,upx,upy;
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch(arg1.getAction()){
				case MotionEvent.ACTION_DOWN:{
					downx=arg1.getX();
					downy=arg1.getY();break;
				}
				case MotionEvent.ACTION_UP:{
					upx=arg1.getX();
					upy=arg1.getY();
					if(upx>downx){
						Toast.makeText(MainActivity.this,"向右",1000).show();
					}else if(upx<downx){
						Toast.makeText(MainActivity.this,"向左",1000).show();
					}
				}
				}
				return false;
			}
		});*/
		/*expenslist.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				if(arg1==OnScrollListener.SCROLL_STATE_FLING){
					add_btn.setAlpha(1f);
				}
				else if(arg1==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
					add_btn.setAlpha(0.3f);
				}
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		add_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,AddrecordActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_in_up,R.anim.push_out_up);
			}
		});
		touchlayout.setOnTouchListener(new OnTouchListener() {
			float downx,downy,upx,upy;
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch(arg1.getAction()){
				case MotionEvent.ACTION_DOWN:{
					downx=arg1.getX();
					downy=arg1.getY();break;
				}
				case MotionEvent.ACTION_UP:{
					upx=arg1.getX();
					upy=arg1.getY();
					if(upx-downx>20){
						Toast.makeText(MainActivity.this,"向右",1000).show();
					}else if(upx-downx<-20){
						Toast.makeText(MainActivity.this,"向左",1000).show();
					}
				}
				}
				return true;
			}
		});
		nextmonth.setOnClickListener(new MyOnClickListener());
		lastmonth.setOnClickListener(new MyOnClickListener());
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mreceiver);
	}


	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		long currenttime;
		if(keyCode==KeyEvent.KEYCODE_BACK){
			
				currenttime=System.currentTimeMillis();
				if(currenttime-savedtime<=2000){
					toast.cancel();
					this.finish();
					
				}else{
				savedtime=currenttime;
				toast=Toast.makeText(MainActivity.this,"连续两次返回退出",2000);
				toast.show();
				}
			}
		return false;
	}
	
	private void Init(){
		
		totalexpanse=0;
		String sql="select * from Recordtwo "+"where month="+dates[1]+" and year="+dates[0]+" order by day desc" ;
		list=DBhelper.query(sql, null);
		for(int i=0;i<list.size();i++)
			totalexpanse+=Float.parseFloat(list.get(i).get("expanse"));
		sh=getSharedPreferences("SRSETTINGS",0);
		buget=sh.getInt("SRBUGET",0);
		remain=buget-totalexpanse;
		if(remain<0){
			remaindays.setVisibility(View.GONE);
			tips.setText("亲~你超支了小伙伴们知道吗？");
			progresslayout.setBackgroundResource(R.drawable.overusebgn);
			titlebar.setBackgroundResource(R.drawable.overusetitlebar);
			add_btn.setImageResource(R.drawable.overusebtn);
			isoveruse=true;
			sh.edit().putBoolean("SRISOVERUSE", true).commit();
		}else{
			remaindays.setVisibility(View.VISIBLE);
			progresslayout.setBackgroundResource(R.drawable.progressbackground);
			titlebar.setBackgroundResource(R.drawable.bgbar);
			add_btn.setImageResource(R.drawable.icon_add);
			isoveruse=false;
			int days=DataConsts.Daysofmont(dates[0],dates[1]);
			int remainave=(int) (days-dates[2]!=0?(int) (remain/(days-dates[2])):remain);
			remaindays.setText("本月剩余"+String.valueOf(days-dates[2])+"天");
			tips.setText("日均可用"+(int)(remainave)+"元");
			sh.edit().putBoolean("SRISOVERUSE",false).commit();
		}
		remaintext.setText("剩余预算 "+String.valueOf(buget-totalexpanse)+"元");
		//circle.setParas(totalexpanse/buget, isoveruse);
		new MytextviewTask().execute();
		//new MyprogressTask().execute();
	}

	class MytextviewTask extends AsyncTask<Void,Integer,Void>{

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			expanse.setText("￥"+String.valueOf(totalexpanse));
			circle.setParas(totalexpanse/buget, isoveruse);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
				int step=1;
				step=(int)totalexpanse/1000+1;
				try {
				for(int i=0;i<=totalexpanse;i+=step){
					if(i<totalexpanse/8){
						Thread.sleep(3);
					}
					else{
						Thread.sleep(1);
					}
					publishProgress(i);
				}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if(expanse!=null)
			expanse.setText("￥"+String.valueOf(values[0]));
			circle.setParas(values[0]/(float)buget, isoveruse);
		}
		
	}
	class MyprogressTask extends AsyncTask<Void,Integer,Void>{

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			circle.setParas(totalexpanse/buget, isoveruse);
			}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
				try {
				if(!isoveruse)
				for(int i=0;i<=totalexpanse;i+=1){
					Thread.sleep(1);
					publishProgress(i);
				}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			circle.setParas(values[0]/(float)buget, isoveruse);
		}
		
	}
	class MyOnClickListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int flag=0;
			switch(arg0.getId()){
			case R.id.lastmonth:flag=-1;break;
			case R.id.nextmonth:flag=1;break;
			}
		dates=DataConsts.MonthJudgement(dates, flag);
		String sql="select * from Recordtwo where year="+dates[0]+" and month="+dates[1]+" order by day desc";
		list=DBhelper.query(sql, null);
		adapter.Updatadata(list);
		adapter.notifyDataSetInvalidated();
		if(dates[1]<10)
			currentmonth.setText("0"+String.valueOf(dates[1]));
		else
			currentmonth.setText(String.valueOf(dates[1]));
		if((currmonth==dates[1]&&curryear==dates[0])){
			remaintext.setText("剩余预算 "+String.valueOf(buget-totalexpanse)+"元");
		}else{
			int expanses=0;
			for(int i=0;i<list.size();i++)
				expanses+=Float.parseFloat(list.get(i).get("expanse"));
			remaintext.setText("总共支出 : "+String.valueOf(expanses)+"元");
		}
		
		if(list.size()==0){
			
			norecordimg.setVisibility(View.VISIBLE);
		}else{
			norecordimg.setVisibility(View.GONE);
		}
		
		}
		
	}
	
	
	@Override
	public boolean giveUpTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(list.size()==0)return true;
		if (expenslist.getFirstVisiblePosition() ==0) {  
	        View view =expenslist.getChildAt(0);  
	        if (view != null && view.getTop() >= 0) {  
	            return true;  
	        }  
	    }  
		return false;
	}

}
*/
