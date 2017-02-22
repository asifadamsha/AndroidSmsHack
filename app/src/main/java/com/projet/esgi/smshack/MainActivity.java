package com.projet.esgi.smshack;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.projet.esgi.smshack.Adapter.ConversationAdapter;
import com.projet.esgi.smshack.Data.Conversation;
import com.projet.esgi.smshack.Data.SMSProvider;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ConversationAdapter.loadSMSLoader {


    private ConversationAdapter _adapter;
    private SMSProvider _smsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting des variables privées
        _smsProvider =  new SMSProvider(getContentResolver());
        _adapter =      new ConversationAdapter(this);

        //get All broadcast Receiver for priority
        Intent intent = new Intent("android.provider.Telephony.SMS_RECEIVED");

        List<ResolveInfo> infos = getPackageManager().queryBroadcastReceivers(intent, 0);
        for (ResolveInfo info : infos) {
            System.out.println("Receiver name:" + info.activityInfo.name + ";     priority=" + info.priority);
        }

        //setting de notre recyclerView
        RecyclerView rv = (RecyclerView) this.findViewById(R.id.recyclerViewConversation);
        rv.setAdapter(_adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

    protected List<Conversation> parseConversation() {

        //la liste des SMS
        List<Conversation> listeSMSInbox = _smsProvider.getConversations();



        return listeSMSInbox;
    }

    private void refresh() {
        _adapter.addSMSItems(parseConversation());
        _adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }


    @Override
    public void load(int threadId, String user) {
        System.out.println("CLICK sur la conversation aec l'id : "+threadId);

        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra(ConversationActivity.INTENT_THREAD_ID, threadId);
        intent.putExtra(ConversationActivity.INTENT_THREAD_USER, user);

        startActivity(intent);
    }
}
