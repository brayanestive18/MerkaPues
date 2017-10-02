package com.brayadiaz.merkapues;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private String correo = "", contrasena = "", repContrasena = "", user = "";
    EditText eCorreo, eContrasena, eRepContrasena, eUser;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        eRepContrasena = (EditText) findViewById(R.id.eReContrasena);
        eUser = (EditText) findViewById(R.id.eUser);
    }

    public void registrar(View view) {
        correo = eCorreo.getText().toString();
        contrasena = eContrasena.getText().toString();
        repContrasena = eRepContrasena.getText().toString();
        user = eUser.getText().toString();

        if(correo.equals("") || contrasena.equals("") || user.equals("")){
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

            prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            editor = prefs.edit();

            //ALmacenar el valor de optLog
            int logId = 3;
            editor.putInt("optLog", logId);
            editor.putString("correo", correo);
            editor.putString("contrasena", contrasena);
            editor.putString("name", user);
            //editor.commit();
            editor.apply();
            
            Toast.makeText(this,"Registro Exitoso",Toast.LENGTH_SHORT).show();

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
    if (!validarEmail("miEmail@gmail.com")){
    miEditText.setError("Email no válido")
}

*/