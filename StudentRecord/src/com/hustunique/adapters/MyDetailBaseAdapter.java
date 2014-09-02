package com.hustunique.adapters;

import java.util.ArrayList;
import java.util.Map;

import com.example.studentrecord.R;
import com.hustunique.tools.DataConsts;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyDetailBaseAdapter extends BaseAdapter{

	private Context mcontext;
	private ArrayList<Map<String,String>> list;
	private LayoutInflater minflater;
	
	public MyDetailBaseAdapter(Context context,ArrayList<Map<String,String>> list){
		this.mcontext=context;
		this.list=list;
		minflater=LayoutInflater.from(mcontext);
	}
	
	public void Updatadata(ArrayList<Map<String,String>> list){
		this.list=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mholder;
		
		if(arg1==null){
			mholder=new ViewHolder();
			arg1=minflater.inflate(R.layout.detail_list_item,null);
			mholder.img=(ImageView)arg1.findViewById(R.id.detail_type_ico);
			mholder.type_text=(TextView)arg1.findViewById(R.id.detail_type_text);
			mholder.type_notes=(TextView)arg1.findViewById(R.id.detail_type_notes);
			mholder.expanse=(TextView)arg1.findViewById(R.id.detail_expanse_item);
			arg1.setTag(mholder);
		}else{
			mholder=(ViewHolder) arg1.getTag();
		}
			
		mholder.type_text.setText(list.get(arg0).get("type"));
		mholder.expanse.setText(String.valueOf(Float.parseFloat(list.get(arg0).get("expanse"))));
		
		ArrayList<Map<String,String>> icolist=DataConsts.getList(mcontext);
		for(int i=0;i<icolist.size();i++){
			if(icolist.get(i).get("typename").compareTo(list.get(arg0).get("type"))==0){
				mholder.img.setImageResource(Integer.valueOf(icolist.get(i).get("drawid")));
				break;
			}
		}
		
		if(list.get(arg0).get("notes").compareTo("")==0){
			mholder.type_notes.setVisibility(View.GONE);
			//RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.MATCH_PARENT);
			//mholder.type_text.setGravity(Gravity.CENTER_VERTICAL);
			//mholder.type_text.setLayoutParams(params);
			RelativeLayout.LayoutParams paras=(android.widget.RelativeLayout.LayoutParams)mholder.type_text.getLayoutParams();
			paras.topMargin=32;
			mholder.type_text.setLayoutParams(paras);
		}else{
			mholder.type_notes.setVisibility(View.VISIBLE);
			mholder.type_notes.setText(list.get(arg0).get("notes"));
			RelativeLayout.LayoutParams paras=(android.widget.RelativeLayout.LayoutParams)mholder.type_text.getLayoutParams();
			RelativeLayout.LayoutParams para2=(android.widget.RelativeLayout.LayoutParams)mholder.type_notes.getLayoutParams();
			paras.topMargin=12;
			para2.topMargin=1;
			mholder.type_text.setLayoutParams(paras);
			mholder.type_notes.setLayoutParams(para2);
		}
		
		TranslateAnimation manimationshow=new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f);
		arg1.setAnimation(manimationshow);
		/////////////////////////////Modified needed//////////////////////////////////////////
		int duration=0;
		if(arg0%20<10)
			duration=arg0%20+10;
		else
			duration=arg0%21+1;
		manimationshow.setDuration(duration*60);
		manimationshow.startNow();
		
		return arg1;
	}
	
	class ViewHolder {
		ImageView img;
		TextView type_text;
		TextView type_notes;
		TextView expanse;
	}

}
