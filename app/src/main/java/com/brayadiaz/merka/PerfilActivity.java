package com.brayadiaz.merka;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import com.squareup.picasso.Picasso;

public class PerfilActivity extends MenuDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//public class PerfilActivity extends AppCompatActivity {

    private String correoR, name, url_photo;
    private TextView tUser, tEmail;
    private ImageView photo;


    private GoogleApiClient mGoogleApiClient;
    private int logId;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_perfil);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frame);
        getLayoutInflater().inflate(R.layout.activity_perfil, contentFrameLayout);

        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        logId = prefs.getInt("optLog", 0); //editor.putInt("optLog", 0);
        correoR = prefs.getString("correo", "No Data");
        name = prefs.getString("name", "No Data");
        url_photo = prefs.getString("url_photo", "No Data");


        /*
        editor.putString("user", IDuser);
        editor.putString("toke", toke);
        editor.putString("email", correoL);
        editor.putString("name", user);
        editor.putString("url_photo", url_photo.toString());
        editor.commit();*/


        //editor.putString("toke", toke);

        tUser = (TextView) findViewById(R.id.t_user);
        tEmail = (TextView) findViewById(R.id.t_email);
        photo = (ImageView) findViewById(R.id.photo);

        if (logId == 1 || logId == 2) {
            DescargaImagen m = new DescargaImagen();
            m.execute();
        }
        tUser.setText(name);
        tEmail.setText(correoR);


        /*String imageURL;
        imageURL = "http://graph.facebook.com/"+user+"/picture?type=small";

        photo.setImageBitmap(getBitmapFromURL(imageURL));*/
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }*/
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;

        switch (id){
            case R.id.mMain:
                intent = new Intent(PerfilActivity.this, MainActivity.class);
                //intent.putExtra("contrasena",contrasenaR);
                //intent.putExtra("correo", correoR);
                startActivity(intent);
                finish();
                break;
            case R.id.mCerrar:
                prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                editor = prefs.edit();

                editor.putInt("optLog", 0);
                editor.commit();

                //if (logId == 1) {
                    LoginManager.getInstance().logOut(); // Facebook
                /*} else if (logId == 2) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // ...
                                }
                            });
                } else {//if (logId == 3) {
                    LoginManager.getInstance().logOut();
                    //Putextras correo nombre contraseña a login
                }*/

                //intent = new Intent(PerfilActivity.this, LoginActivity.class);
                //intent.putExtra("contrasena",contrasenaR);
                //intent.putExtra("correo", correoR);
                //startActivity(intent);
                //finish();

               // break;
       // }

        //return super.onOptionsItemSelected(item);
    //}

    class DescargaImagen extends AsyncTask<Void,Void,Void>{

        Bitmap descarga = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {//descargamos la imagen de perfil del usuario
                descarga = Picasso.with(PerfilActivity.this).load(url_photo).get();

            } catch (Exception e) {
                e.printStackTrace();
            }

                return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (descarga != null) {
                photo.setImageBitmap(descarga);
            }
            super.onPostExecute(aVoid);
        }
    }

    /*public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }*/

}
