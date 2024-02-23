package com.heitaox.sql.executor.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> implements Serializable {

    public int code;
    public String message;
    public T data;

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> CommonResponse<T> success() {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(0);
        response.setMessage("处理成功");
        return response;
    }

    public static <T> CommonResponse<T> success(String message) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(0);
        response.setMessage(message);
        return response;
    }

    public static <T> CommonResponse<T> success (T data) {
        return success(data, "success");
    }

    public static <T> CommonResponse<T> success(T data, String message) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(0);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> CommonResponse<T> failure(int code, String errMessage) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(code);
        response.setMessage(errMessage);
        return response;
    }

    public static <T> CommonResponse<T> failure(String errMessage) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(-1);
        response.setMessage(errMessage);
        return response;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
