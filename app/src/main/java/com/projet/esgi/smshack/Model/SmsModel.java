package com.projet.esgi.smshack.Model;

import android.telephony.SmsMessage;

/**
 * Created by Mickael on 05/12/2016.
 *  SMS MODEL
 */

public class SmsModel {

    String originAdress = "";
    String message ="";
    int smsStatus;
    long timestampMillis;


    public SmsModel(SmsMessage sms){
        this.originAdress = sms.getOriginatingAddress();
        this.message = sms.getMessageBody();
        this.smsStatus = sms.getStatus();
        this.timestampMillis = sms.getTimestampMillis();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOriginAdress() {
        return originAdress;
    }

    public void setOriginAdress(String originAdress) {
        this.originAdress = originAdress;
    }

    public int getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(int smsStatus) {
        this.smsStatus = smsStatus;
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }

    public void setTimestampMillis(long timestampMillis) {
        this.timestampMillis = timestampMillis;
    }

    @Override
    public String toString() {
        return "SmsModel{" +
                "originAdress='" + originAdress + '\'' +
                ", message='" + message + '\'' +
                ", smsStatus=" + smsStatus +
                ", timestampMillis=" + timestampMillis +
                '}';
    }
}
