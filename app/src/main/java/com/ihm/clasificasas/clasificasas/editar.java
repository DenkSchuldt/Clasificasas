package com.ihm.clasificasas.clasificasas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class editar extends Activity {

    Button editar;
    EditText nombres, apellidos, movil, correo, contrasena;
    JSONParser jsonParser = new JSONParser();
    String url_login = "http://"+IP.address+"/clasificasas/editarperfil.php";
    String TAG_SUCCESS = "success";
    String TAG_USUARIO = "usuario";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        editar = (Button) findViewById(R.id.usuario_editar_informacion);
        editar.setOnClickListener(editarButtonhandler);

        nombres = (EditText) findViewById(R.id.usuario_editar_nombres);
        apellidos = (EditText) findViewById(R.id.usuario_editar_apellidos);
        movil = (EditText) findViewById(R.id.usuario_editar_movil);
        correo = (EditText) findViewById(R.id.usuario_editar_correo);
        contrasena = (EditText) findViewById(R.id.usuario_editar_contrasena);
    }

    View.OnClickListener editarButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.usuario_editar_informacion:
                new EditarUsuario().execute();
            break;
        }
        }
    };

    public Dialog createDialog(String title,String s) {
        //using the builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle(title);
        builder.setMessage(s);
        return builder.create();
    }
    public  void  showAlertDialog(String title,String message){
        final AlertDialog  alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /**
     * Background Async Task to Create new product
     * */
    class EditarUsuario extends AsyncTask<String, String, String> {
        String names;
        String lastnames;
        String mobile;
        String email;
        String password;
        @Override
        protected String doInBackground(String... params) {
            names = nombres.getText().toString();
            lastnames = apellidos.getText().toString();
            mobile = movil.getText().toString();
            email = correo.getText().toString();
            password = contrasena.getText().toString();
            if(validar()) {
                List<NameValuePair> p = new ArrayList<NameValuePair>();
                p.add(new BasicNameValuePair("usuario", getIntent().getExtras().getString("TAG_USUARIO")));
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
                p.add(new BasicNameValuePair("correo",email));

                JSONObject json = jsonParser.makeHttpRequest(url_login, "POST", p);
                try {
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Intent i = new Intent(getApplicationContext(), usuario.class);
                        i.putExtra("TAG_USUARIO", json.getString(TAG_USUARIO));
                        startActivity(i);
                        overridePendingTransition(R.animator.shrinka, R.animator.shrinkb);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                            showAlertDialog("Error de autenticacion","La contraseña es incorrecta o usted ha escrito un usuario invalido");
                            }
                        });
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
            if(password.isEmpty()) {
                runOnUiThread(new Runnable(){
                    public void run(){
                        showAlertDialog("Error de registro",
                                "La contraseña esta vacía");
                    }
                });
                return false;
            }
            return true;
        }
    }
}
