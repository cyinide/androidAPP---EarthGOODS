package com.example.guestuser.damirkrkalicearthgoods.helper;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.reflect.Type;

public class MyApiRequest {

    public static String CONTENT_TYPE_JSON = "application/json";
    public static String ENDPOINT_LOCATIONS = "lokacije";
    public static String endpoint_markets = "restorani";
    public static String endpoint_likes = "restorani/%1$d/like";
    public static String endpoint_dislike = "restorani/%1$d/unlike";
    public static String endpoint_food = "restorani/%1$d/hrana";
    public static String endpoint_orders = "narudzbe";
    public static String endpoint_order_create = "narudzbe/create";
    public static String endpoint_order_delete = "narudzbe/%1$d/delete";
    public static String endpoint_auth = "auth";
    public static String endpoint_logout = "auth/logout";
    public static String endpoint_register = "auth/register";
    public static String endpoint_update_user = "auth/update";
    public static String endpoint_delete_user = "auth/delete";

    public static <T> AsyncTask request(final String endpoint, final MyUrlConnection.HttpMethod httpMethod, final Object postObject, final MyAbstractRunnable<T> callback) {
        try {
            AsyncTask<Void, Void, MyApiResult> task = new AsyncTask<Void, Void, MyApiResult>() {

                @Override
                protected MyApiResult doInBackground(Void... voids) {

                    String jsonPostObject = postObject == null ? null : MyGson.build().toJson(postObject);
                    MyApiResult result = MyUrlConnection.request(MyApiConfig.apiBase + "/" + endpoint, httpMethod, jsonPostObject, CONTENT_TYPE_JSON);

                    return result;
                }

                @Override
                protected void onPostExecute(MyApiResult result) {
                    if (result.isException) {
                        if (result.resultCode == 0) {
                            callback.error(result.resultCode, "Greška u komunikaciji sa serverom.");
                        } else {
                            callback.error(result.resultCode, "Greška na serveru, provjerite podatke.");
                        }
                    } else {
                        Type genericType = callback.getGenericType();

                        try {
                            //Log.i("Test", "asyncApiRequest JSONResult: " + result.value);
                            T mappedObj = MyGson.build().fromJson(result.value, genericType);
                            callback.run(mappedObj);
                        } catch (Exception e) {
                            Log.i("Test", "asyncApiRequest - Exception: " + e.getMessage());
                            callback.error(0, "Greška pri prikazivanju podataka.");
                        }
                    }
                }
            };

            task.execute();
            return task;
        } catch (Exception e) {
            Log.i("Test", "---asyncTask--- Catched exception...");
            //e.printStackTrace();
        }
        return null;
    }

    public static <T> AsyncTask get(final String endpoint, final MyAbstractRunnable<T> callback){
        return request(endpoint, MyUrlConnection.HttpMethod.GET, null, callback);
    }

    public static <T> AsyncTask post(final String endpoint, Object postObject, final MyAbstractRunnable<T> callback){
        return request(endpoint, MyUrlConnection.HttpMethod.POST, postObject, callback);
    }

}
