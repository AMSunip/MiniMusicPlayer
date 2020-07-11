package am.android.manager;

public class FormatManager
{
	public static String getTimeFormat(long totalDuration)
	{
		String time="";
		int sum=(int) (totalDuration/1000);
		int hour,minute,second;
		hour=sum/3600;
		minute=(sum-hour*3600)/60;
		second=sum%60;
		if(hour<10)
		{
			time+="0"+String.valueOf(hour);
		}
		else
		{
			time+=String.valueOf(hour);
		}
		
		if(minute<10)
		{
			time+=":"+"0"+String.valueOf(minute);
		}
		else
		{
			time+=":"+String.valueOf(minute);
		}
		
		if(second<10)
		{
			time+=":"+"0"+String.valueOf(second);
		}
		else
		{
			time+=":"+String.valueOf(second);
		}	
		return time;
	}
}
