package com.deepika.keralaelectionlive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class TabHome extends Fragment {
    public static Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.context=getActivity();
        View rootView = inflater.inflate(R.layout.tab_home, container, false);

        CardView cardView=(CardView)rootView.findViewById(R.id.card_leading);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,LeadingListActivity.class);
                context.startActivity(intent);
            }
        });
        return rootView;
    }
}