package com.projet.esgi.smshack.Data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by fabibi on 06/02/2017.
 */

public class SMSProvider
{
    private ContentResolver _contentResolver;

    public SMSProvider(ContentResolver cr)
    {
        this._contentResolver = cr;
    }

    public List<Conversation> getConversations()
    {
        //la liste des Conversation que l'on va retourner
        List<Conversation> listeConversation = new ArrayList<Conversation>();

        //les colonnes permettant de récupérer les diverses conversations
        String[] smsColumns = new String[]{
            Telephony.Sms.Conversations.MESSAGE_COUNT,
            Telephony.Sms.Conversations.SNIPPET ,
            Telephony.Sms.Conversations.THREAD_ID,
        };

        //création d'un curseur permettant d'accéder aux différentes conversations
        Cursor curseurConversation = _contentResolver.query(Telephony.Sms.Conversations.CONTENT_URI,
                smsColumns, null, null, Telephony.Sms.Conversations.DEFAULT_SORT_ORDER);

        //lecture des conversations
        while(curseurConversation.moveToNext()) {
            //récupération pour chaque row des informations nécessaires
            int msgCount =      curseurConversation.getInt(curseurConversation.getColumnIndex(Telephony.Sms.Conversations.MESSAGE_COUNT));
            int threadId =      curseurConversation.getInt(curseurConversation.getColumnIndex(Telephony.Sms.Conversations.THREAD_ID));
            String snippet =    curseurConversation.getString(curseurConversation.getColumnIndex(Telephony.Sms.Conversations.SNIPPET));

            //nous  allons maintenant pour chaque conversation récupérer le dernier message (on définis ici les colonnes nécessaires)
            String[] smsConversationsSMS = new String[]{
                Telephony.Sms.Conversations.ADDRESS,
                Telephony.Sms.Conversations.BODY ,
                Telephony.Sms.Conversations.PERSON,
                Telephony.Sms.Conversations.DATE,
            };

            //création d'un curseur permettant d'accéder au dernier SMS d'une conversation
            Cursor curseurThreadSMS= _contentResolver.query(Uri.parse("content://sms/"), smsConversationsSMS,
                    Telephony.Sms.THREAD_ID+"=" + threadId, null, "DATE DESC LIMIT 1");

            System.out.println("CONVERSATION \nnb :" + msgCount + " snippet : " + snippet + " threadId : " + threadId);

            //lecture du dernier SMS
            if (curseurThreadSMS.moveToNext()) {
                //récupération pour chaque row des informations nécessaires
                String address =    curseurThreadSMS.getString(curseurThreadSMS.getColumnIndex(Telephony.Sms.Conversations.ADDRESS));
                String body =       curseurThreadSMS.getString(curseurThreadSMS.getColumnIndex(Telephony.Sms.Conversations.BODY));
                String person =     curseurThreadSMS.getString(curseurThreadSMS.getColumnIndex(Telephony.Sms.Conversations.PERSON));
                String date =       curseurThreadSMS.getString(curseurThreadSMS.getColumnIndex(Telephony.Sms.Conversations.DATE));

                //System.out.println("    SMS \n    address : "+address + " body : " + body + " person : " + person + " date : " + date);


                //création d'un nouveau SMS
                Conversation tempoConversation = new Conversation();

                //set et ajout du SMS dans notre liste de SMS
                tempoConversation.setThreadId(threadId);
                tempoConversation.setAddress(address);
                tempoConversation.setBody(body);
                tempoConversation.setDate(new Date(Long.valueOf(date)));

                listeConversation.add(tempoConversation);

            }
            curseurThreadSMS.close();
        }
        curseurConversation.close();

        return listeConversation;

    }


    public List<SMS> getSMSForConversation(int threadId) {

        //la liste des SMS que l'on va retourner
        List<SMS> listeSMS = new ArrayList<>();

        //nous  allons maintenant pour chaque conversation récupérer le dernier message (on définis ici les colonnes nécessaires)
        String[] columnsConversationsSMS = new String[]{
                Telephony.Sms.Conversations.ADDRESS,
                Telephony.Sms.Conversations.BODY ,
                Telephony.Sms.Conversations.PERSON,
                Telephony.Sms.Conversations.DATE,
                Telephony.Sms.Conversations._ID,
        };

        //création d'un curseur permettant d'accéder au dernier SMS d'une conversation
        Cursor curseurInboxSMS= _contentResolver.query(Telephony.Sms.Inbox.CONTENT_URI, columnsConversationsSMS,
                Telephony.Sms.THREAD_ID+"=" + threadId, null, "DATE DESC");

        //création d'un curseur permettant d'accéder au dernier SMS d'une conversation
        Cursor curseurSentSMS= _contentResolver.query(Telephony.Sms.Sent.CONTENT_URI, columnsConversationsSMS,
                Telephony.Sms.THREAD_ID+"=" + threadId, null, "DATE DESC");

        //lecture des sms INBOX
        while(curseurInboxSMS.moveToNext()) {
            //récupération pour chaque row des informations nécessaires
            String address =    curseurInboxSMS.getString(curseurInboxSMS.getColumnIndex(Telephony.Sms.Conversations.ADDRESS));
            String body =       curseurInboxSMS.getString(curseurInboxSMS.getColumnIndex(Telephony.Sms.Conversations.BODY));
            String person =     curseurInboxSMS.getString(curseurInboxSMS.getColumnIndex(Telephony.Sms.Conversations.PERSON));
            String date =       curseurInboxSMS.getString(curseurInboxSMS.getColumnIndex(Telephony.Sms.Conversations.DATE));
            int id =            curseurInboxSMS.getInt(curseurInboxSMS.getColumnIndex(Telephony.Sms.Conversations._ID));

            System.out.println("SMS INBOX \n    address : "+address + " body : " + body + " person : " + person + " date : " + date + " id : " + id);

            //création d'un nouveau SMS
            SMS tempoSMS = new SMS();

            //set et ajout du SMS dans notre liste de SMS
            tempoSMS.setId(id);
            tempoSMS.setAddress(address);
            tempoSMS.setBody(body);
            tempoSMS.setDate(new Date(Long.valueOf(date)));
            tempoSMS.setInbox(true);
            listeSMS.add(tempoSMS);
        }

        //lecture des sms SENT
        while(curseurSentSMS.moveToNext()) {
            //récupération pour chaque row des informations nécessaires
            String address =    curseurSentSMS.getString(curseurSentSMS.getColumnIndex(Telephony.Sms.Conversations.ADDRESS));
            String body =       curseurSentSMS.getString(curseurSentSMS.getColumnIndex(Telephony.Sms.Conversations.BODY));
            String person =     curseurSentSMS.getString(curseurSentSMS.getColumnIndex(Telephony.Sms.Conversations.PERSON));
            String date =       curseurSentSMS.getString(curseurSentSMS.getColumnIndex(Telephony.Sms.Conversations.DATE));
            int id =            curseurSentSMS.getInt(curseurSentSMS.getColumnIndex(Telephony.Sms.Conversations._ID));

            System.out.println("SMS OUTBOX \n    address : "+address + " body : " + body + " person : " + person + " date : " + date + " id : " + id);


            //création d'un nouveau SMS
            SMS tempoSMS = new SMS();

            //set et ajout du SMS dans notre liste de SMS
            tempoSMS.setId(id);
            tempoSMS.setAddress(address);
            tempoSMS.setBody(body);
            tempoSMS.setDate(new Date(Long.valueOf(date)));
            tempoSMS.setInbox(false);

            listeSMS.add(tempoSMS);
        }
        curseurSentSMS.close();

        Collections.sort(listeSMS);


        for (SMS sms : listeSMS) {
            System.out.println("listeSMS ||| address : "+sms.getAddress() + " body : " + sms.getBody() + " date : " + sms.getDate() + " id : " + sms.getId());
        }



        return listeSMS;
    }
}
