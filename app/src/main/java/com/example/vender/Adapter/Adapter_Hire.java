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
import com.example.vender.Model.TalentOffer;
import com.example.vender.R;

import java.util.List;

public class Adapter_Hire extends RecyclerView.Adapter<Adapter_Hire.ViewHolder> {
    private Context context;
    private List<TalentOffer> talentOfferList;

    public Adapter_Hire(Context context, List<TalentOffer> talentOfferList) {
        this.context = context;
        this.talentOfferList = talentOfferList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_offer_hire, viewGroup, false);
        return new Adapter_Hire.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.namatalent.setText(talentOfferList.get(i).getNamatalent());
        viewHolder.namaevent.setText(talentOfferList.get(i).getNamaevent());
        Glide.with(context)
                .load(talentOfferList.get(i).getFoto())
                .placeholder(R.drawable.blankprofile)
                .into(viewHolder.image);
        viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailTalentActivity.class);
                TalentOffer talent = new TalentOffer();
                talent.setOfferid(talentOfferList.get(i).getOfferid());
                talent.setEventid(talentOfferList.get(i).getEventid());
                talent.setOfferstatus(talentOfferList.get(i).getOfferstatus());
                talent.setNamaevent(talentOfferList.get(i).getNamaevent());
                talent.setTalentid(talentOfferList.get(i).getTalentid());
                talent.setNamatalent(talentOfferList.get(i).getNamatalent());
                talent.setPhonenumber(talentOfferList.get(i).getPhonenumber());
                talent.setSkill(talentOfferList.get(i).getSkill());
                talent.setBio(talentOfferList.get(i).getBio());
                talent.setKota(talentOfferList.get(i).getKota());
                talent.setMin_fee(talentOfferList.get(i).getMin_fee());
                talent.setMax_fee(talentOfferList.get(i).getMax_fee());
                talent.setExperience(talentOfferList.get(i).getExperience());
                talent.setRating(talentOfferList.get(i).getRating());
                talent.setNumberofjobs(talentOfferList.get(i).getNumberofjobs());
                talent.setInstagram(talentOfferList.get(i).getInstagram());
                talent.setYoutube(talentOfferList.get(i).getYoutube());
                talent.setFoto(talentOfferList.get(i).getFoto());
                intent.putExtra("talentoffer", talent);
                intent.putExtra("dari", "offer");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return talentOfferList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView namatalent, namaevent;
        ConstraintLayout constraintLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.talent_tn);
            namatalent = (TextView) itemView.findViewById(R.id.nama_talent);
            namaevent = (TextView) itemView.findViewById(R.id.nama_event);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.cl);
        }
    }
}
