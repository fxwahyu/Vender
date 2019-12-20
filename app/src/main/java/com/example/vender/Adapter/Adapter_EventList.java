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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vender.Activity.DetailEventList;
import com.example.vender.Model.Event;
import com.example.vender.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_EventList extends RecyclerView.Adapter<Adapter_EventList.ViewHolder> {
    private Context context;
    private List<Event> eventList;

    public Adapter_EventList(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_find_eventlist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.nama_event.setText(eventList.get(i).getNama_event());
        viewHolder.tanggal_event.setText("Date : "+eventList.get(i).getTanggal_event());
        viewHolder.kota_event.setText(eventList.get(i).getKota());

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
                Intent intent = new Intent(context, DetailEventList.class);
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
        TextView nama_event, tanggal_event, kota_event, lowongan_event;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.event_tn);
            nama_event = (TextView) itemView.findViewById(R.id.nama_event);
            tanggal_event = (TextView) itemView.findViewById(R.id.tanggal_event);
            kota_event = (TextView) itemView.findViewById(R.id.kota_event);
            lowongan_event = (TextView) itemView.findViewById(R.id.lowongan_event);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }

    public void setImage(ViewHolder viewHolder, final String link){
        Picasso.with(context).load(link).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.image, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Toast.makeText(context, link, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
