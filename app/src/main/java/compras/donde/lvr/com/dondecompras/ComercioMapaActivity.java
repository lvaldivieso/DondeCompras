package compras.donde.lvr.com.dondecompras;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo on 25/02/2016.
 */
public class ComercioMapaActivity extends Activity {

    boolean mShowMap;
    GoogleMap mMap;
    TextView comercio, descripcion_e, direccion_e, localidad, telefono ;
    ImageButton favorito, favorito_no;
    Double latitud, longitud;
    ProgressDialog mProgressDialog;
    JSONParser jsonParser = new JSONParser();
    JSONArray jsonarray;
    ListView listview;
    ListViewAdapterArticulo adapter;
    String idComercio;
    String idCategoria;
    static String NOMBRE = "nombre";
    static String TIPO = "tipo";
    static String DESCRIPCION = "descripcion";
    static String VALOR = "valor";
    private static final String _URL = "http://tiny-alien.com.ar/api/v1/dondecompras/favorito";
    private static final String _URL1 = "http://tiny-alien.com.ar/api/v1/dondecompras/lista_articulos";
    ArrayList<HashMap<String, String>> arraylist;
    AQuery aq = new AQuery(this);
    String id_usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinator_activity_comercio_mapa);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_articulo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //          .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), ArticuloNuevoActivity.class);
                intent.putExtra("idCategoria", idCategoria);
                intent.putExtra("idComercio", idComercio);
                startActivity(intent);
            }
        });

        comercio = (TextView) findViewById(R.id.txt_nombre_comercio);
        localidad = (TextView) findViewById(R.id.localidad);
        telefono = (TextView) findViewById(R.id.telefono);
        direccion_e = (TextView) findViewById(R.id.txt_direccion_e);
        descripcion_e = (TextView) findViewById(R.id.txt_descripcion_e);
        favorito = (ImageButton) findViewById(R.id.btn_favorito);
        favorito_no = (ImageButton) findViewById(R.id.btn_favorito_no);

        comercio.setText(getIntent().getStringExtra("comercio"));
        localidad.setText(getIntent().getStringExtra("localidad"));
        telefono.setText(getIntent().getStringExtra("telefono"));
        direccion_e.setText(getIntent().getStringExtra("direccion"));
        descripcion_e.setText(getIntent().getStringExtra("descripcion"));
        String esfavorito = (getIntent().getStringExtra("favorito"));
        idComercio = (getIntent().getStringExtra("id_comercio"));
        idCategoria = (getIntent().getStringExtra("id_categoria"));
        //Toast.makeText(getApplicationContext(), idCategoria, Toast.LENGTH_SHORT).show();

        if (esfavorito.equals("null")){
            favorito.setVisibility(View.INVISIBLE);
            favorito_no.setVisibility(View.VISIBLE);
        }else{
            favorito_no.setVisibility(View.INVISIBLE);
            favorito.setVisibility(View.VISIBLE);
        }
        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BorrarFavorito().execute();
                favorito.setVisibility(View.INVISIBLE);
                favorito_no.setVisibility(View.VISIBLE);
            }
        });
        favorito_no.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v){
                GuardarFavorito();
                favorito_no.setVisibility(View.INVISIBLE);
                favorito.setVisibility(View.VISIBLE);
            }
        });

        SharedPreferences DondeComprasPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        id_usuario = (DondeComprasPreferencias.getString("id_usuario", ""));

        mShowMap = GooglePlayServiceUtility.isPlayServiceAvailable(this) && initMap();
        mostrarPosicionComercio();
        new listarArticulosComercio().execute();
    }
    private class BorrarFavorito extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ComercioMapaActivity.this);
            mProgressDialog.setTitle("Donde Compras...");
            mProgressDialog.setMessage("Borrando...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            arraylist = new ArrayList<HashMap<String, String>>();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("id_comercio", idComercio));
            params.add(new BasicNameValuePair("id_usuario",id_usuario));
            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(_URL, "DELETE",
                    params);
            Log.d("Json resultado", json.toString());
            return null;
        }

        @Override
        protected void onPostExecute(String args) {
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Favorito Borrado", Toast.LENGTH_SHORT).show();
        }
    }

    public void GuardarFavorito() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id_comercio", idComercio);
        params.put("id_usuario", id_usuario);
        aq.ajax(_URL, params, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d("json", json.toString());
                Toast.makeText(getApplicationContext(), "Favorito Añadido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean initMap() {
        if(mMap == null){
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
        }
        return (mMap != null);
    }

    private void mostrarPosicionComercio(){

        latitud  = Double.valueOf(getIntent().getStringExtra("latitud")).doubleValue();
        longitud = Double.valueOf(getIntent().getStringExtra("longitud")).doubleValue();

        LatLng latLng = new LatLng(latitud, longitud);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,16);
        mMap.moveCamera(update);
        String markerTitle = (getIntent().getStringExtra("comercio"));
        mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(markerTitle)
                        .anchor(.5f, .5f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin1))
        );
    }

    private class listarArticulosComercio extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ComercioMapaActivity.this);
            mProgressDialog.setTitle("Donde Compras...");
            mProgressDialog.setMessage("Cargando...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            arraylist = new ArrayList<HashMap<String, String>>();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("id_comercio",idComercio));
            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(_URL1, "GET",
                    params);
            Log.d("Json resultado", json.toString());
            try {
                jsonarray = json.getJSONArray("lista_Articulos");
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    json = jsonarray.getJSONObject(i);
                    map.put("nombre", json.getString("nombre"));
                    map.put("valor", json.getString("valor"));
                    map.put("descripcion", json.getString("descripcion"));
                    map.put("tipo", json.getString("tipo"));
                    arraylist.add(map);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String args) {
            listview = (ListView) findViewById(R.id.listview);
            adapter = new ListViewAdapterArticulo(ComercioMapaActivity.this, arraylist);
            listview.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }
    }
