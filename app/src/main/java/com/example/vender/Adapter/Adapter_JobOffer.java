package com.example.vender.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vender.Activity.DetailEventOffer;
import com.example.vender.Model.EventOffer;
import com.example.vender.R;

import java.util.List;

public class Adapter_JobOffer extends RecyclerView.Adapter<Adapter_JobOffer.ViewHolder> {
//    private List<Event> eventList;
//    private List<Offer> offerList;
    private List<EventOffer> eventOfferList;
    private Context context;

    public Adapter_JobOffer(Context context, List<EventOffer> eventOfferList) {
//        this.eventList = event_list;
        this.context = context;
        this.eventOfferList = eventOfferList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_offer_joboffer, viewGroup, false);
        return new Adapter_JobOffer.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.nama_event.setText(eventOfferList.get(i).getNama_event());
        viewHolder.tanggal_event.setText("Date : "+eventOfferList.get(i).getTanggal_event());
        viewHolder.kota_event.setText(eventOfferList.get(i).getKota());
        Glide.with(context)
                .load(eventOfferList.get(i).getFoto())
                .placeholder(R.drawable.blankevent)
                .into(viewHolder.image);
        viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailEventOffer.class);
                EventOffer eventOffer = new EventOffer();
                eventOffer.setId(eventOfferList.get(i).getId());
                eventOffer.setFoto(eventOfferList.get(i).getFoto());
                eventOffer.setNama_event(eventOfferList.get(i).getNama_event());
                eventOffer.setVenue(eventOfferList.get(i).getVenue());
                eventOffer.setKota(eventOfferList.get(i).getKota());
                eventOffer.setTanggal_event(eventOfferList.get(i).getTanggal_event());
                eventOffer.setLowongan(eventOfferList.get(i).getLowongan());
                eventOffer.setDeskripsi(eventOfferList.get(i).getDeskripsi());
                eventOffer.setStatus(eventOfferList.get(i).getStatus());
                eventOffer.setMin_fee(eventOfferList.get(i).getMin_fee());
                eventOffer.setMax_fee(eventOfferList.get(i).getMax_fee());
                eventOffer.setUser_id(eventOfferList.get(i).getUser_id());
                eventOffer.setOffer_id(eventOfferList.get(i).getOffer_id());
                eventOffer.setOffer_status(eventOfferList.get(i).getOffer_status());
                eventOffer.setPhonenumber(eventOfferList.get(i).getPhonenumber());
                intent.putExtra("eventoffer", eventOffer);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventOfferList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView nama_event, tanggal_event, kota_event, lowongan_event;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.event_tn);
            nama_event = (TextView) itemView.findViewById(R.id.nama_event);
            tanggal_event = (TextView) itemView.findViewById(R.id.tanggal_event);
            kota_event = (TextView) itemView.findViewById(R.id.kota_event);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.cl);
        }
    }

}

