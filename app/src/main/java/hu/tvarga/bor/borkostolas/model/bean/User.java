package hu.tvarga.bor.borkostolas.model.bean;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    private int user_id;
    private String user_name;
    private String user_password;
    private boolean isLoggedIn;
    private int permission;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public User(){}

    public void setPrefs(Context context){
        SharedPreferences pref = context.getSharedPreferences("borkostolasPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("user_id", this.user_id);
        editor.putString("user_name", this.user_name);
        editor.putString("user_password", this.user_password);
        editor.putBoolean("isLoggedIn", this.isLoggedIn);
        editor.putInt("permission", this.permission);
        editor.commit();
    }

    public void populateFromPrefs(Context context){
        SharedPreferences pref = context.getSharedPreferences("borkostolasPref", 0);
        this.user_id = pref.getInt("user_id", -1);
        this.user_name = pref.getString("user_name", "");
        this.user_password = pref.getString("user_password", "");
        this.isLoggedIn = pref.getBoolean("isLoggedIn", false);
        this.permission = pref.getInt("permission", 0);
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_password='" + user_password + '\'' +
                ", isLoggedIn=" + isLoggedIn +
                ", permission=" + permission +
                '}';
    }
}

