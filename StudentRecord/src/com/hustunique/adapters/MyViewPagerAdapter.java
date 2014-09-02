package com.hustunique.adapters;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyViewPagerAdapter extends PagerAdapter{
	
	private ArrayList<View> mlist;
	private int currposition;
	public MyViewPagerAdapter(ArrayList<View> list){
		mlist=list;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(mlist.get(position));
	}


	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(mlist.get(position),0);
		this.currposition=position;
		return mlist.get(position);
	}
	
	public int getCurrposition(){
		return currposition;
	}
	
}
