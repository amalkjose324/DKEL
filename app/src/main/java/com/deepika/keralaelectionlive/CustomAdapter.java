package com.deepika.keralaelectionlive;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

/**
 * Created by mail on 06-03-2018.
 */

public class CustomAdapter extends BaseAdapter {
    private Activity activity;
    ArrayList<String> const_names;
    TabDomains tabDomains;
    LayoutInflater layoutInflater=null;
    DbHelper dbHelper;

    public CustomAdapter(TabDomains tabDomains, ArrayList<String> const_names){
        activity= tabDomains.getActivity();
        dbHelper=new DbHelper(activity);
        this.const_names=const_names;
        this.tabDomains = tabDomains;
        layoutInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return const_names.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    String domain;
    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        domain=const_names.get(position);
        View rowView=layoutInflater.inflate(R.layout.list_tab_domains,null);
        TextView textView=(TextView)rowView.findViewById(R.id.result_text);
        ImageView imageView=(ImageView)rowView.findViewById(R.id.icon_result);
        ImageButton imageButton=(ImageButton)rowView.findViewById(R.id.imgStar);
        if(dbHelper.getFavStatus(domain)){
            imageButton.setImageResource(R.drawable.ic_star_gray_24dp);
        }
        else {
            imageButton.setImageResource(R.drawable.ic_star_outline_gray_24dp);
        }
        textView.setText(domain);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHelper.getFavStatus(domain)){
                    dbHelper.removeFromFavourite(domain);
                }
                else {
                    dbHelper.addToFavourite(domain);
                }
            }
          //  getSupportFragmentManager().beginTransaction().detach(getVisibleFragment()).attach(getVisibleFragment()).commit();
        });
        String firstLetter =String.valueOf(domain.charAt(0));
        ColorGenerator generator = ColorGenerator.DEFAULT; // or use DEFAULT
        // generate random color
        int color = generator.getColor(getItem(position));
        //int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px
        imageView.setImageDrawable(drawable);
        return rowView;
    }

}
