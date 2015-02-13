package com.ihm.clasificasas.clasificasas;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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


/**
 * Created by Denny on 27/07/2014.
 */
public class usuario  extends FragmentActivity {

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

        agregarPublicaciones();
        agregarFavoritos();
    }

    public void agregarPublicaciones(){
        int centinel = 0;
        boolean empty = true;
        File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/CLASIFICASAS/publicaciones.xml");
        if (newxmlfile.exists()) {
            try {
                InputStream is = new FileInputStream(newxmlfile.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList publicaciones = doc.getElementsByTagName("publicacion");
                for (int i = 0; i < publicaciones.getLength(); i++) {
                    if (publicaciones.item(i).getChildNodes().item(3).getTextContent().equals(getIntent().getExtras().getString("TAG_USUARIO"))) {
                        if(centinel%2 == 0) centinel = 0;
                        Node publicacion = getPublicacion(publicaciones.item(i).getChildNodes().item(1).getTextContent());
                        crearTile(publicacion,centinel,0);
                        centinel+=1;
                        empty = false;
                    }
                }
            } catch (Exception e) {}
        }
        if(empty){
            float scale = getResources().getDisplayMetrics().density;
            TextView emptyText = new TextView(this);
            LinearLayout.LayoutParams eparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            eparams.setMargins(0,(int)(30*scale+0.5f), 0, 0);
            emptyText.setText("No hay información que mostrar");
            emptyText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
            emptyText.setTextColor(Color.parseColor("#8f9092"));
            emptyText.setGravity(Gravity.CENTER);
            LinearLayout rootLayout = (LinearLayout)findViewById(R.id.usuario_publicadas_layout);
            rootLayout.addView(emptyText,eparams);
        }
    }

    public void agregarFavoritos(){
        int centinel = 0;
        boolean empty = true;
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
                    if (favoritos.item(i).getChildNodes().item(1).getTextContent().equals(getIntent().getExtras().getString("TAG_USUARIO"))) {
                        if(centinel%2 == 0) centinel = 0;
                        Node publicacion = getPublicacion(favoritos.item(i).getChildNodes().item(3).getTextContent());
                        crearTile(publicacion,centinel,1);
                        centinel+=1;
                        empty = false;
                    }
                }
            } catch (Exception e) {}
        }
        if(empty){
            float scale = getResources().getDisplayMetrics().density;
            TextView emptyText = new TextView(this);
            LinearLayout.LayoutParams eparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            eparams.setMargins(0,(int)(30*scale+0.5f), 0, 0);
            emptyText.setText("No hay información que mostrar");
            emptyText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
            emptyText.setTextColor(Color.parseColor("#8f9092"));
            emptyText.setGravity(Gravity.CENTER);
            LinearLayout rootLayout = (LinearLayout)findViewById(R.id.usuario_favoritas_layout);
            rootLayout.addView(emptyText,eparams);
        }
    }

    public void crearTile(final Node publicacion, int centinel, final int seccion){
        final float scale = getResources().getDisplayMetrics().density;
        LinearLayout linLayout = new LinearLayout(this);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)(157.5*scale+0.5f), (int)(110*scale+0.5f));
        if(centinel == 1)
            layoutParams.setMargins((int)(170*scale+0.5f),-(int)(110*scale+0.5f),0,0);
        else
            layoutParams.setMargins(0,(int)(10*scale+0.5f),0,0);

        ImageView portada = new ImageView(this);
        LinearLayout.LayoutParams pparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
        File sdDir = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/fotos");
        File[] sdDirFiles = sdDir.listFiles();
        for(File singleFile : sdDirFiles){
            if(singleFile.getName().equals(publicacion.getChildNodes().item(9).getTextContent())){
                portada.setImageDrawable(Drawable.createFromPath(singleFile.getAbsolutePath()));
            }
        }
        pparams.setMargins(0,(int)(2*scale+0.5f),0,0);
        portada.setScaleType(ImageView.ScaleType.FIT_XY);
        linLayout.addView(portada,pparams);

        TextView costo = new TextView(this);
        LinearLayout.LayoutParams cparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cparams.setMargins(0,-(int)(40*scale+0.5f), 0, 0);
        costo.setText("$" + publicacion.getChildNodes().item(5).getTextContent());
        costo.setPadding((int) (10 * scale + 0.5f), (int) (10 * scale + 0.5f), (int) (10 * scale + 0.5f), (int) (10 * scale + 0.5f));
        costo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        costo.setTextColor(Color.WHITE);
        costo.setBackgroundColor(Color.parseColor("#494B4F"));
        linLayout.addView(costo,cparams);

        ImageView info = new ImageView(this);
        LinearLayout.LayoutParams iparams = new LinearLayout.LayoutParams((int)(35*scale+0.5f),(int)(35*scale+0.5f));
        iparams.setMargins(0,-(int)(112*scale+0.5f),-(int)(4*scale+0.5f),0);
        info.setImageResource(R.drawable.ic_action_info);
        iparams.gravity = Gravity.END;
        linLayout.addView(info,iparams);

        linLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seccion == 0){
                    Intent publicada = new Intent(usuario.this, publicada.class);
                    if (getIntent().hasExtra("TAG_USUARIO"))
                        publicada.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                    publicada.putExtra("TAG_ID", publicacion.getChildNodes().item(1).getTextContent());
                    startActivity(publicada);
                    overridePendingTransition(R.animator.pushdownin, R.animator.pushdownout);
                }
                if(seccion == 1) {
                    Intent detalle = new Intent(usuario.this, detalle.class);
                    if (getIntent().hasExtra("TAG_USUARIO"))
                        detalle.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                    detalle.putExtra("TAG_ID", publicacion.getChildNodes().item(1).getTextContent());
                    detalle.putExtra("TAG_FAVORITA", "");
                    startActivity(detalle);
                    overridePendingTransition(R.animator.pushdownin, R.animator.pushdownout);
                }
            }
        });

        LinearLayout rootLayout = null;
        if(seccion == 0)
            rootLayout = (LinearLayout)findViewById(R.id.usuario_publicadas_layout);
        if(seccion == 1)
            rootLayout = (LinearLayout)findViewById(R.id.usuario_favoritas_layout);
        rootLayout.addView(linLayout,layoutParams);
    }

    public Node getPublicacion(String idpublicacion){
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/publicaciones.xml");
            InputStream is = new FileInputStream(file.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();
            NodeList publicacion = doc.getElementsByTagName("publicacion");
            for (int i = 0; i < publicacion.getLength(); i++) {
                if(publicacion.item(i).getChildNodes().item(1).getTextContent().equals(idpublicacion))
                    return publicacion.item(i);

            }
        }catch (Exception e){}
        return null;
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
        MenuItem usuario = menu.findItem(R.id.usuario);
        usuario.setTitle(getIntent().getExtras().getString("TAG_USUARIO"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.usuario:
                Intent user = new Intent(usuario.this, editar.class);
                user.putExtra("TAG_USUARIO",getIntent().getExtras().getString("TAG_USUARIO"));
                startActivity(user);
                overridePendingTransition(R.animator.shrinka, R.animator.shrinkb);
                finish();
                break;
            case R.id.salir:
                Intent main = new Intent(usuario.this, main.class);
                startActivity(main);
                overridePendingTransition(R.animator.shrinka, R.animator.shrinkb);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
