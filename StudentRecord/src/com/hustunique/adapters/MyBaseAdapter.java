package com.hustunique.adapters;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.studentrecord.R;
import com.hustunique.tools.DataConsts;

public class MyBaseAdapter extends BaseAdapter{


	private Context mcontext;
	private ArrayList<Map<String,String>> mlist;
	//private ArrayList<Boolean> tags;
	private LayoutInflater minflater;
	
	public MyBaseAdapter(Context context ,ArrayList<Map<String,String>> list){
		this.mcontext=context;
		this.mlist=list;
		minflater=LayoutInflater.from(mcontext);
		//tags=new ArrayList<Boolean>();
		//for(int i=0;i<list.size();i++)
			//tags.add(false);
	}
	
	public void Updatadata(ArrayList<Map<String,String>> list){
		this.mlist=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(arg1==null){
			holder=new ViewHolder();
			arg1=minflater.inflate(R.layout.historlist_item,null);
			holder.date_day=(TextView)arg1.findViewById(R.id.date_day);
			holder.date_weekday=(TextView)arg1.findViewById(R.id.date_weekday);
			holder.expanseitem=(TextView)arg1.findViewById(R.id.expanse_item);
			holder.type_notes=(TextView)arg1.findViewById(R.id.type_notes);
			holder.type_text=(TextView)arg1.findViewById(R.id.type_text);
			holder.type_img=(ImageView)arg1.findViewById(R.id.type_ico);
			arg1.setTag(holder);
		}else{
			holder=(ViewHolder) arg1.getTag();
		}	
		if(arg0>0&&Integer.parseInt(mlist.get(arg0).get("day"))==Integer.parseInt(mlist.get(arg0-1).get("day"))){
			holder.date_day.setVisibility(View.INVISIBLE);
			holder.date_weekday.setVisibility(View.INVISIBLE);
		}else{
			holder.date_day.setVisibility(View.VISIBLE);
			holder.date_weekday.setVisibility(View.VISIBLE);
		}
		holder.type_text.setText(mlist.get(arg0).get("type"));
		holder.date_day.setText(mlist.get(arg0).get("day"));
		holder.expanseitem.setText(String.valueOf(Float.valueOf(mlist.get(arg0).get("expanse"))*100/100f));
		holder.date_weekday.setText(DataConsts.Dayofweek(Integer.valueOf(mlist.get(arg0).get("week"))));
		
		ArrayList<Map<String,String>> icolist=DataConsts.getList(mcontext);
		for(int i=0;i<icolist.size();i++){
			if(icolist.get(i).get("typename").compareTo(mlist.get(arg0).get("type"))==0){
				holder.type_img.setImageResource(Integer.valueOf(icolist.get(i).get("drawid")));
				break;
			}
		}
		
		if(mlist.get(arg0).get("notes").compareTo("")==0){
			holder.type_notes.setVisibility(View.GONE);
			//RelativeLayout.LayoutParams params = new 	RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.MATCH_PARENT);
			//holder.type_text.setGravity(Gravity.CENTER_VERTICAL);
			RelativeLayout.LayoutParams paras=(android.widget.RelativeLayout.LayoutParams)holder.type_text.getLayoutParams();
			paras.topMargin=23;
			holder.type_text.setLayoutParams(paras);
			
		}else{
			holder.type_notes.setVisibility(View.VISIBLE);
			holder.type_notes.setText(mlist.get(arg0).get("notes"));
			RelativeLayout.LayoutParams paras=(android.widget.RelativeLayout.LayoutParams)holder.type_text.getLayoutParams();
			RelativeLayout.LayoutParams para2=(android.widget.RelativeLayout.LayoutParams)holder.type_notes.getLayoutParams();
			paras.topMargin=12;
			para2.topMargin=1;
			holder.type_text.setLayoutParams(paras);
			holder.type_notes.setLayoutParams(para2);
		}
		
		/*TranslateAnimation manimationshow=new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f);
		arg1.setAnimation(manimationshow);
		/////////////////////////////Modified needed//////////////////////////////////////////
		int duration=0;
		int speed=60;
		if(arg0%20<=10){
			duration=arg0%20+8;
			speed=50;
		}
		else
			duration=arg0%21+1;
		
		//if(tags.get(arg0)==false){
		manimationshow.setDuration(duration*speed);
		manimationshow.startNow();
		//tags.set(arg0, true);
		//}*/
		return arg1;
	}
	
	class ViewHolder{
		TextView date_day;
		TextView date_weekday;
		TextView type_text;
		TextView type_notes;
		TextView expanseitem;
		ImageView type_img;
		//Button   delete;
	}
}
