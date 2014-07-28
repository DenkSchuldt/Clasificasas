package com.ihm.clasificasas.clasificasas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Denny on 27/07/2014.
 */
public class usuario  extends Activity {

    Button buscar;
    Button publicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario);

        buscar = (Button) findViewById(R.id.usuario_buscar);
        publicar = (Button) findViewById(R.id.usuario_publicar);

        buscar.setOnClickListener(usuarioButtonhandler);
        publicar.setOnClickListener(usuarioButtonhandler);
    }

    View.OnClickListener usuarioButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.usuario_buscar:
                Intent buscar_in = new Intent(usuario.this, buscar_in.class);
                startActivity(buscar_in);
                overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
            break;
            case R.id.usuario_publicar:
                Intent usuario = new Intent(usuario.this, buscar_in.class);
                startActivity(usuario);
                overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
            break;
        }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent main = new Intent(usuario.this, main.class);
            startActivity(main);
            overridePendingTransition(R.animator.shrinka, R.animator.shrinkb);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
