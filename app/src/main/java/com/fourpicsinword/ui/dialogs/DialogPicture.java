package com.fourpicsinword.ui.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.fourpicsinword.ui.adapters.AdapterViewPagerPicture;

import com.fourpicsinword.R;

/**
 * Created by NGHIA_IT on 12/21/2015.
 */
public class DialogPicture extends DialogFragment {
    private int[] listPicture;
    private AdapterViewPagerPicture adapterViewPagerPicture;
    private int position;
    private static String TAG = "DialogPicture";

    public static DialogFragment newIntance(int[] listPicture, int positionClick) {
        DialogPicture dialogPicture = new DialogPicture();
        Bundle bundle = new Bundle();
        bundle.putIntArray("LIST_PICTURE", listPicture);
        bundle.putInt("POSITION", positionClick);
        dialogPicture.setArguments(bundle);

        return dialogPicture;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getData(savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_picture, container, false);

        ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewpager);

        adapterViewPagerPicture = new AdapterViewPagerPicture(getChildFragmentManager(), listPicture);
        viewpager.setAdapter(adapterViewPagerPicture);
        viewpager.setCurrentItem(position);


        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                DialogPicture.this.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("LIST_PICTURE", listPicture);
        outState.putInt("POSITION", position);
    }

    public void getData(Bundle savedInstanceState) {
        if (getArguments() != null) {
            this.listPicture = getArguments().getIntArray("LIST_PICTURE");
            this.position = getArguments().getInt("POSITION");
        }

        if (savedInstanceState != null) {
            this.listPicture = savedInstanceState.getIntArray("LIST_PICTURE");
            this.position = savedInstanceState.getInt("POSITION");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }
}
