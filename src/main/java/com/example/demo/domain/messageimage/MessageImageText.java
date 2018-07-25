package com.example.demo.domain.messageimage;

import java.util.List;

public class MessageImageText {
    private List<OpenId> touser;
    private Mpnews mpnews;
    private String msgtype;
    private String send_ignore_reprint;

    public List<OpenId> getTouser() {
        return touser;
    }

    public void setTouser(List<OpenId> touser) {
        this.touser = touser;
    }

    public Mpnews getMpnews() {
        return mpnews;
    }

    public void setMpnews(Mpnews mpnews) {
        this.mpnews = mpnews;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getSend_ignore_reprint() {
        return send_ignore_reprint;
    }

    public void setSend_ignore_reprint(String send_ignore_reprint) {
        this.send_ignore_reprint = send_ignore_reprint;
    }
}
