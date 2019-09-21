package com.example.bgom.iecalayoutlogintwo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import modelosdatos.Upload;


public class AdapterDatos extends RecyclerView.Adapter <AdapterDatos.imageViewHolder>{
    private Context mContext;
    private List<Upload>mUploads;

    public AdapterDatos(Context context, List<Upload>uploads){
        this.mContext=context;
        this.mUploads=uploads;
    }

    @NonNull
    @Override
    //inflar la vista
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return  new imageViewHolder(v);
    }

    @Override
    //Asigna el contenido de los controles
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {

        Upload uploadCurrent=mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Picasso.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    //optener del listado
    public int getItemCount() {
        return mUploads.size();
    }

    public class imageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageView;

        public imageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName=itemView.findViewById(R.id.lblTitleCardView);
            imageView= itemView.findViewById(R.id.imgCardView);

        }
    }


}
