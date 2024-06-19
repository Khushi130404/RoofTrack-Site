package com.example.site_supervisor;

import android.content.Context;
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
    List<Uri> image;

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
        img.setImageURI(image.get(position));
        return view;
    }
}
