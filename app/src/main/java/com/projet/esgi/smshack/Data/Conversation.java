package com.projet.esgi.smshack.Data;

import java.util.Date;

/**
 * Created by fabibi on 05/02/2017.
 */

public class Conversation
{

    private int     _threadId;
    private String  _address;
    private String  _body;
    private Date    _date;


    public int getThreadId() {
        return _threadId;
    }

    public void setThreadId(int _threadId) {
        this._threadId = _threadId;
    }

    public String getBody() {
        return _body;
    }

    public void setBody(String body) {
        this._body = body;
    }


    public String getAddress() {
        return _address;
    }

    public void setAddress(String address) {
        this._address = address;
    }

    public Date getDate() {
        return _date;
    }

    public void setDate(Date date) {
        this._date = date;
    }

}
