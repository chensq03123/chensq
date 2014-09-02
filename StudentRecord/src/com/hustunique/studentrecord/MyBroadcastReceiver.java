package com.hustunique.studentrecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.studentrecord.R;
import com.hustunique.tools.DBhelper;
import com.hustunique.tools.DataConsts;

public class MyBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().compareTo("android.net.conn.CONNECTIVITY_CHANGE")==0){
			datedetection(context);
		}else if(intent.getAction().compareTo("com.hustunique.ALARMRECORD")==0){
			onAlarm(context);
		}
	}
	
	private void datedetection(Context context){
		Date date=new Date();
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		SharedPreferences sh=context.getSharedPreferences("SRSETTINGS",0);
		int hour_set=sh.getInt("SRTIMESET_HOUR",17);
		int min_set=sh.getInt("SRTIMESET_MIN",30);
		//boolean isalarmenable=sh.getBoolean("SRISALARM",true);
		String saveddates=sh.getString("SRTIMESET","周一周三周五");
		String currentdate=DataConsts.Dayofweek(c.get(Calendar.DAY_OF_WEEK));
		if(saveddates.contains(currentdate)){
			Calendar cl=Calendar.getInstance();
			cl.set(Calendar.HOUR_OF_DAY,hour_set);
			cl.set(Calendar.MINUTE, min_set);
			cl.set(Calendar.SECOND,0);
			Intent intent=new Intent("com.hustunique.ALARMRECORD");
			PendingIntent pi=PendingIntent.getBroadcast(context, 0, intent,0);
	        //设置一个PendingIntent对象，发送广播
	        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        //获取AlarmManager对象
	        am.set(AlarmManager.RTC_WAKEUP,cl.getTimeInMillis(), pi);
		}
	}
	
	private void onAlarm(Context context){
		//addNotificaction(context);
		SharedPreferences sh=context.getSharedPreferences("SRSETTINGS",0);
		boolean isalarmenable=sh.getBoolean("SRISALARM",true);
		if(isalarmenable){
		Date date=new Date();
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		int year=c.get(Calendar.YEAR);
		int month=c.get(Calendar.MONTH)+1;
		int day=c.get(Calendar.DAY_OF_MONTH);
		String sql="select * from Recordtwo where year="+year+" and month="+month+" and day="+day+ " order by id desc";
		ArrayList<Map<String,String>> mlist=DBhelper.query(sql, null);
		
		if(mlist.size()==0){
			addNotificaction(context);
		}
		}
	}
	
	private void addNotificaction(Context context) {  
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);  
        // 创建一个Notification  
        Notification notification = new Notification();  
        // 设置显示在手机最上边的状态栏的图标  
        notification.icon =R.drawable.logo;  
        // 当当前的notification被放到状态栏上的时候，提示内容  
        notification.tickerText = "学僧记账有新通知";  
          
        /*** 
         * notification.contentIntent:一个PendingIntent对象，当用户点击了状态栏上的图标时，该Intent会被触发 
         * notification.contentView:我们可以不在状态栏放图标而是放一个view 
         * notification.deleteIntent 当当前notification被移除时执行的intent 
         * notification.vibrate 当手机震动时，震动周期设置 
         */ 
        long[] vibreate= new long[]{0,150,150,150}; 
        notification.vibrate=vibreate;
        // 添加声音提示  
        notification.defaults=Notification.DEFAULT_SOUND;  
        // audioStreamType的值必须AudioManager中的值，代表着响铃的模式  
        notification.audioStreamType= android.media.AudioManager.ADJUST_LOWER;  
        notification.flags|=Notification.FLAG_AUTO_CANCEL;
          
        //下边的两个方式可以添加音乐  
        //notification.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");   
        //notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");   
        Intent intent = new Intent(context, AddrecordActivity.class);  
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);  
        // 点击状态栏的图标出现的提示信息设置  
        notification.setLatestEventInfo(context, "学僧记账", "亲~今天还没记账呢，点我记账~", pendingIntent);  
        manager.notify(1, notification);  
          
    }  
}
