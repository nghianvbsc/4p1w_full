package com.fourpicsinword.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.fourpicsinword.common.objects.ItemOutPut;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by NGHIA_IT on 12/21/2015.
 */
public class SharedPreference {
    private static SharedPreferences sharedPreferences;


    public static void getPrefs(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(Config.NAME_MEMORY_APP, Context.MODE_PRIVATE);
        }

    }

    public static void saveNumberQuestion(Context context, int numverQuesterPass){
        getPrefs(context);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt(Config.PASS_NUMBER_QUESTION, numverQuesterPass);
        e.commit();
    }

    public static int getNumberQuestion(Context context){
        getPrefs(context);
        return sharedPreferences.getInt(Config.PASS_NUMBER_QUESTION, 1);

    }

    public static Integer getGold(Context context) {
        getPrefs(context);
        return sharedPreferences.getInt(Config.GOLD_SAVE, 0);
    }

    public static void saveGold(int gold, Context context) {
        getPrefs(context);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt(Config.GOLD_SAVE, gold);
        e.commit();
    }

    public static void saveAnswer(Context context, ArrayList<ItemOutPut> answers) {
        getPrefs(context);
        SharedPreferences.Editor e = sharedPreferences.edit();
        String json = null;
        if(answers!=null) {
            json = new Gson().toJson(answers);
        }else{
            json = null;
        }
        e.putString(Config.SAVE_ANSWER, json);
        e.commit();
    }

    public static ArrayList<ItemOutPut> getAnswer(Context context) {
        getPrefs(context);
        ArrayList<ItemOutPut> answers = new ArrayList<>();

        String json = sharedPreferences.getString(Config.SAVE_ANSWER, null);
        if(json!=null){
            Type type = new TypeToken<ArrayList<ItemOutPut>>(){}.getType();
            answers = new Gson().fromJson(json, type);
        }

        return answers;
    }

    public static void isOpenFirst(Context context) {
        getPrefs(context);
        if(sharedPreferences.getBoolean(Config.OPEN_FIRST, true)){
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putInt(Config.GOLD_SAVE, Config.GOLD_FOR_FIRST_INSTALL_APP);
            e.putBoolean(Config.OPEN_FIRST, false);
            e.commit();
        }
    }
}
