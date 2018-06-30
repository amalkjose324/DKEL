package com.deepika.keralaelectionlive;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mail on 06-03-2018.
 */

public class DomainWiseResultCustomAdapter extends BaseAdapter {
    private Context context;
    ArrayList<HashMap<String,String>> candidate_details;
    LayoutInflater layoutInflater=null;
    public DomainWiseResultCustomAdapter(Context context, ArrayList<HashMap<String,String>> candidate_details){
        this.context=context;
        this.candidate_details=candidate_details;
        layoutInflater = LayoutInflater.from(context);
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
        View rowView=layoutInflater.inflate(R.layout.list_tab_domainwise_result,null);
        TextView name=(TextView)rowView.findViewById(R.id.candidate_name);
        TextView domain=(TextView)rowView.findViewById(R.id.candidate_domain);
        RelativeLayout candidate_layout=(RelativeLayout)rowView.findViewById(R.id.candidate_layout) ;
        TextView panel_code=(TextView)rowView.findViewById(R.id.panel_code);
        TextView idText=(TextView)rowView.findViewById(R.id.candidate_id);
        TextView party=(TextView)rowView.findViewById(R.id.candidate_party);
        TextView votes=(TextView)rowView.findViewById(R.id.candidate_votes);
        ImageView imageView=(ImageView)rowView.findViewById(R.id.icon_result);
        panel_code.setTextColor(Color.rgb(255,255,255));
        panel_code.setTextSize(16f);
        if(candidate_details.get(position).get("panel").equals("UDF")){
            panel_code.setText("U\nD\nF");
            panel_code.setBackgroundColor(Color.rgb(15,130,63));
            name.setTextColor(Color.rgb(15,130,63));
            party.setTextColor(Color.rgb(15,130,63));
            candidate_layout.setBackgroundResource(R.drawable.bg_list_green);
        }else if(candidate_details.get(position).get("panel").equals("LDF")){
            panel_code.setText("L\nD\nF");
            panel_code.setBackgroundColor(Color.rgb(214,39,40));
            party.setTextColor(Color.rgb(214,39,40));
            name.setTextColor(Color.rgb(214,39,40));
            candidate_layout.setBackgroundResource(R.drawable.bg_list_red);
        }else if(candidate_details.get(position).get("panel").equals("NDA")){
            panel_code.setText("N\nD\nA");
            panel_code.setBackgroundColor(Color.rgb(243,112,34));
            party.setTextColor(Color.rgb(243,112,34));
            name.setTextColor(Color.rgb(243,112,34));
            candidate_layout.setBackgroundResource(R.drawable.bg_list_orange);
        }else {
            panel_code.setText("O\nT\nH");
            panel_code.setBackgroundColor(Color.rgb(31,119,180));
            party.setTextColor(Color.rgb(31,119,180));
            name.setTextColor(Color.rgb(31,119,180));
            candidate_layout.setBackgroundResource(R.drawable.bg_list_blue);
        }
        name.setText(candidate_details.get(position).get("name"));
        domain.setText(candidate_details.get(position).get("domain"));
        idText.setText(candidate_details.get(position).get("id"));
        party.setText(candidate_details.get(position).get("party"));
        votes.setText(candidate_details.get(position).get("votes") );
        Picasso.with(context).load(candidate_details.get(position).get("image")).transform(new RoundedTransformation(360, 0)).into(imageView);
        return rowView;
    }
}
