package com.fourpicsinword.module;

import com.fourpicsinword.common.objects.Question;

import java.util.ArrayList;

import com.fourpicsinword.R;

/**
 * Created by NGHIA_IT on 12/21/2015
 */
public class LibraryQuestionPicture {


    public static ArrayList<Question> createQuestionList() {
        ArrayList<Question> questions = new ArrayList<>();

        Question aQuestion1 = new Question();
        aQuestion1.setListUrlPicture(new int[]{R.drawable.n0, R.drawable.one, R.drawable.two, R.drawable.three});
        aQuestion1.setTrueAnswer("BONMUA");
        questions.add(aQuestion1);

        Question aQuestion2 = new Question();
        aQuestion2.setListUrlPicture(new int[]{R.drawable.thanhcong1, R.drawable.thanhcong2, R.drawable.thanhcong3, R.drawable.thanhcong4});
        aQuestion2.setTrueAnswer("THANHCONG");
        questions.add(aQuestion2);


        return questions;
    }
}
