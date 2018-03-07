package com.deepika.keralaelectionlive;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

    public CustomAdapter(TabDomains tabDomains, ArrayList<String> const_names){
        activity= tabDomains.getActivity();
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

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View rowView=layoutInflater.inflate(R.layout.list_tab_domains,null);
        TextView textView=(TextView)rowView.findViewById(R.id.result_text);
        ImageView imageView=(ImageView)rowView.findViewById(R.id.icon_result);
        textView.setText(const_names.get(position));
        String firstLetter =String.valueOf(const_names.get(position).charAt(0));
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
