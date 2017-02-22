package com.projet.esgi.smshack.Data;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by fabibi on 05/02/2017.
 */

public class SMS implements Comparable<SMS>
{
    private int     _id;
    private String  _address;
    private String  _body;
    private Date    _date;
    private Boolean _inbox;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
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

    public Boolean getInbox() {
        return _inbox;
    }

    public void setInbox(Boolean _inbox) {
        this._inbox = _inbox;
    }

    @Override
    public int compareTo(SMS sms) {

        return sms.getId() - this.getId();

    }
}
