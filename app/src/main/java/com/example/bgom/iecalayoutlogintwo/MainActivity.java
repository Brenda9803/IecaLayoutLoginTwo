package com.example.bgom.iecalayoutlogintwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
//Clase principal
public class MainActivity extends AppCompatActivity  {
//declaracion de variables
    EditText txtUser, txtPassword;
    Button btnLogin, btnAddU;
    SignInButton btnLoginGoogle;
    private FirebaseAuth firebaseAuth;

    private  static final String TAG ="GoogleActivity";
    private static final int RC_SIGN_IN=9001;
    private GoogleSignInClient mGoogleSignInClient;

//Metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializarla variable conexion con firebase
        firebaseAuth=FirebaseAuth.getInstance();
        //ASIGNARCONTROLES A LAS VARIABLES
        //casteo convierte la instancia de los tipos de datos
        txtUser=findViewById(R.id.txtUser);
        txtPassword=findViewById(R.id.txtPassword);
        btnLogin=findViewById(R.id.btnLog);
        btnAddU=findViewById(R.id.btnAdd);
        btnLoginGoogle=findViewById(R.id.sign_in_button);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                login(txtUser.getText().toString().trim(),txtPassword.getText().toString());
            }

        });
        btnAddU.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //llamamos al metodo crear cuenta
               crearCuenta(txtUser.getText().toString().trim(),txtPassword.getText().toString());
           }
        });
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        // Configure Google Sign In ----paso 1---
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.   ---paso 2--
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }//esta cierra el metodo oncreate
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Menu.class);
            startActivity(intent);
        }
    }
    // -- paso 4 --
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
                //LO agregue de git
                //updateUI(null);
            }
        }
    }
    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        //[START_EXCLUDE silent]
        // showProgressDialog();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
//enviar a otra ventana
                            Intent intent=new Intent(getApplicationContext(),Menu.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                          //  Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // [START_EXCLUDE]
                       // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    //-- paso 3--
    private void signIn () {
        Intent signInIntent = mGoogleSignInClient . getSignInIntent ();
        startActivityForResult ( signInIntent , RC_SIGN_IN );
    }


    public void crearCuenta(String user,String password) {
        //aqui colocamos las instrucciones para agregar un usuario a la cuenta de firebase

    //validar campos
        if(user.isEmpty()){
            Toast.makeText(getApplicationContext(),"Campo usuario obligatorio",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()){
            Toast.makeText(getApplicationContext(),"Campo contraseña obligatorio",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<8){
            Toast.makeText(getApplicationContext(),"Debe contener al menos 8 caracteres",Toast.LENGTH_SHORT).show();
            return;

        }

        //agregar usuario
        firebaseAuth.createUserWithEmailAndPassword(user,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //validar si al usuario se registró con éxito en la plataforma
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Registro completado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    //da informacion al usuario
                    if(task.getException()instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"Ya existe el usuario con esos datos",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Error al ejecutar",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }//cierra crear cuenta

    public void login(final String user, final String password){
        //TextUtils manipula texto
        if(TextUtils.isEmpty(user)){
            txtUser.setError("Campo obligaorio");
            txtUser.setFocusable(true);
            return;
        }
        if(TextUtils.isEmpty(password)){
            txtPassword.setError("Campo obligatorio");
            txtPassword.setFocusable(true);
            return;
        }
        if (password.length()<8){
            txtPassword.setError("Al menos 8 caracteres");
            txtPassword.setFocusable(true);
            return;
        }
        //enviar datos a la plataforma y validar que exista almenos un usuario y password registrados
        firebaseAuth.signInWithEmailAndPassword(user,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //ir a la siguiente actividad
                    Intent intent=new Intent(getApplicationContext(),Menu.class);
                    //sacar el contenido de la caja de texto para enviarlos
                    //trim quita espacios
                    intent.putExtra("myUser",user);
                    intent.putExtra("myPassword",password);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Verifica usuario y contraseña",Toast.LENGTH_SHORT).show();

                }


            }
        });




    }



}






