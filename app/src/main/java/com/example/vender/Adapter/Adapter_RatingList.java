package com.example.vender.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.vender.Model.Jobs;
import com.example.vender.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_RatingList extends RecyclerView.Adapter<Adapter_RatingList.ViewHolder> {
    Context context;
    List<Jobs> jobsList;
    private static final String URL_CHANGERATING = "https://venderapp.000webhostapp.com/changerating.php";

    public Adapter_RatingList(Context context, List<Jobs> jobsList) {
        this.context = context;
        this.jobsList = jobsList;
    }

    @NonNull
    @Override
    public Adapter_RatingList.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_rating, viewGroup, false);
        return new Adapter_RatingList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_RatingList.ViewHolder viewHolder, final int i) {
        viewHolder.nama.setText(jobsList.get(i).getNama());
        Glide.with(context)
                .load(jobsList.get(i).getFoto())
                .placeholder(R.drawable.blankprofile)
                .into(viewHolder.image);
        if (jobsList.get(i).getRating() == 0){
            viewHolder.star1.setBackgroundResource(R.drawable.starempty);
            viewHolder.star2.setBackgroundResource(R.drawable.starempty);
            viewHolder.star3.setBackgroundResource(R.drawable.starempty);
            viewHolder.star4.setBackgroundResource(R.drawable.starempty);
            viewHolder.star5.setBackgroundResource(R.drawable.starempty);
        } else if (jobsList.get(i).getRating() == 1){
            viewHolder.star1.setBackgroundResource(R.drawable.star);
            viewHolder.star2.setBackgroundResource(R.drawable.starempty);
            viewHolder.star3.setBackgroundResource(R.drawable.starempty);
            viewHolder.star4.setBackgroundResource(R.drawable.starempty);
            viewHolder.star5.setBackgroundResource(R.drawable.starempty);
        } else if (jobsList.get(i).getRating() == 2){
            viewHolder.star1.setBackgroundResource(R.drawable.star);
            viewHolder.star2.setBackgroundResource(R.drawable.star);
            viewHolder.star3.setBackgroundResource(R.drawable.starempty);
            viewHolder.star4.setBackgroundResource(R.drawable.starempty);
            viewHolder.star5.setBackgroundResource(R.drawable.starempty);
        } else if (jobsList.get(i).getRating() == 3){
            viewHolder.star1.setBackgroundResource(R.drawable.star);
            viewHolder.star2.setBackgroundResource(R.drawable.star);
            viewHolder.star3.setBackgroundResource(R.drawable.star);
            viewHolder.star4.setBackgroundResource(R.drawable.starempty);
            viewHolder.star5.setBackgroundResource(R.drawable.starempty);
        } else if (jobsList.get(i).getRating() == 4){
            viewHolder.star1.setBackgroundResource(R.drawable.star);
            viewHolder.star2.setBackgroundResource(R.drawable.star);
            viewHolder.star3.setBackgroundResource(R.drawable.star);
            viewHolder.star4.setBackgroundResource(R.drawable.star);
            viewHolder.star5.setBackgroundResource(R.drawable.starempty);
        } else if (jobsList.get(i).getRating() == 5){
            viewHolder.star1.setBackgroundResource(R.drawable.star);
            viewHolder.star2.setBackgroundResource(R.drawable.star);
            viewHolder.star3.setBackgroundResource(R.drawable.star);
            viewHolder.star4.setBackgroundResource(R.drawable.star);
            viewHolder.star5.setBackgroundResource(R.drawable.star);
        }
        viewHolder.star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRating(jobsList.get(i).getEventid(), jobsList.get(i).getTalentid(),1, i);
            }
        });
        viewHolder.star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRating(jobsList.get(i).getEventid(), jobsList.get(i).getTalentid(),2, i);
            }
        });
        viewHolder.star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRating(jobsList.get(i).getEventid(), jobsList.get(i).getTalentid(),3, i);
            }
        });
        viewHolder.star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRating(jobsList.get(i).getEventid(), jobsList.get(i).getTalentid(),4, i);
            }
        });
        viewHolder.star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRating(jobsList.get(i).getEventid(), jobsList.get(i).getTalentid(),5, i);
            }
        });
    }

    public void changeRating(final int eventid, final int talentid, final int rating, final int index){
        final List<Jobs> jobs = this.jobsList;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGERATING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf(
                                    "{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(context, "Success",
                                        Toast.LENGTH_SHORT).show();
                                jobsList.get(index).setRating(rating);
                                notifyDataSetChanged();

                            } else if (success.equals("0")) {
                                Toast.makeText(context, "Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(eventid));
                params.put("talent_id", String.valueOf(talentid));
                params.put("rating", String.valueOf(rating));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView nama;
        Button star1, star2, star3, star4, star5;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.talent_photo);
            nama = (TextView) itemView.findViewById(R.id.talent_name);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            star1 = (Button) itemView.findViewById(R.id.star1);
            star2 = (Button) itemView.findViewById(R.id.star2);
            star3 = (Button) itemView.findViewById(R.id.star3);
            star4 = (Button) itemView.findViewById(R.id.star4);
            star5 = (Button) itemView.findViewById(R.id.star5);
        }
    }
}
