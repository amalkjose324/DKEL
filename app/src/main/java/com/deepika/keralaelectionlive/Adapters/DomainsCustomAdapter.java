package com.deepika.keralaelectionlive.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.deepika.keralaelectionlive.DatabaseResources.DbHelper;
import com.deepika.keralaelectionlive.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mail on 06-03-2018.
 */

public class DomainsCustomAdapter extends BaseAdapter {
    private Context context;
    ArrayList<HashMap<String,String>> domain_names;
    LayoutInflater layoutInflater=null;
    DbHelper dbHelper;
    ArrayList<Integer> fav_list;
    public DomainsCustomAdapter(Context context, ArrayList<HashMap<String,String>> domain_names){
        this.context=context;
        dbHelper=new DbHelper(context);
        this.domain_names=domain_names;
        layoutInflater = LayoutInflater.from(context);
        fav_list=dbHelper.getDomainFavList();
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
    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        final int domain_id =Integer.parseInt(domain_names.get(position).get("id"));
        View rowView=layoutInflater.inflate(R.layout.list_tab_domains,null);
        TextView textView= rowView.findViewById(R.id.result_text);
        TextView idText= rowView.findViewById(R.id.result_id);
        ImageView imageView= rowView.findViewById(R.id.icon_result);
        imageButton= rowView.findViewById(R.id.imgStar);
        if(fav_list.contains(domain_id)){
            imageButton.setImageResource(R.drawable.ic_star_gray_24dp);
        }
        else {
            imageButton.setImageResource(R.drawable.ic_star_outline_gray_24dp);
        }
        textView.setText(domain_names.get(position).get("name"));
        idText.setText(domain_names.get(position).get("id"));
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fav_list.contains(domain_id)){
                    dbHelper.removeDomainFromFavourite(domain_id);
                    dbHelper.pushDomainList();
//                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().detach(getVisibleFragment()).attach(getVisibleFragment()).commit();
                }
                else {
                    dbHelper.addDomainToFavourite(domain_id);
                    dbHelper.pushDomainList();
//                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().detach(getVisibleFragment()).attach(getVisibleFragment()).commit();
                }
            }
          //  getSupportFragmentManager().beginTransaction().detach(getVisibleFragment()).attach(getVisibleFragment()).commit();
        });
        String firstLetter =String.valueOf(domain_names.get(position).get("name").charAt(0));
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
