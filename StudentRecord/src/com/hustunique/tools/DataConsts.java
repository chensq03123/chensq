package com.hustunique.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.example.studentrecord.R;
import com.hustunique.studentrecord.MainActivity;
public class DataConsts {
	
	 static int[] Mytypeico={R.drawable.clothing,R.drawable.digitalequiement,R.drawable.eating,R.drawable.entertaining,
		R.drawable.holiday,R.drawable.outting,R.drawable.phonepay,R.drawable.taobao,R.drawable.snecks,R.drawable.learning}; 
	 static String[] Mytypetext={"衣着打扮","数码装备","吃饭夜宵","聚会K歌","旅行度假","打车出行","话费充值","淘宝购物","零食饮料","学习资料"};

	public static String[] weeks={"周日","周一","周二","周三","周四","周五","周六"};
	
	public static void InitData(Context context){
		SharedPreferences Mytype=context.getSharedPreferences("Mytypes",0);
		Editor ed=Mytype.edit();
		ed.putInt("SRTYPESCOUNT",10);
		for(int i=0;i<10;i++){
			ed.putString("typeico"+i,String.valueOf(DataConsts.Mytypeico[i]));
			ed.putString("typename"+i,DataConsts.Mytypetext[i]);
			ed.commit();
		}
	}
	
	public static ArrayList<Map<String,String>> getList(Context context){
	ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
		/*for(int i=0;i<Mytypeico.length;i++){
			HashMap<String,String> map=new HashMap<String, String>();
			map.put("drawid",String.valueOf(DataConsts.Mytypeico[i]));
			map.put("typename",DataConsts.Mytypetext[i]);
			list.add(map);
		}
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("drawid", String.valueOf(R.drawable.type_add));
		map.put("typename","添加分类");
		list.add(map);*/
	SharedPreferences Mytype=context.getSharedPreferences("Mytypes",0);
	int size=Mytype.getInt("SRTYPESCOUNT",10);
	Log.i("ssssssssssssssss",String.valueOf(size));
	for(int i=0;i<size;i++){
		String drawid=Mytype.getString("typeico"+i,"");
		String typename=Mytype.getString("typename"+i,"");
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("drawid",drawid);
		map.put("typename",typename);
		list.add(map);
	}
	HashMap<String,String> map=new HashMap<String, String>();
	map.put("drawid", String.valueOf(R.drawable.heo));
	map.put("typename","添加分类");
	list.add(map);
		return list;
	}
	
	////////////Editting//////////////////////////////////////////
	public void setTypeconst(Context context){
		ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(int i=0;i<Mytypeico.length;i++){
			HashMap<String,String> map=new HashMap<String, String>();
			map.put("drawid",String.valueOf(this.Mytypeico[i]));
			map.put("typename",this.Mytypetext[i]);
			list.add(map);
		}
		SharedPreferences Mytype=context.getSharedPreferences("Mytypes",0);
		int size=Mytype.getInt("SRTYPESCOUNT",10);
		for(int i=0;i<size;i++){
			String drawid=Mytype.getString("typeico"+i,"");
			String typename=Mytype.getString("typename"+i,"");
			
		}
	}
	
	public static int CaculateWeekDay(int y,int m,int d){
        if(m==1){m=13;y--;}
        if(m==2){m=14;y--;}
        int c=y/100;
        y%=100;
        int week=(c/4-2*c+y+y/4+13*(m+1)/5+d-1)%7;
        if(week<0){week=7-(-week)%7;}
        
        return week;
        }
	
	public static  String Dayofweek(int num){
		switch(num){
		case 0:return "周日";
		case 1:return "周一";
		case 2:return "周二";
		case 3:return "周三";
		case 4:return "周四";
		case 5:return "周五";
		case 6:return "周六";
		}
		return null;
	}
	
	public static void Broadcastsending(Context mcontext){
		Intent intent=new Intent(MainActivity.Action);
		mcontext.sendBroadcast(intent);
	} 
	
	public static int[] DateJudgement(int[] date,int flag){
		
		 Date datef =null;
		 String d=null;
		 int[] newdate= new int[3];
		 Calendar cd=Calendar.getInstance();
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 String datestr=date[0]+"-"+date[1]+"-"+date[2];
		 try {
			datef=sdf.parse(datestr);
			cd.setTime(datef);
			cd.add(Calendar.DAY_OF_MONTH,flag);
			d=sdf.format(cd.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 String[] str=d.split("-");
		for(int i=0;i<str.length;i++)
			newdate[i]=Integer.parseInt(str[i]);
		 return newdate;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static int[] MonthJudgement(int[] date,int flag){
		
		 Date datef =null;
		 String d=null;
		 int[] newdate= new int[3];
		 Calendar cd=Calendar.getInstance();
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 String datestr=date[0]+"-"+date[1]+"-"+date[2];
		 try {
			datef=sdf.parse(datestr);
			cd.setTime(datef);
			cd.add(Calendar.MONTH,flag);
			d=sdf.format(cd.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 String[] str=d.split("-");
		for(int i=0;i<str.length;i++)
			newdate[i]=Integer.parseInt(str[i]);
		 return newdate;
	}
	
	public static boolean isLeapYear(int year){
		  if((year%4==0&&year%100!=0)||year%400==0)
			   return true;
			  else
			   return false;
			 }
	
	public static int Daysofmont(int year,int month){
		switch(month){
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:return 31;
		case 4:case 6:case 9:case 11:return 30;
		case 2: if(isLeapYear(year))return 29;else return 28;
		}
		return 0;
	}
}
	
