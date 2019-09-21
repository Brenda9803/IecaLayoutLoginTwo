package com.example.bgom.iecalayoutlogintwo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import modelosdatos.Upload;

public class Storage extends AppCompatActivity implements View.OnClickListener{

    Button btnChooseFile,btnUpload;
    EditText txtFileName;
    ImageView imgStorage;
    private static final int PICK_IMAGE_REQUEST=1; //solo va a tener un valor para siempre con el final

    private StorageReference mStorageRef;//abre conexion con proyecto
    private DatabaseReference mDatabaseRef;//da la base de datos
    private Uri mImageUri; //hace las conecciones

    private StorageTask mUploadTask;
    ProgressBar progressBar;

    Button btnShowImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        btnChooseFile=findViewById(R.id.btnChooseFile);
        btnUpload=findViewById(R.id.btnUpload);
        txtFileName=findViewById(R.id.txtFileName);
        imgStorage=findViewById(R.id.imgStorage);
        progressBar=findViewById(R.id.progressBar);
        btnShowImages=findViewById(R.id.btnShowImage);

        btnChooseFile.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnShowImages.setOnClickListener(this);
        //
        //
        //        //instanciar el storage para conectar la app  a la base de datos
        //        //
        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnChooseFile:
                seleccionarImagen();

                break;
            case R.id.btnUpload:

                if(mUploadTask!=null && mUploadTask.isInProgress()){
                    Toast.makeText(getApplicationContext(),getString(R.string.msgInProgress),Toast.LENGTH_SHORT).show();
                }else{
                    subirArchivo();
                }

                break;
            case R.id.btnShowImage:

                startActivity(new Intent(getApplicationContext(), ImageActivity.class));//sin variable por que no ocupo mandar datos
                break;
        }

    }

    private void subirArchivo() {
        //validar que tengamos una imagen cargada
        if(mImageUri!=null){
            //subimos archivo
            //quita las / del direccionamiento del archivo
            final StorageReference fileReference=mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

            //abrir conexion o tarea para subir el archivo
            mUploadTask=fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //abrir el hilo de la conexion
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },5000);
                    Toast.makeText(getApplicationContext(),getString(R.string.msgSuccess),Toast.LENGTH_SHORT).show();
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //usamos nuestro modelo de datos para crear la estructura
                            //que subiremos a firebase dentro de la base de datos
                            Upload upload=new Upload(
                                    txtFileName.getText().toString().trim(), uri.toString()
                            );
                            String uploadId=mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //calcular los milisegundos que dura
                    int p =(int)(100*
                            (taskSnapshot.getBytesTransferred()
                                    /taskSnapshot.getTotalByteCount()
                            ));
                    progressBar.setProgress(p);
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),getString(R.string.msgNFileSelected),Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR= getContentResolver();
        //indicar de que tipo es la imagen
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void seleccionarImagen() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //validacion del codigo

        //data
        //data.getData  validar que trae un dato especifico
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            //consultamos la informacion que regresa el chooser de android
            mImageUri=data.getData();
            //previsualizacion de la imagen
            imgStorage.setImageURI(mImageUri);

        }
    }
}
