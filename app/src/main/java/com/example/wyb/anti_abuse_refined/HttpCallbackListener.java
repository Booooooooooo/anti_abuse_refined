package com.example.wyb.anti_abuse_refined;

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
