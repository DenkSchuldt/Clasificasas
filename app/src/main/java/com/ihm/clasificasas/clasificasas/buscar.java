package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ihm.clasificasas.clasificasas.R.*;

/**
 * Created by Denny on 28/07/2014.
 */
public class buscar extends Activity {


    JSONParser jsonParser = new JSONParser();
    String url_datos = "http://"+IP.address+"/clasificasas/obtenermaxymin.php";
    String max, min;
    TextView txtmin, txtmax;
    SeekBar seekBar;
    TextView presupuesto;
    Button buscar, avanzada;

    Spinner spinner_ciudad;
    Spinner spinner_tipo;
    String[] ciudades = {
            "Ambato",
            "Babahoyo",
            "Cuenca",
            "Esmeraldas",
            "Guayaquil",
            "Ibarra",
            "Manabí",
            "Portoviejo",
            "Quevedo",
            "Quito",
            "Salinas"
    };
    String[] tipo = {
            "Venta",
            "Alquiler"
    };
    String ciu, voa, costo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.buscar);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(getIntent().hasExtra("TAG_USUARIO")) new buscarCasa().execute();
        buscar = (Button) findViewById(R.id.buscar_in_btn);
        buscar.setOnClickListener(buscarInButtonhandler);

        spinner_ciudad = (Spinner)findViewById(R.id.buscar_in_ciudad);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ciudades);
        spinner_ciudad.setAdapter(adapter);
        spinner_ciudad.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            }
        );

        spinner_tipo = (Spinner)findViewById(R.id.buscar_in_tipo_casa);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipo);
        spinner_tipo.setAdapter(adapter2);
        spinner_tipo.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            }
        );

        presupuesto = (TextView) findViewById(R.id.buscar_presupuesto);

        seekBar = (SeekBar) findViewById(R.id.buscar_in_seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                int resta = Integer.parseInt(max) - Integer.parseInt(min);
                int calculo = Integer.parseInt(min) + ((int)(progress/100.0)*resta);

                presupuesto.setText(String.valueOf(calculo));
            }
        });
    }

    View.OnClickListener buscarInButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buscar_in_btn:
                Intent resultados = new Intent(com.ihm.clasificasas.clasificasas.buscar.this, resultados.class);
                ciu = spinner_ciudad.getSelectedItem().toString();
                voa = spinner_tipo.getSelectedItem().toString();
                costo = presupuesto.getText().toString().replace("$","");
                if(getIntent().hasExtra("TAG_USUARIO")) {
                    resultados.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                    resultados.putExtra("ciudad",ciu);
                    resultados.putExtra("voa",voa);
                    resultados.putExtra("costo",costo);
                }
                startActivity(resultados);
                overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
                break;
        }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getIntent().hasExtra("TAG_USUARIO")){
                    Intent intent = new Intent(buscar.this, usuario.class);
                    intent.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                    startActivity(intent);
                    overridePendingTransition(animator.pushrightin, animator.pushrightout);
                }else{
                    Intent intent = new Intent(buscar.this, main.class);
                    startActivity(intent);
                    overridePendingTransition(animator.pushrightin, animator.pushrightout);
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class buscarCasa extends AsyncTask<String, Void, JSONArray> {
        JSONArray maxymin;
        @Override
        protected JSONArray doInBackground(String... params) {
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("usuario",getIntent().getExtras().getString("TAG_USUARIO")));
            JSONObject json = jsonParser.makeHttpRequest(url_datos,"POST", p);
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    maxymin = json.getJSONArray("maxymin");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return maxymin;
        }

        @Override
        protected void onPostExecute(JSONArray maxymin) {
            try {
                for (int i = 0; i < maxymin.length(); i++) {
                    JSONObject json_data = maxymin.getJSONObject(i);
                    max = json_data.getString("max");
                    min = json_data.getString("min");
                }
                txtmin = (TextView) findViewById(id.buscar_min_value);
                txtmin.setText("$"+min);
                txtmax = (TextView) findViewById(id.buscar_max_value);
                txtmax.setText("$" + max);
                presupuesto.setText("$"+((Integer.parseInt(min)+Integer.parseInt(max))/2));
            }catch (Exception e){}
        }
    }
}

