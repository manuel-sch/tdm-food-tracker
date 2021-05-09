package com.example.tdm_food_tracker;

import org.json.JSONObject;

import java.util.Objects;

public class JsonRequest {

    private String url;
    private int httpMethod;
    private JSONObject jsonObject;
    private RequestMethod requestMethod;

    public JsonRequest(String url, int httpMethod, RequestMethod requestMethod, JSONObject jsonObject) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.jsonObject = jsonObject;
        this.requestMethod = requestMethod;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(int httpMethod) {
        this.httpMethod = httpMethod;
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
                ", requestMethod=" + httpMethod +
                ", jsonObject=" + jsonObject +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonRequest that = (JsonRequest) o;
        return httpMethod == that.httpMethod &&
                Objects.equals(url, that.url) &&
                Objects.equals(jsonObject, that.jsonObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, httpMethod, jsonObject);
    }
}

