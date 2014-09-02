package com.hustunique.studentrecord;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentrecord.R;
import com.hustunique.adapters.MyDetailBaseAdapter;
import com.hustunique.tools.DBhelper;
import com.hustunique.tools.DataConsts;

public class RecordDetailActivity extends Activity{

	private ListView datalistview;
	private ImageView backimg,nextday,lastday;
	private TextView count,Month,Day;
	private ArrayList<Map<String,String>> mlist;
	int[] dates;
	float expanse=0;
	MyDetailBaseAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recorddetails);
		InitWedgets();
		
		Intent intent=getIntent();
		dates=intent.getIntArrayExtra("Tags");
		String sql="select * from Recordtwo where year="+dates[0]+" and month="+dates[1]+" and day="+dates[2]+ " order by id desc";
		mlist=DBhelper.query(sql, null);
		adapter=new MyDetailBaseAdapter(RecordDetailActivity.this, mlist);
		datalistview.setAdapter(adapter);
		Month.setText(String.valueOf(dates[1]));
		Day.setText(String.valueOf(dates[2]));
		nextday.setOnClickListener(new MyOnClickListener());
		lastday.setOnClickListener(new MyOnClickListener());
		for(int i=0;i<mlist.size();i++){
			expanse+=Float.parseFloat(mlist.get(i).get("expanse"));
		}
		count.setText(String.valueOf(expanse));
	}
	
	
	private void InitWedgets(){
		datalistview=(ListView)findViewById(R.id.detail_list);
		backimg=(ImageView)findViewById(R.id.back);
		count=(TextView)findViewById(R.id.totalcount);
		Month=(TextView)findViewById(R.id.detail_month);
		Day=(TextView)findViewById(R.id.detail_day);
		lastday=(ImageView)findViewById(R.id.last_day);
		nextday=(ImageView)findViewById(R.id.next_day);
		backimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RecordDetailActivity.this.finish();
			}
		});
	
		FrameLayout titlebar=(FrameLayout)findViewById(R.id.titlebar);
		SharedPreferences sh=getSharedPreferences("SRSETTINGS",0);
		if(!sh.getBoolean("SRISOVERUSE",false))
			titlebar.setBackgroundResource(R.drawable.bgbar);
		else
			titlebar.setBackgroundResource(R.drawable.overusetitlebar);
		
	}

	class MyOnClickListener implements OnClickListener{

		ArrayList<Map<String,String>> list;
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int flag=0;
			switch(arg0.getId()){
			case R.id.last_day:flag=-1;break;
			case R.id.next_day:flag=1;break;
			}
		dates=DataConsts.DateJudgement(dates, flag);
		String sql="select * from Recordtwo where year="+dates[0]+" and month="+dates[1]+" and day="+dates[2]+ " order by id desc";
		mlist=DBhelper.query(sql, null);
		adapter.Updatadata(mlist);
		adapter.notifyDataSetInvalidated();
		Month.setText(String.valueOf(dates[1]));
		Day.setText(String.valueOf(dates[2]));
		expanse=0;
		for(int i=0;i<mlist.size();i++){
			expanse+=Float.parseFloat(mlist.get(i).get("expanse"));
		}
		count.setText(String.valueOf(expanse));
		}
		
	}
	
}
