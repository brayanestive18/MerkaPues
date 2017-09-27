package com.brayadiaz.merkapues;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PerfilActivity extends AppCompatActivity {

    private String correoR, contrasenaR;
    private TextView Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Info = (TextView) findViewById(R.id.pInfo);
        Bundle extras = getIntent().getExtras();
        correoR = extras.getString("correo");
        contrasenaR = extras.getString("contrasena");

        Info.setText("Correo Electrónico: " + correoR +"\nContraseña: " + contrasenaR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;

        switch (id){
            case R.id.mMain:
                intent = new Intent(PerfilActivity.this, MainActivity.class);
                intent.putExtra("contrasena",contrasenaR);
                intent.putExtra("correo", correoR);
                startActivity(intent);
                finish();
                break;
            case R.id.mCerrar:
                intent = new Intent(PerfilActivity.this, LoginActivity.class);
                intent.putExtra("contrasena",contrasenaR);
                intent.putExtra("correo", correoR);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
