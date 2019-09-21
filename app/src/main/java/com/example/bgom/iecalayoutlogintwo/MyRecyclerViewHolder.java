package com.example.bgom.iecalayoutlogintwo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import modelosdatos.Model;



//agregue el implement
public class MyRecyclerViewHolder extends RecyclerView.Adapter <MyRecyclerViewHolder.ViewHolder> implements View.OnClickListener{
    private int focusedItem = 0;


    //estructura de datos para llenar los elementos graficos
    //encapsulamiento
    private ArrayList<Model> modelList;
    private  View.OnClickListener listener;//agregado escuchador
    //crear un constructor para inicializar la lista de modelos,
    //con los datos que manda firebase y poder usarlos

//constructor
    public MyRecyclerViewHolder(ArrayList<Model> modelList) {
        this.modelList = modelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //asociar  al xml
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_item_db,parent,false);//llenar un layaut

        view.setOnClickListener(this);//lo agregue pueda escuchar

        //enviamos los botones
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



//hace el recorido de los datos del arreglo
        Model model =modelList.get(position);
        holder.lblId.setText(model.getId());
        holder.lblGrupo.setText(model.getGroup());
        holder.lblMateria.setText(model.getLecture());
        holder.lblAtividad.setText(model.getActivity());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;

    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    //sub clases o clases anidadas
    public class  ViewHolder extends RecyclerView.ViewHolder{

        //aqui vamos a inicializar los componentes gráficos de xml con el texto que trae la lista de objetos
        //que manda el usuario al acceder a la base de datos de firebase

        private TextView lblId, lblGrupo,lblMateria,lblAtividad;
        public  View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            //tomar el control
            this.lblId=view.findViewById(R.id.lblIdModelItem);
            this.lblGrupo=view.findViewById(R.id.lblGrupoModelItem);
            this.lblMateria=view.findViewById(R.id.lblMateriaModelItem);
            this.lblAtividad=view.findViewById(R.id.lblActividadModelItem);




            // de aqui


            // a aqui



        }
    }
}
