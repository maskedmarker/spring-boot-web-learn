package org.example.learn.spring.boot.web.hello.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.StringJoiner;

public class MultipartReq {

    private String username;

    private String email;

    private String password;

    private String confirmPassword;

    private String gender;

    private String bio;

    // 这里不能用jdk的File
    private MultipartFile headPhoto;

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

    public MultipartFile getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(MultipartFile headPhoto) {
        this.headPhoto = headPhoto;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MultipartReq.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("email='" + email + "'")
                .add("password='" + password + "'")
                .add("confirmPassword='" + confirmPassword + "'")
                .add("gender='" + gender + "'")
                .add("bio='" + bio + "'")
                .add("headPhoto=" + headPhoto.getOriginalFilename())
                .toString();
    }
}
