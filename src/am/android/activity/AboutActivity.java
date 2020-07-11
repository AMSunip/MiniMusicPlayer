package am.android.activity;

import am.android.consts.Const;
import am.android.manager.AnimationManagerSystem;
import am.android.manager.FileManager;
import am.android.manager.InforManager;
import am.android.minimusicplayer.R;
import am.android.util.Util;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class AboutActivity extends Activity implements OnClickListener
{	
	private ImageButton  imgbtn_comeback = null;
	private LinearLayout layout_about    = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);		
		
		init();	
		
	}

	private void init()
	{
		initView();
	}

	private void initView() 
	{
		imgbtn_comeback=(ImageButton)findViewById(R.id.imgbtn_comeback);
		imgbtn_comeback.setOnClickListener(this);
		layout_about=(LinearLayout)findViewById(R.id.layout_about);
		layout_about.setBackgroundResource(Util.getBackground(Const.THEME));	
	}

	@Override
	public void onClick(View v)
	{
		if(v==imgbtn_comeback)
		{
		  finish();
		  AnimationManagerSystem.fromFade(AboutActivity.this);
		  if(!FileManager.isSDCardExist())
		  {
			  InforManager.showInfor(AboutActivity.this, "温馨提示：检测不到SD卡");
		  }
		}
	}	
}
