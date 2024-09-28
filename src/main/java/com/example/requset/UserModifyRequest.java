package com.example.requset;

public class UserModifyRequest {
    private int id;
    private String name;
    private int old;
    private String email;
    private String sex;

    public UserModifyRequest() {
    }

    public UserModifyRequest(int id, String name, int old, String email, String sex) {
        this.id = id;
        this.name = name;
        this.old = old;
        this.email = email;
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOld() {
        return old;
    }

    public String getEmail() {
        return email;
    }

    public String getSex() {
        return sex;
    }
}
