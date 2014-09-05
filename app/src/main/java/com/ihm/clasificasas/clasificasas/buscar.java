package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import static com.ihm.clasificasas.clasificasas.R.*;

/**
 * Created by Denny on 28/07/2014.
 */
public class buscar extends Activity {

    SeekBar seekBar;
    TextView presupuesto;
    Button buscar;

    Spinner spinner_ciudad;
    Spinner spinner_tipo;
    String[] ciudades = {
            "Guayaquil",
            "Quito",
            "Cuenca",
            "Babahoyo",
            "Quevedo",
            "Ambato",
            "Ibarra"
    };
    String[] tipo = {
            "Venta",
            "Alquiler"
    };

    /*public String leerDatabase(){
        InputStream is = getResources().openRawResource(raw.database);
        String s="nada";
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document dom = builder.parse(is);
            Element root = dom.getDocumentElement();
            s= root.getTagName();
        } catch (Exception e) {
        }
        return s;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.buscar);
        ActionBar actionBar = getActionBar();
        //Log.v("database", leerDatabase());
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
                        //int position = spinner_ciudad.getSelectedItemPosition();
                        //Toast.makeText(getApplicationContext(), "You have selected " + ciudades[+position], Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
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
                        //int position = spinner_tipo.getSelectedItemPosition();
                        //Toast.makeText(getApplicationContext(), "You have selected " + tipo[+position], Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
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
                presupuesto.setText(String.valueOf(progress * 1000));
            }
        });
    }

    View.OnClickListener buscarInButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buscar_in_btn:
                Intent resultados = new Intent(com.ihm.clasificasas.clasificasas.buscar.this, resultados.class);
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
                Intent intent = new Intent(com.ihm.clasificasas.clasificasas.buscar.this, usuario.class);
                startActivity(intent);
                overridePendingTransition(animator.pushrightin, animator.pushrightout);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
