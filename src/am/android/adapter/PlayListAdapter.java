package am.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import am.android.consts.Const;
import am.android.minimusicplayer.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayListAdapter extends BaseAdapter 
{
	private Context                           context           = null;
	private ArrayList<HashMap<String,String>> listdata_playlist = new ArrayList<HashMap<String,String>>();
	private TextView                          tv_item_filename  = null;
	
    public PlayListAdapter(Context context,ArrayList<HashMap<String,String>> listdata_playlist)
    {
    	this.context=context;
    	this.listdata_playlist=listdata_playlist;
    }
	
	@Override
	public int getCount()
	{
		return listdata_playlist.size();
	}

	@Override
	public Object getItem(int position)
	{
		return listdata_playlist.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group)
	{
		LayoutInflater inflater=LayoutInflater.from(context);
		convertView=(View)inflater.inflate(R.layout.item_item_playlist, null);
		tv_item_filename=(TextView)convertView.findViewById(R.id.tv_item_filename);
		
		if(Const.THEME.equals("0"))
		{
			//¼«ÖÂ¼òÔ¼
			tv_item_filename.setTextColor(Color.rgb(0,255,128));
		}
		else if(Const.THEME.equals("1"))
		{
			//¹Åµä»³¾É
			tv_item_filename.setTextColor(Color.rgb(0,255,128));
		}
		else if(Const.THEME.equals("2"))
		{
			//ÃÎ»ÃÇåÐÂ
			tv_item_filename.setTextColor(Color.rgb(255,255,255));
		}
		else
		{
			//¼«Ò¹ìÅ¿á
			tv_item_filename.setTextColor(Color.rgb(0, 128, 255));
		}
			
		tv_item_filename.setText(String.valueOf(position+1)+"."+listdata_playlist.get(position).get("fileName").toString().trim());
		
		if(Const.currentSongIndex==position)
		{
	        convertView.setBackgroundResource(R.drawable.style_border_alpha3);	   
	        tv_item_filename.setTextColor(Color.rgb(255,255,255));  
		}
		
		return convertView;
	}
}
