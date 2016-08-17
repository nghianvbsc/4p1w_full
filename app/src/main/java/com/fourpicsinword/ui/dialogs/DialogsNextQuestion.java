package com.fourpicsinword.ui.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fourpicsinword.ui.activities.MainActivity;
import com.fourpicsinword.module.Config;
import com.fourpicsinword.module.SharedPreference;
import com.fourpicsinword.module.Utils;
import com.fourpicsinword.R;
import com.fourpicsinword.ui.adapters.AdapterGridviewOutput;
import com.fourpicsinword.ui.custom_views.GifViewFire;
import com.fourpicsinword.common.interfaces.DialogNextQuestionCallBack;
import com.fourpicsinword.common.objects.ItemOutPut;
import com.fourpicsinword.common.objects.Question;
import com.nineoldandroids.animation.Animator;
import com.ucweb.union.ads.mediation.MediationAd;
import com.ucweb.union.ads.mediation.MediationAdError;
import com.ucweb.union.ads.mediation.MediationAdListener;
import com.ucweb.union.ads.mediation.MediationAdRequest;
import com.ucweb.union.ads.mediation.MediationAdRequestException;
import com.ucweb.union.ads.mediation.MediationInterstitial;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by NGHIA_IT on 12/22/2015
 */
public class DialogsNextQuestion extends DialogFragment {
    public String onAdOpened = "onAdOpened";
    public String onAdLoaded = "onAdLoaded";
    public String onAdError = "onAdError";
    public String onAdClosed = "onAdClosed";
    public String onAdClicked = "onAdClicked";


    public AdapterGridviewOutput adapterOutput;
    private GifViewFire fire;
    public GridView gridviewOutput;
    private Button continueQuestion;
    private TextView message, title;
    private ImageView iconGold, light;

    private ArrayList<ItemOutPut> arrCharacterTruAnswer;
    private Question question;
    private Integer goldToAdd;

    private LinearLayout ads;
    public static String TAG = "DialogsNextQuestion";


    public static DialogsNextQuestion newInstance(Question aQuestion, int goldToAdd) {
        DialogsNextQuestion dialogsNextQuestion = new DialogsNextQuestion();

        Bundle bundle = new Bundle();
        bundle.putSerializable("QUESTION", (Serializable) aQuestion);
        bundle.putInt("GOLD_TO_ADD", goldToAdd);
        dialogsNextQuestion.setArguments(bundle);

        return dialogsNextQuestion;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.slider_in_left_out_right;
        return dialog;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("QUESTION", (Serializable) question);
        outState.putInt("GOLD_TO_ADD", goldToAdd);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getData(savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_next_question, container, false);
        setCancelable(false);


        init(view);

        if (MainActivity.counntPass % Config.COUNT_SHOW_ADS == 0) {
            continueQuestion.setEnabled(false);
            showAdsInterstitial(new AdsCallback() {
                @Override
                public void listenner(String even) {
                    if (!even.equals(onAdOpened)) {
                        continueQuestion.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.RubberBand)
                                .duration(1000)
                                .playOn(continueQuestion);
                    }
                }
            });
        }
        animatonAddGold();

        return view;
    }

    private void getData(Bundle savedInstanceState) {
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable("QUESTION");
            goldToAdd = getArguments().getInt("GOLD_TO_ADD");
        }
        if (savedInstanceState != null) {
            question = (Question) savedInstanceState.getSerializable("QUESTION");
            goldToAdd = savedInstanceState.getInt("GOLD_TO_ADD");
        }

        arrCharacterTruAnswer = Utils.createListObjectAnswer(Utils.convertStringToArraylist(question.getTrueAnswer()));
    }

    private void init(View view) {
        adapterOutput = new AdapterGridviewOutput(getActivity(), arrCharacterTruAnswer, arrCharacterTruAnswer);


        gridviewOutput = (GridView) view.findViewById(R.id.gridviewOutput);
        continueQuestion = (Button) view.findViewById(R.id.continueQuestion);
        iconGold = (ImageView) view.findViewById(R.id.iconGold);
        light = (ImageView) view.findViewById(R.id.light);
        fire = (GifViewFire) view.findViewById(R.id.fire);
        iconGold.setVisibility(View.GONE);
        fire.setVisibility(View.GONE);
        continueQuestion.setVisibility(View.INVISIBLE);
        message = (TextView) view.findViewById(R.id.message);
        title = (TextView) view.findViewById(R.id.title);

        message.setText("You've " + goldToAdd + " gold");
        rotateView(light);

        if (arrCharacterTruAnswer.size() >= 7) {
            gridviewOutput.setNumColumns(7);
        } else {
            gridviewOutput.setNumColumns(arrCharacterTruAnswer.size());
        }

        title.setText(Utils.randomTitle());

        gridviewOutput.setAdapter(adapterOutput);

        continueQuestion.setOnClickListener(continueListenner);

        YoYo.with(Techniques.RollIn)
                .duration(1200)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (MainActivity.counntPass % Config.COUNT_SHOW_ADS != 0) {

                            continueQuestion.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.RubberBand)
                                    .duration(1000)
                                    .playOn(continueQuestion);

                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(gridviewOutput);
    }

    public View.OnClickListener continueListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() instanceof DialogNextQuestionCallBack) {
                ((DialogNextQuestionCallBack) getActivity()).dialogNextQuestionCallback(Config.CONTINUE);
            }
        }
    };

    private void showAdsInterstitial(final AdsCallback adsCallback) {
        final MediationInterstitial interstitial = new MediationInterstitial(getActivity());
        try {
            MediationAdRequest adRequest = MediationAdRequest
                    .build(Utils.getPubIdAds(Config.INTERSTITIAL));
            // The test id below can only be used for test;
            interstitial.setAdListener(new MediationAdListener() {

                @Override
                public void onAdOpened(MediationAd arg0) {
                    Log.i(TAG, "Interstitial::onAdOpened");
                    adsCallback.listenner(onAdOpened);

                }

                @Override
                public void onAdLoaded(MediationAd arg0) {
                    Log.i(TAG, "Interstitial::onAdLoaded");
                    adsCallback.listenner(onAdLoaded);
                    interstitial.show();
                }

                @Override
                public void onAdError(MediationAd arg0,
                                      MediationAdError arg1) {
                    Log.i(TAG, "Interstitial::onAdError");
                    adsCallback.listenner(onAdError);
                    continueQuestion.setEnabled(true);
                }

                @Override
                public void onAdClosed(MediationAd arg0) {
                    Log.i(TAG, "Interstitial::onAdClosed");
                    adsCallback.listenner(onAdClosed);
                    continueQuestion.setEnabled(true);
                }

                @Override
                public void onAdClicked(MediationAd arg0) {
                    Log.i(TAG, "Interstitial::onAdClicked");
                    continueQuestion.setEnabled(true);
                    adsCallback.listenner(onAdClicked);
                }
            });
            interstitial.loadAd(adRequest);
        } catch (MediationAdRequestException e) {
            e.printStackTrace();
        }
    }

    public void animatonAddGold() {

        TranslateAnimation animation = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.translate_add_gold);
        animation.setDuration(2000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iconGold.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fire.setVisibility(View.VISIBLE);
                DialogMainGame.vGold.setText(SharedPreference.getGold(getActivity()) + "");
                iconGold.setVisibility(View.GONE);
                fire.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iconGold.setVisibility(View.VISIBLE);
        iconGold.startAnimation(animation);
    }

    public void rotateView(final View view) {
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(4000);
        rotate.setRepeatCount(-1);
        rotate.setInterpolator(new LinearInterpolator());

        view.startAnimation(rotate);

    }


    public interface AdsCallback {
        public void listenner(String even);
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }
}
