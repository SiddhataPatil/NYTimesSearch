package com.codepath.siddhatapatil.nytimessearch.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by siddhatapatil on 9/23/17.
 */

public class Article implements Serializable{
    String webUrl;
    String headline;
    String thumbnail;
    public String getThumbnail() {
        return thumbnail;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public  Article (JSONObject jsonObject){
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbnail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                this.thumbnail = "";
            }
        }catch (JSONException e){

        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array)
    {
        ArrayList<Article> results = new ArrayList<>();
        for(int x=0;x<array.length();x++)
        {
            try{
                results.add (new Article(array.getJSONObject(x)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }
}
