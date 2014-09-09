package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;

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
public class resultados extends Activity {

    Button informacion;
    String voa, ciu, costo, usuario;
    JSONParser jsonParser = new JSONParser();
    String url_datos = "http://"+IP.address+"/clasificasas/buscarcasa.php";
    List<Casa> Casas = new ArrayList<Casa>();
    Casa c;
    LinearLayout llresultados;
    Context t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultados);
        t=this;
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        new buscarCasas().execute();
        voa = getIntent().getExtras().getString("voa");
        ciu = getIntent().getExtras().getString("ciudad");
        costo = getIntent().getExtras().getString("costo");
        usuario = getIntent().getExtras().getString("TAG_USUARIO");
        informacion = (Button) findViewById(R.id.resultado_mas_informacion);
        informacion.setOnClickListener(onClickListener);
        llresultados = (LinearLayout) findViewById(R.id.llresultados);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getIntent().hasExtra("TAG_USUARIO")){
                    Intent intent = new Intent(resultados.this, buscar.class);
                    intent.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                    startActivity(intent);
                    overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                }else{
                    Intent intent = new Intent(resultados.this, buscar.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.resultado_mas_informacion:
                    Intent detalle = new Intent(resultados.this, detalle.class);
                    if(getIntent().hasExtra("TAG_USUARIO"))
                        detalle.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                    startActivity(detalle);
                    overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
                    break;
            }
        }
    };

    class buscarCasas extends AsyncTask<String, Void, JSONArray> {
        JSONArray casas;
        @Override
        protected JSONArray doInBackground(String... params) {
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("usuario",getIntent().getExtras().getString("TAG_USUARIO")));
            JSONObject json = jsonParser.makeHttpRequest(url_datos,"POST", p);
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    casas = json.getJSONArray("casa");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return casas;
        }

        @Override
        protected void onPostExecute(JSONArray casas) {
            try {
                for (int i = 0; i < casas.length(); i++) {
                    JSONObject json_data = casas.getJSONObject(i);
                    c.banos =  json_data.getString("banos");
                    c.costo =  json_data.getString("costo");
                    c.direccion =  json_data.getString("direccion");
                    c.cuartos =  json_data.getString("cuartos");
                    c.pisos =  json_data.getString("pisos");
                    c.descripcion =  json_data.getString("descripcion");
                    c.idcasa =  json_data.getString("idcasapublicada");
                    Casas.add(c);
                }
                for(Casa Ca : Casas) {
                    LinearLayout LL = new LinearLayout(t);
                    LL.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(310, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(5, 0, 5, 0);
                }
            }catch (Exception e){}
        }
    }
}
