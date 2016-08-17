package com.fourpicsinword.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.fourpicsinword.module.Config;
import com.fourpicsinword.module.LibraryQuestionPicture;
import com.fourpicsinword.module.SharedPreference;
import com.fourpicsinword.module.Utils;
import com.fourpicsinword.R;
import com.fourpicsinword.ui.dialogs.DialogMainGame;
import com.fourpicsinword.ui.dialogs.DialogPicture;
import com.fourpicsinword.ui.dialogs.DialogReviewQuestion;
import com.fourpicsinword.ui.dialogs.DialogSubGold;
import com.fourpicsinword.ui.dialogs.DialogsNextQuestion;
import com.fourpicsinword.common.interfaces.DialogMainGameClickCallback;
import com.fourpicsinword.common.interfaces.DialogNextQuestionCallBack;
import com.fourpicsinword.common.interfaces.DialogReviewQuestionClickCallBack;
import com.fourpicsinword.common.interfaces.DialogSubGoldCallBack;
import com.fourpicsinword.common.interfaces.PictureClickCallBack;
import com.fourpicsinword.common.objects.Question;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements DialogMainGameClickCallback, PictureClickCallBack, DialogReviewQuestionClickCallBack, DialogNextQuestionCallBack {

    private DialogMainGame dialogMainGame;
    private ArrayList<Question> questions;
    private Question question;
    int numberQuestion;

    private DialogPicture dialogPicture;

    private ProgressBar loadingGame;
    private View viewLoading;
    private int mProgressStatus = 0;
    private DialogReviewQuestion dialogReviewQuestion;
    public static int counntPass = 0;
    private DialogsNextQuestion dialogsNextQuestion;


    private void init() {
        questions = LibraryQuestionPicture.createQuestionList();
        numberQuestion = SharedPreference.getNumberQuestion(this);
        loadingGame = (ProgressBar) findViewById(R.id.loadingGame);
        viewLoading = findViewById(R.id.viewLoading);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        SharedPreference.isOpenFirst(MainActivity.this);
        handleSplash();
    }

    private void handleSplash() {
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mProgressStatus += 5;

                    // Update the progress bar
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            loadingGame.setProgress(mProgressStatus);
                        }
                    });
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: switch to game
                        redirectGame();
                        viewLoading.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }

    private void redirectGame() {
        openDiaogReviewQuestion();

    }

    private void openDiaogReviewQuestion() {
        question = questions.get(numberQuestion - 1);
        if (dialogReviewQuestion != null) {
            dialogReviewQuestion.dismiss();
            dialogReviewQuestion = null;
        }
        dialogReviewQuestion = DialogReviewQuestion.newInstance(question, "OPEN_GAME");
        dialogReviewQuestion.show(getSupportFragmentManager(), "");
    }

    @Override
    public void isAnswer(boolean trueAnswer) {
        if (trueAnswer) {
            counntPass++;

            SharedPreference.saveAnswer(MainActivity.this, null);
            int goldToAdd = Utils.getGoldForAdd(numberQuestion);
            int newGold = SharedPreference.getGold(MainActivity.this) + goldToAdd;
            SharedPreference.saveGold(newGold, MainActivity.this);

            numberQuestion = numberQuestion + 1;
            if (numberQuestion > questions.size()) {

                SharedPreference.saveNumberQuestion(MainActivity.this, 1);
                SharedPreference.saveGold(Config.GOLD_FOR_FIRST_INSTALL_APP, MainActivity.this);
                numberQuestion = 1;
            }

            SharedPreference.saveNumberQuestion(MainActivity.this, numberQuestion);
            dialogsNextQuestion = DialogsNextQuestion.newInstance(question, goldToAdd);
            dialogsNextQuestion.show(getSupportFragmentManager(), "DIALOG_RESULT");
            dialogMainGame.answer.clear();


        } else {
            dialogMainGame.answerFalseListenner();
        }

    }

    @Override
    public void openDialogPictureDetail(String dialog, int position) {
        switch (dialog) {
            case "DialogPicture":
                if (dialogPicture != null) {
                    dialogPicture = null;
                }

                dialogPicture = (DialogPicture) DialogPicture.newIntance(question.getListUrlPicture(), position);
                dialogPicture.show(getSupportFragmentManager(), "DIALOG_PICTURE");
                break;
        }

    }

    @Override
    public void evenHelp() {
        //TODO: check gold
        int gold = SharedPreference.getGold(MainActivity.this);

        if (gold >= Config.GOLD_HELP) {
            // mở dialog xác nhận trừ điểm
            DialogSubGold dialogSubGold = new DialogSubGold(MainActivity.this, 0, Config.GOLD_HELP, new DialogSubGoldCallBack() {
                @Override
                public void dialogSubgoldCallback(String even, int gold) {
                    if ("yes".equals(even)) {
                        int myGold = SharedPreference.getGold(MainActivity.this) - gold;
                        SharedPreference.saveGold(myGold, MainActivity.this);
                        dialogMainGame.helpListenner("HELP");

                    } else {
                        dialogMainGame.help.setText("");
                        dialogMainGame.helpListenner("DON_HELP");
                    }
                }
            });
            dialogSubGold.show();

        } else {
            // mở dialog mua gold
            DialogSubGold dialogSubGold = new DialogSubGold(MainActivity.this, 1, Config.GOLD_HELP, new DialogSubGoldCallBack() {
                @Override
                public void dialogSubgoldCallback(String even, int gold) {
                    dialogMainGame.help.setText("");
                    if ("yes".equals(even)) {
                        dialogMainGame.share.performClick();
                    } else {

                    }
                }
            });
            dialogSubGold.show();

        }
    }

    @Override
    public void dialogMainGameDismiss() {
        //TODO: open dialog reviewQuestion
        if (dialogReviewQuestion != null) {
            dialogReviewQuestion.dismiss();
            dialogReviewQuestion = null;
        }

        dialogReviewQuestion = DialogReviewQuestion.newInstance(question, "BACK_GAME");
        dialogReviewQuestion.show(getSupportFragmentManager(), "");
    }

    @Override
    public void shareScreen(Uri uri) {


        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HELP ME! Answer a Question");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, can you help me answer a question?");

        startActivityForResult(Intent.createChooser(shareIntent, "Share Screen App To"), Config.CODE_INTENT_ACTION_SHARE);
        SharedPreference.saveAnswer(MainActivity.this, dialogMainGame.answer);
        dialogMainGame.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.CODE_INTENT_ACTION_SHARE) {

            if (SharedPreference.getGold(MainActivity.this) < Config.GOLD_HELP) {
                int newGold = SharedPreference.getGold(MainActivity.this) + Config.GOLD_ADD_TO_SHARE;
                SharedPreference.saveGold(newGold, MainActivity.this);
            }


            dialogMainGame = DialogMainGame.newInstance(question, "RE_OPEN_QUESTION");
            dialogMainGame.show(getSupportFragmentManager(), "DIALOF_GAME");

        }
    }

    private void showQuestion(int positionQuestion) {
        question = questions.get(positionQuestion - 1);
        if (dialogMainGame != null) {
            // TODO: dismis dialog
            dialogMainGame.uploadView(question);
        } else {

            // TODO: number question before
            dialogMainGame = DialogMainGame.newInstance(question, "NEW_QUESTION");
            dialogMainGame.show(getSupportFragmentManager(), "DIALOG_GAME");
        }
    }

    @Override
    public void evenDIalogPictureClick() {
//        TODO: close dialog picture detail
        if (dialogPicture != null) dialogPicture.dismiss();
    }

    @Override
    public void evenInDialogReviewGame(String even, String type) {
        switch (even) {
            case Config.PLAY:
                if ("BACK_GAME".equals(type)) {
                    dialogMainGame = DialogMainGame.newInstance(question, "NEW_QUESTION");
                    dialogMainGame.show(getSupportFragmentManager(), "DIALOG_GAME");

                } else {
                    showQuestion(numberQuestion);
                }
                break;
        }

    }

    @Override
    public void dialogNextQuestionCallback(String even) {
        dialogsNextQuestion.dismiss();
        dialogsNextQuestion = null;
        switch (even) {

            case Config.CONTINUE:
                showQuestion(numberQuestion);
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (dialogMainGame == null) {
            SharedPreference.saveAnswer(MainActivity.this, null);
        } else {
            SharedPreference.saveAnswer(MainActivity.this, dialogMainGame.answer);
        }

    }
}
