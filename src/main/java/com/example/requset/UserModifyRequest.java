package com.example.requset;

public class UserModifyRequest {
    private int id;
    private String name;
    private String email;
    private String sex;

    public UserModifyRequest() {
    }

    public UserModifyRequest(int id, String name, String email, String sex) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
