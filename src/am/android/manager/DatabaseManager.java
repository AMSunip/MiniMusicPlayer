package am.android.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import am.android.consts.Const;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager
{
	
	public static void createPlayList(Context context,ArrayList<HashMap<String,String>>  playlistdata)
	{	
		Const.file_playlist.delete();
		DatabaseManager.fileExist(context,"playlist.db");
		SQLiteDatabase	 database = SQLiteDatabase.openDatabase(Const.PATH_DATABASE+"//playlist.db",null, SQLiteDatabase.OPEN_READWRITE);
		for(int i=0;i<playlistdata.size();i++)
		{
			 ContentValues  cv = new ContentValues();
			 cv.put("filePath", playlistdata.get(i).get("filePath").toString().trim());
	         database.insert("playlist", null, cv);	
		}
		 database.close(); 
	}
	
	
	public static void deleteItem(ArrayList<HashMap<String,String>> playlistdata,int position)
	{
		SQLiteDatabase database=SQLiteDatabase.openDatabase(Const.PATH_DATABASE+"//playlist.db",null, SQLiteDatabase.OPEN_READWRITE);		
		database.delete("playlist","filePath=?",new String[]{playlistdata.get(position).get("filePath").toString().trim()});
		database.close();
	}
	
	
	public static void fileExist(Context context,String fileName)
	{
		File   file = new File(Const.PATH_DATABASE+"//"+fileName);
		if(!file.exists())
		{
		 try 
		 {
			file.createNewFile();
			InputStream is=context.getAssets().open(fileName);
			FileOutputStream fos=new FileOutputStream(file);
			byte[] buffer=new byte[1024];
			while(is.read(buffer)!=-1)
			{
				fos.write(buffer);
			}
			fos.close();
			is.close();	
			
			if(fileName.equals("playlist.db"))
			{
				SQLiteDatabase database=SQLiteDatabase.openDatabase(Const.PATH_DATABASE+"//playlist.db",null, SQLiteDatabase.OPEN_READWRITE);		
				database.delete("playlist","_id=? and filePath=?",new String[]{"1","filePath"});
				database.close();
			}
									
		  } 
		 catch (IOException e)
		 {
			e.printStackTrace();
		 }		  
	   }	
	}
	
	
	public static ArrayList<HashMap<String,String>> getMyMusic()
	{
		ArrayList<HashMap<String,String>>  playlistdata = new ArrayList<HashMap<String,String>>();
		playlistdata.clear();
		String filePath="";
		String fileName="";
		SQLiteDatabase database=SQLiteDatabase.openDatabase(Const.PATH_DATABASE+"//playlist.db",null, SQLiteDatabase.OPEN_READWRITE);
	    Cursor  cursor = database.rawQuery("select * from playlist", null);
	    
	    while(cursor.moveToNext())
	    {	
	    	filePath=cursor.getString(1);
	    	fileName=filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());; 	
	    	HashMap<String,String> map=new HashMap<String,String>();
	    	map.put("filePath", filePath);	
	    	map.put("fileName", fileName);	
	    	playlistdata.add(map);
	    }  
	    database.close();      
	    return playlistdata;        
	}
	
	
	public static void getMySettings()
	{
		SQLiteDatabase database=SQLiteDatabase.openDatabase(Const.PATH_DATABASE+"//settings2.db", null, SQLiteDatabase.OPEN_READWRITE);
		Cursor cursor=database.rawQuery("select * from settings2", null);
		
		while(cursor.moveToNext())
		{
			Const.STARTPLAYING=cursor.getString(1);
			Const.THEME=cursor.getString(2);
			Const.PLAYMODE=cursor.getString(3);
		}
		
        database.close(); 
	}
	
	public static void writerDataToSettingFile(Context context,String STARTPLAYING,String THEME,String PLAYMODE)
	{
	    fileExist(context,"settings2.db");    
	    ContentValues cv=new ContentValues();
	    cv.put("startplaying",STARTPLAYING);
	    cv.put("theme",THEME);
	    cv.put("playmode",PLAYMODE);
	   
	    SQLiteDatabase database=SQLiteDatabase.openDatabase(Const.PATH_DATABASE+"//settings2.db", null,SQLiteDatabase.OPEN_READWRITE);
	    database.update("settings2", cv, "_id=?", new String[]{"1"});
	    database.close();  
	}	
}
