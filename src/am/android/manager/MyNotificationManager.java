package am.android.manager;

import am.android.minimusicplayer.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

public class MyNotificationManager
{

	@SuppressWarnings("deprecation")
	public static void setNotificationSound(Context context,Notification baseNF,NotificationManager nm,PendingIntent pd,String cardinfor,String content)
	{
		 baseNF.icon = R.drawable.app_icon;  	               		           
         baseNF.tickerText = "【迷你音乐播放器】桌面模式";  
         baseNF.defaults |= Notification.DEFAULT_LIGHTS;  		               		        
         baseNF.flags |= Notification.FLAG_AUTO_CANCEL;  		               
         baseNF.flags |= Notification.FLAG_NO_CLEAR;  		                  		           
         baseNF.setLatestEventInfo(context, cardinfor, content, pd);  
         nm.notify(110, baseNF);  	  
	}
}
