package com.fourpicsinword.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourpicsinword.common.interfaces.PictureClickCallBack;
import com.github.siyamed.shapeimageview.RoundedImageView;

import com.fourpicsinword.R;

/**
 * Created by NGHIA_IT on 12/21/2015.
 */
public class FragmentPicture extends Fragment {
    private int pictureId;

    public static FragmentPicture newInstance(int drawableIdPicture){
        FragmentPicture fragmentPicture = new FragmentPicture();
        Bundle bundle = new Bundle();
        bundle.putInt("PICTURE_ID", drawableIdPicture);
        fragmentPicture.setArguments(bundle);
        return fragmentPicture;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getArguments()!=null){
            pictureId = getArguments().getInt("PICTURE_ID");
        }

        if(savedInstanceState!=null){
            pictureId = savedInstanceState.getInt("PICTURE_ID");
        }

        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        RoundedImageView imageView = (RoundedImageView) view.findViewById(R.id.imageView);

        imageView.setImageDrawable(getActivity().getResources().getDrawable(pictureId));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FragmenPicture", "picture click listenner ...");
                if(getActivity() instanceof PictureClickCallBack){

                    ((PictureClickCallBack) getActivity()).evenDIalogPictureClick();
                }
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("PICTURE_ID", pictureId);
    }
}
