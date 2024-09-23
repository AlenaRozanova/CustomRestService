package com.example.requset;

public class UserInsertRequest {
    private String name;
    private String email;
    private String sex;

    public UserInsertRequest(String name, String email, String sex) {
        this.name = name;
        this.email = email;
        this.sex = sex;
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

    @Override
    public String toString() {
        return "UserInsertRequest{" +
               "name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", sex='" + sex + '\'' +
               '}';
    }
}
