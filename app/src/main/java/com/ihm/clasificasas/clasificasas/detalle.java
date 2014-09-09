package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denny on 27/07/2014.
 */
public class detalle extends Activity {

    ImageView phoneButton, emailButton, favButton;
    TextView txtmobil, txtcorreo, txtbano, txtdir, txtdes,txtcons, txtterreno, txtpisos,txtcuartos,txtcosto, txtnombres, txtbanos  ;

    JSONParser jsonParser = new JSONParser();

    String TAG_SUCCESS = "success";
    String TAG_CASA = "casaspublicada";
    String url_get = "http://"+IP.address+"/clasificasas/obtenerdatosdecasa.php";
    String direccion, banos, ventaoalquile,costo, latitud, longitud, terren, pisos, descripcion,cuartos,fotoportada,ventaoalquiler, terreno, construccion, nombre, correo, mobil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        new obtenerCasa().execute();
        phoneButton = (ImageView) findViewById(R.id.detalle_phone_button);
        phoneButton.setOnClickListener(onClickListener);

        emailButton = (ImageView) findViewById(R.id.detalle_email_button);
        emailButton.setOnClickListener(onClickListener);

        favButton = (ImageView) findViewById(R.id.detalle_fav_button);
        favButton.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getIntent().hasExtra("TAG_USUARIO")){
                    Intent intent = new Intent(detalle.this, resultados.class);
                    intent.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                    startActivity(intent);
                    overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                }else{
                    Intent intent = new Intent(detalle.this, resultados.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detalle_phone_button:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + txtmobil.getText()));
                startActivity(callIntent);
                break;
            case R.id.detalle_email_button:
                Intent send = new Intent(Intent.ACTION_SEND);
                send.setType("plain/text");
                send.putExtra(Intent.EXTRA_EMAIL, new String[] { txtcorreo.getText().toString() });
                startActivity(Intent.createChooser(send, "Elija un cliente de correo:"));
                break;
        }
        }
    };

    class obtenerCasa extends AsyncTask<String, Void, JSONArray> {
        String id=getIntent().getExtras().getString("idcasa");
        JSONArray casa;

        @Override
        protected JSONArray doInBackground(String... params) {
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("idcasapublicada", id));
            JSONObject json = jsonParser.makeHttpRequest(url_get,"POST", p);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                     casa = json.getJSONArray(TAG_CASA);
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return casa;
        }

        @Override
        protected void onPostExecute(JSONArray casa) {
            try {
                for (int i = 0; i < casa.length(); i++) {
                    JSONObject json_data = casa.getJSONObject(i);
                    costo = "$"+json_data.getString("costo");
                    direccion = "Ubicacion: " + json_data.getString("direccion");
                    latitud = json_data.getString("latitud");
                    cuartos = "Cuartos: "+json_data.getString("cuartos");
                    longitud = json_data.getString("longitud");
                    pisos = "Pisos: "+json_data.getString("pisos");
                    descripcion = json_data.getString("descripcion");
                    fotoportada = json_data.getString("fotoportada");
                    terren = "Terreno: "+json_data.getString("terreno")+ " m\u00B2";
                    construccion = "Contruccion: "+ json_data.getString("construccion") + " m\u00B2";
                    ventaoalquile = json_data.getString("ventaoalquiler");
                    nombre = "Vendedor: "+ json_data.getString("nombres") + " " + json_data.getString("apellidos");
                    mobil = json_data.getString("mobil");
                    correo = json_data.getString("correo");
                    banos = "Baños: "+ json_data.getString("banos");
                }
                txtmobil = (TextView) findViewById(R.id.txtmobil);
                txtmobil.setText(mobil);
                txtcorreo = (TextView) findViewById(R.id.txtmail);
                txtcorreo.setText(correo);
                txtbano = (TextView) findViewById(R.id.txtbanos);
                txtbano.setText(banos);
                txtcosto = (TextView) findViewById(R.id.txtcosto);
                txtcosto.setText(costo);
                txtdir = (TextView) findViewById(R.id.txtdireccion);
                txtdir.setText(direccion);
                txtdes = (TextView) findViewById(R.id.txtdes);
                txtdes.setText(descripcion);
                txtcons = (TextView) findViewById(R.id.txtcons);
                txtcons.setText(construccion);
                txtterreno = (TextView) findViewById(R.id.txtterreno);
                txtterreno.setText(terren);
                txtpisos = (TextView) findViewById(R.id.txtpisos);
                txtpisos.setText(pisos);
                txtcuartos = (TextView) findViewById(R.id.txtcuartos);
                txtcuartos.setText(cuartos);
                txtnombres = (TextView) findViewById(R.id.txtusu);
                txtnombres.setText(nombre);
                txtbanos = (TextView) findViewById(R.id.txtbanos);
                txtbanos.setText(banos);
            }catch (Exception e){}
        }
    }
}
