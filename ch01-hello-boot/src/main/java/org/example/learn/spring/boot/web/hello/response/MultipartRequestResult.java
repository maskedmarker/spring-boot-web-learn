package org.example.learn.spring.boot.web.hello.response;

import org.example.learn.spring.boot.web.hello.util.JsonUtils;

public class MultipartRequestResult {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JsonUtils.toStr(this);
    }
}
