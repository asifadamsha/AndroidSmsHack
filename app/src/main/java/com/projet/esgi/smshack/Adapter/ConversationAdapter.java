package com.projet.esgi.smshack.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.projet.esgi.smshack.Data.Conversation;
import com.projet.esgi.smshack.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabibi on 05/02/2017.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationAdapterViewHolder> {

    private List<Conversation> _listeConversation = new ArrayList<>();
    private loadSMSLoader _loader;

    public interface loadSMSLoader {
        public void load(int threadId, String user);
    }

    public ConversationAdapter(loadSMSLoader loader) {
        this._loader    = loader;
    }

    //créer une interface pour ca
    public void addSMSItems(List<Conversation> listeConversation) {
        _listeConversation.clear();
        _listeConversation.addAll(listeConversation);
    }


    @Override
    public ConversationAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_convesation, parent, false);
        return new ConversationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationAdapterViewHolder holder, int position) {
        Conversation conversation = _listeConversation.get(position);
        holder.setConversation(conversation);
    }

    @Override
    public int getItemCount() {
        return _listeConversation.size();
    }


    public class ConversationAdapterViewHolder extends RecyclerView.ViewHolder {

        //le sms que gère notre holder
        private Conversation _conversation;

        //les sous-vues de la view que gère notre holder
        private TextView _body;
        private TextView _expediteur;
        private TextView _date;

        public ConversationAdapterViewHolder(final View itemView) {
            super(itemView);

            _body =         (TextView) itemView.findViewById(R.id.bodyTextView);
            _date =         (TextView) itemView.findViewById(R.id.dateTextView);
            _expediteur =   (TextView) itemView.findViewById(R.id.expediteurTextView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _loader.load(_conversation.getThreadId(), _conversation.getAddress());

                }
            });
        }

        public void setConversation(Conversation conversation) {
            //formatage de la date du RSSItem et affichage
            SimpleDateFormat formater = new SimpleDateFormat("'le' dd/MM/yyyy 'à' hh:mm:ss");
            String datePub= (conversation.getDate() != null) ? formater.format(conversation.getDate()) : "";


            //on change la valeur des TextView de notre vue
            _conversation = conversation;
            _body.setText(conversation.getBody());
            _expediteur.setText(conversation.getAddress());
            _date.setText(datePub);

        }





    }
}
