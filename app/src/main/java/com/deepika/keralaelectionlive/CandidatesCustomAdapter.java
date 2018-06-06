package com.deepika.keralaelectionlive;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by mail on 06-03-2018.
 */

public class CandidatesCustomAdapter extends BaseAdapter {
    private Context context;
    ArrayList<HashMap<String,String>> candidate_details;
    LayoutInflater layoutInflater=null;
    DbHelper dbHelper;
    ArrayList<Integer>fav_list;
    public CandidatesCustomAdapter(Context context, ArrayList<HashMap<String,String>> candidate_details){
        this.context=context;
        dbHelper=new DbHelper(context);
        this.candidate_details=candidate_details;
        layoutInflater = LayoutInflater.from(context);
        fav_list=dbHelper.getCandidateFavList();
    }

    @Override
    public int getCount() {
        return candidate_details.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ImageButton imageButton;
    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        final int candidate =Integer.parseInt(candidate_details.get(position).get("id"));
        View rowView=layoutInflater.inflate(R.layout.list_tab_candidates,null);
        TextView name=(TextView)rowView.findViewById(R.id.candidate_name);
        TextView domain=(TextView)rowView.findViewById(R.id.candidate_domain);
        TextView party=(TextView)rowView.findViewById(R.id.candidate_party);
        TextView idText=(TextView)rowView.findViewById(R.id.result_id);
        TextView votes=(TextView)rowView.findViewById(R.id.candidate_votes);
        ImageView imageView=(ImageView)rowView.findViewById(R.id.icon_result);
        imageButton=(ImageButton)rowView.findViewById(R.id.imgStar);
        if(fav_list.contains(candidate)){
            imageButton.setImageResource(R.drawable.ic_star_gray_24dp);
        }
        else {
            imageButton.setImageResource(R.drawable.ic_star_outline_gray_24dp);
        }
        name.setText(candidate_details.get(position).get("name"));
        idText.setText(candidate_details.get(position).get("id"));
        domain.setText(candidate_details.get(position).get("domain"));
        party.setText(candidate_details.get(position).get("party")+" ("+candidate_details.get(position).get("panel")+")");
        votes.setText(candidate_details.get(position).get("votes"));
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fav_list.contains(candidate)){
                    dbHelper.removeCandidateFromFavourite(candidate);
                    dbHelper.pushCandidateList();
//                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().detach(getVisibleFragment()).attach(getVisibleFragment()).commit();
                }
                else {
                    dbHelper.addCandidateToFavourite(candidate);
                    dbHelper.pushCandidateList();
//                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().detach(getVisibleFragment()).attach(getVisibleFragment()).commit();
                }
            }
            //  getSupportFragmentManager().beginTransaction().detach(getVisibleFragment()).attach(getVisibleFragment()).commit();
        });
        Picasso.with(context).load(candidate_details.get(position).get("image"))
                .transform(new RoundedTransformation(360, 0))
                .into(imageView);
        return rowView;
    }
    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.getUserVisibleHint())
                return (Fragment) fragment;
        }
        return null;
    }
}
