package com.ihm.clasificasas.clasificasas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class main extends Activity {

    Button buscar;
    Button ingresar;
    Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        buscar = (Button) findViewById(R.id.home_buscar);
        ingresar = (Button) findViewById(R.id.home_ingresar);
        registrar = (Button) findViewById(R.id.home_registrar);

        buscar.setOnClickListener(homeButtonhandler);
        ingresar.setOnClickListener(homeButtonhandler);
        registrar.setOnClickListener(homeButtonhandler);
    }

    View.OnClickListener homeButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_buscar:
                Intent buscar_out = new Intent(main.this, buscar_out.class);
                startActivity(buscar_out);
                overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
            break;
            case R.id.home_ingresar:
                Intent usuario = new Intent(main.this, usuario.class);
                startActivity(usuario);
                overridePendingTransition(R.animator.shrinka, R.animator.shrinkb);
            break;
            case R.id.home_registrar:
                Intent registrar = new Intent(main.this, registrar.class);
                startActivity(registrar);
                overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
            break;
        }
        }
    };
}
