package com.deepika.keralaelectionlive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TabHome extends Fragment {
    public static Context context;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.context=getActivity();
        View rootView = inflater.inflate(R.layout.tab_home, container, false);

        CardView leadingCard=(CardView)rootView.findViewById(R.id.card_leading);
        TextView ludf=(TextView) rootView.findViewById(R.id.ltvUdf);
        TextView lldf=(TextView)rootView.findViewById(R.id.ltvLdf);
        TextView lnda=(TextView)rootView.findViewById(R.id.ltvNda);
        TextView loth=(TextView)rootView.findViewById(R.id.ltvOth);

        leadingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,LeadingListActivity.class);
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
        return rootView;
    }
}