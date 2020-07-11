package am.android.manager;

import java.io.File;
import java.util.HashMap;

import am.android.consts.Const;
import android.content.Context;
import android.os.Environment;

public class FileManager
{	
	
	public static void getFiles(String url)
	{	
		File files=new File(url);
		File[] file=files.listFiles();
		try
		{
	     for(File f:file)
	     {
	    	 if(f.isDirectory())
	    	 {
	    		 getFiles(f.getAbsolutePath());
	    	 }
	    	 else
	    	 {
	    		 if(isAudioFile(f.getPath()))
	    		 {	 
	    			Const.filePath= f.getPath();
	    			Const.fileName=Const.filePath.substring(Const.filePath.lastIndexOf("/")+1, Const.filePath.length());
	    	        HashMap<String,String> map=new HashMap<String,String>();
	    	        map.put("filePath", Const.filePath);
	    	        map.put("fileName", Const.fileName);
	    	        Const.listdata_playlist.add(map);
	    		 }
	    	 }
	     }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}	
	}
	
	
	public static void getFiles(String[] url)
	{
		if((url!=null)&&(url.length>0))
		{
			for(int i=0;i<url.length;i++)
			{
				getFiles(url[i]);
			}
		}
	}
	
	
	public static boolean isAudioFile(String path)
	{
		for(String format:Const.audioFormatSet)
		{
			path=path.substring(path.length()-5, path.length());
			
			if(path.contains(format))
			{
				return true;
			}	
		}
		   return false;
	}
	
	
	public static boolean isSDCardExist()
	{
		boolean result = true;
		if(!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
		     result=false;
		}
		return result;
	}
	
	
	public static boolean isFileExist(String filePath)
	{
		boolean result = false;
		File file = new File(filePath);
		
		if(file.exists())
		{
			result=true;
		}
		
		return result;
		
	}
	
	
	public static boolean makeDirectory()
	{
		boolean  isSDCardExist = isSDCardExist();
		if(isSDCardExist)
		{
		    new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"//MiniMusicPlayer").mkdir();
	    	new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"//MiniMusicPlayer//Databases").mkdir();
		}
		return isSDCardExist;
	}
	
	
	public static void makeFile()
	{
		Const.file_playlist=new File(Const.PATH_DATABASE+"//playlist.db");
		Const.file_settings=new File(Const.PATH_DATABASE+"//settings2.db");
	}
	
	
	public static void deleteFile(Context context,String filePath)
	{
		File file = new File(filePath);
		if(file.exists())
		{
			file.delete();		
			InforManager.showInfor(context, "温馨提示：歌曲删除成功");	
		}	
	}
	
}
