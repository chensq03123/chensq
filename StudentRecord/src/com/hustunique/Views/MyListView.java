package com.hustunique.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.Toast;

public class MyListView extends ListView{
	float downx,downy,upx,upy;
	private Context mcontext;
	
	public MyListView(Context context) {  
		  super(context);  
		  mcontext=context;
		 }  
	public MyListView(Context context,AttributeSet attr){
			 super(context,attr);
			 mcontext=context;
		 }
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg1) {
		// TODO Auto-generated method stub
		super.onInterceptTouchEvent(arg1);
		switch(arg1.getAction()){
		case MotionEvent.ACTION_DOWN:{
			downx=arg1.getX();
			downy=arg1.getY();break;
		}
		case MotionEvent.ACTION_UP:{
			upx=arg1.getX();
			upy=arg1.getY();
			if(upx>downx){
				Toast.makeText(mcontext,"向右",1000).show();
				return false;
			}else if(upx<downx){
				Toast.makeText(mcontext,"向左",1000).show();
				return false;
			}
		}
		}
		return true;
	}
	
}
