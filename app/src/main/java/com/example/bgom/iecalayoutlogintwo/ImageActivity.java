package com.example.bgom.iecalayoutlogintwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import modelosdatos.Upload;
public class ImageActivity extends AppCompatActivity {
    //declaracion de variables
    private RecyclerView mRecyclerView;
    private AdapterDatos mAdapter;
    private DatabaseReference mDatabaseRef;//coneccion a bd
    private List<Upload>mUploads; //para el llenado de datos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mRecyclerView=findViewById(R.id.recycler_View);
        mRecyclerView.setHasFixedSize(true);//darle formato al recycler
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads=new ArrayList<>();
//abrir conexion con firebase
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");//nodo al que queremos acceder
//llenar datos de
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //variable y un set de datos el set de datos va
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Upload upload=postSnapshot.getValue(Upload.class);
                    //agrego a la lista cada nodo
                    mUploads.add(upload);
                }
                mAdapter=new AdapterDatos(ImageActivity.this,mUploads);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ImageActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });



    }

}
