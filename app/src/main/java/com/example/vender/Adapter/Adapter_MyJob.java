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
import com.example.vender.Activity.DetailEventMyJob;
import com.example.vender.Model.Event;
import com.example.vender.Model.MyJob;
import com.example.vender.R;

import java.util.List;

public class Adapter_MyJob extends RecyclerView.Adapter<Adapter_MyJob.ViewHolder> {
    private List<MyJob> myJobs;
    private Context context;

    public Adapter_MyJob(Context context, List<MyJob> myJobs) {
        this.context = context;
        this.myJobs = myJobs;
    }

    @NonNull
    @Override
    public Adapter_MyJob.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_myjob, viewGroup, false);
        return new Adapter_MyJob.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_MyJob.ViewHolder viewHolder, final int i) {
        viewHolder.nama_event.setText(myJobs.get(i).getNama_event());
        if (!myJobs.get(i).getStatus().equals("done")) {
            viewHolder.status.setText("On Going");
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.active));
        } else{
            viewHolder.status.setText("Done");
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        Glide.with(context)
                .load(myJobs.get(i).getFoto())
                .placeholder(R.drawable.blankevent)
                .into(viewHolder.image);

        if (myJobs.get(i).getRating() == 0){
            viewHolder.star1.setImageResource(R.drawable.starempty);
            viewHolder.star2.setImageResource(R.drawable.starempty);
            viewHolder.star3.setImageResource(R.drawable.starempty);
            viewHolder.star4.setImageResource(R.drawable.starempty);
            viewHolder.star5.setImageResource(R.drawable.starempty);
        } else if (myJobs.get(i).getRating() == 1){
            viewHolder.star1.setImageResource(R.drawable.star);
            viewHolder.star2.setImageResource(R.drawable.starempty);
            viewHolder.star3.setImageResource(R.drawable.starempty);
            viewHolder.star4.setImageResource(R.drawable.starempty);
            viewHolder.star5.setImageResource(R.drawable.starempty);
        } else if (myJobs.get(i).getRating() == 2){
            viewHolder.star1.setImageResource(R.drawable.star);
            viewHolder.star2.setImageResource(R.drawable.star);
            viewHolder.star3.setImageResource(R.drawable.starempty);
            viewHolder.star4.setImageResource(R.drawable.starempty);
            viewHolder.star5.setImageResource(R.drawable.starempty);
        } else if (myJobs.get(i).getRating() == 3){
            viewHolder.star1.setImageResource(R.drawable.star);
            viewHolder.star2.setImageResource(R.drawable.star);
            viewHolder.star3.setImageResource(R.drawable.star);
            viewHolder.star4.setImageResource(R.drawable.starempty);
            viewHolder.star5.setImageResource(R.drawable.starempty);
        } else if (myJobs.get(i).getRating() == 4){
            viewHolder.star1.setImageResource(R.drawable.star);
            viewHolder.star2.setImageResource(R.drawable.star);
            viewHolder.star3.setImageResource(R.drawable.star);
            viewHolder.star4.setImageResource(R.drawable.star);
            viewHolder.star5.setImageResource(R.drawable.starempty);
        } else if (myJobs.get(i).getRating() == 5){
            viewHolder.star1.setImageResource(R.drawable.star);
            viewHolder.star2.setImageResource(R.drawable.star);
            viewHolder.star3.setImageResource(R.drawable.star);
            viewHolder.star4.setImageResource(R.drawable.star);
            viewHolder.star5.setImageResource(R.drawable.star);
        }

        viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailEventMyJob.class);
                MyJob myJob = new MyJob();
                myJob.setId(myJobs.get(i).getId());
                myJob.setFoto(myJobs.get(i).getFoto());
                myJob.setNama_event(myJobs.get(i).getNama_event());
                myJob.setVenue(myJobs.get(i).getVenue());
                myJob.setKota(myJobs.get(i).getKota());
                myJob.setTanggal_event(myJobs.get(i).getTanggal_event());
                myJob.setLowongan(myJobs.get(i).getLowongan());
                myJob.setDeskripsi(myJobs.get(i).getDeskripsi());
                myJob.setStatus(myJobs.get(i).getStatus());
                myJob.setMin_fee(myJobs.get(i).getMin_fee());
                myJob.setMax_fee(myJobs.get(i).getMax_fee());
                myJob.setUser_id(myJobs.get(i).getUser_id());
                myJob.setRating(myJobs.get(i).getRating());
                myJob.setPhonenumber(myJobs.get(i).getPhonenumber());
                intent.putExtra("myjob", myJob);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myJobs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image, star1, star2, star3, star4, star5;
        TextView nama_event, status;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.event_tn);
            nama_event = (TextView) itemView.findViewById(R.id.nama_event);
            status = (TextView) itemView.findViewById(R.id.status);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.myjob);
            star1 = (ImageView) itemView.findViewById(R.id.star1);
            star2 = (ImageView) itemView.findViewById(R.id.star2);
            star3 = (ImageView) itemView.findViewById(R.id.star3);
            star4 = (ImageView) itemView.findViewById(R.id.star4);
            star5 = (ImageView) itemView.findViewById(R.id.star5);

        }
    }

}
