package com.thatchosenone.travelmakati.notification;

public class Token {
    /*An FCM token, or much commonly known as a registrationToken.
    * An Id issued by the GCMconnection servers to the client app
    * that allows it to receive messages*/
    String token;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
