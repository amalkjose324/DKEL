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

import java.util.ArrayList;
import java.util.HashMap;

public class TabWinnerCandidatesAll extends Fragment {
    public static ArrayList<HashMap<String,String>> candidate_names=new ArrayList<>();
    public static Context context;
    ListView listView;
    DbHelper dbHelper;
    public static View rootView;
    final Handler handler = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.context=getActivity();
        dbHelper=new DbHelper(getActivity());
        rootView = inflater.inflate(R.layout.tab_candidates, container, false);
        listView=(ListView)rootView.findViewById(R.id.list_results);
        dbHelper.pushWinnerCandidateAllList();
        final EditText editText=(EditText)rootView.findViewById(R.id.search_result);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.result_id);
                dbHelper.setSessionCandidateId(Integer.parseInt(tv.getText().toString()));
                Intent intent=new Intent(context,CandidateInfoActivity.class);
                context.startActivity(intent);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<HashMap<String,String>> temp = new ArrayList<>();
                String  text = editText.getText().toString().toLowerCase().trim();
                temp.clear();
                for (int i = 0; i < candidate_names.size(); i++)
                {
                    if (candidate_names.get(i).get("name").toLowerCase().contains(text) || candidate_names.get(i).get("domain").toLowerCase().contains(text))
                    {
                        temp.add(candidate_names.get(i));
                    }

                }
                listView.setAdapter(new WinnerCandidatesCustomAdapter(context,temp));
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return rootView;
    }
    public void setListValues(ArrayList<HashMap<String,String>> arrayList){
        candidate_names=arrayList;
        if(context!=null ) {
            listView = (ListView) rootView.findViewById(R.id.list_results);
            listView.setAdapter(new WinnerCandidatesCustomAdapter(context, arrayList));
        }
    }
}