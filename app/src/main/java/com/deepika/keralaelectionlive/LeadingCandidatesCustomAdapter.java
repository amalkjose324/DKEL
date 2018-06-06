package com.deepika.keralaelectionlive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        TextView name=(TextView)rowView.findViewById(R.id.candidate_name);
        TextView domain=(TextView)rowView.findViewById(R.id.candidate_domain);
        TextView party=(TextView)rowView.findViewById(R.id.candidate_party);
        TextView idText=(TextView)rowView.findViewById(R.id.result_id);
        TextView votes=(TextView)rowView.findViewById(R.id.candidate_votes);
        ImageView imageView=(ImageView)rowView.findViewById(R.id.icon_result);
        name.setText(candidate_details.get(position).get("name"));
        idText.setText(candidate_details.get(position).get("id"));
        domain.setText(candidate_details.get(position).get("domain"));
        party.setText(candidate_details.get(position).get("party")+" ("+candidate_details.get(position).get("panel")+")");
        votes.setText(candidate_details.get(position).get("votes"));
        Picasso.with(context).load(candidate_details.get(position).get("image")).transform(new RoundedTransformation(360, 0)).into(imageView);
        return rowView;
    }
}
