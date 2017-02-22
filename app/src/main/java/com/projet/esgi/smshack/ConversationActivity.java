package com.projet.esgi.smshack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.projet.esgi.smshack.Adapter.ConversationAdapter;
import com.projet.esgi.smshack.Adapter.SMSAdapter;
import com.projet.esgi.smshack.Data.Conversation;
import com.projet.esgi.smshack.Data.SMS;
import com.projet.esgi.smshack.Data.SMSProvider;

import java.util.List;

public class ConversationActivity extends AppCompatActivity {

    public static String INTENT_THREAD_ID =     "id_thread";
    public static String INTENT_THREAD_USER =   "user";

    private SMSAdapter _adapter;
    private SMSProvider _smsProvider;
    private int _threadId;
    private String _user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        //récupération du thread à lire
        _threadId = getIntent().getIntExtra(INTENT_THREAD_ID, -1);
        _user = getIntent().getStringExtra(INTENT_THREAD_USER);

        System.out.println("Je dois afficher le thread avec l'id : " + _threadId + " de l'utilisateur : " + _user);


        this.setTitle(_user);

        //initialisation de notre provider et de notre adapter
        _smsProvider    = new SMSProvider(getContentResolver());
        _adapter        = new SMSAdapter();

        //setting de notre recyclerView
        RecyclerView rv = (RecyclerView) this.findViewById(R.id.recyclerViewSMS);
        rv.setAdapter(_adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }


    protected void refresh() {
        _adapter.addSMSItems(parseSMSConversation(_threadId));
        _adapter.notifyDataSetChanged();
    }


    protected List<SMS> parseSMSConversation(int id) {
        return _smsProvider.getSMSForConversation(id);
    }
}
