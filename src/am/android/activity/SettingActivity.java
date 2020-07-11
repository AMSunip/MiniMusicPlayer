package am.android.activity;

import am.android.consts.Const;
import am.android.manager.AnimationManagerSystem;
import am.android.manager.DatabaseManager;
import am.android.manager.FileManager;
import am.android.manager.InforManager;
import am.android.manager.MyNotificationManager;
import am.android.minimusicplayer.MiniMusicPlayerActivity;
import am.android.minimusicplayer.R;
import am.android.util.Util;
import am.android.view.CheckImageView;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SettingActivity extends Activity  implements OnClickListener
{
	private Button              btn_backtowindows   = null;
	private Button              btn_exit            = null;
	private Button              btn_save            = null;
	
	private CheckImageView      civ_startplaying    = null;
	
	private ImageButton         imgbtn_comeback     = null;
	
	private LinearLayout        layout_setting      = null;
	
	private Notification        notification        = null;         
    private NotificationManager notificationManager = null; 
    
    private PendingIntent       pendingIntent       = null;
	
    private RadioButton         rb_allcycle         = null;
    private RadioButton         rb_allplay          = null;
    private RadioButton         rb_shuffle          = null;
	private RadioButton         rb_singlecycle      = null;
	private RadioButton         rb_singleplay       = null;
	private RadioButton         rb_theme_blackcool  = null;
	private RadioButton         rb_theme_classical  = null;
	private RadioButton         rb_theme_original   = null;
	private RadioButton         rb_theme_refresh    = null;
	
	private RadioGroup          rg_playmode         = null;
	private RadioGroup          rg_themesetting     = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
			
		initView();
		initNotification();	
		initRadioGroup();
	}
	
	private void initView()
	{
		layout_setting=(LinearLayout)findViewById(R.id.layout_setting);
		imgbtn_comeback=(ImageButton)findViewById(R.id.imgbtn_comeback);
		imgbtn_comeback.setOnClickListener(this);
		btn_save=(Button)findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);
		btn_exit=(Button)findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(this);
		btn_backtowindows=(Button)findViewById(R.id.btn_backtowindows);
		btn_backtowindows.setOnClickListener(this);
		
		civ_startplaying=(CheckImageView)findViewById(R.id.civ_startplaying);
		civ_startplaying.setOnClickListener(this);
		
		rg_themesetting=(RadioGroup)findViewById(R.id.rg_themesetting);
		rb_theme_original=(RadioButton)findViewById(R.id.rb_theme_original);
		rb_theme_classical=(RadioButton)findViewById(R.id.rb_theme_classical);
		rb_theme_refresh=(RadioButton)findViewById(R.id.rb_theme_refresh);
		rb_theme_blackcool=(RadioButton)findViewById(R.id.rb_theme_blackcool);
		
		rg_playmode=(RadioGroup)findViewById(R.id.rg_playmode);
		rb_singleplay=(RadioButton)findViewById(R.id.rb_singleplay);
		rb_singlecycle=(RadioButton)findViewById(R.id.rb_singlecycle);
		rb_allplay=(RadioButton)findViewById(R.id.rb_allplay);
		rb_allcycle=(RadioButton)findViewById(R.id.rb_allcycle);
		rb_shuffle=(RadioButton)findViewById(R.id.rb_shuffle);	
		
		layout_setting.setBackgroundResource(Util.getBackground(Const.THEME));
		

		rg_themesetting.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				
				if(checkedId == rb_theme_classical.getId())
				{
					//���⣺�ŵ仳��
					Const.THEME = "1";
				}
				else if(checkedId == rb_theme_refresh.getId())
				{
					//���⣺�λ�����
					Const.THEME = "2";
				}
				else if(checkedId == rb_theme_blackcool.getId())
				{
					//���⣺�λ�����
					Const.THEME = "3";
				}
				else
				{
					//Ĭ�����⣺���¼�Լ
					Const.THEME = "0";
				}
				
				saveSettings();	
	
			}
		});
		
		
		rg_playmode.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				if(checkedId == rb_singlecycle.getId())
				{
					//����ģʽ������ѭ��
					Const.PLAYMODE = "1";
				}
				else if(checkedId == rb_allplay.getId())
				{
					//����ģʽ��˳�򲥷�
					Const.PLAYMODE = "2";
				}
				else if(checkedId == rb_allcycle.getId())
				{
					//����ģʽ���б�ѭ��
					Const.PLAYMODE = "3";
				}
				else if(checkedId == rb_shuffle.getId())
				{
					//����ģʽ���������
					Const.PLAYMODE = "4";
				}
				else
				{
					//��������
					Const.PLAYMODE = "0";
				}
				
				setCurrentModeString(Const.PLAYMODE);
				saveSettings();	
				
			}
		});
		
		
		
		
	}
	
	
	private void initRadioGroup()
	{	
		/******��������******/
		if(Const.STARTPLAYING.equals("1"))
		{
			civ_startplaying.setChecked(true);
		}
		else
		{
			civ_startplaying.setChecked(false);
		}
		/******��������******/
		
		/******��������******/
		if(Const.THEME.equals("1"))
		{
			//�ŵ仳��
			rg_themesetting.check(rb_theme_classical.getId());
		}
		else if(Const.THEME.equals("2"))
		{
			//�λ�����
			rg_themesetting.check(rb_theme_refresh.getId());
		}
		else if(Const.THEME.equals("3"))
		{
			//�ſἫҹ
			rg_themesetting.check(rb_theme_blackcool.getId());
		}
		else
		{
			//���¼�Լ
			rg_themesetting.check(rb_theme_original.getId());
		}
		/******��������******/
		
		/******����ģʽ******/
		if(Const.PLAYMODE.equals("1"))
		{
			rg_playmode.check(rb_singlecycle.getId());
		}
		else if(Const.PLAYMODE.equals("2"))
		{
			rg_playmode.check(rb_allplay.getId());
		}
		else if(Const.PLAYMODE.equals("3"))
		{
			rg_playmode.check(rb_allcycle.getId());
		}
		else if(Const.PLAYMODE.equals("4"))
		{
			rg_playmode.check(rb_shuffle.getId());
		}
		else
		{
			rg_playmode.check(rb_singleplay.getId());
		}	
		/******����ģʽ******/
	}
	
	
	private void initNotification()
	{
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
	    notification = new Notification();   
	    Intent intent = new Intent(this,MiniMusicPlayerActivity.class); 
	    notification.icon = R.drawable.app_icon;  
	    notification.tickerText = "���������ֲ�����������ģʽ";  
	    pendingIntent=PendingIntent.getActivity(SettingActivity.this, 0, intent, 0);
	}

	
	@Override
	public void onClick(View v) 
	{
		if(v.getId() == imgbtn_comeback.getId())
		{
		  finish();
		  
		  if(!FileManager.isSDCardExist())
		  {
			  InforManager.showInfor(SettingActivity.this, "��ܰ��ʾ����ⲻ��SD��");
		  }
		  	 
			 AnimationManagerSystem.fromFade(SettingActivity.this);
		 	  	  
		}
		
		if(v.getId()==btn_save.getId())
		{
			saveSettings();
			InforManager.showInfor(SettingActivity.this, "��ܰ��ʾ�����ñ���ɹ�");
		}
		
		if(v.getId() == civ_startplaying.getId())
	    {
	        if(civ_startplaying.getChecked().equals("1"))
	        {
	        	civ_startplaying.setChecked(false);
	        	
	        }
	        else
	        {
	        	civ_startplaying.setChecked(true);
	        }
	        
	        saveSettings();
	    }
		
		if(v.getId() == btn_exit.getId())
		{	
			setResult(RESULT_OK);
			finish();
		}	
			
		if(v.getId() == btn_backtowindows.getId())
		{
			finish();
			MyNotificationManager.setNotificationSound(SettingActivity.this,notification,notificationManager,pendingIntent,"�������������","�������ֲ�����");
			Intent intent=new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
		}	
	}
	
	private void saveSettings()
	{
		if(FileManager.isSDCardExist())
		{
		    Const.STARTPLAYING = civ_startplaying.getChecked();
		    layout_setting.setBackgroundResource(Util.getBackground(Const.THEME));   
		    DatabaseManager.writerDataToSettingFile(SettingActivity.this,Const.STARTPLAYING,Const.THEME,Const.PLAYMODE);
		}
		else
		{
			layout_setting.setBackgroundResource(Util.getBackground(Const.THEME));   
			InforManager.showInfor(SettingActivity.this, "��ܰ��ʾ����ⲻ��SD��");
		}
	}
	
	
	
	private void setCurrentModeString(String  result)
	{
		if(result.equals("1"))
		{
			Const.isSinglePlay=false;
			Const.isSingleCycle=true;
			Const.isAllPlay=false;
			Const.isAllCycle=false;
			Const.isShuffle=false; 
		}
		else if(result.equals("2"))
		{
			Const.isSinglePlay=false;
			Const.isSingleCycle=false;
			Const.isAllPlay=true;
			Const.isAllCycle=false;
			Const.isShuffle=false; 
		}		
		else if(result.equals("3"))
		{
			Const.isSinglePlay=false;
			Const.isSingleCycle=false;
			Const.isAllPlay=false;
			Const.isAllCycle=true;
			Const.isShuffle=false; 
		}
		else if(result.equals("4"))
		{
			Const.isSinglePlay=false;
			Const.isSingleCycle=false;
			Const.isAllPlay=false;
			Const.isAllCycle=false;
			Const.isShuffle=true; 
		}
		else
		{
			Const.isSinglePlay=true;
			Const.isSingleCycle=false;
			Const.isAllPlay=false;
			Const.isAllCycle=false;
			Const.isShuffle=false; 
		}	
	}	
	
	
}
