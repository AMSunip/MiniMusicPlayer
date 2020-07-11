package am.android.dialog;

import am.android.interfaces.MyOnClickListener;
import am.android.minimusicplayer.R;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MyDialogRefresh implements OnClickListener
{
	private AlertDialog       dialog                        = null;
	private Context           context                       = null;
	private LinearLayout      layout_dialog_cancel          = null;
	private LinearLayout      layout_dialog_changemusic     = null;
	private LinearLayout      layout_dialog_changetheme     = null;
	private LinearLayout      layout_dialog_positionmusic   = null;
	private LinearLayout      layout_dialog_refreshplaylist = null;
	private MyOnClickListener listener                      = null;
	
	public MyDialogRefresh(Context context)
	{		
		this.context = context;
		dialog = new AlertDialog.Builder(this.context).create();
	}
	
	public MyDialogRefresh(Context context,MyOnClickListener listener)
	{		
		this.context = context;
		dialog = new AlertDialog.Builder(this.context).create();
		this.listener=listener;
	}
	
	public void show()
	{	
		
		dialog.show();	
		Window  window = dialog.getWindow();
		
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setGravity(Gravity.RIGHT | Gravity.TOP);
		
		lp.x=5;
		lp.y=100;
		lp.alpha=1.0f;          //完全不透明
		window.setAttributes(lp);
		window.setContentView(R.layout.dialog_refresh);	
		
		layout_dialog_positionmusic = (LinearLayout)window.findViewById(R.id.layout_dialog_positionmusic);	
		layout_dialog_positionmusic.setOnClickListener(this);
		layout_dialog_refreshplaylist = (LinearLayout)window.findViewById(R.id.layout_dialog_refreshplaylist);	
		layout_dialog_refreshplaylist.setOnClickListener(this);
		layout_dialog_changemusic=(LinearLayout)window.findViewById(R.id.layout_dialog_changemusic);
		layout_dialog_changemusic.setOnClickListener(this);
		layout_dialog_changetheme=(LinearLayout)window.findViewById(R.id.layout_dialog_changetheme);
		layout_dialog_changetheme.setOnClickListener(this);
		layout_dialog_cancel = (LinearLayout)window.findViewById(R.id.layout_dialog_cancel);	
		layout_dialog_cancel.setOnClickListener(this);		
		
	}
	
	public void dismiss()
	{
		dialog.dismiss();
	}
	
	@Override
	public void onClick(View v) 
	{	
		listener.onClick(v);
	}	
	
}
