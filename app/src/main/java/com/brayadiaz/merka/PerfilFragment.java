package com.brayadiaz.merka;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private String correoR, name, url_photo;
    private TextView tUser, tEmail;
    private ImageView photo;
    private View view;


    private GoogleApiClient mGoogleApiClient;
    private int logId;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    public PerfilFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_perfil, container, false);
        prefs = getContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        logId = prefs.getInt("optLog", 0); //editor.putInt("optLog", 0);
        correoR = prefs.getString("correo", "No Data");
        name = prefs.getString("name", "No Data");
        url_photo = prefs.getString("url_photo", "No Data");

        tUser = (TextView) view.findViewById(R.id.t_user);
        tEmail = (TextView) view.findViewById(R.id.t_email);
        photo = (ImageView) view.findViewById(R.id.photo);

        if (logId == 1 || logId == 2) {
            DescargaImagen m = new DescargaImagen();
            m.execute();
        }
        tUser.setText(name);
        tEmail.setText(correoR);

        return view;
    }


    class DescargaImagen extends AsyncTask<Void,Void,Void> {

        Bitmap descarga = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {//descargamos la imagen de perfil del usuario
                descarga = Picasso.with(getContext()).load(url_photo).get();

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

}
