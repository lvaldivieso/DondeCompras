package compras.donde.lvr.com.dondecompras;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
    static String LOCALIDAD = "localidad";
    static String TELEFONO = "telefono";
    static String ID = "id_categoria";
    static String IMG_PREVIA = "img_previa";
    static String NOMBRE_ESTABLECIMIENTO = "nombre_esta";
    static String LATITUD = "latitud_esta";
    static String LONGITUD = "longitud_esta";
    static String ES_FAVORITO = "favorito";
    static String ID_COMERCIO = "id_comercio";
    private static final String _URL = "http://tiny-alien.com.ar/api/v1/dondecompras/comercio";
    ListViewAdapterDetalle adapter;
    ArrayList<HashMap<String, String>> arraylist;
    JSONArray jsonarray;
    ListView listview;
    private ProgressDialog mProgressDialog;
    JSONParser jsonParser = new JSONParser();
    String posicion;
    double latitud, latitude;
    double longitud, longitude;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinator_categorias_lista_detalle);
        GPSTracker gps = new GPSTracker(this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        latitud = latitude;
        longitud = longitude;

        Log.d("Coordenadas_Lat", String.valueOf(latitud));
        Log.d("Coordenadas_log", String.valueOf(longitud));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //          .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), ComercioNuevo.class);
                intent.putExtra("idCategoria", posicion);
                startActivity(intent);
            }
        });
        posicion = getIntent().getStringExtra("posicion");

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            gps.showSettingsAlert();
        }

        new DownloadJSON().execute();
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
            params.add(new BasicNameValuePair("latitud", String.valueOf(latitud)));
            params.add(new BasicNameValuePair("longitud", String.valueOf(longitud)));
            params.add(new BasicNameValuePair("latitud", String.valueOf(latitud)));
            params.add(new BasicNameValuePair("usuario", "3"));
            params.add(new BasicNameValuePair("categoria", posicion));
            params.add(new BasicNameValuePair("distancia", distancia));
            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(_URL, "GET", params);
            Log.d("Json resultado", json.toString());

            try {

                jsonarray = json.getJSONArray("Comercio");
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap();
                    json = jsonarray.getJSONObject(i);
                    map.put("id_categoria", json.getString("id_categoria"));
                    map.put("nombre_esta", json.getString("nombre"));
                    map.put("direccion_esta", json.getString("calle"));
                    map.put("localidad", json.getString("localidad"));
                    map.put("telefono", json.getString("telefono"));
                    map.put("descripcion_esta", json.getString("descripcion"));
                    map.put("latitud_esta", json.getString("latitud"));
                    map.put("longitud_esta", json.getString("longitud"));
                    map.put("img_previa", json.getString("ruta_imagen"));
                    map.put("favorito", json.getString("es_favorito"));
                    map.put("id_comercio", json.getString("id_comercio"));
                    arraylist.add(map);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String args) {
            if (jsonarray.length() != 0){
            listview = (ListView) findViewById(R.id.listview_detalle);
            adapter = new ListViewAdapterDetalle(CategoriasDetalle.this, arraylist);
            listview.setAdapter(adapter);
            mProgressDialog.dismiss();
        }else {
                mProgressDialog.dismiss();
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_dialog_no_encontrado);
                ImageView image = (ImageView) dialog.findViewById(R.id.no_disponible);
                image.setImageResource(R.drawable.nodisponible);
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ComercioNuevo.class);
                        intent.putExtra("idCategoria", posicion);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        }
    }
}
