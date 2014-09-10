package com.example.headdiary.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.headdiary.R;
import com.example.headdiary.data.UserDAO;

import android.R.integer;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MessageAdapter extends BaseAdapter{
	public static final String ArrayKey_FirstLine="FirstLine";//2014-1-30 紧张型头痛
	public static final String ArrayKey_SecondLine="SecondLine";//持续时间：6小时15分钟
	public static final String ArrayKey_Degree="Degree";
	private ArrayList<HashMap<String, Object>> mDataArrayList;
	private Context context;

	public MessageAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
		super();
		// TODO Auto-generated constructor stub
		mDataArrayList=data;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDataArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.mylistview_item, parent, false);
            // configure view holder
            DocumentViewHolder viewHolder = new DocumentViewHolder();
            viewHolder.firstLine = (TextView) v.findViewById(R.id.listitem_doctor_fistLine);
            viewHolder.secondLine = (TextView) v.findViewById(R.id.listitem_doctor_secondLine);
            //viewHolder.icon = (ImageView) v.findViewById(R.id.listitem_document_icon);
            v.setTag(viewHolder);
        }
		
		// fill data
	    DocumentViewHolder holder = (DocumentViewHolder) v.getTag();
	    holder.firstLine.setText((CharSequence) mDataArrayList.get(position).get(ArrayKey_FirstLine));
	    holder.secondLine.setText((CharSequence) mDataArrayList.get(position).get(ArrayKey_SecondLine));
	    int unRead = UserDAO.getInstance().getUnreadSuggestion();
	    if(position<unRead){
	     holder.secondLine.setTextColor(Color.BLACK);
	    }

        return v;
	}

	 
 
	class DocumentViewHolder {
        public TextView firstLine;
        public TextView secondLine;
       // public ImageView icon;
    }
}
