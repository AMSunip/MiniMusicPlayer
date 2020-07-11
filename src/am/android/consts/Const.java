package am.android.consts;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Environment;

public class Const 
{
	
    public static ArrayList<HashMap<String,String>> listdata_playlist=new ArrayList<HashMap<String,String>>();
    
    public static boolean isAllPlay     = false;
	public static boolean isAllCycle    = true;
	public static boolean isChecked     = false;
	public static boolean isUpdate      = false;
	public static boolean isShuffle     = false; 
	public static boolean isSinglePlay  = false;
	public static boolean isSingleCycle = false;
  
	public static File file_playlist=new File(Const.PATH_DATABASE+"//playlist.db");
    public static File file_settings=new File(Const.PATH_DATABASE+"//settings2.db");
	
    public static int currentSongIndex=0;
    public static int pauseSongIndex=-1;  //不能设为0，否则程序启动后在某些设置情况下回有问题
      
	public static String fileName="";
    public static String filePath="";
	public static String PATH_DATABASE=Environment.getExternalStorageDirectory().getAbsolutePath()+"//MiniMusicPlayer//Databases";
	public static String PLAYMODE="3";
    public static String STARTPLAYING="0";
    public static String THEME="0";
    
    public static String[] audioFormatSet=new String[]{"mp3","wav","3gp","ape","flac"};
    public static String[] items =new String[]{"单曲播放", "单曲循环", "顺序播放","列表循环","随机播放"}; 
    
}
