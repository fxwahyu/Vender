package com.example.vender.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import com.example.vender.Model.WorkedTalent;
import com.example.vender.R;

import java.util.List;

public class Adapter_WorkedTalentList extends RecyclerView.Adapter<Adapter_WorkedTalentList.ViewHolder> {
    private Context context;
    private List<WorkedTalent> workedTalents;

    public Adapter_WorkedTalentList(Context context, List<WorkedTalent> workedTalents) {
        this.context = context;
        this.workedTalents = workedTalents;
    }

    @NonNull
    @Override
    public Adapter_WorkedTalentList.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_workedtalent, viewGroup, false);
        return new Adapter_WorkedTalentList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_WorkedTalentList.ViewHolder viewHolder, final int i) {
        viewHolder.nama.setText(workedTalents.get(i).getNamatalent());
        viewHolder.skill.setText(workedTalents.get(i).getSkill());
        String[] skill_ = workedTalents.get(i).getSkill().split(",");
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
                .load(workedTalents.get(i).getFoto())
                .placeholder(R.drawable.blankprofile)
                .into(viewHolder.image);
        viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailTalentActivity.class);
                Talent talent = new Talent();
                talent.setId(workedTalents.get(i).getTalentid());
                talent.setNama(workedTalents.get(i).getNamatalent());
                talent.setSkill(workedTalents.get(i).getSkill());
                talent.setBio(workedTalents.get(i).getBio());
                talent.setKota(workedTalents.get(i).getKota());
                talent.setMin_fee(workedTalents.get(i).getMin_fee());
                talent.setMax_fee(workedTalents.get(i).getMax_fee());
                talent.setExperience(workedTalents.get(i).getExperience());
                talent.setRating(workedTalents.get(i).getRating());
                talent.setNumberofjobs(workedTalents.get(i).getNumberofjobs());
                talent.setInstagram(workedTalents.get(i).getInstagram());
                talent.setYoutube(workedTalents.get(i).getYoutube());
                intent.putExtra("talent", talent);
                intent.putExtra("foto", workedTalents.get(i).getFoto());
                intent.putExtra("phonenumber", workedTalents.get(i).getPhonenumber());
                intent.putExtra("dari", "workedtalent");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workedTalents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView nama, skill;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.talent_tn);
            nama = (TextView) itemView.findViewById(R.id.nama_talent);
            skill = (TextView) itemView.findViewById(R.id.skill);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.cl);
        }
    }
}
