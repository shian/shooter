package net.shian5.app.shared;

import dontCare.gf.gae.gwtChannel.client.Message;

/**
 * Created by shian on 2015/10/23.
 */
@SuppressWarnings("serial")
public class ChatMessage implements Message{
    private String name;
    private String msg;

    public ChatMessage(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    public ChatMessage() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
