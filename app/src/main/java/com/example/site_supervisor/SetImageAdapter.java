package com.example.site_supervisor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.List;

public class SetImageAdapter extends ArrayAdapter
{
    Context cont;
    int resource;
    List<byte[]> image;

    public SetImageAdapter(@NonNull Context cont, int resource, @NonNull List image)
    {
        super(cont, resource, image);
        this.cont = cont;
        this.resource = resource;
        this.image = image;
    }

    public View getView(final int position, View convetView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(resource,null,false);

        ImageView img = view.findViewById(R.id.img);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image.get(position),0,image.get(position).length);
        img.setImageBitmap(bitmap);
        return view;
    }
}
