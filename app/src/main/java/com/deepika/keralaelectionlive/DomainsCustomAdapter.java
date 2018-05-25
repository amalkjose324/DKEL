package com.deepika.keralaelectionlive;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
import java.util.List;

/**
 * Created by mail on 06-03-2018.
 */

public class DomainsCustomAdapter extends BaseAdapter {
    private Context context;
    ArrayList<String> domain_names;
    TabDomains tabDomains;
    LayoutInflater layoutInflater=null;
    DbHelper dbHelper;

    public DomainsCustomAdapter(Context context, ArrayList<String> domain_names){
        this.context=context;
        dbHelper=new DbHelper(context);
        this.domain_names=domain_names;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return domain_names.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ImageButton imageButton;
    String domain;
    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        domain=domain_names.get(position);
        View rowView=layoutInflater.inflate(R.layout.list_tab_domains,null);
        TextView textView=(TextView)rowView.findViewById(R.id.result_text);
        ImageView imageView=(ImageView)rowView.findViewById(R.id.icon_result);
        imageButton=(ImageButton)rowView.findViewById(R.id.imgStar);
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
                if(dbHelper.getFavStatus(domain_names.get(position))){
                    dbHelper.removeFromFavourite(domain_names.get(position));
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().detach(getVisibleFragment()).attach(getVisibleFragment()).commit();
                }
                else {
                    dbHelper.addToFavourite(domain_names.get(position));
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().detach(getVisibleFragment()).attach(getVisibleFragment()).commit();
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
    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.getUserVisibleHint())
                return (Fragment) fragment;
        }
        return null;
    }
}
