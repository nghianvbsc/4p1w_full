package com.fourpicsinword.ui.dialogs;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fourpicsinword.module.Config;
import com.fourpicsinword.module.SharedPreference;
import com.fourpicsinword.module.Utils;
import com.fourpicsinword.R;
import com.fourpicsinword.ui.adapters.AdapterGridviewInput;
import com.fourpicsinword.ui.adapters.AdapterGridviewOutput;
import com.fourpicsinword.ui.adapters.AdapterGridviewPicture;
import com.fourpicsinword.common.interfaces.DialogMainGameClickCallback;
import com.fourpicsinword.common.objects.ItemOutPut;
import com.fourpicsinword.common.objects.Question;
import com.nineoldandroids.animation.Animator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NGHIA_IT on 12/21/2015
 */
public class DialogMainGame extends DialogFragment {

    public Question question;
    public GridView gridviewPicture, gridviewInput, gridviewOutput;
    public static TextView vNumberQuestion, vGold;
    public AdapterGridviewPicture adapterPicture;
    public AdapterGridviewInput adapterInput;
    public AdapterGridviewOutput adapterOutput;
    public TextView help, passQuestion;
    private ImageButton buttonNavbarLeft;
    public Button share;
    private View root;

    public ArrayList<ItemOutPut> answer;
    public int lengthTrueAnswer;
    public static HashMap<String, String> relationInoutAndOutClick;
    int gold = 0;
    int numberQuestion = 1;
    private ArrayList<Integer> listIdUrlPic;
    private ArrayList<String> libraryCharacterToAnswer;
    private ArrayList<ItemOutPut> arrCharacterTruAnswer;

    private int countGridViewSize;
    private int position = 0;
    private int countRepit = 0;
    private static View view, viewOld;
    private boolean isRunningSelectChoise = false;
    private String type;


    public static DialogMainGame newInstance(Question question, String type) {
        DialogMainGame dialogMainGame = new DialogMainGame();
        Bundle bundle = new Bundle();
        bundle.putSerializable("QUESTION", (Serializable) question);
        bundle.putString("TYPE", type);
        dialogMainGame.setArguments(bundle);

        return dialogMainGame;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getData(savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_main_game, container, false);
        initUI(view);


        return view;
    }

    public void uploadView(Question aQuestion) {
        this.question = aQuestion;

        lengthTrueAnswer = question.getTrueAnswer().length();
        gold = SharedPreference.getGold(getActivity());
        numberQuestion = SharedPreference.getNumberQuestion(getActivity());

        vNumberQuestion.setText(numberQuestion + "");
        vGold.setText(gold + "");

        answer.clear();
        answer.addAll(Utils.createListAnswer(lengthTrueAnswer));
        relationInoutAndOutClick = new HashMap<>();

        arrCharacterTruAnswer.clear();
        arrCharacterTruAnswer.addAll(Utils.createListObjectAnswer(Utils.convertStringToArraylist(question.getTrueAnswer())));

        listIdUrlPic.clear();
        listIdUrlPic.addAll(Utils.convertArrToArrayList(question.getListUrlPicture()));

        libraryCharacterToAnswer.clear();
        libraryCharacterToAnswer.addAll(Utils.convertStringToArraylist(Utils.randomListTextForChoise(question.getTrueAnswer())));


        final int size = gridviewInput.getChildCount();
        for (int index = 0; index < size; index++) {
            gridviewInput.getChildAt(index).setVisibility(View.VISIBLE);
        }


        adapterPicture.notifyDataSetChanged();
        adapterInput.notifyDataSetChanged();
        adapterOutput.notifyDataSetChanged();


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

        lengthTrueAnswer = question.getTrueAnswer().length();
        gold = SharedPreference.getGold(getActivity());
        numberQuestion = SharedPreference.getNumberQuestion(getActivity());
        listIdUrlPic = Utils.convertArrToArrayList(question.getListUrlPicture());
        libraryCharacterToAnswer = Utils.convertStringToArraylist(Utils.randomListTextForChoise(question.getTrueAnswer()));
        arrCharacterTruAnswer = Utils.createListObjectAnswer(Utils.convertStringToArraylist(question.getTrueAnswer()));

        if (SharedPreference.getAnswer(getActivity()) != null) {
            if (SharedPreference.getAnswer(getActivity()).size() != 0) {
                answer = SharedPreference.getAnswer(getActivity());
            } else {
                answer = Utils.createListAnswer(lengthTrueAnswer);

            }
        } else {
            answer = Utils.createListAnswer(lengthTrueAnswer);

        }


        if (type.equals("NEW_QUESTION")) {
            relationInoutAndOutClick = new HashMap<>();
        } else if (type.equals("RE_OPEN_QUESTION")) {
//            relationInoutAndOutClick = new HashMap<>();
        }


    }

    public void initUI(View view) {


        adapterPicture = new AdapterGridviewPicture(getActivity(), listIdUrlPic);
        adapterInput = new AdapterGridviewInput(getActivity(), libraryCharacterToAnswer);
        adapterOutput = new AdapterGridviewOutput(getActivity(), answer, arrCharacterTruAnswer);

        gridviewPicture = (GridView) view.findViewById(R.id.gridviewPicture);
        gridviewInput = (GridView) view.findViewById(R.id.gridviewInput);
        gridviewOutput = (GridView) view.findViewById(R.id.gridviewOutput);
        vNumberQuestion = (TextView) view.findViewById(R.id.numberQuestion);
        root = view.findViewById(R.id.root);
        share = (Button) view.findViewById(R.id.share);
        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(shareListenner);
        buttonNavbarLeft = (ImageButton) view.findViewById(R.id.buttonNavbarLeft);
        buttonNavbarLeft.setBackground(getActivity().getResources().getDrawable(R.drawable.icon_back));
        buttonNavbarLeft.setOnClickListener(backListenner);

        vGold = (TextView) view.findViewById(R.id.gold);
        help = (TextView) view.findViewById(R.id.help);
        passQuestion = (TextView) view.findViewById(R.id.passQuestion);

        vNumberQuestion.setText(numberQuestion + "");
        vGold.setText(gold + "");
        if (lengthTrueAnswer >= 7) {
            gridviewOutput.setNumColumns(7);
        } else {
            gridviewOutput.setNumColumns(lengthTrueAnswer);
        }

        gridviewPicture.setAdapter(adapterPicture);
        gridviewInput.setAdapter(adapterInput);
        gridviewOutput.setAdapter(adapterOutput);


        gridviewPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() instanceof DialogMainGameClickCallback) {
                    ((DialogMainGameClickCallback) getActivity()).openDialogPictureDetail("DialogPicture", position);
                }
            }
        });

        gridviewOutput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (isRunningSelectChoise) {
                    //Todo: open help
                    // get character true

                    if (answer.get(position).isOpenHelp()) {
                        return;
                    }

                    if (relationInoutAndOutClick.get(String.valueOf(position)) != null) {
                        gridviewInput.getChildAt(Integer.parseInt(relationInoutAndOutClick.get(String.valueOf(position)))).setVisibility(View.VISIBLE);
                    }

                    String characterTrueAnswer = arrCharacterTruAnswer.get(position).getCharacterTrue();
                    gridviewOutput.getChildAt(position).setBackground(getActivity().getResources().getDrawable(R.drawable.background_text_result_helper));
                    isRunningSelectChoise = false;

                    // find character true in list ountut
                    for (int index = 0; index < libraryCharacterToAnswer.size(); index++) {
                        String characterInItem = libraryCharacterToAnswer.get(index);
                        if (characterInItem.trim().equals(characterTrueAnswer.trim()) && gridviewInput.getChildAt(index).getVisibility() == View.VISIBLE) {
                            gridviewInput.getChildAt(index).setVisibility(View.INVISIBLE);
                            answer.remove(position);

                            ItemOutPut itemOutPut = new ItemOutPut();
                            itemOutPut.setIsOpenHelp(true);
                            itemOutPut.setCharacterTrue(characterTrueAnswer);

                            answer.add(position, itemOutPut);


                            adapterOutput.notifyDataSetChanged();
                            relationInoutAndOutClick.put(String.valueOf(position), String.valueOf(index));

                            break;
                        }
                    }
                    help.setText("");


                } else {
                    if (answer.size() > position && !"".equals(answer.get(position).getCharacterTrue().trim())) {

                        answer.remove(position);
                        ItemOutPut itemOutPut = new ItemOutPut();
                        itemOutPut.setCharacterTrue("");
                        itemOutPut.setIsOpenHelp(false);

                        answer.add(position, itemOutPut);
                        adapterOutput.notifyDataSetChanged();
                        int positionRelationInCharacterListInout = Integer.parseInt(relationInoutAndOutClick.get(String.valueOf(position)));
                        gridviewInput.getChildAt(positionRelationInCharacterListInout).setVisibility(View.VISIBLE);
                        relationInoutAndOutClick.remove(String.valueOf(position));

                    }
                }

            }
        });

        gridviewInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: check enough answer

                if (isRunningSelectChoise) {
                    return;
                }

                int countCharacterChoise = 0;
                for (ItemOutPut chracterChoiseOld : answer) {
                    if (!chracterChoiseOld.getCharacterTrue().trim().equals("")) {
                        countCharacterChoise++;
                    }
                }
                if (countCharacterChoise == lengthTrueAnswer) {
                    return;
                }

                gridviewInput.getChildAt(position).setVisibility(View.INVISIBLE);

                //TODO: pass data from input to output
                String characterSelect = ((TextView) gridviewInput.getChildAt(position).findViewById(R.id.textView)).getText().toString().trim();
                Log.d("CHARACTER CHOISE", " character choise is " + characterSelect);

                int index = 0;
                boolean isAddFirst = false;
                for (index = 0; index < answer.size(); index++) {
                    ItemOutPut chracterChoiseOld = answer.get(index);
                    if (chracterChoiseOld.getCharacterTrue().trim().equals("")) {
                        isAddFirst = true;
                        answer.remove(index);

                        ItemOutPut itemOutPut = new ItemOutPut();
                        itemOutPut.setCharacterTrue(characterSelect);
                        itemOutPut.setIsOpenHelp(false);

                        answer.add(index, itemOutPut);
                        relationInoutAndOutClick.put(String.valueOf(index), String.valueOf(position));
                        adapterOutput.notifyDataSetChanged();
                        break;
                    }
                }

                if (!isAddFirst) {
                    relationInoutAndOutClick.put(String.valueOf(answer.size()), String.valueOf(position));

                    ItemOutPut itemOutPut = new ItemOutPut();
                    itemOutPut.setCharacterTrue(characterSelect);
                    itemOutPut.setIsOpenHelp(false);

                    answer.add(itemOutPut);
                    adapterOutput.notifyDataSetChanged();
                }


                //TODO: check answer is true
                String answerOnOutput = "";
                for (ItemOutPut itemOutPut : answer) {
                    answerOnOutput += itemOutPut.getCharacterTrue();
                }

                if (question.getTrueAnswer().equals(answerOnOutput.trim())) {

                    if (getActivity() instanceof DialogMainGameClickCallback) {
                        ((DialogMainGameClickCallback) getActivity()).isAnswer(true);
                    }
                } else {
                    // TODO: check enought character notify answer fasle;
                    countCharacterChoise = 0;
                    for (ItemOutPut itemOutPut : answer) {
                        if (!itemOutPut.getCharacterTrue().trim().equals("")) {
                            countCharacterChoise++;
                        }
                    }
                    if (countCharacterChoise == lengthTrueAnswer) {
                        if (getActivity() instanceof DialogMainGameClickCallback) {
                            ((DialogMainGameClickCallback) getActivity()).isAnswer(false);
                        }
                    }
                }


            }
        });

        help.setOnClickListener(helpListenner);
        passQuestion.setOnClickListener(passQuestionListenner);

    }

    private View.OnClickListener backListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() instanceof DialogMainGameClickCallback) {
                ((DialogMainGameClickCallback) getActivity()).dialogMainGameDismiss();
                dismiss();
            }

        }
    };

    private View.OnClickListener shareListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() instanceof DialogMainGameClickCallback) {
                root.setDrawingCacheEnabled(true);
                root.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                if (isRunningSelectChoise) {
                    isRunningSelectChoise = false;
                    help.setText("");
                    int oldGold = SharedPreference.getGold(getActivity()) + Config.GOLD_HELP;
                    SharedPreference.saveGold(oldGold, getActivity());
                    vGold.setText(oldGold + "");
                    adapterOutput.notifyDataSetChanged();

                }

                Bitmap screenShort = Utils.loadBitmapFromView(root, root.getWidth(), root.getHeight());
                Uri uri = Utils.getImageUri(getActivity(), screenShort);
                ((DialogMainGameClickCallback) getActivity()).shareScreen(uri);


            }

        }
    };

    private View.OnClickListener helpListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ("".equals(help.getText().toString().trim())) {
                // turn on help
                help.setText("?");
                if (getActivity() instanceof DialogMainGameClickCallback) {
                    ((DialogMainGameClickCallback) getActivity()).evenHelp();
                }

            } else {
                // turn off help
                if (isRunningSelectChoise) {
                    isRunningSelectChoise = false;
                    int goldReturn = SharedPreference.getGold(getActivity()) + Config.GOLD_HELP;
                    SharedPreference.saveGold(goldReturn, getActivity());
                    vGold.setText(goldReturn + "");
                }

                help.setText("");
            }

        }
    };

    private View.OnClickListener passQuestionListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() instanceof DialogMainGameClickCallback) {
                uploadView(question);
            }
        }
    };


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("QUESTION", (Serializable) question);
        outState.putString("TYPE", type);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.slider_in_left_out_right;
        dialog.setCancelable(false);
        return dialog;
    }

    public void helpListenner(String even) {
        // Todo: pdate gold

        if (!"HELP".equals(even)) {
            return;
        }

        isRunningSelectChoise = true;
        gold = SharedPreference.getGold(getActivity());
        vGold.setText(gold + "");


        // Todo: vibrating input choise
        countGridViewSize = gridviewOutput.getChildCount();
        position = 0;
        view = gridviewOutput.getChildAt(position).findViewById(R.id.textView);

        countRepit = 0;
        SwingCallBack swingCallBack = new SwingCallBack() {
            @Override
            public void swingListenner(String even) {
                if ("END".equals(even)) {
                    if (isRunningSelectChoise) {
                        if (position < countGridViewSize - 1) {
                            position++;
                            if (position == 0) {
                                viewOld = gridviewOutput.getChildAt(countGridViewSize - 1).findViewById(R.id.textView);
                            } else {
                                viewOld = gridviewOutput.getChildAt(position - 1).findViewById(R.id.textView);
                            }

                            view = gridviewOutput.getChildAt(position).findViewById(R.id.textView);
                            swingView(view, viewOld, this);

                        } else {
                            countRepit++;
                            position = 0;
                            viewOld = gridviewOutput.getChildAt(countGridViewSize - 1).findViewById(R.id.textView);

                            view = gridviewOutput.getChildAt(position).findViewById(R.id.textView);
                            swingView(view, viewOld, this);
                        }

                    } else {
                        countRepit = 0;
                        view.setBackground(getActivity().getResources().getDrawable(R.drawable.background_text_result));
                    }


                }
            }
        };

        swingView(view, null, swingCallBack);


    }

    private void swingView(View currentView, View oldView, final SwingCallBack awSwingCallBack) {
        YoYo.with(Techniques.Swing)
                .duration(300)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        awSwingCallBack.swingListenner("START");
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        awSwingCallBack.swingListenner("END");
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(view);
        if (oldView != null) {
            oldView.setBackground(getActivity().getResources().getDrawable(R.drawable.background_text_result));
        }
        currentView.setBackground(getActivity().getResources().getDrawable(R.drawable.background_text_result_helper));
    }

    public void answerFalseListenner() {
        YoYo.with(Techniques.Swing)
                .duration(400)
                .playOn(gridviewOutput);
    }

    public interface SwingCallBack {
        public void swingListenner(String even);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof DialogMainGameClickCallback) {
            ((DialogMainGameClickCallback) getActivity()).dialogMainGameDismiss();
        }
        dismiss();
    }
}
