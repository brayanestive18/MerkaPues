package com.brayadiaz.merka;


import android.os.Bundle;
import android.support.v4.app.Fragment;

public class PromoActivity extends NavigationActivity {

    /*private String correoR, name, url_photo;
    private TextView Info;
    private ImageView photo;
    private int logId;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        if (findViewById(R.id.containerView) != null){

            Fragment fragment = new TabFragment();

            getSupportFragmentManager().beginTransaction().
                    add(R.id.containerView, fragment).commit();
        }

        /*
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        logId = prefs.getInt("optLog", 0); //editor.putInt("optLog", 0);
        correoR = prefs.getString("correo", "No Data");
        name = prefs.getString("name", "No Data");
        url_photo = prefs.getString("url_photo", "No Data");
        Info = (TextView) findViewById(R.id.textViewUser);
        photo = (ImageView) findViewById(R.id.imageView);

        if (logId == 1 || logId == 2) {
            PromoActivity.DescargaImagen m = new PromoActivity.DescargaImagen();
            m.execute();
        }*/

    }

    /*class DescargaImagen extends AsyncTask<Void,Void,Void> {

        Bitmap descarga = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {//descargamos la imagen de perfil del usuario
                descarga = Picasso.with(PromoActivity.this).load(url_photo).get();

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
    }*/

}

