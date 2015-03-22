package hu.tvarga.bor.borkostolas.model.bean;

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

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public User(int user_id, String user_name, String user_password, boolean isLoggedIn, int permission) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_password = user_password;
        this.isLoggedIn = isLoggedIn;
        this.permission = permission;
    }

    public User(int user_id, String user_name, String user_password) {
        this.user_password = user_password;
        this.user_name = user_name;
        this.user_id = user_id;
    }

    public User(){}

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

