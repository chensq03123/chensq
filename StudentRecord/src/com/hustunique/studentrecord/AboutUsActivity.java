package com.hustunique.studentrecord;

import com.example.studentrecord.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;

public class AboutUsActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutuslayout);
		SharedPreferences sh=getSharedPreferences("SRSETTINGS",0);
		FrameLayout titlebar=(FrameLayout)findViewById(R.id.titlebar);
		
		if(!sh.getBoolean("SRISOVERUSE",false))
			titlebar.setBackgroundResource(R.drawable.bgbar);
		else
			titlebar.setBackgroundResource(R.drawable.overusetitlebar);
		ImageView backimg=(ImageView)findViewById(R.id.back);
		backimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AboutUsActivity.this.finish();
			}
		});
	}
	
}
