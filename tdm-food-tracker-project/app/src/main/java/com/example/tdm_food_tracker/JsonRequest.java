package com.example.tdm_food_tracker;

import org.json.JSONObject;

import java.util.Objects;

public class JsonRequest {

    private String url;
    private int requestMethod;
    private JSONObject jsonObject;

    public JsonRequest(String url, int requestMethod, JSONObject jsonObject) {
        this.url = url;
        this.requestMethod = requestMethod;
        this.jsonObject = jsonObject;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public String toString() {
        return "JsonRequest{" +
                "url='" + url + '\'' +
                ", requestMethod=" + requestMethod +
                ", jsonObject=" + jsonObject +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonRequest that = (JsonRequest) o;
        return requestMethod == that.requestMethod &&
                Objects.equals(url, that.url) &&
                Objects.equals(jsonObject, that.jsonObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod, jsonObject);
    }
}
