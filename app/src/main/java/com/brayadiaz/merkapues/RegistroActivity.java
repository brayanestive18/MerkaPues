package com.brayadiaz.merkapues;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private String correo = "", contrasena = "", repContrasena = "";
    EditText eCorreo, eContrasena, eRepContrasena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        eRepContrasena = (EditText) findViewById(R.id.eReContrasena);
    }

    public void registrar(View view) {
        correo = eCorreo.getText().toString();
        contrasena = eContrasena.getText().toString();
        repContrasena = eRepContrasena.getText().toString();

        if(correo.equals("") || contrasena.equals("")){
            Toast.makeText(this,"Por Favor, Ingrese todos los campos",Toast.LENGTH_SHORT).show();
        }

        /*else if (!correo.contains("@")){
            Toast.makeText(this,"Ingrese un correo válido",Toast.LENGTH_SHORT).show();
        }*/

        else if (!validarEmail(correo)){
            //eCorreo.setError("Email no válido");
            Toast.makeText(this,"Ingrese un correo válido",Toast.LENGTH_SHORT).show();
        }

        else if (!contrasena.equals(repContrasena)){
            Toast.makeText(this,"Las Contraseñas no coinciden",Toast.LENGTH_SHORT).show();
        }

        else {

            Intent intent = new Intent();
            intent.putExtra("correo", correo);
            intent.putExtra("contrasena", contrasena);

            setResult(RESULT_OK, intent);

            finish();

        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

}
/*
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    if (!validarEmail("miEmail@gmail.com")){
    miEditText.setError("Email no válido")
}

*/