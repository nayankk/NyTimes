package com.xc0ffee.nytimes.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Article implements Serializable {

    String mWebUrl;
    String mHeadline;
    String mThumbnail;

    public Article(JSONObject object) {
        try {
            this.mWebUrl = object.getString("web_url");
            this.mHeadline = object.getJSONObject("headline").getString("main");

            JSONArray mmJsonArray = object.getJSONArray("multimedia");
            if (mmJsonArray.length() > 0) {
                JSONObject mmObject = mmJsonArray.getJSONObject(0);
                mThumbnail = "http://www.nytimes.com/" + mmObject.getString("url");
            } else {
                mThumbnail = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                list.add(new Article(array.getJSONObject(i)));
            } catch (JSONException e) {

            }
        }
        return list;
    }

    public String getmWebUrl() {
        return mWebUrl;
    }

    public String getmHeadline() {
        return mHeadline;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }
}
