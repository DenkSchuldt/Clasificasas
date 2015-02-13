package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class editar extends Activity {

    Button editar;
    EditText nombres, apellidos, movil, correo, contrasena, confirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editar = (Button) findViewById(R.id.editar_btn);
        editar.setOnClickListener(editarButtonhandler);

        nombres = (EditText) findViewById(R.id.editar_nombres);
        apellidos = (EditText) findViewById(R.id.editar_apellidos);
        movil = (EditText) findViewById(R.id.editar_movil);
        correo = (EditText) findViewById(R.id.editar_correo);
        contrasena = (EditText) findViewById(R.id.editar_contrasena);
        confirmar = (EditText) findViewById(R.id.editar_confirmar);

        setUsuario();
    }

    View.OnClickListener editarButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editar_btn:
                    if(validar()){
                        if(editarUsuario()){
                            Intent intent = new Intent(editar.this, usuario.class);
                            intent.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                            startActivity(intent);
                            overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                        }else{
                            Context context = getApplicationContext();
                            CharSequence text = "Algo salió mal. Inténtelo nuevamente.";
                            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(editar.this, usuario.class);
                intent.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                startActivity(intent);
                overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setUsuario(){
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/usuarios.xml");
            InputStream is = new FileInputStream(file.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();
            NodeList usuario = doc.getElementsByTagName("usuario");
            for (int i = 0; i < usuario.getLength(); i++) {
                if(usuario.item(i).getChildNodes().item(9).getTextContent().equals(getIntent().getExtras().getString("TAG_USUARIO"))) {
                    nombres.setText(usuario.item(i).getChildNodes().item(1).getTextContent());
                    apellidos.setText(usuario.item(i).getChildNodes().item(3).getTextContent());
                    movil.setText(usuario.item(i).getChildNodes().item(5).getTextContent());
                    correo.setText(usuario.item(i).getChildNodes().item(7).getTextContent());
                    contrasena.setText(usuario.item(i).getChildNodes().item(11).getTextContent());
                    confirmar.setText(usuario.item(i).getChildNodes().item(11).getTextContent());
                }
            }

        }catch (Exception e){}
    }

    public boolean validar(){
        Context context = getApplicationContext();
        if(!nombres.getText().toString().matches("^[a-zA-Z\\s]+")){
            CharSequence text = "Los nombres solo puede contener letras de a-z minúsculas o mayúsculas";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(!apellidos.getText().toString().matches("^[a-zA-Z\\s]+")){
            CharSequence text = "Los apellidos solo puede contener letras de a-z minúsculas o mayúsculas";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(!movil.getText().toString().matches("[0-9]+") || movil.getText().toString().length()!=10) {
            CharSequence text = "El numero de telefono debe ser de 10 dígitos";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern pttrn = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = pttrn.matcher(correo.getText().toString());
        if(!m.matches()) {//email
            CharSequence text = "Dirección de correo inválida";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(contrasena.getText().toString().isEmpty()) {
            CharSequence text = "El campo de la contraseña esta vacío";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
        }
        if(confirmar.getText().toString().isEmpty()) {
            CharSequence text = "El campo de confirmación de la contraseña esta vacío";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(!contrasena.getText().toString().equals(confirmar.getText().toString())) {
            CharSequence text = "Las contraseñas no coinciden";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    public boolean editarUsuario(){
        File users = new File(Environment.getExternalStorageDirectory()+"/CLASIFICASAS/usuarios.xml");
        boolean success = true;
        try {
            InputStream is = new FileInputStream(users.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();
            NodeList usuarios = doc.getElementsByTagName("usuario");
            FileOutputStream fileos = new FileOutputStream(users);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "usuarios");
            for(int i=0; i<usuarios.getLength(); i++){
                serializer.startTag(null, "usuario");
                if(usuarios.item(i).getChildNodes().item(9).getTextContent().equals(getIntent().getExtras().getString("TAG_USUARIO"))){
                    serializer.startTag(null,"nombres");
                    serializer.text(nombres.getText().toString());
                    serializer.endTag(null, "nombres");
                    serializer.startTag(null, "apellidos");
                    serializer.text(apellidos.getText().toString());
                    serializer.endTag(null, "apellidos");
                    serializer.startTag(null, "movil");
                    serializer.text(movil.getText().toString());
                    serializer.endTag(null, "movil");
                    serializer.startTag(null, "correo");
                    serializer.text(correo.getText().toString());
                    serializer.endTag(null, "correo");
                    serializer.startTag(null, "username");
                    serializer.text(getIntent().getExtras().getString("TAG_USUARIO"));
                    serializer.endTag(null, "username");
                    serializer.startTag(null, "password");
                    serializer.text(contrasena.getText().toString());
                    serializer.endTag(null, "password");
                }else {
                    serializer.startTag(null, "nombres");
                    serializer.text(usuarios.item(i).getChildNodes().item(1).getTextContent());
                    serializer.endTag(null, "nombres");
                    serializer.startTag(null, "apellidos");
                    serializer.text(usuarios.item(i).getChildNodes().item(3).getTextContent());
                    serializer.endTag(null, "apellidos");
                    serializer.startTag(null, "movil");
                    serializer.text(usuarios.item(i).getChildNodes().item(5).getTextContent());
                    serializer.endTag(null, "movil");
                    serializer.startTag(null, "correo");
                    serializer.text(usuarios.item(i).getChildNodes().item(7).getTextContent());
                    serializer.endTag(null, "correo");
                    serializer.startTag(null, "username");
                    serializer.text(usuarios.item(i).getChildNodes().item(9).getTextContent());
                    serializer.endTag(null, "username");
                    serializer.startTag(null, "password");
                    serializer.text(usuarios.item(i).getChildNodes().item(11).getTextContent());
                    serializer.endTag(null, "password");
                }
                serializer.endTag(null, "usuario");
            }
            serializer.endTag(null, "usuarios");
            serializer.endDocument();
            serializer.flush();
            fileos.close();
            return true;
        }catch (Exception e){}
        return false;
    }
}
