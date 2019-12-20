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
import com.example.vender.Activity.DetailTalentActivity;
import com.example.vender.Model.Talent;
import com.example.vender.R;

import java.util.List;

public class Adapter_TalentList extends RecyclerView.Adapter<Adapter_TalentList.ViewHolder> {
    private Context context;
    private List<Talent> talent_list;
    private List<String> arrfoto;

    public Adapter_TalentList(Context context, List<Talent> talent_list, List<String> arrfoto) {
        this.context = context;
        this.talent_list = talent_list;
        this.arrfoto = arrfoto;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_find_talentlist, viewGroup, false);
        return new Adapter_TalentList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.nama.setText(talent_list.get(i).getNama());
        viewHolder.lokasi.setText(talent_list.get(i).getKota());
        viewHolder.skill.setText(talent_list.get(i).getSkill());
        String[] skill_ = talent_list.get(i).getSkill().split(",");
        String skill = "";
        for (int j = 0; j < skill_.length; j++){
            if (j == skill_.length-1){
                skill += skill_[j].substring(0, skill_[j].length());
            } else{
                skill += skill_[j]+", ";
            }
        }
        viewHolder.skill.setText(skill);

        Glide.with(context)
                .load(arrfoto.get(i))
                .placeholder(R.drawable.blankprofile)
                .into(viewHolder.image);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailTalentActivity.class);
                Talent talent = new Talent();
                talent.setId(talent_list.get(i).getId());
                talent.setNama(talent_list.get(i).getNama());
                talent.setSkill(talent_list.get(i).getSkill());
                talent.setBio(talent_list.get(i).getBio());
                talent.setKota(talent_list.get(i).getKota());
                talent.setMin_fee(talent_list.get(i).getMin_fee());
                talent.setMax_fee(talent_list.get(i).getMax_fee());
                talent.setExperience(talent_list.get(i).getExperience());
                talent.setRating(talent_list.get(i).getRating());
                talent.setNumberofjobs(talent_list.get(i).getNumberofjobs());
                talent.setInstagram(talent_list.get(i).getInstagram());
                talent.setYoutube(talent_list.get(i).getYoutube());
                intent.putExtra("talent", talent);
                intent.putExtra("foto", arrfoto.get(i));
                intent.putExtra("dari", "talentlist");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return talent_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView nama, lokasi, skill;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.talent_tn);
            nama = (TextView) itemView.findViewById(R.id.nama_talent);
            lokasi = (TextView) itemView.findViewById(R.id.lokasi);
            skill = (TextView) itemView.findViewById(R.id.skill);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}


