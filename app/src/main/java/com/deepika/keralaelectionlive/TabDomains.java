package com.deepika.keralaelectionlive;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TabDomains extends Fragment {
    ArrayList<String> domain_names=new ArrayList<>();
    //ArrayList<String> domain_names_eng=new ArrayList<>();
    DbHelper dbHelper;
    Context context;
    ListView listView;
    final Handler handler = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context=getActivity();
        View rootView = inflater.inflate(R.layout.tab_domains, container, false);
        dbHelper=new DbHelper(getActivity());
        domain_names=dbHelper.getDomainNames();
        listView=(ListView)rootView.findViewById(R.id.list_results);
        final EditText editText=(EditText)rootView.findViewById(R.id.search_result);
        listView.setAdapter(new DomainsCustomAdapter(this,domain_names));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.result_text);
                Toast.makeText(getContext(),tv.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<String> temp = new ArrayList<String>();
                String  text = editText.getText().toString().toLowerCase().trim();
                temp.clear();
                for (int i = 0; i < domain_names.size(); i++)
                {
                    if (domain_names.get(i).toLowerCase().contains(text))
                    {
                            temp.add(domain_names.get(i));
                    }
                }
                listView.setAdapter(new DomainsCustomAdapter(TabDomains.this,temp));
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(new DomainsCustomAdapter(TabDomains.this,domain_names));             //
            }
        }, 300);
        return rootView;
    }
}