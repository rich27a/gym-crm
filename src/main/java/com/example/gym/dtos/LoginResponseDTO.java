package com.example.gym.dtos;

public class LoginResponseDTO {
    private String msg;
    private String Token;

    public LoginResponseDTO(String msg, String token) {
        this.msg = msg;
        Token = token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
