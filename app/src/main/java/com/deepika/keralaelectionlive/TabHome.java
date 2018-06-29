package com.deepika.keralaelectionlive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class TabHome extends Fragment {
    public static Context context;
    public static TextView ludf,lldf,lnda,loth,wudf,wldf,wnda,woth,ludf_val,lldf_val,lnda_val,loth_val,wudf_val,wldf_val,wnda_val,woth_val;
    DbHelper dbHelper;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.context=getActivity();
        View rootView = inflater.inflate(R.layout.tab_home, container, false);
        dbHelper=new DbHelper(getActivity());
        CardView leadingCard=(CardView)rootView.findViewById(R.id.card_leading);
        CardView wonCard=(CardView)rootView.findViewById(R.id.wonCard);
        ludf=(TextView) rootView.findViewById(R.id.ltvUdf);
        lldf=(TextView)rootView.findViewById(R.id.ltvLdf);
        lnda=(TextView)rootView.findViewById(R.id.ltvNda);
        loth=(TextView)rootView.findViewById(R.id.ltvOth);
        wudf=(TextView) rootView.findViewById(R.id.wtvUdf);
        wldf=(TextView)rootView.findViewById(R.id.wtvLdf);
        wnda=(TextView)rootView.findViewById(R.id.wtvNda);
        woth=(TextView)rootView.findViewById(R.id.wtvOth);

        ludf_val=(TextView) rootView.findViewById(R.id.ltvUcount);
        lldf_val=(TextView)rootView.findViewById(R.id.ltvLcount);
        lnda_val=(TextView)rootView.findViewById(R.id.ltvNcount);
        loth_val=(TextView)rootView.findViewById(R.id.ltvOcount);
        wudf_val=(TextView) rootView.findViewById(R.id.wtvUcount);
        wldf_val=(TextView)rootView.findViewById(R.id.wtvLcount);
        wnda_val=(TextView)rootView.findViewById(R.id.wtvNcount);
        woth_val=(TextView)rootView.findViewById(R.id.wtvOcount);

        dbHelper.pushVoteSummery();
        leadingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,LeadingListActivity.class);
                context.startActivity(intent);
            }
        });
        wonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WonListActivity.class);
                context.startActivity(intent);
            }
        });
        ludf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,LeadingListActivity.class);
                intent.putExtra("FRAGMENT_ID",1);
                context.startActivity(intent);
            }
        });
        lldf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,LeadingListActivity.class);
                intent.putExtra("FRAGMENT_ID",2);
                context.startActivity(intent);
            }
        });
        lnda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,LeadingListActivity.class);
                intent.putExtra("FRAGMENT_ID",3);
                context.startActivity(intent);
            }
        });
        loth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,LeadingListActivity.class);
                intent.putExtra("FRAGMENT_ID",4);
                context.startActivity(intent);
            }
        });
        wudf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WonListActivity.class);
                intent.putExtra("FRAGMENT_ID",1);
                context.startActivity(intent);
            }
        });
        wldf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WonListActivity.class);
                intent.putExtra("FRAGMENT_ID",2);
                context.startActivity(intent);
            }
        });
        wnda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WonListActivity.class);
                intent.putExtra("FRAGMENT_ID",3);
                context.startActivity(intent);
            }
        });
        woth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WonListActivity.class);
                intent.putExtra("FRAGMENT_ID",4);
                context.startActivity(intent);
            }
        });
        return rootView;
    }
    public void setVoteSummery(HashMap<String,Integer> details){
        if(context!=null) {
            int total_leads=details.get("Total_Leads");
            int total_wons=details.get("Total_Wons");
            Float lead_point=(float)(0.8/total_leads);
            Float won_point=(float)(0.8/total_wons);

            float lead_udf= details.get("Lead_UDF")*lead_point;
            float lead_ldf= details.get("Lead_LDF")*lead_point;
            float lead_nda= details.get("Lead_NDA")*lead_point;
            float lead_oth= details.get("Lead_OTH")*lead_point;

            float won_udf=details.get("Won_UDF")*won_point;
            float won_ldf=details.get("Won_LDF")*won_point;
            float won_nda=details.get("Won_NDA")*won_point;
            float won_oth=details.get("Won_OTH")*won_point;

            setSize(ludf,ludf_val,lead_udf,details.get("Lead_UDF"),total_leads);
            setSize(lldf,lldf_val,lead_ldf,details.get("Lead_LDF"),total_leads);
            setSize(lnda,lnda_val,lead_nda,details.get("Lead_NDA"),total_leads);
            setSize(loth,loth_val,lead_oth,details.get("Lead_OTH"),total_leads);
            setSize(wudf,wudf_val,won_udf,details.get("Won_UDF"),total_wons);
            setSize(wldf,wldf_val,won_ldf,details.get("Won_LDF"),total_wons);
            setSize(wnda,wnda_val,won_nda,details.get("Won_NDA"),total_wons);
            setSize(woth,woth_val,won_oth,details.get("Won_OTH"),total_wons);
        }
    }
    public void setSize(TextView panel,TextView score,Float value,int count,int total){
        value=value.isNaN()?0.0f:value;
        if(value==0.0){
            panel.setVisibility(View.GONE);
        }else{
            panel.setVisibility(View.VISIBLE);
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) panel.getLayoutParams();
        params.weight = value;
        panel.setLayoutParams(params);
        LinearLayout.LayoutParams paramsc = (LinearLayout.LayoutParams) score.getLayoutParams();
        paramsc.weight =0.9f - value;
        score.setLayoutParams(paramsc);
        score.setText(String.valueOf(count+"/"+total));
    }
}