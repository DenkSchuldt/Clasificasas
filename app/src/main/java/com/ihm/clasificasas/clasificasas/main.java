package com.ihm.clasificasas.clasificasas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.renderscript.Element;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class main extends Activity {

    Button buscar;
    Button ingresar;
    Button registrar;
    TextView user, password;
    ArrayList<String> mImageLink;

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
                    if(verificarUsuario()){
                        Intent us = new Intent(main.this, usuario.class);
                        us.putExtra("TAG_USUARIO",user.getText().toString());
                        startActivity(us);
                        overridePendingTransition(R.animator.shrinka, R.animator.shrinkb);
                        finish();
                    }else{
                        Context context = getApplicationContext();
                        CharSequence text = "Usuario o Contraseña inválida";
                        Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    break;
                case R.id.home_registrar:
                    Intent registrar = new Intent(main.this, registrar.class);
                    startActivity(registrar);
                    overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
                    break;
            }
        }
    };

    public boolean verificarUsuario(){
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/usuarios.xml");
            InputStream is = new FileInputStream(file.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();
            NodeList usernames = doc.getElementsByTagName("username");
            NodeList passwords = doc.getElementsByTagName("password");
            for (int i = 0; i < usernames.getLength(); i++) {
                String usuario = usernames.item(i).getFirstChild().getNodeValue();
                String clave   = passwords.item(i).getFirstChild().getNodeValue();
                if(user.getText().toString().equals(usuario) && password.getText().toString().equals(clave))
                    return true;
            }

        }catch (Exception e){}
        return false;
    }

    public Dialog createDialog(String title,String s) {
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
}
