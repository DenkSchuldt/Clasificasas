package com.ihm.clasificasas.clasificasas;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
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
import org.json.JSONArray;
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
    TextView nombre, usuario, movil, email;
    JSONParser jsonParser = new JSONParser();
    String url_datos = "http://"+IP.address+"/clasificasas/obtenerdatosdeusuario.php";
    String url_publicaciones = "http://"+IP.address+"/clasificasas/casaspublicadasporusuario.php";
    String url_favs = "http://"+IP.address+"/clasificasas/casasfavoritasporusuario.php";
    String snombre, susuario, smovil, scorreo;
    String cubicacion, ccosto, cid;

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

        nombre = (TextView) findViewById(R.id.usuario_nombre);
        usuario = (TextView) findViewById(R.id.usuario_usuario);
        movil = (TextView) findViewById(R.id.usuario_movil);
        email = (TextView) findViewById(R.id.usuario_correo);

        new infoUsuario().execute();
        new publicacionesUsuario().execute();
        new favoritasUsuario().execute();
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

    /*
     * infoUsuario
     * Retorna los datos del usuario
     */
    class infoUsuario extends AsyncTask<String, Void, JSONArray> {
        JSONArray user;
        @Override
        protected JSONArray doInBackground(String... params) {
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("usuario",getIntent().getExtras().getString("TAG_USUARIO")));
            JSONObject json = jsonParser.makeHttpRequest(url_datos,"POST", p);
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    user = json.getJSONArray("datosusuario");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return user;
        }
        @Override
        protected void onPostExecute(JSONArray user) {
            try {
                for (int i = 0; i < user.length(); i++) {
                    JSONObject json_data = user.getJSONObject(i);
                    snombre = json_data.getString("nombres") + " " + json_data.getString("apellidos");
                    susuario = json_data.getString("usuario");
                    smovil = json_data.getString("mobil");
                    scorreo = json_data.getString("correo");
                }
                nombre.setText(snombre);
                usuario.setText(susuario);
                movil.setText(smovil);
                email.setText(scorreo);
            } catch (Exception e) {}
        }
    }

    /*
     * publicacionesUsuario
     * Agrega las publicaciones del usuario
     */
    class publicacionesUsuario extends AsyncTask<String, Void, JSONArray> {
        JSONArray publicaciones;
        @Override
        protected JSONArray doInBackground(String... params) {
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("usuario",getIntent().getExtras().getString("TAG_USUARIO")));
            JSONObject json = jsonParser.makeHttpRequest(url_publicaciones,"POST", p);
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    publicaciones = json.getJSONArray("casaspublicadasporusuario");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return publicaciones;
        }
        @Override
        protected void onPostExecute(JSONArray user) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.usuario_publicaciones);
            try {
                for (int i = 0; i < user.length(); i++) {
                    JSONObject json_data = user.getJSONObject(i);
                    cubicacion = json_data.getString("direccion");
                    if(cubicacion.length() > 17) cubicacion = cubicacion.substring(0,14)+"...";
                    ccosto = "$" + json_data.getString("costo");
                    cid = json_data.getString("idcasapublicada");

                    LinearLayout linLayout = new LinearLayout(usuario.getContext());
                    linLayout.setBackgroundColor(Color.WHITE);
                    linLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0,20,0,0);

                    TextView tv1 = new TextView(usuario.getContext());
                    tv1.setText(cubicacion.toString());
                    tv1.setPadding(10,20,10,20);
                    tv1.setTextSize(20);
                    tv1.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                    linLayout.addView(tv1);

                    TextView tv2 = new TextView(usuario.getContext());
                    tv2.setText(ccosto.toString());
                    tv2.setPadding(10,20,10,20);
                    tv2.setTextSize(20);
                    tv2.setGravity(Gravity.END);
                    linLayout.addView(tv2);

                    linLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent casa = new Intent(usuario.this,casausuario.class);
                            casa.putExtra("TAG_IDCASA",cid);
                            casa.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                            startActivity(casa);
                            overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
                        }
                    });

                    ll.addView(linLayout,layoutParams);
                }
            } catch (Exception e) {}
        }
    }

    /*
     * favoritasUsuario
     * Agrega las casas favoritas del usuario
     */
    class favoritasUsuario extends AsyncTask<String, Void, JSONArray> {
        JSONArray favoritas;
        @Override
        protected JSONArray doInBackground(String... params) {
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("usuario",getIntent().getExtras().getString("TAG_USUARIO")));
            JSONObject json = jsonParser.makeHttpRequest(url_favs,"POST", p);
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    favoritas = json.getJSONArray("casasfavoritas");
                    System.out.println(favoritas);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return favoritas;
        }
        /*@Override
        protected void onPostExecute(JSONArray user) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.usuario_publicaciones);
            try {
                for (int i = 0; i < user.length(); i++) {
                    JSONObject json_data = user.getJSONObject(i);
                    cubicacion = json_data.getString("direccion");
                    ccosto = "$" + json_data.getString("costo");

                    LinearLayout linLayout = new LinearLayout(usuario.getContext());
                    linLayout.setBackgroundColor(Color.WHITE);
                    linLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0,20,0,0);

                    TextView tv1 = new TextView(usuario.getContext());
                    tv1.setText(cubicacion.toString());
                    tv1.setPadding(10,20,10,20);
                    tv1.setTextSize(20);
                    linLayout.addView(tv1);

                    TextView tv2 = new TextView(usuario.getContext());
                    tv2.setText(ccosto.toString());
                    tv2.setPadding(10,20,10,20);
                    tv2.setTextSize(20);
                    tv2.setGravity(Gravity.END);
                    linLayout.addView(tv2);

                    ll.addView(linLayout,layoutParams);
                }
            } catch (Exception e) {}
        }*/
    }
}
