package am.android.util;

import am.android.minimusicplayer.R;

public class Util
{

	public static int getBackground(String THEME)
	{
		int ID_IMAGE  = R.drawable.background_original;
		
		if(THEME.equals("1"))
		{
			//ID_IMAGE = R.drawable.background_classical;
			ID_IMAGE = R.drawable.theme_classical;
		}
		else if(THEME.equals("2"))
		{
			//ID_IMAGE = R.drawable.background_refresh;
			ID_IMAGE = R.drawable.theme_refresh;
		}
		else if(THEME.equals("3"))
		{
			//ID_IMAGE = R.drawable.background_blackcool;
			ID_IMAGE = R.drawable.theme_blackcool;
		}
		
		return ID_IMAGE;	
	}
	

}
