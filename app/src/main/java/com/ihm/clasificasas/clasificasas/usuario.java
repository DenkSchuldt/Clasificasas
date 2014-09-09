package com.ihm.clasificasas.clasificasas;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Denny on 27/07/2014.
 */
public class usuario  extends FragmentActivity {

    Button buscar;
    Button publicar;
    ImageView edit;
    TextView nombre, movil, email;
    JSONParser jsonParser = new JSONParser();
    String url_datos = "http://"+IP.address+"/clasificasas/obtenerdatosdeusuario.php";
    String url_casas = "http://"+IP.address+"/clasificasas/casaspublicadasporusuario.php";
    String url_favs = "http://"+IP.address+"/clasificasas/obtenerdatosdeusuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario);

        buscar = (Button) findViewById(R.id.usuario_buscar);
        publicar = (Button) findViewById(R.id.usuario_publicar);
        edit = (ImageView) findViewById(R.id.usuario_editar);

        buscar.setOnClickListener(usuarioButtonhandler);
        publicar.setOnClickListener(usuarioButtonhandler);
        edit.setOnClickListener(usuarioButtonhandler);
    }

    View.OnClickListener usuarioButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.usuario_buscar:
                    Intent buscar = new Intent(usuario.this,buscar.class);
                    buscar.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                    startActivity(buscar);
                    overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
                    break;
                case R.id.usuario_editar:
                    Intent editar = new Intent(usuario.this, editar.class);
                    editar.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                    startActivity(editar);
                    overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
                    break;
                case R.id.usuario_publicar:
                    Intent publicar = new Intent(usuario.this, publicar.class);
                    publicar.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                    startActivity(publicar);
                    overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.salir:
                Intent main = new Intent(usuario.this, main.class);
                startActivity(main);
                overridePendingTransition(R.animator.shrinka, R.animator.shrinkb);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class infoUsuario extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("usuario",getIntent().getExtras().getString("TAG_USUARIO")));
            JSONObject json = jsonParser.makeHttpRequest(url_datos,"POST", p);
            try {
                int success = json.getInt("success");
                if (success == 1) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
