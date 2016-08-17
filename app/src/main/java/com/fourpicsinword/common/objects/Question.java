package com.fourpicsinword.common.objects;

import java.io.Serializable;

/**
 * Created by NGHIA_IT on 12/21/2015.
 */
public class Question implements Serializable{
    private int[] listUrlPicture;
    private String trueAnswer;

    public int[] getListUrlPicture() {
        return listUrlPicture;
    }

    public void setListUrlPicture(int[] listUrlPicture) {
        this.listUrlPicture = listUrlPicture;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        this.trueAnswer = trueAnswer;
    }
}
