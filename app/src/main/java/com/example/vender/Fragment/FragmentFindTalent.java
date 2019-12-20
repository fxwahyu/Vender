package com.example.vender.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vender.Adapter.Adapter_EventList;
import com.example.vender.Adapter.Adapter_TalentList;
import com.example.vender.Model.Event;
import com.example.vender.Model.Talent;
import com.example.vender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentFindTalent extends Fragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private List<Talent> talentList;
    private List<String> arrFoto;
    private static String URL_TALENTLIST = "https://venderapp.000webhostapp.com/talentlist.php";
    private RecyclerView rv;
    private TextView kosong;
    private LinearLayout linearLayout;
    private Spinner filter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_find_talent, container, false);

        talentList = new ArrayList<>();
        arrFoto = new ArrayList<>();
        rv = (RecyclerView) view.findViewById(R.id.rv_talentlist);
        kosong = (TextView) view.findViewById(R.id.kosong);

        linearLayout = (LinearLayout) view.findViewById(R.id.filterlayout) ;

        this.filter = (Spinner) view.findViewById(R.id.filter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.array_filter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.filter.setAdapter(adapter);
        this.filter.setOnItemSelectedListener(this);

        takedata();

        return view;
    }

    public void changeData(){
        if (talentList.isEmpty()){
            System.out.println("tidak ada");
            rv.setVisibility(View.GONE);
            kosong.setVisibility(View.VISIBLE);
        } else{
            System.out.println("ada");
            rv.setVisibility(View.VISIBLE);
            kosong.setVisibility(View.GONE);
        }
        RecyclerView myrv = (RecyclerView) view.findViewById(R.id.rv_talentlist);
        Adapter_TalentList myAdapter = new Adapter_TalentList(getActivity(), talentList, arrFoto);
        myrv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        myrv.setAdapter(myAdapter);
    }

    public void changeData(String filter){
        System.out.println(filter);
        List<Talent> filteredTalents = new ArrayList<>();
        List<String> filteredFoto = new ArrayList<>();
        for (int i = 0; i < talentList.size(); i++){
            if (talentList.get(i).getSkill().contains(filter)){
                System.out.println(talentList.get(i).getSkill());
                Talent talent = talentList.get(i);
                filteredTalents.add(talent);
                filteredFoto.add(arrFoto.get(i));
            }
        }

        if (filteredTalents.isEmpty()){
            System.out.println("tidak ada");
            rv.setVisibility(View.GONE);
            kosong.setVisibility(View.VISIBLE);
        } else{
            System.out.println("ada");
            rv.setVisibility(View.VISIBLE);
            kosong.setVisibility(View.GONE);
        }

        RecyclerView myrv = (RecyclerView) view.findViewById(R.id.rv_talentlist);
        Adapter_TalentList myAdapter = new Adapter_TalentList(getActivity(), filteredTalents, filteredFoto);
        myrv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        myrv.setAdapter(myAdapter);
    }

    public void takedata(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TALENTLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("talent");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("id");
                            String nama = object.getString("nama");
                            String skill = object.getString("skill");
                            String bio = object.getString("bio");
                            String kota = object.getString("kota");
                            String min_fee = object.getString("min_fee");
                            String max_fee = object.getString("max_fee");
                            String experience = object.getString("experience");
                            int rating = object.getInt("rating");
                            int numberofjobs = object.getInt("numberofjobs");
                            String instagram = object.getString("instagram");
                            String youtube = object.getString("youtube");
                            String foto = object.getString("foto");

                            arrFoto.add(foto);
                            Talent talentItem = new Talent(id, nama, skill, bio, kota, min_fee, max_fee, experience, rating, numberofjobs, instagram, youtube);
                            talentList.add(talentItem);
                        }

                        changeData();

                    } else if (success.equals("0")){
                        rv.setVisibility(View.GONE);
                        kosong.setVisibility(View.VISIBLE);
//                        Toast.makeText(view.getContext(), "Belum ada talent", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0){
            changeData();
        } else if (position == 1){
            changeData("Penyanyi");
        } else if (position == 2){
            changeData("Pemain Musik");
        } else if (position == 3){
            changeData("Penari");
        } else if (position == 4){
            changeData("Band/Vocal Group");
        } else if (position == 5){
            changeData("MC");
        } else if (position == 6){
            changeData("DJ");
        } else if (position == 7){
            changeData("Pesulap/Badut");
        } else if (position == 8){
            changeData("Fotografer");
        } else if (position == 9){
            changeData("Videografer");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
