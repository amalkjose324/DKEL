package com.deepika.keralaelectionlive;

import android.content.Context;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PicassoClient {
    public  static  void setImage(final Context context, final String imageUri, final ImageView img,final String name)
    {
        Picasso.with(context).load(imageUri).fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(context).load(imageUri).into(img);
                    }

                    @Override
                    public void onError() {
                        String firstLetter =String.valueOf(name.charAt(0));
                        ColorGenerator generator = ColorGenerator.DEFAULT;
                        int color = generator.getRandomColor();
                        TextDrawable drawable = TextDrawable.builder()
                                .buildRound(firstLetter, color); // radius in px
                        img.setImageDrawable(drawable);
                    }
                });
    }
}
