package com.fourpicsinword.ui.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fourpicsinword.module.Config;
import com.fourpicsinword.module.SharedPreference;
import com.fourpicsinword.module.Utils;
import com.fourpicsinword.R;
import com.fourpicsinword.ui.adapters.AdapterGridviewPicture;
import com.fourpicsinword.common.interfaces.DialogReviewQuestionClickCallBack;
import com.fourpicsinword.common.objects.Question;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by NGHIA_IT on 12/22/2015.
 */
public class DialogReviewQuestion extends DialogFragment {
    private GridView gridviewPicture;
    private Button play;
    private ImageButton buttonNavbarLeft;
    private TextView vNumberQuestion, vGold;

    int gold = 0;
    int numberQuestion = 1;


    private AdapterGridviewPicture adapterGridviewPicture;
    private Question question;
    private String type;
    private ArrayList<Integer> listPictureId;

    private LinearLayout ads;
    private static String TAG = "DialogReviewQuestion";


    public static DialogReviewQuestion newInstance(Question aQuestion, String type) {
        DialogReviewQuestion dialogReviewQuestion = new DialogReviewQuestion();
        Bundle bundle = new Bundle();
        bundle.putSerializable("QUESTION", (Serializable) aQuestion);
        bundle.putString("TYPE", type);
        dialogReviewQuestion.setArguments(bundle);

        return dialogReviewQuestion;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setCancelable(false);
        View view = inflater.inflate(R.layout.dialog_review_question, container, false);
        getData(savedInstanceState);
        init(view);
        return view;
    }

    private void init(View view) {
        adapterGridviewPicture = new AdapterGridviewPicture(getActivity(), listPictureId);
        gridviewPicture = (GridView) view.findViewById(R.id.gridviewPicture);
        play = (Button) view.findViewById(R.id.play);
        vGold = (TextView) view.findViewById(R.id.gold);
        vNumberQuestion = (TextView) view.findViewById(R.id.numberQuestion);

        vGold.setText(gold + "");
        vNumberQuestion.setText(numberQuestion + "");
        gridviewPicture.setAdapter(adapterGridviewPicture);
        play.setOnClickListener(playListenner);

        buttonNavbarLeft = (ImageButton) view.findViewById(R.id.buttonNavbarLeft);
        buttonNavbarLeft.setBackground(getActivity().getResources().getDrawable(R.drawable.icon_setting));
        buttonNavbarLeft.setVisibility(View.GONE);
    }

    private View.OnClickListener playListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() instanceof DialogReviewQuestionClickCallBack) {
                ((DialogReviewQuestionClickCallBack) getActivity()).evenInDialogReviewGame(Config.PLAY, type);
            }
            dismiss();
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.slider_in_left_out_right;
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("QUESTION", (Serializable) question);
        outState.putString("TYPE", type);
    }

    public void getData(Bundle savedInstanceState) {
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable("QUESTION");
            type = getArguments().getString("TYPE");
        }
        if (savedInstanceState != null) {
            question = (Question) savedInstanceState.getSerializable("QUESTION");
            type = savedInstanceState.getString("TYPE");
        }

        listPictureId = Utils.convertArrToArrayList(question.getListUrlPicture());
        gold = SharedPreference.getGold(getActivity());
        numberQuestion = SharedPreference.getNumberQuestion(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
