package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
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

/**
 * Created by Denny on 27/07/2014.
 */
public class registrar extends Activity {

    Button registrar;
    TextView nombres, apellidos, movil, correo, usuario, contrasena, confirmar;
    JSONParser jsonParser = new JSONParser();
    String url_insert = "http://"+IP.address+"/clasificasas/insertarusuario.php";
    String TAG_SUCCESS = "success";
    String TAG_MESSAGE = "message";
    String TAG_USUARIO = "usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        registrar = (Button) findViewById(R.id.registrar_btn);
        registrar.setOnClickListener(registrarButtonhandler);

        nombres = (TextView) findViewById(R.id.registrar_nombres);
        apellidos = (TextView) findViewById(R.id.registrar_apellidos);
        movil = (TextView) findViewById(R.id.registrar_movil);
        correo = (TextView) findViewById(R.id.registrar_correo);
        usuario = (TextView) findViewById(R.id.registrar_usuario);
        contrasena = (TextView) findViewById(R.id.registrar_contrasena);
        contrasena.setTransformationMethod(new PasswordTransformationMethod());
        confirmar = (TextView) findViewById(R.id.registrar_confirmar);
        confirmar.setTransformationMethod(new PasswordTransformationMethod());
    }

    View.OnClickListener registrarButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registrar_btn:
                new RegistrarUsuario().execute();
            break;
        }
        }
    };
    public  void  showAlertDialog(String title,String message)
    {

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(registrar.this, main.class);
                startActivity(intent);
                overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class RegistrarUsuario extends AsyncTask<String, String, String> {
        String names;
        String lastnames;
        String mobile;
        String email;
        String user;
        String password;
        String confirm;
        @Override
        protected String doInBackground(String... params) {
            names = nombres.getText().toString();
            lastnames = apellidos.getText().toString();
            mobile = movil.getText().toString();
            email = correo.getText().toString();
            user = usuario.getText().toString();
            password = contrasena.getText().toString();
            confirm = confirmar.getText().toString();
            if(validar()){
                List<NameValuePair> p = new ArrayList<NameValuePair>();
                p.add(new BasicNameValuePair("usuario", user));
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                    md.update(password.getBytes());
                    byte byteData[] = md.digest();
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < byteData.length; i++) {
                        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                    }
                    p.add(new BasicNameValuePair("cont", sb.toString()));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                p.add(new BasicNameValuePair("mobil", mobile));
                p.add(new BasicNameValuePair("nombres", names));
                p.add(new BasicNameValuePair("apellidos", lastnames));
                p.add(new BasicNameValuePair("correo", email));
                JSONObject json = jsonParser.makeHttpRequest(url_insert,"POST", p);
                try {
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.e("DENTRO DE SUCCESS","Holaaaaaaaaaa");
                        Intent us = new Intent(registrar.this, usuario.class);
                        us.putExtra("TAG_USUARIO",json.getString(TAG_USUARIO));
                        startActivity(us);
                        overridePendingTransition(R.animator.shrinka, R.animator.shrinkb);
                        finish();
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        public boolean validar(){
            if(!names.matches("^[a-zA-Z\\s]+")){
                runOnUiThread(new Runnable(){
                    public void run(){
                        showAlertDialog("Error de registro",
                                "Los nombres solo puede contener letras de a-z minúsculas o mayúsculas");
                    }
                });
                return false;
            }
            if(!lastnames.matches("^[a-zA-Z\\s]+")){
                runOnUiThread(new Runnable(){
                    public void run(){
                        showAlertDialog("Error de registro",
                                "El apellido solo puede contener letras de a-z minúsculas o mayúsculas");
                    }
                });
            return false;
            }
            if(!mobile.matches("[0-9]+") || mobile.length()!=10) {
                runOnUiThread(new Runnable(){
                    public void run(){
                        showAlertDialog("Error de registro",
                                "El numero de telefono debe de contener 10 números y solamente números");
                    }
                });
                return false;
            }
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern pttrn = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = pttrn.matcher(email);
            if(!m.matches()) {//email
                runOnUiThread(new Runnable(){
                    public void run(){
                        showAlertDialog("Error de registro",
                                "La dirección de email es inválida");
                    }
                });
                return false;
            }
            if(user.isEmpty()) {
                runOnUiThread(new Runnable(){
                    public void run(){
                        showAlertDialog("Error de registro",
                                "El usuario esta vacío");
                    }
                });
                return false;
            }
            if(password.isEmpty()) {
                runOnUiThread(new Runnable(){
                    public void run(){
                        showAlertDialog("Error de registro",
                                "La contraseña esta vacía");
                    }
                });
                return false;
            }
            if(confirm.isEmpty()) {
                runOnUiThread(new Runnable(){
                    public void run(){
                        showAlertDialog("Error de registro",
                                "El campo de confirmación de la contraseña esta vacía");
                    }
                });
                return false;
            }
            if(!password.equals(confirm)) {
                runOnUiThread(new Runnable(){
                    public void run(){
                        showAlertDialog("Error de registro",
                                "Las contraseñas no coinciden");
                    }
                });
                return false;
            }
            return true;
        }
    }
}
