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

public class TabDomainWiseAnalysis extends Fragment {
    public static Context context;
    DbHelper dbHelper;
    public static View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.context=getActivity();
        dbHelper=new DbHelper(getActivity());
        rootView = inflater.inflate(R.layout.tab_domainwise_analysis, container, false);
        return rootView;
    }
    public void setListValues(ArrayList<String> arrayList){
//        domain_names=arrayList;
//        listView=(ListView)rootView.findViewById(R.id.list_results);
//        listView.setAdapter(new DomainsCustomAdapter(context,arrayList));
    }
}