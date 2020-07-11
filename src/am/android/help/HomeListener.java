package am.android.help;

import am.android.interfaces.OnHomePressedListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class HomeListener 
{
	 private Context               context  = null;    
	 private IntentFilter          filter   = null;  
	 private InnerRecevier         recevier = null;  
	 private OnHomePressedListener listener = null;
	  
	 public HomeListener(Context context) 
	 {   
	     this.context = context;   
	     this.filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);   
	 } 
	 
	 public void setOnHomePressedListener(OnHomePressedListener listener) 
	 {   
	      this.listener = listener;   
	      this.recevier = new InnerRecevier();   
	 }   
	 
	 public void startWatch()
	 {   
	       if (recevier != null) 
	       {   
	           context.registerReceiver(recevier, filter);   
	       }   
	 }  
	 
	 public void stopWatch() 
	 {   
	       if (recevier != null) 
	       {   
	           context.unregisterReceiver(recevier);   
	       }   
	 }  
	 
	 
	 
	 class InnerRecevier extends BroadcastReceiver
	 {
	 	 final String SYSTEM_DIALOG_REASON_KEY            = "reason";   
	     final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";   
	     final String SYSTEM_DIALOG_REASON_RECENT_APPS    = "recentapps";   
	     final String SYSTEM_DIALOG_REASON_HOME_KEY       = "homekey";   
	 		
	 	 @Override
	 	 public void onReceive(Context context, Intent intent)
	 	 {
	 		 String action = intent.getAction();  
	 		 
	          if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) 
	          {   
	              String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);   
	              if (reason != null) 
	              {   
	                  if (listener != null) 
	                  {   
	                      if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY))
	                      {   	         
	                          listener.onHomePressed();   
	                      } 
	                      else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS))
	                      {   	                      
	                          listener.onHomeLongPressed();   
	                      }   
	                  }   
	              }   
	          }   
	      }   
	 	}
	 
}
