package com.fourpicsinword.module;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.fourpicsinword.common.objects.ItemOutPut;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by NGHIA_IT on 12/21/2015.
 */
public class Utils {
    public static ImageLoader imageLoader;
    public static String strCharacter = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";

    public static void showImage(ImageView imageView, String urlPicture) {
        if (urlPicture == null) return;
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance(); // Get singleton instance
        }


        imageLoader.displayImage(urlPicture, imageView);
    }


    public static String randomListTextForChoise(String trueAnswer) {
        String result = "";
        ArrayList<String> arrResult = new ArrayList<>();
        int lengthTrueAnswer = trueAnswer.length();
        int countTextAdd = Config.sumCharacterInput - lengthTrueAnswer;
        int index = 0;

        String[] listCharacter = strCharacter.trim().split(" ");
        String[] listCharacterTrueAnswer = trueAnswer.trim().split("");

        for (String s : listCharacterTrueAnswer) {
            if (!s.trim().equals("")) arrResult.add(s);
        }

        HashMap<String, String> mapCharacter = convertArrStringToMap(listCharacter);

        for (String s : arrResult) {
            if (mapCharacter.containsKey(s)) {
                mapCharacter.remove(s);
            }
        }

        ArrayList<String> libaryCharacter = convertMapToArrayList(mapCharacter);
        for (index = 0; index < countTextAdd; index++) {
            int min = 0;
            int max = libaryCharacter.size() - 1;
            Random r = new Random();
            int pisition = r.nextInt(max - min + 1) + min;
            String characterAddToListAnswer = libaryCharacter.get(pisition);
//            libaryCharacter.remove(pisition);
            arrResult.add(characterAddToListAnswer);

        }


        Log.d("Utils", "list answer to choise before shuffle: " + arrResult.toString());
        Collections.shuffle(arrResult);
        Log.d("Utils", "list answer to choise affter shuffle: " + arrResult.toString());

        return convertArrStringToString(arrResult);

    }

    public static ArrayList<String> convertMapToArrayList(Map<String, String> map) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Map.Entry entry : map.entrySet()) {
            arrayList.add(entry.getValue().toString().trim());
        }

        return arrayList;
    }


    public static HashMap<String, String> convertArrStringToMap(String[] listString) {
        HashMap<String, String> map = new HashMap<>();
        for (String s : listString) {
            map.put(s.trim(), s.trim());
        }

        return map;
    }


    public static String convertArrStringToString(ArrayList<String> strString) {
        String result = "";
        for (String s : strString) {
            result += s;
        }

        return result;
    }


    public static ArrayList<String> convertStringToArraylist(String strString) {
        ArrayList<String> result = new ArrayList<>();
        String[] arr = strString.split("");

        for (String s : arr) {
            if (!s.trim().equals(""))
                result.add(s);
        }

        return result;
    }


    public static Integer getGoldForAdd(int numberQuestion) {
        Integer goldAdd = 0;
        int index = 0;
        for (index = 0; index < Config.moldGold.length; index++) {
            int moldGoldItem = Config.moldGold[index];

            if (index == 0) {
                if (numberQuestion <= moldGoldItem) {
                    goldAdd = Config.goldForMold[index];
                }
            } else {
                int moldGoldItemOld = Config.moldGold[index - 1];
                if (numberQuestion < moldGoldItem && moldGoldItem > moldGoldItemOld) {
                    goldAdd = Config.goldForMold[index - 1];
                }
            }

        }
        return goldAdd;
    }


    public static ArrayList<Integer> convertArrToArrayList(int[] listUrlPicture) {
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer integer : listUrlPicture) {
            result.add(integer);
        }
        return result;
    }


    public static ArrayList<ItemOutPut> createListObjectAnswer(ArrayList<String> strings) {
        ArrayList<ItemOutPut> list = new ArrayList<>();

        for (String s : strings) {
            ItemOutPut newItem = new ItemOutPut();
            newItem.setCharacterTrue(s.trim());
            newItem.setIsOpenHelp(false);
            list.add(newItem);
        }
        return list;
    }

    public static ArrayList<ItemOutPut> createListAnswer(int lengthTrueAnswer) {
        ArrayList<ItemOutPut> list = new ArrayList<>();
        for (int index = 0; index < lengthTrueAnswer; index++) {
            ItemOutPut itemOutPut = new ItemOutPut();
            itemOutPut.setCharacterTrue("");
            itemOutPut.setIsOpenHelp(false);
            list.add(itemOutPut);
        }

        return list;
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getPubIdAds(String type) {
//        int min = 0;
//        int max = Config.ARR_PUB_ID.length-1;
//        Random r = new Random();
//        int pisition = r.nextInt(max - min + 1) + min;

        if (type.equals(Config.BANNER)) {
            return Config.ARR_PUB_ID[1];
        } else if (type.equals(Config.INTERSTITIAL)) {
            return Config.ARR_PUB_ID[0];
        }
        return null;
    }

    public static String randomTitle() {
        int min = 0;
        int max = Config.TEXT_TITLE.length - 1;
        Random r = new Random();
        int pisition = r.nextInt(max - min + 1) + min;

        return  Config.TEXT_TITLE[pisition];
    }
}
