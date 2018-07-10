package com.deepika.keralaelectionlive;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mail on 06-03-2018.
 */

public class LeadingCandidatesCustomAdapter extends BaseAdapter {
    private Context context;
    ArrayList<HashMap<String,String>> candidate_details;
    LayoutInflater layoutInflater=null;
    DbHelper dbHelper;
    public LeadingCandidatesCustomAdapter(Context context, ArrayList<HashMap<String,String>> candidate_details){
        this.context=context;
        dbHelper=new DbHelper(context);
        layoutInflater = LayoutInflater.from(context);
        this.candidate_details=candidate_details;
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
    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        final int candidate =Integer.parseInt(candidate_details.get(position).get("id"));
        View rowView=layoutInflater.inflate(R.layout.list_tab_leading_won_candidates,null);
        TextView name= rowView.findViewById(R.id.candidate_name);
        RelativeLayout candidate_layout= rowView.findViewById(R.id.candidate_layout);
        TextView domain= rowView.findViewById(R.id.candidate_domain);
        TextView party= rowView.findViewById(R.id.candidate_party);
        TextView idText= rowView.findViewById(R.id.result_id);
        TextView panel_code= rowView.findViewById(R.id.panel_code);
        TextView votes= rowView.findViewById(R.id.candidate_votes);
        ImageView imageView= rowView.findViewById(R.id.icon_result);
        panel_code.setTextColor(Color.rgb(255,255,255));
        panel_code.setTextSize(16f);
        if(candidate_details.get(position).get("panel").equals("UDF")){
            panel_code.setText("U\nD\nF");
            panel_code.setBackgroundResource(R.drawable.bg_rect_green);
            name.setTextColor(Color.rgb(15,130,63));
            party.setTextColor(Color.rgb(15,130,63));
            candidate_layout.setBackgroundResource(R.drawable.bg_list_green);
        }else if(candidate_details.get(position).get("panel").equals("LDF")){
            panel_code.setText("L\nD\nF");
            panel_code.setBackgroundResource(R.drawable.bg_rect_red);
            party.setTextColor(Color.rgb(214,39,40));
            name.setTextColor(Color.rgb(214,39,40));
            candidate_layout.setBackgroundResource(R.drawable.bg_list_red);
        }else if(candidate_details.get(position).get("panel").equals("NDA")){
            panel_code.setText("N\nD\nA");
            panel_code.setBackgroundResource(R.drawable.bg_rect_orange);
            party.setTextColor(Color.rgb(243,112,34));
            name.setTextColor(Color.rgb(243,112,34));
            candidate_layout.setBackgroundResource(R.drawable.bg_list_orange);
        }else {
            panel_code.setText("O\nT\nH");
            panel_code.setBackgroundResource(R.drawable.bg_rect_blue);
            party.setTextColor(Color.rgb(31,119,180));
            name.setTextColor(Color.rgb(31,119,180));
            candidate_layout.setBackgroundResource(R.drawable.bg_list_blue);
        }


        name.setText(candidate_details.get(position).get("name"));
        idText.setText(candidate_details.get(position).get("id"));
        domain.setText(candidate_details.get(position).get("domain"));
        party.setText(candidate_details.get(position).get("party"));
        votes.setText(candidate_details.get(position).get("votes"));
        Picasso.with(context).load(candidate_details.get(position).get("image")).transform(new RoundedTransformation(360, 0)).into(imageView);
        return rowView;
    }
}
