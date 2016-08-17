package com.fourpicsinword.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.fourpicsinword.R;

/**
 * Created by NGHIA_IT on 12/18/2015.
 */
public class AdapterGridviewInput extends BaseAdapter {
    private ArrayList<String> textList;
    private Context context;

    public AdapterGridviewInput(Context context, ArrayList<String> textList){
        this.context = context;
        this.textList = textList;
    }

    @Override
    public int getCount() {
        return textList == null?0: textList.size();
    }

    @Override
    public String getItem(int position) {
        return textList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.adapter_gridview_input, parent, false);

            holder = new Holder();
            holder.textView = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        holder.textView.setText(getItem(position));


        return convertView;
    }

    class Holder{
        private TextView textView;
    }
}
