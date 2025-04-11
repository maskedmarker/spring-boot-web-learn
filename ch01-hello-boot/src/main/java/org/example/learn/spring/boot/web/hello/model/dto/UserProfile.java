package org.example.learn.spring.boot.web.hello.model.dto;

import org.example.learn.spring.boot.web.hello.util.JsonUtils;

public class UserProfile {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String gender;
    private String bio;
    private byte[] headPhoto;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public byte[] getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(byte[] headPhoto) {
        this.headPhoto = headPhoto;
    }

    @Override
    public String toString() {
        return JsonUtils.toStr(this);
    }
}
