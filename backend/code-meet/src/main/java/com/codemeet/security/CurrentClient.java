package com.codemeet.security;

public class CurrentClient {

     private  String nickName;

    public CurrentClient(String nickName) {
        this.nickName = nickName;
        System.out.println("nickName: from CurrenClient ==>  " + nickName);
    }

    public CurrentClient() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
