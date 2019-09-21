package com.example.bgom.iecalayoutlogintwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//agregado despues del move
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelosdatos.*;


public class RealTime extends AppCompatActivity implements View.OnClickListener {

    Spinner spGrupo;
    Spinner spMateria;
    TextView txtActividad;
    Button btnGuardar, btnEliminar, btnActualizar;
    RecyclerView recyclerView;
    //definir variables de conexion a la base no-sql

    FirebaseDatabase firebaseDatabase;
    DatabaseReference modelClass;
//Declaracion de arreglos para mostrar datos en los spenners
    String [] grupos={"TI-701","AG-701","GE-701","IN-701","ME-701"};
    String [] materias={"Calculo I","Calculo II","Ecuaciones Dif","Colorimetria","Comunicacion asertiva"};

    String itemSeccionado;

    //donde guardo los datos recolectados de snapshot
    ArrayList <Model> list=new ArrayList<>();
//variable del tipo de mi adaptador
    public MyRecyclerViewHolder myRecyclerViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time);//enlazar el xml
        // tomar el control de los elementos

        spGrupo=findViewById(R.id.spGrupo);
        spMateria=findViewById(R.id.spMateria);
        txtActividad=findViewById(R.id.txtActividad);
        btnGuardar=findViewById(R.id.btnGuardar);
        btnEliminar=findViewById(R.id.btnEliminar);
        btnActualizar=findViewById(R.id.btnActualizar);
        recyclerView=findViewById(R.id.recycler_View);

        //asignar eventos
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //instancia lo de firebase database
        firebaseDatabase=FirebaseDatabase.getInstance();

        modelClass=firebaseDatabase.getReference("Model");//iguala los modelos que tiene firebase


       //llenar spinners
        spGrupo.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,grupos));
        spMateria.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,materias));



        btnGuardar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);

        getDataFromFirebase();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGuardar:
                //tap del boton guardar
                addNode();
                break;
            case R.id.btnEliminar:
                deleteNode(itemSeccionado);
                break;
            case R.id.btnActualizar:
                updateNode(itemSeccionado);
                break;

        }
    }

    public void addNode(){
        //recolectar datos del formulario para el grupo
        String datosGrupo =spGrupo.getSelectedItem().toString();
        String datosMateria=spMateria.getSelectedItem().toString();
        String datosActividad=txtActividad.getText().toString();

        if(datosActividad.isEmpty()){
            txtActividad.setError("Llenar campo");
            txtActividad.setFocusable(true);

        }else{
            //agregamos dato al firebase
            //consultamos la base donde se agregaran los elementos
            String idDatabase =modelClass.push().getKey();

            //instancia del modelo de datos, para poder guardar informacion
            Model myActivity=new Model(idDatabase,datosGrupo,datosMateria,datosActividad);

            //gurdar en la base de datos de firebase
            modelClass.child("Lecture").child(idDatabase).setValue(myActivity);
            Toast.makeText(getApplicationContext(),"Agregado correctamente",Toast.LENGTH_SHORT).show();
        }
    }

    //consultar datois de la base de datos de realtime
    //almacenada en firebase

    public  void  getDataFromFirebase(){
        modelClass.child("Lecture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //haace una copia
                if(dataSnapshot.exists()){
                    //procesar informacion que recolectamos de firebase
                    list.clear();
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                     //  String id= "-LoSDFNl4Hve655GTJ4v";
                      String id= ds.child("id").getValue().toString();
                        String grupo= ds.child("group").getValue().toString();
                        String materia= ds.child("lecture").getValue().toString();
                        String actividad= ds.child("activity").getValue().toString();
                        //lista de modelos agrego un nuevo modelo de datos
                        list.add(new Model(id, grupo, materia,actividad));

                    }
                    //llenar el recycler view
                    myRecyclerViewHolder=new MyRecyclerViewHolder(list);
                    recyclerView.setAdapter(myRecyclerViewHolder);

                    //agregado
                    //MyRecyclerViewHolder adapter=new MyRecyclerViewHolder(list);
                    myRecyclerViewHolder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            itemSeccionado=list.get(recyclerView.getChildAdapterPosition(view)).getId();

                            Toast.makeText(getApplicationContext(),"seleccionó: "+ itemSeccionado,Toast.LENGTH_SHORT).show();

                            //para el focus
                            txtActividad.setText( list.get(recyclerView.getChildAdapterPosition(view)).getActivity());
                            txtActividad.setFocusable(true);
                            //termina el focus


                            //para el spiner AG ..

                            String inicializarItemGroup =list.get(recyclerView.getChildAdapterPosition(view)).getGroup();
                            String inicializarItemLecture =list.get(recyclerView.getChildAdapterPosition(view)).getLecture();

                            //  Toast.makeText(getApplicationContext(),"El grupo es: "+ inicializarItem,Toast.LENGTH_SHORT).show();

                            /**
                             * setSelection(): sirve para inicializar una posición especifica(recibe un valor entero)
                             *
                             * Este método realiza la magia
                             * obtenerPosicionItem(): recibe dos parámmetros un spinner y un String
                             */
                            spGrupo.setSelection(obtenerPosicionItemGroup(spGrupo, inicializarItemGroup));
                            spMateria.setSelection(obtenerPosicionItemLecture(spMateria, inicializarItemLecture));

                            //..


                        }
                    });
                    //terminacion


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//metodo para borrar un elemento de la base de datos
    public void deleteNode(String id){
        modelClass.child("Lecture").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"Elemento eliminado correctamente",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"No se pudo eliminar, intenta nuevamente",Toast.LENGTH_SHORT).show();

            }
        });
    }
    //metodo para actualizar un elemento de la base de datos
    public void updateNode(String id){

            //agregue de aqui
            String datosGrupo =spGrupo.getSelectedItem().toString();
            String datosMateria=spMateria.getSelectedItem().toString();
            String datosActividad=txtActividad.getText().toString();

            if(datosActividad.isEmpty()){
                txtActividad.setError("Llenar campo");
                txtActividad.setFocusable(true);

            }else{ //a aqui

                //tomar los valored de la bd y cambiarlos
                Map<String,Object>dataMap=new HashMap<>();


                dataMap.put("group",datosGrupo);
                dataMap.put("lecture",datosMateria);
                dataMap.put("activity",datosActividad);

                modelClass.child("Lecture").child(id).updateChildren(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Elemento actualizado correctamente",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"No se pudo actualizar, intente de nuevo",Toast.LENGTH_SHORT).show();

                    }
                });
            } //y esta linea tambien
    }


    //Método para obtener la posición de un ítem del spinner grupo
    public static int obtenerPosicionItemGroup(Spinner spinnerGroup, String grupo) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinnerGroup.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinnerGroup.getItemAtPosition(i).toString().equalsIgnoreCase(grupo)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }

    //Método para obtener la posición de un ítem del spinner materia
    public static int obtenerPosicionItemLecture(Spinner spinnerLecture, String Materia) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinnerLecture.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinnerLecture.getItemAtPosition(i).toString().equalsIgnoreCase(Materia)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }


}
