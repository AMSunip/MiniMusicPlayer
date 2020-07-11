package am.android.view;

import am.android.minimusicplayer.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CheckImageView extends ImageView 
{
    private boolean IsChecked=false;
	
	public CheckImageView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);    
		TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CheckImageViewStyle);
		this.IsChecked=a.getBoolean(R.styleable.CheckImageViewStyle_checked, false);
		a.recycle();	
	}
	
	public void setChecked(boolean IsChecked)
	{
	    if(IsChecked)
	    {  
	      this.IsChecked=true;
		  this.setBackgroundResource(R.drawable.on);
	    }
	    else
	    {
	      this.IsChecked=false;
	      this.setBackgroundResource(R.drawable.off);
	    }
	}

	
	public String  getChecked()
	{
		if(this.IsChecked)
		{
			return "1";
		}
		else
		{
			return "0";
		}
	}
}
