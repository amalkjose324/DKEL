package com.deepika.keralaelectionlive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TabDomainWiseAnalysis extends Fragment {
    public static Context context;
    public static ArrayList<Entry> vote_values = new ArrayList<Entry>();
    public static ArrayList<String> xVals=new ArrayList<String>();
    public static ArrayList<Integer> colour=new ArrayList<Integer>();
    DbHelper dbHelper;
    public static View rootView;
    TextView t_vote,u_vote,l_vote,n_vote,o_vote,u_percentage,l_percentage,n_percentage,o_percentage;
    PieChart pieChart;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.context=getActivity();
        dbHelper=new DbHelper(getActivity());
        rootView = inflater.inflate(R.layout.tab_domainwise_analysis, container, false);
        dbHelper.pushDomainWiseAnalysis();
        return rootView;
    }
    public void setListValues(ArrayList<HashMap<String,String>> arrayList){
        if(context!=null && arrayList.size()>0) {
            pieChart =(PieChart) rootView.findViewById(R.id.domainwise_analysis_pie);
            pieChart.setUsePercentValues(true);
            pieChart.setTransparentCircleRadius(30f);
            pieChart.setHoleRadius(30f);
            u_vote=(TextView) rootView.findViewById(R.id.udf_vote);
            l_vote=(TextView) rootView.findViewById(R.id.ldf_vote);
            n_vote=(TextView) rootView.findViewById(R.id.nda_vote);
            o_vote=(TextView) rootView.findViewById(R.id.oth_vote);
            t_vote=(TextView) rootView.findViewById(R.id.total_votes);
            u_percentage=(TextView) rootView.findViewById(R.id.udf_percentage);
            l_percentage=(TextView) rootView.findViewById(R.id.ldf_percentage);
            n_percentage=(TextView) rootView.findViewById(R.id.nda_percentage);
            o_percentage=(TextView) rootView.findViewById(R.id.oth_percentage);
            vote_values.clear();
            xVals.clear();
            colour.clear();
            int total_votes=0;
            for(int i=0;i<arrayList.size();i++){
                total_votes+=Integer.parseInt(arrayList.get(i).get("votes"));
            }
            t_vote.setText(String.valueOf(total_votes));
            pieChart.setCenterText("Kerala\nElection\nLive");
            pieChart.setCenterTextColor(Color.parseColor("#154A7D"));
            pieChart.setHoleColor(Color.TRANSPARENT);
            for(int i=0;i<arrayList.size();i++){
                String panel=arrayList.get(i).get("panel");
                if(panel.equals("UDF")){
                    xVals.add("UDF");
                    colour.add(Color.rgb(15,130,63));
                    float vote=Float.parseFloat(arrayList.get(i).get("votes"));
                    vote_values.add(new Entry(vote, 0));
                    u_vote.setText(String.format(Locale.CANADA, "%.0f",vote));
                    u_percentage.setText(String.format(Locale.CANADA,"%.1f",vote/total_votes*100)+"%");
                }else if(panel.equals("LDF")){
                    xVals.add("LDF");
                    colour.add(Color.rgb(214,39,40));
                    float vote=Float.parseFloat(arrayList.get(i).get("votes"));
                    vote_values.add(new Entry(vote, 1));
                    l_vote.setText(String.format(Locale.CANADA, "%.0f",vote));
                    l_percentage.setText(String.format(Locale.CANADA,"%.1f",vote/total_votes*100)+"%");
                }else if(panel.equals("NDA")){
                    xVals.add("NDA");
                    colour.add(Color.rgb(243,112,34));
                    float vote=Float.parseFloat(arrayList.get(i).get("votes"));
                    vote_values.add(new Entry(vote, 2));
                    n_vote.setText(String.format(Locale.CANADA, "%.0f",vote));
                    n_percentage.setText(String.format(Locale.CANADA,"%.1f",vote/total_votes*100)+"%");
                }else{
                    xVals.add("OTH");
                    colour.add(Color.rgb(31,119,180));
                    float vote=Float.parseFloat(arrayList.get(i).get("votes"));
                    vote_values.add(new Entry(vote, 3));
                    o_vote.setText(String.format(Locale.CANADA, "%.0f",vote));
                    o_percentage.setText(String.format(Locale.CANADA, "%.1f",vote/total_votes*100)+"%");
                }
            }
            Toast.makeText(context,vote_values.toString(),Toast.LENGTH_SHORT).show();
            PieDataSet dataSet = new PieDataSet(vote_values, "");
            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            pieChart.setData(data);
            pieChart.setDescription("");
            dataSet.setColors(colour);
            data.setValueTextSize(15f);
            data.setValueTextColor(Color.WHITE);
            pieChart.animateXY(1400, 1400);
        }
//        domain_names=arrayList;
//        listView=(ListView)rootView.findViewById(R.id.list_results);
//        listView.setAdapter(new DomainsCustomAdapter(context,arrayList));
    }
}