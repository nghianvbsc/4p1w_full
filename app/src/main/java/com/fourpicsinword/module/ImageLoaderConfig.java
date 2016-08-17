package com.fourpicsinword.module;

import android.content.Context;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Created by Nguyen Van Nghia on 6/16/2015.
 */
public class ImageLoaderConfig extends BaseImageDownloader {

    public ImageLoaderConfig(Context context){
        super(context);
    }

    @Override
    protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
        HttpURLConnection conn = super.createConnection(url, extra);
        Map<String, String> headers = (Map<String, String>) extra;
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                conn.setRequestProperty(header.getKey(), header.getValue());
                conn.setRequestMethod("GET");
            }
        }
        return conn;
    }
}
