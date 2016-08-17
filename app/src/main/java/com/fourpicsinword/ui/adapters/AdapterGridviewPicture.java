package com.fourpicsinword.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;

import com.fourpicsinword.R;

/**
 * Created by NGHIA_IT on 12/18/2015.
 */
public class AdapterGridviewPicture extends BaseAdapter {
    private ArrayList<Integer> imagesUrlList;
    private Context context;

    public AdapterGridviewPicture(Context context, ArrayList<Integer> imagesUrlList) {
        this.imagesUrlList = imagesUrlList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imagesUrlList == null ? 0 : imagesUrlList.size();
    }

    @Override
    public Integer getItem(int position) {
        return imagesUrlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.adapter_gridview_picture, parent, false);

            holder = new Holder();
            holder.imageView = (RoundedImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.imageView.setImageDrawable(context.getResources().getDrawable(getItem(position)));

        return convertView;
    }

    class Holder {
        RoundedImageView imageView;
    }
}
