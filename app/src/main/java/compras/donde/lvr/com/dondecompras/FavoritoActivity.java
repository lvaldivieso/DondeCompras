package compras.donde.lvr.com.dondecompras;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

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
public class FavoritoActivity extends AppCompatActivity {

    static String DESCRIPCION = "descripcion_esta";
    static String DIRECCION = "direccion_esta";
    static String ID = "id_categoria";
    static String IMG_PREVIA = "img_previa";
    static String NOMBRE_ESTABLECIMIENTO = "nombre_esta";
    static String LATITUD = "latitud_esta";
    static String LONGITUD = "longitud_esta";
    static String ID_COMERCIO = "id_comercio";
    static String ES_FAVORITO = "favorito";
    private static final String _URL = "http://tiny-alien.com.ar/api/v1/dondecompras/favorito";

    ListViewAdapterFavorito adapter;
    ArrayList<HashMap<String, String>> arraylist;
    JSONArray jsonarray;
    ListView listview;
    private ProgressDialog mProgressDialog;
    JSONParser jsonParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito);

        new DownloadJSON().execute();
    }

    public class DownloadJSON extends AsyncTask<String, String, String> {

        public DownloadJSON() {
        }
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(FavoritoActivity.this);
            mProgressDialog.setTitle("Donde Compras...");
            mProgressDialog.setMessage("Cargando...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        protected String doInBackground(String... args) {

            arraylist = new ArrayList();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("id_usuario", "3"));

            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(_URL, "GET", params);
            Log.d("Json resultado", json.toString());

            try {
                jsonarray = json.getJSONArray("Favoritos");
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap();
                    json = jsonarray.getJSONObject(i);
                    map.put("id_favorito", json.getString("id_favoritos"));
                    map.put("id_categoria", json.getString("id_categoria"));
                    map.put("id_comercio", json.getString("id_comercio"));
                    map.put("nombre_esta", json.getString("nombre"));
                    map.put("direccion_esta", json.getString("direccion"));
                    map.put("descripcion_esta", json.getString("descripcion"));
                    map.put("latitud_esta", json.getString("latitud"));
                    map.put("longitud_esta", json.getString("longitud"));
                    map.put("img_previa", json.getString("ruta_imagen"));
                    map.put("favorito", json.getString("es_favorito"));
                    arraylist.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String args) {
            listview = (ListView) findViewById(R.id.listview_favorito);
            adapter = new ListViewAdapterFavorito(FavoritoActivity.this, arraylist);
            listview.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }
}
