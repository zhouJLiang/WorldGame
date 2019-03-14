package com.q.exception;

public class GameException extends Exception {

    private int code;
    private String message;

    public GameException(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + "code:" + code + ",info:" + message == null ? message : "";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
