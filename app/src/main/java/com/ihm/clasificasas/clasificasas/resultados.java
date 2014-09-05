package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Denny on 27/07/2014.
 */
public class resultados extends Activity {

    Button informacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultados);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        informacion = (Button) findViewById(R.id.resultado_mas_informacion);
        informacion.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(resultados.this, buscar.class);
                startActivity(intent);
                overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.resultado_mas_informacion:
                    Intent detalle = new Intent(com.ihm.clasificasas.clasificasas.resultados.this, detalle.class);
                    startActivity(detalle);
                    overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
                    break;
            }
        }
    };
}