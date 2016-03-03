package compras.donde.lvr.com.dondecompras;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoriasDetalle extends AppCompatActivity {
    static String DESCRIPCION = "descripcion_esta";
    static String DIRECCION = "direccion_esta";
    static String ID = "id_categoria";
    static String IMG_PREVIA = "img_previa";
    static String NOMBRE_ESTABLECIMIENTO = "nombre_esta";
    static String LATITUD = "latitud_esta";
    static String LONGITUD = "longitud_esta";
    private static final String _URL = "http://190.210.203.145/api/v1/dondecompras/comercio";
    ListViewAdapterDetalle adapter;
    ArrayList<HashMap<String, String>> arraylist;
    JSONArray jsonarray;
    JSONObject jsonobject;
    double latitud;
    ListView listview;
    double longitud;
    private ProgressDialog mProgressDialog;
    String posicion;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinator_categorias_lista_detalle);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              //          .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), ComercioNuevoActivity.class);
                startActivity(intent);
            }
        });

        new DownloadJSON().execute();
        posicion = getIntent().getStringExtra("posicion");
        LocationManager mlocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        MyLocationListener mlocListener = new MyLocationListener();
        mlocListener.setMainActivity(this);
        mlocManager.requestLocationUpdates("gps", 0, 0, mlocListener);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("El GPS no esta Activado");
            builder.setMessage("Por favor áctivalo para una mejor ubicación");
            builder.setPositiveButton("HACERLO ...", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }
    private class DownloadJSON extends AsyncTask<String, String, String> {
        private DownloadJSON() {
        }
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(CategoriasDetalle.this);
            mProgressDialog.setTitle("Donde Compras...");
            mProgressDialog.setMessage("Cargando...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        protected String doInBackground(String... args) {
            String distancia = getSharedPreferences("PreferenciasUsuario", 0).getString("distancia", "1");
            arraylist = new ArrayList();
            List params = new ArrayList();
          //  params.add(new BasicNameValuePair("", String.valueOf(latitud)));
          //  params.add(new BasicNameValuePair("", String.valueOf(longitud)));
          //  params.add(new BasicNameValuePair("", String.valueOf(latitud)));
            params.add(new BasicNameValuePair("", posicion));
            params.add(new BasicNameValuePair("", distancia));
            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(_URL, "GET", params);
            Log.d("Json resultado", json.toString());
            Log.d("Coordenadas_Lat", String.valueOf(latitud));
            Log.d("Coordenadas_log", String.valueOf(longitud));

//            Toast.makeText(getApplicationContext(), " Latitud " + String.valueOf(latitud) + " Longitud " + String.valueOf(longitud), Toast.LENGTH_SHORT).show();
            try {
                jsonarray = json.getJSONArray("Comercio");
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap();
                    json = jsonarray.getJSONObject(i);
                    map.put("id_categoria", json.getString("id_categoria"));
                    map.put("nombre_esta", json.getString("nombre"));
                    map.put("direccion_esta", json.getString("direccion"));
                    map.put("descripcion_esta", json.getString("descripcion"));
                    map.put("latitud_esta", json.getString("latitud"));
                    map.put("longitud_esta", json.getString("longitud"));
                    map.put("img_previa", json.getString("ruta_imagen"));
                    arraylist.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String args) {
            listview = (ListView) findViewById(R.id.listview_detalle);
            adapter = new ListViewAdapterDetalle(CategoriasDetalle.this, arraylist);
            listview.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }
    public class MyLocationListener implements LocationListener {
        CategoriasDetalle mainActivity;
        public CategoriasDetalle getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity(CategoriasDetalle mainActivity) {
            this.mainActivity = mainActivity;
        }
        public void onLocationChanged(Location location) {
            location.getLatitude();
            location.getLongitude();
            latitud = location.getLatitude();
            longitud = location.getLongitude();
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Activado", Toast.LENGTH_SHORT).show();
        }
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Inactivo", Toast.LENGTH_SHORT).show();
        }
    }
}