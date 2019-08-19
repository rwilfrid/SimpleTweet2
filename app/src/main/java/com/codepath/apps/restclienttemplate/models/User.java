package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    public String nom;
    public  long uid;
    public String createdAt;
    public String screenName;
    public String profileImageUrl;
    public String mediaUrl;


    // empty constructor needed by the Parceler library
public User(){

}
    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();

        user.uid = jsonObject.getLong("id");
        user.createdAt = jsonObject.getString("created_at");
        user.nom = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url");
        //user.mediaUrl = jsonObject.getString("media_url");
        return user;

    }
}
