package com.example.requset;

public class UserInsertRequest {
    private String name;
    private int old;
    private String email;
    private String sex;

    public UserInsertRequest(String name, int old, String email, String sex) {
        this.name = name;
        this.old = old;
        this.email = email;
        this.sex = sex;
    }

    public int getOld() {
        return old;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSex() {
        return sex;
    }

    @Override
    public String toString() {
        return "UserInsertRequest{" +
               "name='" + name + '\'' +
                ", old='" + old + '\'' +
               ", email='" + email + '\'' +
               ", sex='" + sex + '\'' +
               '}';
    }
}
