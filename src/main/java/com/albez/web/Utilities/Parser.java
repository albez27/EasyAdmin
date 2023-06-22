package com.albez.web.Utilities;
import org.json.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    public static HashMap<Integer, String> parserGroups(String dictionary){
        JSONObject obj = new JSONObject(dictionary);
        JSONArray items = obj.getJSONObject("response").getJSONArray("items");
        HashMap<Integer, String> map = new HashMap<>();
        for (int i = 0; i < items.length(); i++)
        {
            int id = items.getJSONObject(i).getInt("id");
            String name = items.getJSONObject(i).getString("name");
            map.put(id, name);
        }
        return map;
    }
    public static ArrayList<String> parseComment(String dictionary){
        JSONObject obj = new JSONObject(dictionary);
        JSONObject resultObj = obj.getJSONObject("result");
        String result = "";
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 0; i < resultObj.length(); i++){
            result = resultObj.getJSONObject(String.valueOf(i)).getString("text") + ";"
                    + String.valueOf(resultObj.getJSONObject(String.valueOf(i)).getInt("class"));

            arrayList.add(result);
        }
        return arrayList;
    }
    public static HashMap<Integer, String> parserScreenNameGroups(String response){
        JSONArray items = new JSONArray(response);
        HashMap<Integer, String> map = new HashMap<>();
        for(int i = 0; i < items.length(); i++){
            int id = items.getJSONObject(i).getInt("id");
            String screenName = items.getJSONObject(i).getString("screen_name");
            map.put(id, screenName);
        };
        return map;
    }

    public static HashMap<Integer, String> parserLinkToPostGroups(String response){
        JSONArray items = new JSONArray(response);
        HashMap<Integer, String> map = new HashMap<>();
        for(int i = 0; i < items.length(); i++){
            int id = items.getJSONObject(i).getInt("id");
            String screenName = items.getJSONObject(i).getString("photo_100");
            map.put(id, screenName);
        }
        return map;
    }

}
