package cn.milkmo.dnsxxx;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -4845320657447597730L;

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_ERROR = -1;

    public static final String MSG_SUCCESS = "success";
    public static final String MSG_ERROR = "error";

    private int code;
    private String msg;
    private T data;

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success() {
        return new Result<String>(CODE_SUCCESS, MSG_SUCCESS, null);
    }

    public static Result success(String msg) {
        return new Result<>(CODE_SUCCESS, msg, null);
    }

    public static <T> Result success(T data) {
        return new Result<>(CODE_SUCCESS, MSG_SUCCESS, data);
    }

    public static <T> Result success(String msg, T data) {
        return new Result<>(CODE_SUCCESS, msg, data);
    }

    public static Result error() {
        return new Result<>(CODE_ERROR, MSG_ERROR, null);
    }

    public static Result error(String msg) {
        return new Result<>(CODE_ERROR, msg, null);
    }

    public static <T> Result error(T data) {
        return new Result<>(CODE_ERROR, MSG_ERROR, data);
    }

    public static <T> Result error(String msg, T data) {
        return new Result<>(CODE_ERROR, msg, data);
    }

    public static Result error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    @Override
    public String toString() {
        return "Result{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }
}
