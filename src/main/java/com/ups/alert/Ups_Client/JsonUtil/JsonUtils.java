package com.ups.alert.Ups_Client.JsonUtil;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonUtils
{
   public static String objToString(Object object){
      return new Gson().toJson(object);
   }

   /*
    * public static <T> T deserializeJson(String json, Class<T> targetType) {
    * ObjectMapper objectMapper = new ObjectMapper(); try { return
    * objectMapper.readValue(json, targetType); } catch (Exception e) {
    * e.printStackTrace(); return null; } }
    */

    public static <T> T stringToObj(String jsonString, Type classOfT) {
        return new Gson().fromJson(jsonString, classOfT);
    }

}
