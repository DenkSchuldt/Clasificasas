package com.ihm.clasificasas.clasificasas;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class main extends Activity {

    Button buscar;
    Button ingresar;
    Button registrar;
    TextView user, password;
    JSONParser jsonParser = new JSONParser();
    String url_login = "http://"+IP.address+"/clasificasas/login.php";
    String TAG_SUCCESS = "success";
    String TAG_USUARIO = "usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        buscar = (Button) findViewById(R.id.home_buscar);
        ingresar = (Button) findViewById(R.id.home_ingresar);
        registrar = (Button) findViewById(R.id.home_registrar);

        user = (TextView) findViewById(R.id.home_user);
        password = (TextView) findViewById(R.id.home_password);
        password.setTransformationMethod(new PasswordTransformationMethod());

        buscar.setOnClickListener(homeButtonhandler);
        ingresar.setOnClickListener(homeButtonhandler);
        registrar.setOnClickListener(homeButtonhandler);
    }

    View.OnClickListener homeButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_buscar:
                Intent buscar = new Intent(main.this, buscar.class);
                startActivity(buscar);
                overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
            break;
            case R.id.home_ingresar:
                new LoginUsuario().execute();
            break;
            case R.id.home_registrar:
                Intent registrar = new Intent(main.this, registrar.class);
                startActivity(registrar);
                overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
            break;
        }
        }
    };

    /**
     * Background Async Task to Create new product
     * */
    class LoginUsuario extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("usuario", user.getText().toString()));
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(password.getText().toString().getBytes());
                byte byteData[] = md.digest();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < byteData.length; i++) {
                    sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }
                p.add(new BasicNameValuePair("cont",sb.toString()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            JSONObject json = jsonParser.makeHttpRequest(url_login,"POST", p);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), usuario.class);
                    i.putExtra("TAG_USUARIO", json.getString(TAG_USUARIO));
                    startActivity(i);
                    overridePendingTransition(R.animator.shrinka, R.animator.shrinkb);
                    finish();
                } else {
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
