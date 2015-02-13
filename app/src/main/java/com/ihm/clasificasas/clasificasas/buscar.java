package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.ihm.clasificasas.clasificasas.R.*;

/**
 * Created by Denny on 28/07/2014.
 */
public class buscar extends Activity {

    String max, min;
    TextView txtmin, txtmax;
    SeekBar seekBar;
    TextView presupuesto;
    Button buscar, avanzada;
    int minValue, maxValue;

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

        presupuesto = (EditText) findViewById(R.id.buscar_presupuesto);

        txtmin = (TextView) findViewById(id.buscar_min_value);
        txtmax = (TextView) findViewById(id.buscar_max_value);
        getMinMax();

        presupuesto.setText(""+(minValue+maxValue)/2);

        seekBar = (SeekBar) findViewById(R.id.buscar_in_seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                String value = "";
                if(progress!=0)
                    value = "" + (progress*maxValue)/100;
                else value = "" + minValue;
                presupuesto.setText(value);
            }
        });
    }

    public void getMinMax(){
        ArrayList<Integer> array = new ArrayList<Integer>();
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/publicaciones.xml");
            InputStream is = new FileInputStream(file.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();
            NodeList valores = doc.getElementsByTagName("costo");
            for (int i = 0; i < valores.getLength(); i++) {
                array.add(Integer.parseInt(valores.item(i).getFirstChild().getNodeValue()));
            }
            Collections.sort(array);
            txtmin.setText("$"+array.get(0));
            txtmax.setText("$"+array.get(array.size()-1));
            minValue = array.get(0);
            maxValue = array.get(array.size()-1);
        }catch (Exception e){
        }
    }

    View.OnClickListener buscarInButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buscar_in_btn:
                Intent resultados = new Intent(com.ihm.clasificasas.clasificasas.buscar.this, resultados.class);
                resultados.putExtra("TAG_CIUDAD",spinner_ciudad.getSelectedItem().toString());
                resultados.putExtra("TAG_TIPO",spinner_tipo.getSelectedItem().toString());
                resultados.putExtra("TAG_PRESUPUESTO",presupuesto.getText().toString().replace("$",""));
                if(getIntent().hasExtra("TAG_USUARIO")) resultados.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
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
}

