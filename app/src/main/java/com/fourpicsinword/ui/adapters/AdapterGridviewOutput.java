package com.fourpicsinword.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fourpicsinword.common.objects.ItemOutPut;

import java.util.ArrayList;

import com.fourpicsinword.R;

/**
 * Created by NGHIA_IT on 12/21/2015.
 */
public class AdapterGridviewOutput extends BaseAdapter {
    private ArrayList<ItemOutPut> listInput;
    private ArrayList<ItemOutPut> lengthTrueAnswer;
    private Context context;

    public AdapterGridviewOutput(Context context, ArrayList<ItemOutPut> listInput, ArrayList<ItemOutPut> lengthTrueAnswer){
        this.context = context;
        this.listInput = listInput;
        this.lengthTrueAnswer = lengthTrueAnswer;
    }
    @Override
    public int getCount() {
        return lengthTrueAnswer.size();
    }

    @Override
    public ItemOutPut getItem(int position) {
        return listInput.get(position);
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
            convertView = inflater.inflate(R.layout.adapter_gridview_output, parent, false);

            holder = new Holder();
            holder.textView = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        if(position>listInput.size()-1){
            holder.textView.setText("");
            holder.textView.setBackground(context.getResources().getDrawable(R.drawable.background_text_result));

        }else{
            holder.textView.setText(getItem(position).getCharacterTrue());
            if(getItem(position).isOpenHelp()){
                holder.textView.setBackground(context.getResources().getDrawable(R.drawable.background_text_result_helper));
            }else{
                holder.textView.setBackground(context.getResources().getDrawable(R.drawable.background_text_result));
            }
        }

        return convertView;
    }

    class Holder{
        private TextView textView;
    }
}
