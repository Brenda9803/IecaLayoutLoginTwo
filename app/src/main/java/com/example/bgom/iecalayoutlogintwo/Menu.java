package com.example.bgom.iecalayoutlogintwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Menu extends AppCompatActivity {
    //ya escrito
    //Declaracion de variables
    String usuarioRecuperado;
    String passRecuperada;//aqui termina
    Button multimedia;
    Button cerrarSesion;
    Button btnStorage;
    TextView bienvenida;
    GoogleSignInClient mGoogleSignInClient;
    Button RT;


//ya escrito
    Intent intentRecuperado;//aqui termina

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //ya escrito a partir de aqui
       //optiene el nombre y usuario y muestra un mensaje con los datos
        intentRecuperado=getIntent();
        usuarioRecuperado=intentRecuperado.getStringExtra("myUser");
        passRecuperada=intentRecuperado.getStringExtra("myPassword");
        Toast.makeText(getApplicationContext(),usuarioRecuperado,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),passRecuperada,Toast.LENGTH_SHORT).show();
        //hasta aqui
//toma el control de los datos
        multimedia=findViewById(R.id.btnMultimedia);
        bienvenida=findViewById(R.id.lblBienvenida);
        bienvenida.setText("Hola "+usuarioRecuperado);
        RT=findViewById(R.id.btnRT);

        cerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnStorage=findViewById(R.id.btnIrStorage);
//metodos onClick
        multimedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),Opciones.class);
                startActivity(intent);
            }
        });
        RT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          Intent intent=new Intent(getApplicationContext(),RealTime.class);
            startActivity(intent);   }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutt ();

            }
        });
        btnStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Storage.class);
                startActivity(intent);
            }
        });




    }
    //metodo para cerrar cerrar sesion
    private void signOutt () {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });
    }




}
