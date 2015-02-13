package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Denny on 27/07/2014.
 */
public class resultados extends Activity {

    Button informacion;
    String ciudad, tipo, presupuesto;
    Double p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultados);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ciudad = getIntent().getExtras().getString("TAG_CIUDAD");
        tipo = getIntent().getExtras().getString("TAG_TIPO");
        try {
            presupuesto = getIntent().getExtras().getString("TAG_PRESUPUESTO");
            p = Double.parseDouble(presupuesto);
        }catch(Exception e){}
        obtenerResultados();
    }

    public void obtenerResultados(){
        boolean resultados = false;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/publicaciones.xml");
            InputStream is = new FileInputStream(file.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();
            NodeList publicacion = doc.getElementsByTagName("publicacion");
            for (int i = 0; i < publicacion.getLength(); i++) {
                if(publicacion.item(i).getChildNodes().item(7).getTextContent().equals(tipo) && publicacion.item(i).getChildNodes().item(11).getTextContent().equals(ciudad) && (Double.parseDouble(publicacion.item(i).getChildNodes().item(5).getTextContent())<=p && !publicacion.item(i).getChildNodes().item(3).getTextContent().equals(getIntent().getExtras().getString("TAG_USUARIO")))) {
                    crearResultado(publicacion.item(i));
                    resultados = true;
                }
            }
            if(!resultados){
                crearVacio();
            }
        }catch (Exception e){}
    }

    public void crearVacio() {
        final float scale = getResources().getDisplayMetrics().density;
        LinearLayout linLayout = new LinearLayout(this);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)(320*scale+0.5f), LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) (5 * scale + 0.5f), 0, (int) (5 * scale + 0.5f), 0);

        ImageView imgv = new ImageView(this);
        LinearLayout.LayoutParams imgvparams = new LinearLayout.LayoutParams((int)(50*scale+0.5f),(int)(50*scale+0.5f));
        imgvparams.gravity = Gravity.CENTER;
        imgvparams.setMargins(0,(int)(40*scale+0.5f),0,(int)(20*scale+0.5f));
        imgv.setImageResource(R.drawable.ic_null);
        linLayout.addView(imgv,imgvparams);

        TextView no = new TextView(this);
        LinearLayout.LayoutParams noparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        no.setGravity(Gravity.CENTER);
        no.setText("No se han encontrado resultados");
        no.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
        linLayout.addView(no,noparams);

        LinearLayout rootLayout = (LinearLayout)findViewById(R.id.llresultados);
        rootLayout.addView(linLayout,layoutParams);
    }

    public void crearResultado(final Node resultado){
        final float scale = getResources().getDisplayMetrics().density;
        LinearLayout linLayout = new LinearLayout(this);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)(310*scale+0.5f), LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int)(5*scale+0.5f),0,(int)(5*scale+0.5f),0);
        linLayout.setBackgroundResource(R.drawable.border);

        TextView tv = new TextView(this);
        LinearLayout.LayoutParams tvparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvparams.setMargins((int)(10*scale+0.5f),(int)(10*scale+0.5f), 0, 0);
        tv.setText("$"+resultado.getChildNodes().item(5).getTextContent());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
        if(getIntent().hasExtra("TAG_USUARIO")){
            final ImageView imgv = new ImageView(this);
            LinearLayout.LayoutParams imgvparams = new LinearLayout.LayoutParams((int)(50*scale+0.5f),(int)(50*scale+0.5f));
            imgvparams.gravity = Gravity.END;
            imgvparams.setMargins(0,-(int)(40*scale+0.5f),0,0);
            if(checkFavorito(resultado.getChildNodes().item(1).getTextContent())){
                imgv.setImageResource(R.drawable.ic_action_star_fav);
                imgv.setTag(R.drawable.ic_action_star_fav);
            }else{
                imgv.setImageResource(R.drawable.ic_action_star);
                imgv.setTag(R.drawable.ic_action_star);
            }
            linLayout.addView(tv,tvparams);
            imgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer integer = (Integer) imgv.getTag();
                    switch(integer){
                        case R.drawable.ic_action_star:
                            imgv.setImageResource(R.drawable.ic_action_star_fav);
                            imgv.setTag(R.drawable.ic_action_star_fav);
                            guardarFavorito(resultado.getChildNodes().item(1).getTextContent());
                            break;
                        case R.drawable.ic_action_star_fav:
                            imgv.setImageResource(R.drawable.ic_action_star);
                            imgv.setTag(R.drawable.ic_action_star);
                            removerFavorito(resultado.getChildNodes().item(1).getTextContent());
                            break;
                    }
                }
            });
            linLayout.addView(imgv,imgvparams);
        }else{
            tv.setGravity(Gravity.CENTER);
            linLayout.addView(tv,tvparams);
        }

        ImageView portada = new ImageView(this);
        LinearLayout.LayoutParams pparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,(int)(170*scale+0.5f));
        File sdDir = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/fotos");
        File[] sdDirFiles = sdDir.listFiles();
        for(File singleFile : sdDirFiles){
            if(singleFile.getName().equals(resultado.getChildNodes().item(9).getTextContent())){
                portada.setImageDrawable(Drawable.createFromPath(singleFile.getAbsolutePath()));
            }
        }
        pparams.setMargins(0,(int)(2*scale+0.5f),0,0);
        portada.setScaleType(ImageView.ScaleType.FIT_XY);
        linLayout.addView(portada,pparams);

        TextView ubicacion = new TextView(this);
        LinearLayout.LayoutParams uparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        uparams.setMargins((int)(10*scale+0.5f),(int)(10*scale+0.5f), 0, 0);
        ubicacion.setText("Ubicación: "+resultado.getChildNodes().item(13).getTextContent());
        ubicacion.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
        linLayout.addView(ubicacion,uparams);

        View hr = new View(this);
        LinearLayout.LayoutParams hrparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,(int)(1*scale+0.5f));
        hrparams.setMargins(0,(int)(10*scale+0.5f), 0, 0);
        hr.setBackgroundColor(Color.GRAY);
        linLayout.addView(hr,hrparams);

        TextView cuartos = new TextView(this);
        LinearLayout.LayoutParams cparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cparams.setMargins((int)(10*scale+0.5f),(int)(10*scale+0.5f), 0, 0);
        cuartos.setText("Cuartos: "+resultado.getChildNodes().item(19).getTextContent());
        cuartos.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
        linLayout.addView(cuartos,cparams);

        TextView banos = new TextView(this);
        LinearLayout.LayoutParams bparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bparams.setMargins((int)(180*scale+0.5f),-(int)(26*scale+0.5f), 0, 0);
        banos.setText("Baños: "+resultado.getChildNodes().item(21).getTextContent());
        banos.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
        linLayout.addView(banos,bparams);

        TextView pisos = new TextView(this);
        LinearLayout.LayoutParams piparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        piparams.setMargins((int)(10*scale+0.5f),-(int)(2*scale+0.5f), 0, 0);
        pisos.setText("Pisos: "+resultado.getChildNodes().item(23).getTextContent());
        pisos.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
        linLayout.addView(pisos,piparams);

        TextView descripcion = new TextView(this);
        LinearLayout.LayoutParams dparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dparams.setMargins((int)(10*scale+0.5f),-(int)(2*scale+0.5f), 0, 0);
        descripcion.setText("Descripción:");
        descripcion.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
        linLayout.addView(descripcion,dparams);

        TextViewEx descripcion_text = new TextViewEx(this);
        LinearLayout.LayoutParams desparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String text = resultado.getChildNodes().item(29).getTextContent();
        if(text.length()<20){
            descripcion_text.setText(text);
        }else if(text.length()<65){
            descripcion_text.setText(text);
        }else if(text.length()>=65){
            String result = "";
            for(int i=0; i<65; i++)
                result = result + text.charAt(i);
            result = result + "...";
            descripcion_text.setText(result,true);
        }
        desparams.setMargins((int)(5*scale+0.5f),0,0, 0);
        descripcion_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
        linLayout.addView(descripcion_text,desparams);

        Button btn = new Button(this);
        LinearLayout.LayoutParams buttonparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,(int)(49*scale+0.5f));
        buttonparams.setMargins(0,(int)(15*scale+0.5f),0,0);
        btn.setText("Más información");
        btn.setTextColor(Color.WHITE);
        btn.setBackgroundResource(R.drawable.buttom);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detalle = new Intent(resultados.this, detalle.class);
                detalle.putExtra("TAG_CIUDAD",ciudad);
                detalle.putExtra("TAG_TIPO",tipo);
                detalle.putExtra("TAG_PRESUPUESTO",presupuesto);
                if(getIntent().hasExtra("TAG_USUARIO")) detalle.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                detalle.putExtra("TAG_ID",resultado.getChildNodes().item(1).getTextContent());
                startActivity(detalle);
                overridePendingTransition(R.animator.pushdownin, R.animator.pushdownout);
            }
        });
        linLayout.addView(btn,buttonparams);

        LinearLayout rootLayout = (LinearLayout)findViewById(R.id.llresultados);
        rootLayout.addView(linLayout,layoutParams);
    }

    public boolean checkFavorito(String idpublicacion){
        File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/CLASIFICASAS/favoritos.xml");
        if (newxmlfile.exists()) {
            try {
                InputStream is = new FileInputStream(newxmlfile.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList favoritos = doc.getElementsByTagName("favorito");
                for (int i = 0; i < favoritos.getLength(); i++) {
                    if (favoritos.item(i).getChildNodes().item(1).getTextContent().equals(getIntent().getExtras().getString("TAG_USUARIO")) && favoritos.item(i).getChildNodes().item(3).getTextContent().equals(idpublicacion)) {
                        return true;
                    }
                }
            } catch (Exception e) {}
        }
        return false;
    }

    public void guardarFavorito(String idpublicacion) {
        File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/CLASIFICASAS/favoritos.xml");
        boolean success = true;
        if (!newxmlfile.exists()) {
            try {
                success = newxmlfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (success) {
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                XmlSerializer serializer = Xml.newSerializer();
                try {
                    serializer.setOutput(fileos, "UTF-8");
                    serializer.startDocument(null, Boolean.valueOf(true));
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                    serializer.startTag(null, "favoritos");
                        serializer.startTag(null, "favorito");
                            serializer.startTag(null,"usuario");
                            serializer.text(getIntent().getExtras().getString("TAG_USUARIO"));
                            serializer.endTag(null, "usuario");
                            serializer.startTag(null, "idpublicacion");
                            serializer.text(idpublicacion);
                            serializer.endTag(null, "idpublicacion");
                        serializer.endTag(null, "favorito");
                    serializer.endTag(null, "favoritos");
                    serializer.endDocument();
                    serializer.flush();
                    fileos.close();
                } catch (Exception e) {
                    Log.e("Exception","error occurred while creating xml file");
                }
            }
        }else {
            try {
                InputStream is = new FileInputStream(newxmlfile.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList favoritos = doc.getElementsByTagName("favorito");
                FileOutputStream fileos = new FileOutputStream(newxmlfile);
                XmlSerializer serializer = Xml.newSerializer();
                serializer.setOutput(fileos, "UTF-8");
                serializer.startDocument(null, Boolean.valueOf(true));
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                serializer.startTag(null, "favoritos");
                    for(int i=0; i<favoritos.getLength(); i++){
                        serializer.startTag(null, "favorito");
                            serializer.startTag(null,"usuario");
                            serializer.text(favoritos.item(i).getChildNodes().item(1).getTextContent());
                            serializer.endTag(null, "usuario");
                            serializer.startTag(null, "idpublicacion");
                            serializer.text(favoritos.item(i).getChildNodes().item(3).getTextContent());
                            serializer.endTag(null, "idpublicacion");
                        serializer.endTag(null, "favorito");
                    }
                    serializer.startTag(null, "favorito");
                        serializer.startTag(null,"usuario");
                        serializer.text(getIntent().getExtras().getString("TAG_USUARIO"));
                        serializer.endTag(null, "usuario");
                        serializer.startTag(null, "idpublicacion");
                        serializer.text(idpublicacion);
                        serializer.endTag(null, "idpublicacion");
                    serializer.endTag(null, "favorito");
                serializer.endTag(null, "favoritos");
                serializer.endDocument();
                serializer.flush();
                fileos.close();
            }catch (Exception e){}
        }
    }

    public void removerFavorito(String idpublicacion) {
        try{
            File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/CLASIFICASAS/favoritos.xml");
            InputStream is = new FileInputStream(newxmlfile.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();
            NodeList favoritos = doc.getElementsByTagName("favorito");
            FileOutputStream fileos = new FileOutputStream(newxmlfile);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "favoritos");
                for(int i=0; i<favoritos.getLength(); i++){
                    if(!(favoritos.item(i).getChildNodes().item(1).getTextContent().equals(getIntent().getExtras().getString("TAG_USUARIO")) && favoritos.item(i).getChildNodes().item(3).getTextContent().equals(idpublicacion))){
                        serializer.startTag(null, "favorito");
                            serializer.startTag(null, "usuario");
                            serializer.text(favoritos.item(i).getChildNodes().item(1).getTextContent());
                            serializer.endTag(null, "usuario");
                            serializer.startTag(null, "idpublicacion");
                            serializer.text(favoritos.item(i).getChildNodes().item(3).getTextContent());
                            serializer.endTag(null, "idpublicacion");
                        serializer.endTag(null, "favorito");
                    }
                }
            serializer.endTag(null, "favoritos");
            serializer.endDocument();
            serializer.flush();
            fileos.close();
        }catch (Exception e){}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getIntent().hasExtra("TAG_USUARIO")){
                    Intent intent = new Intent(resultados.this, buscar.class);
                    intent.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                    startActivity(intent);
                    overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                }else{
                    Intent intent = new Intent(resultados.this, buscar.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
