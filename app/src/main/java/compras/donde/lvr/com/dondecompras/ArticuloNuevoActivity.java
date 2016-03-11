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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Leonardo on 21/02/2016.
 */
public class ArticuloNuevoActivity extends AppCompatActivity {
    JSONArray jsonarray;
    ListView listview;
    ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    Spinner clase;
    Spinner tipo;
    Spinner marca;
    ArrayList<String> listaclases;
    ArrayList<String> listatipo;
    ArrayList<String> listamarca;
    ArrayList<HashMap<String, String>> arraylist;
    int posicion_lista_clases;
    String posicion_lista_tipo;
    private static final String _URL = "http://tiny-alien.com.ar/api/v1/dondecompras/clase";
    private static final String _URL_tipo = "http://tiny-alien.com.ar/api/v1/dondecompras/tipo";
    private static final String _URL_marca = "http://tiny-alien.com.ar/api/v1/dondecompras/marca";

    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo_nuevo);


        clase = (Spinner) findViewById(R.id.lista_clase);
        tipo = (Spinner) findViewById(R.id.lista_tipo);
        marca = (Spinner) findViewById(R.id.lista_marca);
        new DownloadJSON().execute();
        listaclases = new ArrayList<String>();
        listatipo = new ArrayList<String>();
        listamarca = new ArrayList<String>();
    }

    private class DownloadJSON extends AsyncTask<String, String, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ArticuloNuevoActivity.this);

        }

        @Override
        protected Void doInBackground(String... args) {
            arraylist = new ArrayList<HashMap<String, String>>();
            List params = new ArrayList();
            //params.add(new BasicNameValuePair("",""));
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
                            String item = String.valueOf(clase.getSelectedItem());
                            Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
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
            params.add(new BasicNameValuePair("", String.valueOf(posicion_lista_clases)));
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
            params.add(new BasicNameValuePair("", String.valueOf(posicion_lista_tipo)));
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
}