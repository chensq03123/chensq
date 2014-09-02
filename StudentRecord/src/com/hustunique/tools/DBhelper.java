package com.hustunique.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DBhelper {
	
	public static String path;
	public static SQLiteDatabase createorOpenDatabase(){
		SQLiteDatabase sld=null;
		try{
			sld=SQLiteDatabase.openOrCreateDatabase(path+"/StudentRecord1.db3",null);
		}catch(Exception e){e.printStackTrace();}
		return sld;
	}
	
	public static void createTable(){
		SQLiteDatabase sld=createorOpenDatabase();
		try{
			sld.execSQL("create table if not exists Recordtwo(id Integer primary key autoincrement not null,"
														+ "type varchar(15) not null,"
														+ "expanse Float not null,"
														+"notes varchar(35),"
														+ "year Integer,"
														+ "month Integer,"
														+ "day Integer,"
														+ "week Integer) ");
			sld.close();
		}catch(Exception e){}
	}
	
	public static boolean insert(Myrecord myrecord){
		SQLiteDatabase sld=createorOpenDatabase();
		ArrayList<Integer> list=myrecord.getDate();
		try{
			String sql="insert into Recordtwo(type,expanse,notes,year,month,day,week) values(\""+myrecord.getType()+"\","+myrecord.getCount()+","+"\""+myrecord.getNotes()+"\","+list.get(0)+","
					+list.get(1)+","+list.get(2)+","+list.get(3)+")";
			sld.execSQL(sql);
			sld.close();
			return true;
		}catch(Exception e){}
		return false;
	}
	
	public static boolean delete(String sql){
		SQLiteDatabase sld=createorOpenDatabase();
		try{
			sld.execSQL(sql);
			sld.close();
			return true;
		}catch(Exception e){}
		return false;
	}
	
	public static ArrayList<Map<String,String>> query(String sql,String temp){
		Vector<Vector<String>> result=query(sql);
		ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(int i=0;i<result.size();i++){
				Map<String,String> map=new HashMap<String, String>();
				map.put("id",result.get(i).get(0));
				map.put("type", result.get(i).get(1));
				map.put("expanse", result.get(i).get(2));
				map.put("notes",result.get(i).get(3));
				map.put("year", result.get(i).get(4));
				map.put("month", result.get(i).get(5));
				map.put("day", result.get(i).get(6));
				map.put("week", result.get(i).get(7));
				list.add(map);
		}
		return list;
	}
	
	public static Vector<Vector<String>> query(String sql){
		Vector<Vector<String>> vector=new Vector<Vector<String>>();
		SQLiteDatabase sld=createorOpenDatabase();
		try{
			Cursor cur=sld.rawQuery(sql, new String[] {});
			while(cur.moveToNext()){
				Vector<String> v=new Vector<String>();
				int col=cur.getColumnCount();
				for(int i=0;i<col;i++){
					v.add(cur.getString(i));
				}
				vector.add(v);
			}
			cur.close();
			sld.close();
		}catch(Exception e){}
		
		return vector;
	}
	
}
