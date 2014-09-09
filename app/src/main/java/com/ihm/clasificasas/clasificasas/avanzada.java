package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import static com.ihm.clasificasas.clasificasas.R.animator;
import static com.ihm.clasificasas.clasificasas.R.id;
import static com.ihm.clasificasas.clasificasas.R.layout;

/**
 * Created by Denny on 28/07/2014.
 */
public class avanzada extends Activity {

    SeekBar seekBar;
    TextView presupuesto;
    Button buscar;

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
        setContentView(layout.avanzada);
        ActionBar actionBar = getActionBar();
        //Log.v("database", leerDatabase());
        actionBar.setDisplayHomeAsUpEnabled(true);

        buscar = (Button) findViewById(id.buscar_avanzada_btn);
        buscar.setOnClickListener(buscarInButtonhandler);

        spinner_ciudad = (Spinner)findViewById(id.buscar_in_ciudad);
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

        spinner_tipo = (Spinner)findViewById(id.buscar_in_tipo_casa);
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

        presupuesto = (TextView) findViewById(id.buscar_presupuesto);

        seekBar = (SeekBar) findViewById(id.buscar_in_seek);
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
            case id.buscar_avanzada_btn:
                Intent resultados = new Intent(avanzada.this, resultados.class);
                if(getIntent().hasExtra("TAG_USUARIO"))
                    resultados.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                startActivity(resultados);
                overridePendingTransition(animator.pushleftin, animator.pushleftout);
                break;
        }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(avanzada.this, buscar.class);
                if(getIntent().hasExtra("TAG_USUARIO"))
                    intent.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                startActivity(intent);
                overridePendingTransition(animator.pushrightin, animator.pushrightout);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
