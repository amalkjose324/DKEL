package com.deepika.keralaelectionlive.TabActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.deepika.keralaelectionlive.Adapters.LeadingCandidatesCustomAdapter;
import com.deepika.keralaelectionlive.CandidateInfoActivity;
import com.deepika.keralaelectionlive.DatabaseResources.DbHelper;
import com.deepika.keralaelectionlive.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class TabLeadingCandidatesNDA extends Fragment {
    public static ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
    public static Context context;
    ListView listView;
    DbHelper dbHelper;
    public static View rootView;
    final Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        dbHelper = new DbHelper(getActivity());
        rootView = inflater.inflate(R.layout.tab_candidates, container, false);
        listView = rootView.findViewById(R.id.list_results);
        dbHelper.pushLeadingCandidateNDAList();
        final EditText editText = rootView.findViewById(R.id.search_result);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(R.id.result_id);
                dbHelper.setSessionCandidateId(Integer.parseInt(tv.getText().toString()));
                Intent intent = new Intent(context, CandidateInfoActivity.class);
                context.startActivity(intent);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<HashMap<String, String>> temp = new ArrayList<>();
                String text = editText.getText().toString().toLowerCase(Locale.ENGLISH).trim();
                temp.clear();
                for (int i = 0; i < candidate_names.size(); i++) {
                    if (candidate_names.get(i).get("name").toLowerCase(Locale.ENGLISH).contains(text) || candidate_names.get(i).get("domain").toLowerCase(Locale.ENGLISH).contains(text)) {
                        temp.add(candidate_names.get(i));
                    }

                }
                listView.setAdapter(new LeadingCandidatesCustomAdapter(context, temp));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        closeKeyBoard(); final FloatingActionButton fab_open = (FloatingActionButton) rootView.findViewById(R.id.search_open_btn);
        final FloatingActionButton fab_close = (FloatingActionButton) rootView.findViewById(R.id.search_close_btn);
        fab_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_open.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                editText.setEnabled(true);
                editText.requestFocus();
                fab_close.setVisibility(View.VISIBLE);
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                } catch (Exception e) {
                }
            }
        });
        fab_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_close.setVisibility(View.GONE);
                editText.setVisibility(View.GONE);
                editText.setEnabled(false);editText.setText("");
                fab_open.setVisibility(View.VISIBLE);
                closeKeyBoard();
            }
        });
        return rootView;
    }

    public void setListValues(ArrayList<HashMap<String, String>> arrayList) {
        candidate_names = arrayList;
        if (context != null) {
            listView = rootView.findViewById(R.id.list_results);
            listView.setAdapter(new LeadingCandidatesCustomAdapter(context, arrayList));
        }
    }
    public void closeKeyBoard(){
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
