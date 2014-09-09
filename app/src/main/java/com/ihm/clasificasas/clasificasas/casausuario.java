package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denny on 27/07/2014.
 */
public class casausuario extends Activity {

    TextView txtbano, txtdir, txtdes,txtcons, txtterreno, txtpisos, txtcuartos, txtcosto, txttipo;
    ImageView foto;

    JSONParser jsonParser = new JSONParser();

    String TAG_SUCCESS = "success";
    String url_casa = "http://"+IP.address+"/clasificasas/detallescasa.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.casausuario);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*txtcosto = (TextView) findViewById();
        txttipo = (TextView) findViewById();
        foto = (TextView) findViewById();
        txtdir = (TextView) findViewById();*/


        new obtenerCasa().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(casausuario.this, usuario.class);
                intent.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                startActivity(intent);
                overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class obtenerCasa extends AsyncTask<String, Void, JSONArray> {
        JSONArray casa;
        @Override
        protected JSONArray doInBackground(String... params) {
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("idcasapublicada",getIntent().getExtras().getString("TAG_IDCASA")));
            JSONObject json = jsonParser.makeHttpRequest(url_casa,"POST", p);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                     casa = json.getJSONArray("casa");
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
            String banos, direccion, descripcion, construccion, terreno, pisos, cuartos, costo, tipo, foto, latitud, longitud;
            try {
                JSONObject json_data = casa.getJSONObject(0);
                costo = "$"+json_data.getString("costo");
                tipo = json_data.getString("ventaoalquiler");
                foto = json_data.getString("fotoportada");
                direccion = "Ubicacion: " + json_data.getString("direccion");
                cuartos = "Cuartos: "+json_data.getString("cuartos");
                banos = "Baños: "+json_data.getString("banos");
                pisos = "Pisos: "+json_data.getString("pisos");
                terreno = "Terreno: "+json_data.getString("terreno")+ " m\u00B2";
                construccion = "Contruccion: "+ json_data.getString("construccion") + " m\u00B2";
                descripcion = json_data.getString("descripcion");
                latitud = json_data.getString("latitud");
                longitud = json_data.getString("longitud");
                foto = json_data.getString("fotoportada");


            }catch (Exception e){}
        }
    }
}
