package com.example.vender.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vender.Activity.DetailMyEvent;
import com.example.vender.Model.Event;
import com.example.vender.R;

import java.util.List;

public class Adapter_MyEvent extends RecyclerView.Adapter<Adapter_MyEvent.ViewHolder> {
    private Context context;
    private List<Event> eventList;

    public Adapter_MyEvent(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_find_eventlist, viewGroup, false);
        return new Adapter_MyEvent.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        viewHolder.nama_event.setText(eventList.get(i).getNama_event());
        viewHolder.tanggal_event.setText("Date : "+eventList.get(i).getTanggal_event());
        viewHolder.kotalabel.setText("Status : ");
        viewHolder.kota_event.setText(eventList.get(i).getStatus());
        if (eventList.get(i).getStatus().equals("recruiting")) viewHolder.kota_event.setTextColor(context.getResources().getColor(R.color.active));
        else if (eventList.get(i).getStatus().equals("stopped")) viewHolder.kota_event.setTextColor(context.getResources().getColor(R.color.nonactive));
        else viewHolder.kota_event.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        String[] lowongan_ = eventList.get(i).getLowongan().split(",");
        String lowongan = "";
        for (int j = 0; j < lowongan_.length; j++){
            if (j == lowongan_.length-1){
                lowongan += lowongan_[j].substring(0, lowongan_[j].length());
            } else{
                lowongan += lowongan_[j]+", ";
            }
        }
        viewHolder.lowongan_event.setText("Recruiting : "+lowongan);
        Glide.with(context)
                .load(eventList.get(i).getFoto())
                .placeholder(R.drawable.blankevent)
                .into(viewHolder.image);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMyEvent.class);
                Event event = new Event();
                event.setId(eventList.get(i).getId());
                event.setFoto(eventList.get(i).getFoto());
                event.setNama_event(eventList.get(i).getNama_event());
                event.setVenue(eventList.get(i).getVenue());
                event.setKota(eventList.get(i).getKota());
                event.setTanggal_event(eventList.get(i).getTanggal_event());
                event.setLowongan(eventList.get(i).getLowongan());
                event.setDeskripsi(eventList.get(i).getDeskripsi());
                event.setStatus(eventList.get(i).getStatus());
                event.setMin_fee(eventList.get(i).getMin_fee());
                event.setMax_fee(eventList.get(i).getMax_fee());
                event.setUser_id(eventList.get(i).getUser_id());
                intent.putExtra("event", event);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView nama_event, tanggal_event, kota_event, lowongan_event, kotalabel;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.event_tn);
            nama_event = (TextView) itemView.findViewById(R.id.nama_event);
            tanggal_event = (TextView) itemView.findViewById(R.id.tanggal_event);
            kota_event = (TextView) itemView.findViewById(R.id.kota_event);
            kotalabel = (TextView) itemView.findViewById(R.id.kotalabel);
            lowongan_event = (TextView) itemView.findViewById(R.id.lowongan_event);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}
