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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denny on 28/07/2014.
 */
public class buscar_in extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_in);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(buscar_in.this, usuario.class);
                startActivity(intent);
                overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
