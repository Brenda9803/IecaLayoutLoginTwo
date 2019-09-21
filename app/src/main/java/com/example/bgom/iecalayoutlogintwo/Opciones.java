package com.example.bgom.iecalayoutlogintwo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class Opciones extends AppCompatActivity {
//declaracion de variables
    private ImageView imageViewCamara;
    private ImageView imagenViewVideo;
    private ImageView imagenViewMusica;

//esto no son oligatorias pero son para guardar las imagenes
    //las variables en mayuscula nunca van a cambiar

    private final String CARPETA_RAIZ ="misImagenesPrueba/";
    private final String RUTA_IMAGEN =CARPETA_RAIZ+"misFotos";
    String path;
    String nombreFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
//se toma el control de los elementos
        imageViewCamara = findViewById(R.id.imgCamara);
        imagenViewVideo= findViewById(R.id.imgVideo);
        imagenViewMusica = findViewById(R.id.imgAudio);
        //se mandan llamar los metodos mediante el OnClickListener
        imageViewCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        });

        imagenViewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirVideo();
            }
        });


        imagenViewMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMusica();
            }
        });

    }
    //Metodo para abrir la camara del dispositovo
    public void abrirCamara(){
        //permisos en tiempo de ejecución
        if(
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
        &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED
        &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
        )//cierra if
        {//cuerpo de if
            //llamar a los permisos
            ActivityCompat.requestPermissions(Opciones.this, new String[]
                    {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                    },1);
            return;
        }
        //lleva a la camara
        Intent intent =new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        startActivityForResult(intent,20);


    }
    //metodo para abrir video
    public void abrirVideo(){
        Intent intent =new Intent((MediaStore.ACTION_VIDEO_CAPTURE));
        startActivityForResult(intent,21);
    }

    //metodo para abrir la musica
    public void abrirMusica(){
        path= Environment.getExternalStorageDirectory()+ File.separator;
        File music =new File(path);
        Intent intent =new Intent((MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(music));
        //envia y regresa informacion
        startActivityForResult(intent,22);
    }

//aqui se arroja lo que devolvio el metodo de abrir camara para que sean guardadas las fotos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        switch (requestCode){
            case 20://el 20 significa el numero de requesCode que manda el startActivityForResult
                //aqui guardo mi imagen
                //variable que almacena la repusta de la toma de fotografia en un mapa de bits

                Date date=new Date();

                Bitmap picture=(Bitmap)data.getExtras().get("data");
                ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();

                picture.compress(Bitmap.CompressFormat.PNG,0,arrayOutputStream);
                //parametros () donde y como se va a guardar
                File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"nombre"+date.getTime()+date.getHours()+date.getMinutes()+date.getSeconds()+".png");

                try {
                    FileOutputStream fileOutputStream=new FileOutputStream(file);
                    fileOutputStream.write(arrayOutputStream.toByteArray());
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }


        //super: ejecuta las versiones de metodos de la super clase de donde esta tomando la herencia
        super.onActivityResult(requestCode, resultCode, data);
    }



}
