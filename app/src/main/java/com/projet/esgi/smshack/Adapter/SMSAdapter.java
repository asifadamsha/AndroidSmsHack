package com.projet.esgi.smshack.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projet.esgi.smshack.Data.SMS;
import com.projet.esgi.smshack.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabibi on 05/02/2017.
 */

public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.SMSAdapterViewHolder> {

    private List<SMS> _listeSMS = new ArrayList<SMS>();

    public void addSMSItems(List<SMS> listeSMS) {
        _listeSMS.clear();
        _listeSMS.addAll(listeSMS);
    }

    @Override
    public SMSAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate((viewType == 1 )? R.layout.item_sms_inbox : R.layout.item_sms_outbox, parent, false);
        return new SMSAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SMSAdapterViewHolder holder, int position) {
        SMS sms = _listeSMS.get(position);
        holder.setSMS(sms);
    }

    @Override
    public int getItemCount() {
        return _listeSMS.size();
    }

    @Override
    public int getItemViewType(int position) {
        return _listeSMS.get(position).getInbox() ? 1 : 0;
    }



    public class SMSAdapterViewHolder extends RecyclerView.ViewHolder {

        //le sms que gère notre holder
        private SMS _sms;

        //les sous-vues de la view que gère notre holder
        private TextView _body;
        private TextView _date;


        public SMSAdapterViewHolder(final View itemView) {
            super(itemView);

            _body =         (TextView) itemView.findViewById(R.id.bodySMSTextView);
            _date =         (TextView) itemView.findViewById(R.id.dateSMSTextView);

        }

        public void setSMS(SMS sms) {
            //formatage de la date du RSSItem et affichage
            SimpleDateFormat formater = new SimpleDateFormat("'le' dd/MM/yyyy 'à' hh:mm:ss");
            String datePub= (sms.getDate() != null) ? formater.format(sms.getDate()) : "";

            //on change la valeur des TextView de notre vue et on mémorise notre sms
            _sms = sms;
            _body.setText(sms.getBody());
            _date.setText(datePub);

        }
    }
}
