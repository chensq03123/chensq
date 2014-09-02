package com.hustunique.studentrecord;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

import com.example.studentrecord.R;
import com.hustunique.tools.DBhelper;
import com.hustunique.tools.DataConsts;

public class SplashActivity extends Activity{

	private	Myasynctask task;
	private boolean isfirstrun=true;
	private TextView label;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashlayout);
		task=new Myasynctask();
		task.execute();
		DBhelper.path=this.getFilesDir().toString();
		SharedPreferences sh=this.getSharedPreferences("Isfirstrun",0);
		isfirstrun=sh.getBoolean("Isfirstrun",true);
		if(isfirstrun){
			DBhelper.createTable();
			DataConsts.InitData(SplashActivity.this);
			sh.edit().putBoolean("Isfirstrun",false).commit();
		}
		
		
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			task.cancel(true);
			this.finish();
			}
		return false;
	}
	
	class Myasynctask extends AsyncTask<Void,Integer,Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(isfirstrun)
			{
				Intent intent=new Intent(SplashActivity.this,GuidActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
			else{
				Intent intent =new Intent(SplashActivity.this,MainActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		}
		
	}
}
