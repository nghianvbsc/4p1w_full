package com.fourpicsinword.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fourpicsinword.R;
import com.fourpicsinword.common.interfaces.DialogSubGoldCallBack;

/**
 * Created by NGHIA_IT on 12/23/2015
 */
public class DialogSubGold extends Dialog {
    private TextView message;
    private Button no,  yes;
    private int gold;
    private DialogSubGoldCallBack dialogSubGoldCallBack;
    private int type;

    public DialogSubGold(Context context, int type, int gold, DialogSubGoldCallBack dialogSubGoldCallBack) {
        super(context);
        this.gold = gold;
        this.dialogSubGoldCallBack = dialogSubGoldCallBack;
        this.type = type;
    }

    private void init(){
        message = (TextView) findViewById(R.id.message);
        no = (Button) findViewById(R.id.no);
        yes = (Button) findViewById(R.id.yes);

        if(type == 0){
            message.setText("Do you want toreveal a correct letter for " + gold + " gold");
        }else{
            message.setText("You need share app to add gold!");
        }


        no.setOnClickListener(noListenner);
        yes.setOnClickListener(yesListenner);
    }

    private View.OnClickListener noListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogSubGoldCallBack.dialogSubgoldCallback("no", gold);

            dismiss();
        }
    };

    private View.OnClickListener yesListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogSubGoldCallBack.dialogSubgoldCallback("yes", gold);

            dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_sub_gold);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        init();
    }


}
