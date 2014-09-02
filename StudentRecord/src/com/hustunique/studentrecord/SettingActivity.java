package com.hustunique.studentrecord;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.studentrecord.R;
import com.hustunique.adapters.MyWeeksBaseAdapter;
import com.hustunique.tools.DataConsts;

public class SettingActivity extends Activity {

	private RelativeLayout aboutus,datesetting,timesetting;
	private RelativeLayout bugetsetting;
	private TextView bugetset,dateset,timeset;
	private ImageView backimg;
	private Switch mswitch;
	private SharedPreferences sh;
	private boolean isalarmenable;
	private boolean[] tag={false,false,false,false,false,false,false};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settinglayout);
		
		datesetting=(RelativeLayout)findViewById(R.id.datesetting);
		timesetting=(RelativeLayout)findViewById(R.id.timesetting);
		aboutus=(RelativeLayout)findViewById(R.id.aboutus);
		//timesettingbtn=(RelativeLayout)findViewById(R.id.settingsw);
		bugetsetting=(RelativeLayout)findViewById(R.id.buggetsetting);
		bugetset=(TextView)findViewById(R.id.bugetset);
		dateset=(TextView)findViewById(R.id.dateset);
		timeset=(TextView)findViewById(R.id.timeset);
		backimg=(ImageView)findViewById(R.id.back);
		mswitch=(Switch)findViewById(R.id.settingsw);
		backimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SettingActivity.this.finish();
			}
		});
		sh=getSharedPreferences("SRSETTINGS",0);
		
		FrameLayout titlebar=(FrameLayout)findViewById(R.id.titlebar);
		if(!sh.getBoolean("SRISOVERUSE",false))
			titlebar.setBackgroundResource(R.drawable.bgbar);
		else
			titlebar.setBackgroundResource(R.drawable.overusetitlebar);
		
		int bg=sh.getInt("SRBUGET",1000);
		if(bg==0)
			bugetset.setText("还没设置预算哦~");
		else{
			bugetset.setText(String.valueOf(bg));
		}
		String ts=sh.getString("SRTIMESET","周一  周三 周五");
		int hour_set=sh.getInt("SRTIMESET_HOUR",17);
		int min_set=sh.getInt("SRTIMESET_MIN",30);
		if(ts==null)
			dateset.setText("未设置");
		else{
			dateset.setText(ts);
		}
		
		isalarmenable=sh.getBoolean("SRISALARM",true);
		if(!isalarmenable){
			dateset.setText("未启用提醒功能");
			timeset.setText("未启用提醒功能");
			datesetting.setClickable(false);
			timesetting.setClickable(false);
		}else{
			dateset.setText(ts);
			timeset.setText((hour_set<10?"0"+String.valueOf(hour_set):String.valueOf(hour_set))+":"+(min_set<10?"0"+String.valueOf(min_set):String.valueOf(min_set)));
			datesetting.setClickable(true);
			timesetting.setClickable(true);
		}
		
		aboutus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SettingActivity.this,AboutUsActivity.class);
				startActivity(intent);
			}
		});
		datesetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for(int i=0;i<tag.length;i++) tag[i]=false;
				Builder bd=new AlertDialog.Builder(SettingActivity.this);
				AlertDialog ad=bd.create();
				bd.setTitle("选择提醒时间");
				LinearLayout layout=(LinearLayout) LayoutInflater.from(SettingActivity.this).inflate(R.layout.alarmdatelayout,null);
				GridView gv=(GridView) layout.findViewById(R.id.weekgv);
				MyWeeksBaseAdapter adapter=new MyWeeksBaseAdapter(SettingActivity.this,DataConsts.weeks);
				gv.setAdapter(adapter);
				gv.setOnItemClickListener(new onItemclicklistenser());
				bd.setView(layout);
				bd.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						if(tag.length==0)
							Toast.makeText(SettingActivity.this,"请选择提示日清",2000).show();
						else{
						StringBuilder sb=new StringBuilder();
						for(int j=0;j<tag.length;j++){
							if(tag[j]){
								sb.append("  "+DataConsts.Dayofweek(j));
							}
						}
						if(sb.toString().trim().compareTo("")==0)
							sh.edit().putString("SRTIMESET","周一  周三   周五").commit();
						else
							sh.edit().putString("SRTIMESET",sb.toString()).commit();
						dateset.setText(sh.getString("SRTIMESET","周一  周三 周五"));
						}
					}
				});
				bd.setNegativeButton("取消",null);
				bd.show();
				
			}
		});
		
		mswitch.setChecked(isalarmenable);
		mswitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				sh.edit().putBoolean("SRISALARM",arg1).commit();
				if(!arg1){
					dateset.setText("未启用提醒功能");
					timeset.setText("未启用提醒功能");
					datesetting.setClickable(false);
					timesetting.setClickable(false);
				}else{
					dateset.setText(sh.getString("SRTIMESET","周一  周三 周五"));
					int hour_set=sh.getInt("SRTIMESET_HOUR",17);
					int min_set=sh.getInt("SRTIMESET_MIN",30);
					timeset.setText((hour_set<10?"0"+String.valueOf(hour_set):String.valueOf(hour_set))+":"+(min_set<10?"0"+String.valueOf(min_set):String.valueOf(min_set)));
					datesetting.setClickable(true);
					timesetting.setClickable(true);
				}
			}
		});
		
		timesetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Date date=new Date();
				Calendar cal=Calendar.getInstance();
				cal.setTime(date);
				 int hour=cal.get(Calendar.HOUR_OF_DAY);
				 int min=cal.get(Calendar.MINUTE);
				new TimePickerDialog(SettingActivity.this,new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
						// TODO Auto-generated method stub
						int hour_set=arg1;
						int min_set=arg2;
						Editor ed=sh.edit();
						ed.putInt("SRTIMESET_HOUR",hour_set);
						ed.putInt("SRTIMESET_MIN",min_set);
						ed.commit();
						timeset.setText((hour_set<10?"0"+String.valueOf(hour_set):String.valueOf(hour_set))+":"+(min_set<10?"0"+String.valueOf(min_set):String.valueOf(min_set)));
					}
				}, hour,min,true).show();
			}
		});
		bugetsetting.setOnClickListener(new onClickListener());
	}
	
	private void Bugetsetting(){
		Builder bd=new AlertDialog.Builder(SettingActivity.this);
		LinearLayout layout=(LinearLayout)LayoutInflater.from(SettingActivity.this).inflate(R.layout.bugetsetlayout,null);
		TextView btn=(TextView)layout.findViewById(R.id.comfirm);
		TextView cancel=(TextView)layout.findViewById(R.id.cancel);
		final EditText bugettext=(EditText)layout.findViewById(R.id.bugettext);
		bd.setView(layout);
		bd.setTitle("预算设置");
		final AlertDialog dialog=bd.create();
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String str=bugettext.getText().toString().trim();
				if(str.compareTo("")==0){
					Toast.makeText(SettingActivity.this,"还没输入预算哦！",2000).show();
				}else{
				int buget=Integer.parseInt(str);
				sh.edit().putInt("SRBUGET",buget).commit();
				bugetset.setText(str);
				DataConsts.Broadcastsending(SettingActivity.this);
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
	
	class onClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch(arg0.getId()){
			case R.id.datesetting:{
				break;
			}
			case R.id.settingsw:{
				break;
			}
			case R.id.buggetsetting:{
				Bugetsetting();break;
			}
			}
		}
		
	}
	
	class onItemclicklistenser implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if(tag[arg2]){
				tag[arg2]=false;
				arg1.setBackgroundColor(Color.rgb(0xff,0xff,0xff));
			}else{
				tag[arg2]=true;
				arg1.setBackgroundColor(Color.rgb(0xff,0x99,0x00));
			}
		}
		
	}
}
