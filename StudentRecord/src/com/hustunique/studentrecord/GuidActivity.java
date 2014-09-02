package com.hustunique.studentrecord;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.example.studentrecord.R;
import com.hustunique.adapters.MyViewPagerAdapter;
import com.hustunique.tools.FixedSpeedScroller;

public class GuidActivity extends Activity{

	private ViewPager mviewpager;
	private ArrayList<ImageView> dots;
	private ArrayList<View> viewlist;
	private int imgid[]={R.drawable.guidpic0,R.drawable.guidpic1,R.drawable.guidpic2,R.drawable.guidpic3};
	private MyAnsyctask task;
	private int currindex;
	private TextView start;
	private FixedSpeedScroller mScroller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guidlayout);
		
		dots = new ArrayList<ImageView>();
		
		ImageView img0=(ImageView)findViewById(R.id.dotone);
		dots.add(img0);
		ImageView img1=(ImageView)findViewById(R.id.dottwo);
		dots.add(img1);
		ImageView img2=(ImageView)findViewById(R.id.dotthree);
		dots.add(img2);
		ImageView img3=(ImageView)findViewById(R.id.dotfour);
		dots.add(img3);
		
		viewlist=new ArrayList<View>();
		
		for(int i=0;i<imgid.length;i++){
			ImageView view=new ImageView(GuidActivity.this);
			view.setImageResource(imgid[i]);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			view.setLayoutParams(params);
			view.setScaleType(ScaleType.FIT_XY);
			viewlist.add(view);
		}
		
		start=(TextView)findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(GuidActivity.this,MainActivity.class);
				startActivity(intent);
				GuidActivity.this.finish();
			}
		});
		
		mviewpager=(ViewPager)findViewById(R.id.contentPager);
		MyViewPagerAdapter adapter=new MyViewPagerAdapter(viewlist);
		mviewpager.setAdapter(adapter);
		
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			mScroller = new FixedSpeedScroller(mviewpager.getContext(),new AccelerateInterpolator());
			mField.set(mviewpager, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mviewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(currindex!=arg0)
					task.cancel(true);
				for(int i=0;i<dots.size();i++){
					if(i==arg0)
						dots.get(i).setImageResource(R.drawable.selected);
					else{
						dots.get(i).setImageResource(R.drawable.normal);
					}
				}
				if(arg0==3){
					start.setVisibility(View.VISIBLE);
				}else
					start.setVisibility(View.GONE);
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
		mviewpager.setCurrentItem(0);
		task=new MyAnsyctask();
		task.execute();
	}
	
	private class MyAnsyctask extends AsyncTask<Void,Integer,Void>{

		
		
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
				mviewpager.setCurrentItem(values[0]);
				mScroller.setmDuration(200);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			for(int i=0;i<4;i++){
				try {
						publishProgress(i);
						currindex=i+1;
						Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return null;
		}
		
	}
	
}
