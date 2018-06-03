package com.deepika.keralaelectionlive;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;

public class TabDomainWiseResult extends Fragment {
    public static ArrayList<HashMap<String,String>> candidate_names=new ArrayList<>();
    public static Context context;
    public static ListView listView;
    DbHelper dbHelper;
    public static View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.context=getActivity();
        dbHelper=new DbHelper(getActivity());
        rootView = inflater.inflate(R.layout.tab_domainwise_result, container, false);
        listView=(ListView)rootView.findViewById(R.id.list_results);
        dbHelper.pushDomainWiseCandidateList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.result_text);
                Toast.makeText(getContext(),tv.getText().toString(),Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(context,DomainWiseActivity.class);
//                context.startActivity(intent);
            }
        });
        return rootView;
    }
    public void setListValues(ArrayList<HashMap<String,String>> arrayList){
        candidate_names=arrayList;
        if(context!=null) {
            listView = (ListView) rootView.findViewById(R.id.list_results);
            listView.setAdapter(new DomainWiseResultCustomAdapter(context, arrayList));
        }
    }
}