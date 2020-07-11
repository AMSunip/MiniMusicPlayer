package am.android.dialog;

import am.android.consts.Const;
import am.android.interfaces.MyOnClickListener;
import am.android.minimusicplayer.R;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MyDialogDelete implements OnClickListener
{
	private AlertDialog       dialog           = null;
	private Context           context          = null;
	private CheckBox          cb_delete        = null;
	private MyOnClickListener listener         = null;
	private TextView          tv_infor_delete  = null;
	private TextView          tv_dialog_ok     = null;
	private TextView          tv_dialog_cancel = null;
	private String            infor            = "";
	
	
	public MyDialogDelete(Context context)
	{		
		this.context = context;
		dialog = new AlertDialog.Builder(this.context).create();
	}
	
	public MyDialogDelete(Context context,MyOnClickListener listener)
	{		
		this.context = context;
		dialog = new AlertDialog.Builder(this.context).create();
		this.listener=listener;
	}
	
	public MyDialogDelete(Context context,MyOnClickListener listener,String infor)
	{		
		this.context = context;
		dialog = new AlertDialog.Builder(this.context).create();
		this.listener=listener;
		this.infor=infor;    
	}
	
	public void show()
	{		
		dialog.show();		
		Window  window = dialog.getWindow();
		window.setContentView(R.layout.dialog_delete);		
	    tv_infor_delete = (TextView)window.findViewById(R.id. tv_infor_delete);	
	    cb_delete=(CheckBox)window.findViewById(R.id.cb_delete);
	    
	    cb_delete.setOnCheckedChangeListener(new OnCheckedChangeListener()
	    {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
			{
				Const.isChecked=isChecked;
				
				if(Const.isChecked)
				{
					tv_dialog_ok.setBackgroundResource(R.drawable.style_button_rectangle_red);
				}
				else
				{
					tv_dialog_ok.setBackgroundResource(R.drawable.style_button_rectangle_yellow);
				}
			}
	    	
	    });
	    
		tv_dialog_ok=(TextView)window.findViewById(R.id.tv_dialog_ok);	
		tv_dialog_ok.setOnClickListener(this);	
		tv_dialog_cancel=(TextView)window.findViewById(R.id.tv_dialog_cancel);	
		tv_dialog_cancel.setOnClickListener(this);			
		tv_infor_delete.setText(infor);	
	}
	
	public void setValue(String value)
	{
		 tv_infor_delete.setText(value);
	}
	
	
	public void showDialog()
	{
		dialog.show();
		Window window=dialog.getWindow();
		window.setContentView(R.layout.dialog_delete);
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
