package am.android.minimusicplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import am.android.activity.AboutActivity;
import am.android.activity.SettingActivity;
import am.android.adapter.MyViewPagerAdapter;
import am.android.adapter.PlayListAdapter;
import am.android.consts.Const;
import am.android.dialog.MyDialogDelete;
import am.android.dialog.MyDialogRefresh;
import am.android.help.HomeListener;
import am.android.interfaces.MyOnClickListener;
import am.android.interfaces.OnHomePressedListener;
import am.android.manager.AnimationManager;
import am.android.manager.AnimationManagerSystem;
import am.android.manager.DatabaseManager;
import am.android.manager.FileManager;
import am.android.manager.FormatManager;
import am.android.manager.InforManager;
import am.android.manager.MyNotificationManager;
import am.android.util.StorageList;
import am.android.view.SlidingMenu;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MiniMusicPlayerActivity extends Activity implements OnClickListener
{
	
	private ArrayList<String>   listdata_titleList      = null;
	private ArrayList<View>     listdata_listViews      = null;
	private AudioManager        audioManager            = null;
	
    private Handler             handler                 = new Handler();
	private HomeListener        homelistener            = null;
    
	private ImageButton         imgbtn_main_playlist    = null;
	private ImageButton         imgbtn_mode             = null;
	private ImageButton         imgbtn_next             = null;
	private ImageButton         imgbtn_pause            = null;
    private ImageButton         imgbtn_play             = null; 
	private ImageButton         imgbtn_playlist_refresh = null;
	private ImageButton         imgbtn_previous         = null;
  
	private int                 volumeProgress          = 0;
	
	private LinearLayout        layout_menu             = null;
	private LinearLayout        layout_control_seekbar  = null;
	
    private ListView            listview_playlist       = null;
  
	private MyDialogDelete      myDialogDelete          = null;
	private MyDialogRefresh     myDialogRefresh         = null;

	private MediaPlayer         mediaPlayer             = new MediaPlayer();
	private MyViewPagerAdapter   view_adapter            = null;
    
    private Notification        notification            = null;         
    private NotificationManager notificationManager     = null;  
    
    private PendingIntent       pendingIntent           = null;
	private PagerTabStrip       pagerTabStrip           = null;
	private PlayListAdapter     adapter_playlist        = null;
	
	private RelativeLayout      layout_main             = null;
	
	private Runnable            UpdateTimeTask          = new Runnable() 
	{
        public void run() 
        { 	
       	    long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();
            int percent=(int) (100*currentDuration/totalDuration);
            progressProgressBar.setMax(100);
            progressProgressBar.setProgress(percent);              
            tv_time_end.setText(FormatManager.getTimeFormat(totalDuration));
            tv_time_start.setText(FormatManager.getTimeFormat(currentDuration));
            handler.postDelayed(this, 100);
        }
	 };
	 
	private Runnable            UpdateVolumeTask        = new Runnable()
	{
		@Override
		public void run() 
		{
			handler.postDelayed(UpdateVolumeTask, 500);		
			volumeProgress=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		    volumeProgressBar.setProgress(volumeProgress);			
		}
		
	};
	
	private SeekBar             volumeProgressBar       = null;
	private SeekBar             progressProgressBar     = null;
	private SlidingMenu         view_slidingMenu        = null;
	private StorageList         list_storage            = null;
	
	private TextView            tv_about                = null;
	private TextView            tv_currentmode          = null;
	private TextView            tv_currentProgress      = null;
	private TextView            tv_currentsong          = null;
	private TextView            tv_currentvolume        = null;
	private TextView            tv_exit                 = null;
	private TextView            tv_mainpage             = null;
    private TextView            tv_mini_playlist        = null;
	private TextView            tv_playlist             = null;
	private TextView            tv_setting              = null;
	private TextView            tv_time_end             = null; 
	private TextView            tv_time_start           = null;

	private View                view1                   = null;
	private View                view2                   = null;
	private ViewPager           viewPager               = null;	
  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mini_music_player);
		
		init(); 	
	}


	private void init()
	{
		//初始化时，先判断SD卡是否存在
		//音频文件一般需要存在于SD卡上才能被扫描到，一般用户没有权限访问系统内部存储
		
		//当SD卡存在时
		if(FileManager.isSDCardExist())
		{
		    FileManager.makeDirectory();
		    FileManager.makeFile();	  
		    initView();	
		    initVolume();
		    initNotification();		
		    initDatabaseFile();		
		    setListener();
		}
		//当SD卡不存在时
		else
		{
			initView();
			initNotification();	
			setImgbtnEnable(false);
			InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
		}
	}
	
	
	private void initView()
	{	
		view_slidingMenu = (SlidingMenu) findViewById(R.id.slide_menu);	
		layout_main=(RelativeLayout)findViewById(R.id.layout_main);
	
		layout_menu=(LinearLayout)findViewById(R.id.layout_menu);
		tv_mainpage=(TextView)findViewById(R.id.tv_mainpage);
		tv_mainpage.setOnClickListener(this);
		tv_playlist=(TextView)findViewById(R.id.tv_playlist);
		tv_playlist.setOnClickListener(this);
		tv_setting=(TextView)findViewById(R.id.tv_setting);
		tv_setting.setOnClickListener(this);
		tv_about=(TextView)findViewById(R.id.tv_about);
		tv_about.setOnClickListener(this);
		tv_exit=(TextView)findViewById(R.id.tv_exit);
		tv_exit.setOnClickListener(this);
		
		imgbtn_main_playlist=(ImageButton)findViewById(R.id.imgbtn_main_playlist);
		imgbtn_main_playlist.setOnClickListener(this);
		tv_mini_playlist=(TextView)findViewById(R.id.tv_mini_playlist);
	    imgbtn_playlist_refresh=(ImageButton)findViewById(R.id.imgbtn_playlist_refresh);
		imgbtn_playlist_refresh.setOnClickListener(this);
		
		viewPager = (ViewPager) findViewById(R.id.viewpager); 
		pagerTabStrip=(PagerTabStrip) findViewById(R.id.pagertab); 
		pagerTabStrip.setDrawFullUnderline(false); 
		pagerTabStrip.setTextSpacing(50); 
			
		LayoutInflater inflater=LayoutInflater.from(MiniMusicPlayerActivity.this);
		view1=inflater.inflate(R.layout.item_control, null);
		view2=inflater.inflate(R.layout.item_playlist, null);    
		listdata_listViews = new ArrayList<View>();
		listdata_listViews.add(view1);  
		listdata_listViews.add(view2);   
	    listdata_titleList = new ArrayList<String>();
	    listdata_titleList.add("迷你音乐播放器");  
	    listdata_titleList.add("播放列表"); 
	    
	    layout_control_seekbar=(LinearLayout)view1.findViewById(R.id.layout_control_seekbar);
		volumeProgressBar=(SeekBar)view1.findViewById(R.id.volumeProgressBar);
    	progressProgressBar=(SeekBar)view1.findViewById(R.id.progressProgressBar);
    	tv_currentmode=(TextView)view1.findViewById(R.id.tv_currentmode);
    	tv_currentmode.setOnClickListener(this);
    	tv_currentsong=(TextView)view1.findViewById(R.id.tv_currentsong);
    	tv_currentsong.setOnClickListener(this);
    	tv_time_start=(TextView)view1.findViewById(R.id.tv_time_start);
    	tv_time_end=(TextView)view1.findViewById(R.id.tv_time_end);
    	tv_currentvolume=(TextView)view1.findViewById(R.id.tv_currentvolume);
    	tv_currentProgress=(TextView)view1.findViewById(R.id.tv_currentProgress);
	        	
		imgbtn_previous=(ImageButton)view1.findViewById(R.id.imgbtn_play_previous);
		imgbtn_previous.setOnClickListener(this);
		imgbtn_next=(ImageButton)view1.findViewById(R.id.imgbtn_play_next);
		imgbtn_next.setOnClickListener(this);
		imgbtn_mode=(ImageButton)view1.findViewById(R.id.imgbtn_play_mode);
		imgbtn_mode.setOnClickListener(this);
		imgbtn_pause=(ImageButton)view1.findViewById(R.id.imgbtn_play_pause);
		imgbtn_pause.setOnClickListener(this);
		imgbtn_play=(ImageButton)view1.findViewById(R.id.imgbtn_play_play);
		imgbtn_play.setOnClickListener(this);
		imgbtn_play.setImageResource(R.drawable.imgbtn_play_play_a);
      
		listview_playlist=(ListView)view2.findViewById(R.id.listview_playlist);
	    toTabControl();	
	     
	    view_adapter=new MyViewPagerAdapter(listdata_listViews, listdata_titleList);
	        
	    viewPager.setAdapter(view_adapter);    
	      
	    registerHomeListener();
	    
	}
	
	
	private void initVolume()
	{
		/******音量控制******/
		audioManager=(AudioManager)MiniMusicPlayerActivity.this.getSystemService(Context.AUDIO_SERVICE);
		MiniMusicPlayerActivity.this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		volumeProgressBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
	    volumeProgress=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		volumeProgressBar.setProgress(volumeProgress);
		/******音量控制******/
		UpdateVolumeTask.run();
	}
	
	
	private void initNotification()
	{
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
	    notification = new Notification();   
	    Intent intent = new Intent(this,MiniMusicPlayerActivity.class); 
	    notification.icon = R.drawable.app_icon;  
	    notification.tickerText = "【迷你音乐播放器】桌面模式";  
	    pendingIntent =PendingIntent.getActivity(MiniMusicPlayerActivity.this, 0, intent, 0);
	}
	
	
	private void initDatabaseFile() 
	{
		DatabaseManager.fileExist(MiniMusicPlayerActivity.this,"settings2.db");
		
		if(Const.file_settings.exists())
		{
		  DatabaseManager.getMySettings();
		  setPlayMode(Integer.valueOf(Const.PLAYMODE));
		}
		else
		{
		  setPlayMode(3);	
		}
		
		setInitImgbtnBackground();
		
		if(Const.file_playlist.exists())
		{
			refreshPlayListData();
			
			if(Const.listdata_playlist.size()>0)
			{
			   if(Const.STARTPLAYING.equals("1"))
			   {
			     playSong(0);
			   }
			}
			else
			{
				InforManager.showInfor(MiniMusicPlayerActivity.this,"温馨提示：没有找到本地音乐");
				setImgbtnEnable(false);
			}
		}
		
		else
		{
			Const.listdata_playlist.clear();
			audioList();
		
			if(Const.listdata_playlist.size()>0)
			{
				if(Const.STARTPLAYING.equals("1"))
			    {
				   playSong(0);
				}
			}			
			DatabaseManager.createPlayList(MiniMusicPlayerActivity.this,Const.listdata_playlist);		
		}
	}
	
	
	private void setListener() 
	{
		
		 viewPager.setOnPageChangeListener(new OnPageChangeListener()
	     {
				@Override
				public void onPageSelected(int position) 
				{
				  if(viewPager.getCurrentItem()==0)
				  {
					  imgbtn_main_playlist.setBackgroundResource(R.drawable.app_icon);
					  tv_mini_playlist.setText("迷你音乐播放器");
				      imgbtn_playlist_refresh.setBackgroundResource(R.drawable.imgbtn_comeback_right);
				  }
				  else
				  {	
					  imgbtn_main_playlist.setBackgroundResource(R.drawable.imgbtn_comeback_left);
					  tv_mini_playlist.setText("播放列表");
					  imgbtn_playlist_refresh.setBackgroundResource(R.drawable.imgbtn_refresh);
				  }
				}
				
				@Override
				public void onPageScrolled(int position, float arg1, int arg2)
				{
					
				}	
				@Override
				public void onPageScrollStateChanged(int position)
				{
				
				}
		}); 
		
		 
		listview_playlist.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,long id)
			{
				Const.currentSongIndex = position;
			    playSong(Const.currentSongIndex);  
			    adapter_playlist.notifyDataSetInvalidated();
			    setImgbtnBackground(true);   		    
			}
		});
		
		
		listview_playlist.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,final int position, long id)
			{				

				String infor="        确定从播放列表里面删除歌曲"+"\""+Const.listdata_playlist.get(position).get("fileName").toString()+"\""+"吗?";		
				myDialogDelete = new MyDialogDelete(MiniMusicPlayerActivity.this,new MyOnClickListener()
				{				 				
					@Override
					public void onClick(View v)
					{
			           if(v.getId()==R.id.tv_dialog_ok)
			           {
			        	   String file_delete = Const.listdata_playlist.get(position).get("filePath").toString();
			        	   deleteSong(position);
						   
						   if(Const.isChecked)
						   {
							  FileManager.deleteFile(MiniMusicPlayerActivity.this, file_delete);						  
						   }	   								   					  
						   Const.isChecked=false;					      
			           }
			           
			           if(v.getId()==R.id.tv_dialog_cancel)
			           {
			        	   myDialogDelete.dismiss();	
			           }
			           
			           myDialogDelete.dismiss();			           
					}

					
					
				},infor);
				
				myDialogDelete.show();
						
				return true;		
			}		
		});	
				
		mediaPlayer.setOnCompletionListener(new OnCompletionListener()
	    {
			@Override
			public void onCompletion(MediaPlayer mp)
			{
			
			    if(Const.isSinglePlay)
			    {
			    	setImgbtnBackground(false);
			        InforManager.showInfor(MiniMusicPlayerActivity.this,"温馨提示:这首歌曲已经播放完了");   
			    }
			
			    if(Const.isSingleCycle)
			    {
			        playSong(Const.currentSongIndex);		
			    }
			
			    if(Const.isAllPlay)
			    {
			        if(Const.currentSongIndex < (Const.listdata_playlist.size()- 1))		
			        {
				        playSong(++Const.currentSongIndex); 
			        }
			        else
			        {
			        	setImgbtnBackground(false);
				        InforManager.showInfor(MiniMusicPlayerActivity.this,"温馨提示：播放列表的歌曲已经全部播放完了");	
			        }
			    }
			
			    if(Const.isAllCycle)
			    {
				     if(Const.currentSongIndex < (Const.listdata_playlist.size()- 1))
				     {
				    	playSong(++Const.currentSongIndex); 
				     }
				     else
				     {
					     Const.currentSongIndex = 0;
					     playSong(Const.currentSongIndex); 
				     }
			     }
			
			    if(Const.isShuffle)
			    {
				   Random rand = new Random();
		           Const.currentSongIndex = rand.nextInt((Const.listdata_playlist.size() - 1) - 0 + 1) + 0;
		           playSong(Const.currentSongIndex);
			    }	
			    adapter_playlist.notifyDataSetInvalidated();
		    }    	
	    });	
		
		volumeProgressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
				{
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{
			
				}
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
									
				}    	
		    });
			
		progressProgressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
		    @Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{	
		    	
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) 
			{
			    handler.removeCallbacks(UpdateTimeTask);	
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				update();
			}	
		    });
		
	}


	private void setMyTheme()
	{
		//主题：古典怀旧
		if(Const.THEME.equals("1"))
		{
			layout_menu.setBackgroundResource(R.drawable.background_classical);
			layout_main.setBackgroundResource(R.drawable.theme_classical);
			tv_currentmode.setBackgroundResource(R.drawable.style_item2);
			tv_currentsong.setTextColor(Color.argb(255,0,255,128));
			tv_currentsong.setBackgroundResource(R.drawable.style_border_alpha);
			layout_control_seekbar.setBackgroundColor(Color.argb(64,0,0,0));
			tv_currentvolume.setTextColor(Color.rgb(0,255,128));
			tv_currentProgress.setTextColor(Color.rgb(0,255,128));			
		}
		//主题：梦幻清新
		else if(Const.THEME.equals("2"))
		{
			layout_menu.setBackgroundResource(R.drawable.background_refresh);
			layout_main.setBackgroundResource(R.drawable.theme_refresh);
			tv_currentmode.setBackgroundResource(R.drawable.style_item);
			tv_currentsong.setTextColor(Color.rgb(0,128,255));
			//tv_currentsong.setTextColor(Color.rgb(0,255,128));
			tv_currentsong.setBackgroundResource(R.drawable.style_border_alpha2);
			layout_control_seekbar.setBackgroundColor(Color.argb(64,255,255,255));
			tv_currentvolume.setTextColor(Color.rgb(0,128,255));
			tv_currentProgress.setTextColor(Color.rgb(0,128,255));
			//tv_currentvolume.setTextColor(Color.rgb(0,255,128));
			//tv_currentProgress.setTextColor(Color.rgb(0,255,128));
		}
		//主题：炫酷极夜
		else if (Const.THEME.equals("3"))
		{
			layout_menu.setBackgroundResource(R.drawable.background_blackcool);
			layout_main.setBackgroundResource(R.drawable.theme_blackcool);
			tv_currentmode.setBackgroundResource(R.drawable.style_item2);
			tv_currentsong.setTextColor(Color.rgb(0,128,255));
			tv_currentsong.setBackgroundResource(R.drawable.style_border_alpha);
			layout_control_seekbar.setBackgroundColor(Color.argb(64,0,0,0));
			tv_currentvolume.setTextColor(Color.rgb(0,128,255));
			tv_currentProgress.setTextColor(Color.rgb(0,128,255));
		}
		//主题：极致简约
		else
		{
			layout_menu.setBackgroundResource(R.drawable.background_original);
			layout_main.setBackgroundResource(R.drawable.theme_original);
			tv_currentmode.setBackgroundResource(R.drawable.style_item);
			tv_currentsong.setTextColor(Color.rgb(0,255,128));
			tv_currentsong.setBackgroundResource(R.drawable.style_border_alpha);
			layout_control_seekbar.setBackgroundColor(Color.argb(64,0,0,0));
			tv_currentvolume.setTextColor(Color.rgb(0,255,128));
			tv_currentProgress.setTextColor(Color.rgb(0,255,128));
		}		
		adapter_playlist.notifyDataSetChanged();		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		 if(requestCode == 0 && resultCode == RESULT_OK)
		 {
			 finish();
		     AnimationManagerSystem.fromFade(MiniMusicPlayerActivity.this);
	     }
	}
	
	
	@Override
	public void onBackPressed()
	{
		if(FileManager.isSDCardExist())
		{
		    setMyNotification();
		}
		else
		{
			System.exit(0);
		}
	}


	@Override
	public void onClick(View v) 
	{
		if(v==tv_mainpage)
		{
			if(FileManager.isSDCardExist())
			{
			    view_slidingMenu.closeMenu();
			    toTabControl();	
			}
			else
			{
				InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
			}
		}
		
		if(v==tv_playlist)
		{
			if(FileManager.isSDCardExist())
			{
			  view_slidingMenu.closeMenu();
			  toTabPlayList();	
			}
			else
			{
				InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
			}
		}	
		
		if(v==tv_setting)
		{
			if(FileManager.isSDCardExist())
			{
			    Intent intent = new Intent();			
			    intent.setClass(MiniMusicPlayerActivity.this,SettingActivity.class);			
			    startActivityForResult(intent,0);
			    //AnimationManager.fromDownToUp(MiniMusicPlayerActivity.this);
			    //AnimationManager.fromSlideCross(MiniMusicPlayerActivity.this);
			    AnimationManager.fromZoom(MiniMusicPlayerActivity.this);
			}
			else
			{
				InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
			}
		}	
		
		if(v==tv_about)
		{	
			if(FileManager.isSDCardExist())
			{
			    Intent intent = new Intent();			
			    intent.setClass(MiniMusicPlayerActivity.this,AboutActivity.class);			
			    startActivity(intent);
			    //AnimationManager.fromDownToUp(MiniMusicPlayerActivity.this);
			    AnimationManager.fromZoom(MiniMusicPlayerActivity.this);
			}
			else
			{
				InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
			}
		}	
		
		if(v==tv_exit)
		{
			if(FileManager.isSDCardExist())
			{
			    view_slidingMenu.closeMenu();
			}
			System.exit(0);
		}	
		
		if(v==imgbtn_main_playlist)
		{
			if(viewPager.getCurrentItem()==1)
			{
			    toTabControl();
			    if(!FileManager.isSDCardExist())
			    {
			    	InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
			    }
			}
			else
			{
				setSlide(view_slidingMenu);
				if(!FileManager.isSDCardExist())
				{
					InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
				}
			}
		}
		
		if(v==imgbtn_playlist_refresh)
		{
			if(viewPager.getCurrentItem()==1)
			{
				if(FileManager.isSDCardExist())
				{
				    myDialogRefresh = new MyDialogRefresh(MiniMusicPlayerActivity.this,new MyOnClickListener() {
					
					@Override
					public void onClick(View v)
					{
						if(v.getId()==R.id.layout_dialog_positionmusic)
						{
							if(FileManager.isSDCardExist())
							{
							    listview_playlist.setSelection(Const.currentSongIndex);
							    adapter_playlist.notifyDataSetInvalidated();
							    myDialogRefresh.dismiss();
							}
							else
							{
								myDialogRefresh.dismiss();
								InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
							}
						}
									
						if(v.getId()==R.id.layout_dialog_refreshplaylist)
						{		
							 if(FileManager.isSDCardExist())
							 {
							     MyAsyncTask  task =new MyAsyncTask();						 
							     task.execute();			 							
							     InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：播放列表刷新完成");	
							     Const.currentSongIndex=0;
							     adapter_playlist.notifyDataSetInvalidated();
							     setImgbtnBackground(true);
							 
							     if(Const.listdata_playlist.size()>0)
							     {
								     setImgbtnEnable(true);
							     }
							     else
							     {
								     setImgbtnEnable(false); 
							     }	 
							   }
							 else
							 {
								 myDialogRefresh.dismiss();
								 InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
							 }
							 
						}
						
						if(v.getId()==R.id.layout_dialog_changemusic)
						{
							
							//问题代码块
							
							if(FileManager.isSDCardExist())
							{
								if(Const.listdata_playlist.size()>0)
								{
								  Random rand = new Random();
						          Const.currentSongIndex = rand.nextInt((Const.listdata_playlist.size() - 1) - 0 + 1) + 0;
						          playSong(Const.currentSongIndex);					     
						          listview_playlist.setSelection(Const.currentSongIndex);
							      adapter_playlist.notifyDataSetInvalidated();
								}
								else
								{
									InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：播放列表里面没有歌曲");
								}
							    myDialogRefresh.dismiss();
							}
							else
							{
								 myDialogRefresh.dismiss();
								 InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
							}																										        			 
						}
						
						if(v.getId()==R.id.layout_dialog_changetheme)
						{
							if(FileManager.isSDCardExist())
							{
								/*
								 * 【0】极致简约
								 * 【1】古典怀旧
								 * 【2】梦幻清新
								 * 【3】炫酷极夜
								 * */
							    if(Const.THEME.equals("1"))
							    {
								    Const.THEME="2";
								    InforManager.showInfor(MiniMusicPlayerActivity.this, "当前主题："+"梦幻清新");
							    }
							    else if(Const.THEME.equals("2"))
							    {
								    Const.THEME="3";
								    InforManager.showInfor(MiniMusicPlayerActivity.this, "当前主题："+"炫酷极夜");
							    }
							    else if(Const.THEME.equals("3"))
							    {
								    Const.THEME="0";
								    InforManager.showInfor(MiniMusicPlayerActivity.this, "当前主题："+"极致简约");
							    }
							    else
							    {
								    Const.THEME="1";
								    InforManager.showInfor(MiniMusicPlayerActivity.this, "当前主题："+"古典怀旧");
							     }
							     setMyTheme();
							     DatabaseManager.writerDataToSettingFile(MiniMusicPlayerActivity.this, Const.STARTPLAYING, Const.THEME, Const.PLAYMODE);
							     myDialogRefresh.dismiss();
							  }
							else
							{
								myDialogRefresh.dismiss();
								InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
							}
						}
															
						if(v.getId()==R.id.layout_dialog_cancel)
						{
							myDialogRefresh.dismiss();
							if(!FileManager.isSDCardExist())
							{
								InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
							}
						}												
					}
				});			
				         myDialogRefresh.show();
				}
				else
				{
					InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
				}
			}
			else
			{
				if(FileManager.isSDCardExist())
				{
				  toTabPlayList();
				}
				else
				{
					InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
				}
			}
		}
		
		if(v==tv_currentmode)
		{
			if(FileManager.isSDCardExist())
			{
			    Intent intent = new Intent();			
			    intent.setClass(MiniMusicPlayerActivity.this,SettingActivity.class);			
			    startActivityForResult(intent,0);
			
			    if(view_slidingMenu.isMainScreenShowing())
			    {	
				  AnimationManagerSystem.fromFade(MiniMusicPlayerActivity.this);
			    }	
			    else
			    {
				  // AnimationManager.fromDownToUp(MiniMusicPlayerActivity.this);
			    	 AnimationManager.fromZoom(MiniMusicPlayerActivity.this);
			    }
			}
		    else
			{
		    	InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");	  
			}
		}
				
		if(v==tv_currentsong)
		{
			if(FileManager.isSDCardExist())
			{
			    toTabPlayList();
			    listview_playlist.setSelection(Const.currentSongIndex);
			    adapter_playlist.notifyDataSetInvalidated();
			}
			else
			{
				InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
			}
		}
		
		
		if(v==imgbtn_mode)
		{
			setSlide(view_slidingMenu);
			if(!FileManager.isSDCardExist())
			{
				InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
			}
		}
			
		if(v==imgbtn_previous)
		{
			if(FileManager.isSDCardExist())
			{
			    if(mediaPlayer.isPlaying())
			    {	
			      if(Const.currentSongIndex > 0)
		          {
				     Const.currentSongIndex =Const.currentSongIndex - 1;
		             playSong(Const.currentSongIndex);	       
		          }
		          else
		          {
		    	     Const.currentSongIndex = Const.listdata_playlist.size() - 1; 
		    	     playSong(Const.currentSongIndex);		        
		          }
			    }			
			    else
			    {
				    if(Const.currentSongIndex>0)
				    {
					    Const.currentSongIndex = Const.currentSongIndex - 1;  
			    	    playSongPauseState(Const.currentSongIndex);
				    }
				    else
				    {
					    Const.currentSongIndex = Const.listdata_playlist.size() - 1;  
			    	    playSongPauseState(Const.currentSongIndex);
				    }	
			      }
			        adapter_playlist.notifyDataSetInvalidated();
			}
			else
			{
				InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
			}
		}
		
		if(v==imgbtn_next)
		{
			if(FileManager.isSDCardExist())
			{
			    if(mediaPlayer.isPlaying())
			    {
			       if(Const.currentSongIndex < (Const.listdata_playlist.size() - 1))
		           {
				      Const.currentSongIndex = Const.currentSongIndex + 1;
		              playSong(Const.currentSongIndex);        
		           }
		           else
		           {
		    	      Const.currentSongIndex = 0;
		              playSong( Const.currentSongIndex);
		           }
			     }
			     else
			     {
				      if(Const.currentSongIndex < (Const.listdata_playlist.size() - 1))
				      {
					      Const.currentSongIndex = Const.currentSongIndex + 1;
					      playSongPauseState(Const.currentSongIndex);		
				      }
				      else
				      {
					      Const.currentSongIndex =0;
					      playSongPauseState(Const.currentSongIndex);		
				      }		 	
			      }
			       adapter_playlist.notifyDataSetInvalidated();
			}
			else
			{
			    InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
			}
		}
		
		if(v==imgbtn_pause)
		{
			if(FileManager.isSDCardExist())
			{
			    if(mediaPlayer.isPlaying())
	    	    {
	    		    if(mediaPlayer!=null)
	    		    {
	    			    mediaPlayer.pause();
	                    Const.pauseSongIndex=Const.currentSongIndex;
	                    setImgbtnBackground(false);
	    		    }
	    	     }	
			}
			else
			{
				InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
			}
		}
		
		if(v==imgbtn_play)
		{	
			if(FileManager.isSDCardExist())
			{
			    if(mediaPlayer!=null)
	    	    {
				    if(Const.currentSongIndex==Const.pauseSongIndex)
				    {
					    mediaPlayer.start();
				    }
				    else
				    {
			            playSong(Const.currentSongIndex);
				    }
				    setImgbtnBackground(true);
	    	    }
			}
			else
			{
				InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
			}
		}										
	}
			
	
	@Override
	protected void onDestroy() 
	{
		if(FileManager.isSDCardExist())
		{
		    if(mediaPlayer!=null)
		    {
			    if(mediaPlayer.isPlaying())
			    {
				    mediaPlayer.stop();
			    }
			        mediaPlayer.release();
		    }		
		}
		
		homelistener.stopWatch();
		
		System.exit(0);	  
		super.onDestroy();		
	}
	
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    if(keyCode == KeyEvent.KEYCODE_MENU)
		{
	    	if(FileManager.isSDCardExist())
	    	{
			    setSlide(view_slidingMenu);
	    	}
	    	else
	    	{
	    		setSlide(view_slidingMenu);
	    		InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：检测不到SD卡");
	    	}
		}
	    
		return super.onKeyDown(keyCode, event);		
	}
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		if(FileManager.isSDCardExist())
		{
		    setMyTheme();
		}
	}
	
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		if(FileManager.isSDCardExist())
		{
		    tv_currentmode.setText("播放模式："+Const.items[Integer.valueOf(Const.PLAYMODE)]);
		}
	}
	
	
	private void audioList()
	{	
		Const.listdata_playlist.clear();
		list_storage = new StorageList(this);
		FileManager.getFiles(list_storage.getVolumePaths());
		adapter_playlist=new PlayListAdapter(MiniMusicPlayerActivity.this,Const.listdata_playlist);
		listview_playlist.setAdapter(adapter_playlist);
	}	
	
	
	private void deleteSong(final int position) 
	{
		DatabaseManager.deleteItem(Const.listdata_playlist,position);
		refreshPlayListData();
		
		if(Const.currentSongIndex<position)
		{
			//删除正在播放的歌曲后面的文件，不需要处理		
		}
		else if((Const.currentSongIndex==position))
		{
			//删除正在播放的歌曲
			Const.currentSongIndex=0;
			playSong(Const.currentSongIndex);
			setImgbtnBackground(true);
		}
		else
		{
			//删除正在播放的歌曲前面的文件
			Const.pauseSongIndex=Const.pauseSongIndex-1;
			Const.currentSongIndex=Const.currentSongIndex-1;
			adapter_playlist.notifyDataSetInvalidated();
			Const.fileName=Const.listdata_playlist.get(Const.currentSongIndex).get("fileName").toString().trim();
            tv_currentsong.setText("正在播放:"+"\n"+String.valueOf(Const.currentSongIndex+1)+"."+Const.fileName); 
		}			
		adapter_playlist.notifyDataSetInvalidated();
	}
	
	
	private void playSong(int songIndex)
	{   
        try
        {	
        	/*判断一下是否越界*/
        	if(songIndex<Const.listdata_playlist.size())
        	{
        	    if(FileManager.isFileExist(Const.listdata_playlist.get(songIndex).get("filePath").toString()))
        	    {
        		    mediaPlayer.reset();
            	    mediaPlayer.setDataSource(Const.listdata_playlist.get(songIndex).get("filePath").toString());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Const.fileName=Const.listdata_playlist.get(songIndex).get("fileName").toString().trim();
                    tv_currentsong.setText("正在播放:"+"\n"+String.valueOf(songIndex+1)+"."+Const.fileName); 
                    setImgbtnBackground(true);
                    
                    if(!Const.isUpdate)
			        {
			    	  update();
			        }
        	    }
        	    else
        	    {
        		    deleteSong(songIndex);
        	    }	
            }
        	else
        	{
        		InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：歌曲序号越界错误");
        	}
        	/*判断一下是否越界*/
        }
        
        catch (IllegalArgumentException e) 
        {
            e.printStackTrace();
        } 
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
	}


	private void playSongPauseState(int songIndex)
	{
		 Const.fileName=Const.listdata_playlist.get(songIndex).get("fileName").toString().trim();
         tv_currentsong.setText("正在播放:"+"\n"+String.valueOf(songIndex+1)+"."+Const.fileName); 
	}
	
	
	private void refreshPlayListData()
	{
		Const.listdata_playlist=DatabaseManager.getMyMusic();
		adapter_playlist=new PlayListAdapter(MiniMusicPlayerActivity.this,Const.listdata_playlist);
		listview_playlist.setAdapter(adapter_playlist);
	}
	
	
	private void registerHomeListener() 
	{
		homelistener = new HomeListener(this);
		
		homelistener.setOnHomePressedListener(new OnHomePressedListener()
		{
			
			@Override
			public void onHomePressed()
			{
				setMyNotification();			
			}
			
			@Override
			public void onHomeLongPressed()
			{
				//长按【HOME】键事件相应
			}
		});	
		homelistener.startWatch();
	}
	
	
	private void setImgbtnBackground(boolean isPlaying)
	{
		if(isPlaying)
		{
			imgbtn_play.setEnabled(false);
			imgbtn_pause.setEnabled(true);
			imgbtn_play.setImageResource(R.drawable.imgbtn_play_play_b);
			imgbtn_pause.setImageResource(R.drawable.imgbtn_play_pause_a);
		}
		else
		{
			imgbtn_play.setEnabled(true);
			imgbtn_pause.setEnabled(false);
			imgbtn_play.setImageResource(R.drawable.imgbtn_play_play_a);
			imgbtn_pause.setImageResource(R.drawable.imgbtn_play_pause_b);
		}
	}
		
	
	private void setImgbtnEnable(boolean isEnable)
	{
		if(isEnable)
		{
		    imgbtn_previous.setEnabled(true);
	  	    imgbtn_next.setEnabled(true);
		    imgbtn_play.setEnabled(true);
		    imgbtn_pause.setEnabled(true);
		    progressProgressBar.setEnabled(true);
		}
		else
		{	
		    imgbtn_previous.setEnabled(false);
		    imgbtn_next.setEnabled(false);
			imgbtn_play.setEnabled(false);
			imgbtn_pause.setEnabled(false);
			progressProgressBar.setEnabled(false);
	        InforManager.showInfor(MiniMusicPlayerActivity.this, "温馨提示：部分按钮已经禁用");
	    }
	}
	
	
	private void setInitImgbtnBackground()
	{
		if(Const.STARTPLAYING.equals("0"))
		{
			setImgbtnBackground(false);
		}
		else
		{
			setImgbtnBackground(true);
		}
	}
	
	
	private void setMyNotification() 
	{
		MyNotificationManager.setNotificationSound(MiniMusicPlayerActivity.this,notification,notificationManager,pendingIntent ,"点击进入主界面","迷你音乐播放器");
		Intent intent=new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}
	
	
	private void setPlayMode(int which)
	{
		 if(which==0)
  		 {
  	        Const.isSinglePlay=true;
  	        Const.isSingleCycle=false;
  	        Const.isAllPlay=false;
  	        Const.isAllCycle=false;
  	        Const.isShuffle=false; 
  		 }
		 else if(which==1)
  		 {
			Const.isSinglePlay=false;
			Const.isSingleCycle=true;
			Const.isAllPlay=false;
			Const.isAllCycle=false;
			Const.isShuffle=false; 	 
  		 }	 
  		 else if(which==2)
  		 {
  			Const.isSinglePlay=false;
  			Const.isSingleCycle=false;
  			Const.isAllPlay=true;
  			Const.isAllCycle=false;
  			Const.isShuffle=false; 	
  		 }
  		 
  		 else if(which==3)
  		 {
  			Const.isSinglePlay=false;
  			Const.isSingleCycle=false;
  			Const.isAllPlay=false;
  			Const.isAllCycle=true;
  			Const.isShuffle=false; 	
  		 }
  		  
  		 else 
  		 {
  			Const.isSinglePlay=false;
  			Const.isSingleCycle=false;
  			Const.isAllPlay=false;
  			Const.isAllCycle=false;
  			Const.isShuffle=true; 
  		 }
		 	 
		 tv_currentmode.setText("播放模式："+Const.items[which]);	 
	}
	
	
	private void setSlide(SlidingMenu slideMenu)
	{
		if (slideMenu.isMainScreenShowing()) 
		{
			slideMenu.openMenu();
		}
		else
		{
			slideMenu.closeMenu();
		}
	}	
		
	
	private void toTabControl()
	{
		viewPager.setCurrentItem(0);
		imgbtn_main_playlist.setBackgroundResource(R.drawable.app_icon);
		tv_mini_playlist.setText("迷你音乐播放器");
		imgbtn_playlist_refresh.setBackgroundResource(R.drawable.imgbtn_comeback_right);
	}
	
	
	private void toTabPlayList()
	{
	    viewPager.setCurrentItem(1);
		imgbtn_main_playlist.setBackgroundResource(R.drawable.imgbtn_comeback_left);
		tv_mini_playlist.setText("播放列表");
		imgbtn_playlist_refresh.setBackgroundResource(R.drawable.imgbtn_refresh);
	}
	
 
	private void update()
	{
		handler.removeCallbacks(UpdateTimeTask);
		long totalDuration = mediaPlayer.getDuration();
        progressProgressBar.setMax(100);
        int percent=progressProgressBar.getProgress();
        progressProgressBar.setProgress(percent);
        mediaPlayer.seekTo((int)totalDuration*percent/100);
		updateProgressBar();
		Const.isUpdate=true;
	}
	
	
	private void updateProgressBar() 
	{
	    handler.postDelayed(UpdateTimeTask, 100);
	}
	 
	
	private void longTimeMethod()
	{
	    audioList();
	    if(Const.listdata_playlist.size()>0)
	    {
	      playSong(0);
	      update();
	      DatabaseManager.createPlayList(MiniMusicPlayerActivity.this,Const.listdata_playlist);
	      setImgbtnEnable(true);
	    }
	    else
	    {
	    	setImgbtnEnable(false);
	    }
	 }

	
	public class MyAsyncTask extends AsyncTask<String,Integer,Integer>
	{
		@Override
		protected Integer doInBackground(String... params)
		{				
			return null;
		}
		@Override 
		protected void onPreExecute()
		{
			longTimeMethod();
			myDialogRefresh.dismiss();	          
		}		 
	}
		
}
