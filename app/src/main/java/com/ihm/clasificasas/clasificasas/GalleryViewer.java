package com.ihm.clasificasas.clasificasas;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class GalleryViewer extends Activity {
        private GridView gridView;
        private GridViewAdapter customGridAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_gallery_viewer);

            gridView = (GridView) findViewById(R.id.gridView);

            customGridAdapter = new GridViewAdapter(this,R.layout.grid_row, getData());
            gridView.setAdapter(customGridAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Toast.makeText(GalleryViewer.this, position + "#Selected",
                            Toast.LENGTH_SHORT).show();
                }

            });
        }
     private Bitmap loadFromUrl(String url){
         URL imageUrl = null;
         HttpURLConnection conn = null;
         Bitmap imagen=null;

         try {
            Log.v("url es: ",url);
             imageUrl = new URL(url);
             conn = (HttpURLConnection) imageUrl.openConnection();
             conn.connect();

             BitmapFactory.Options options = new BitmapFactory.Options();
             options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

             imagen = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);

         } catch (IOException e) {

             e.printStackTrace();

         }
         return imagen;

     }

        private ArrayList getData() {
            final ArrayList imageItems = new ArrayList();
            TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            for (int i = 0; i < imgs.length(); i++) {
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                        imgs.getResourceId(i, -1),options);
                imageItems.add(new ImageItem(bitmap, "Image#" + i));
            }
            return imageItems;
        }

    }
