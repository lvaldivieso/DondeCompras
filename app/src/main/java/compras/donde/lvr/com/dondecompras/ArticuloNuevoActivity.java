package compras.donde.lvr.com.dondecompras;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo on 21/02/2016.
 */
public class ArticuloNuevoActivity extends AppCompatActivity {
    JSONArray jsonarray;
    ProgressDialog mProgressDialog;
    Spinner clase;
    Spinner tipo;
    Spinner marca;
    Button guardar;
    ArrayList<String> listaclases;
    ArrayList<String> idclase;
    ArrayList<String> listatipo;
    ArrayList<String> listamarca;
    ArrayList<HashMap<String, String>> arraylist;
    String itemClase;
    EditText precio;
    int posicion_lista_clases;
    String posicion_lista_tipo, idCategoria, idComercio;
    private static final String _URL = "http://tiny-alien.com.ar/api/v1/dondecompras/clase";
    private static final String _URL_tipo = "http://tiny-alien.com.ar/api/v1/dondecompras/tipo";
    private static final String _URL_marca = "http://tiny-alien.com.ar/api/v1/dondecompras/marca";
    private static final String _URL_nuevoprecio = "http://tiny-alien.com.ar/api/v1/dondecompras/nuevoprecio";
    AQuery aq = new AQuery(this);
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo_nuevo);

        guardar = (Button) findViewById(R.id.guardar_articulo);
        clase = (Spinner) findViewById(R.id.lista_clase);
        tipo = (Spinner) findViewById(R.id.lista_tipo);
        marca = (Spinner) findViewById(R.id.lista_marca);
        listaclases = new ArrayList<String>();
        idclase = new ArrayList<String>();
        listatipo = new ArrayList<String>();
        listamarca = new ArrayList<String>();
        idCategoria = (getIntent().getStringExtra("idCategoria"));
        idComercio = (getIntent().getStringExtra("idComercio"));
        precio = (EditText) findViewById(R.id.precio);
        new DownloadClase().execute();
        guardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                guardarArticulo();
            }
        });

    }

    private class DownloadClase extends AsyncTask<String, String, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ArticuloNuevoActivity.this);

        }

        @Override
        protected Void doInBackground(String... args) {
            arraylist = new ArrayList<HashMap<String, String>>();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("id_categoria",idCategoria));
            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(_URL, "GET",
                    params);
            Log.d("Json resultado", json.toString());
            try {
                jsonarray = json.getJSONArray("Clase");
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    json = jsonarray.getJSONObject(i);
                    map.put("id_clase", json.getString("id_clase"));
                    map.put("clase", json.getString("nombre"));
                    arraylist.add(map);
                    listaclases.add(json.optString("nombre"));
                }

            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            clase
                    .setAdapter(new ArrayAdapter<String>(ArticuloNuevoActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            listaclases));
            clase
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            itemClase = String.valueOf(clase.getSelectedItem());
                            Toast.makeText(getApplicationContext(), itemClase, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), String.valueOf(listaclases), Toast.LENGTH_SHORT).show();
                            posicion_lista_clases = position + 1;
                            new DownloadTipos().execute();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
        }
    }
    private class DownloadTipos extends AsyncTask<String, String, Void> {
        @Override
        protected void onPreExecute() {
            listatipo.clear();
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ArticuloNuevoActivity.this);

        }

        @Override
        protected Void doInBackground(String... args) {
            arraylist = new ArrayList<HashMap<String, String>>();
            List params = new ArrayList();
          //  params.add(new BasicNameValuePair("id_clase", String.valueOf(posicion_lista_clases)));
            params.add(new BasicNameValuePair("id_clase", itemClase));
            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(_URL_tipo, "GET",
                    params);
            Log.d("Json resultado", json.toString());
            try {
                jsonarray = json.getJSONArray("Tipo");
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    json = jsonarray.getJSONObject(i);
                    map.put("id_clase", json.getString("id_clase"));
                    map.put("tipo", json.getString("nombre"));
                    map.put("id_tipo", json.getString("id_tipo"));

                    arraylist.add(map);
                    listatipo.add(json.optString("nombre"));
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            tipo
                    .setAdapter(new ArrayAdapter<String>(ArticuloNuevoActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            listatipo));
            tipo
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            posicion_lista_tipo = String.valueOf(tipo.getSelectedItem());
                            new DownloadMarcas().execute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
        }
    }
    private class DownloadMarcas extends AsyncTask<String, String, Void> {
        @Override
        protected void onPreExecute() {
            listamarca.clear();
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ArticuloNuevoActivity.this);

        }

        @Override
        protected Void doInBackground(String... args) {
            arraylist = new ArrayList<HashMap<String, String>>();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("nombre_marca", String.valueOf(posicion_lista_tipo)));
            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(_URL_marca, "GET",
                    params);
            Log.d("Json resultado", json.toString());
            try {
                jsonarray = json.getJSONArray("Marca");
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    json = jsonarray.getJSONObject(i);
                    map.put("id_marca", json.getString("id_marca"));
                    map.put("marca", json.getString("nombre"));
                    map.put("id_tipo", json.getString("id_tipo"));

                    arraylist.add(map);
                    listamarca.add(json.optString("nombre"));
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            marca
                    .setAdapter(new ArrayAdapter<String>(ArticuloNuevoActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            listamarca));
            marca
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
        }
    }
    public void guardarArticulo(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id_comercio", idComercio);
        params.put("id_tipo", 4);
        params.put("valor",precio.getText().toString());
        aq.ajax(_URL_nuevoprecio, params, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                Log.d("json", json.toString());
                Toast.makeText(getApplicationContext(), "Nuevo Precio Añadido", Toast.LENGTH_SHORT).show();
            }
        });

    }

}