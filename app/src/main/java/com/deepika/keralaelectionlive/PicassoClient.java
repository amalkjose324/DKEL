package com.deepika.keralaelectionlive;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoClient {
    public  static  void downloadimg(Context context, String imageUri, ImageView img)
    {
        Picasso.with(context).load(imageUri).into(img);
    }
}
