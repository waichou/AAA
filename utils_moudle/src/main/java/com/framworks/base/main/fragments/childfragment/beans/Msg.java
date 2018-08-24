package com.framworks.base.main.fragments.childfragment.beans;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class Msg {
    public String message;

    public boolean isLeftOrRight;//如果是true表示left ，false表示right


    public Msg() {
    }

    public Msg(String msg, boolean showPosition) {
        message = msg;
        isLeftOrRight = showPosition;
    }
}
