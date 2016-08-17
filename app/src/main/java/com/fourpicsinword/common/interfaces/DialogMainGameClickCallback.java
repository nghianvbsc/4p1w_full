package com.fourpicsinword.common.interfaces;

import android.net.Uri;

/**
 * Created by NGHIA_IT on 12/21/2015.
 */
public interface DialogMainGameClickCallback {
    public void isAnswer(boolean trueAnswer);
    public void openDialogPictureDetail(String dialog, int position);
    public void evenHelp();
    public void dialogMainGameDismiss();
    public void shareScreen(Uri uri);
}
