package com.hustunique.tools;

import java.util.ArrayList;

public class Myrecord {
		private float Count;
		private int Year;
		private int Month;
		private int Day;
		private int week;
		private String type;
		private String notes;
		
		public void setDate(int Year,int Month,int Day,int week){
			this.Year=Year;
			this.Month=Month;
			this.Day=Day;
			this.week=week;
		}
		
		public void setType(String type){
			this.type=type;
		}
		public void setCount(float Count){
			this.Count=Count;
		}
		
		public float getCount(){
			return Count;
		}
		
		public String getType(){
			return this.type;
		}
		
		public ArrayList<Integer> getDate(){
			ArrayList<Integer> list=new ArrayList<Integer>();
			list.add(this.Year);
			list.add(this.Month);
			list.add(this.Day);
			list.add(this.week);
			return list;
		}
		
		public void setNotes(String notes){
			this.notes=notes;
		}
		
		public String getNotes(){
			return this.notes;
		}
}
