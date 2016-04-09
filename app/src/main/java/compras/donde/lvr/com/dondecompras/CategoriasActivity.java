package compras.donde.lvr.com.dondecompras;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoriasActivity extends AppCompatActivity {

    JSONArray jsonarray;
    ListView listview;
    ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    private static final String _URL = "http://tiny-alien.com.ar/api/v1/dondecompras/categoria";
    static String CATEGORIA = "categoria";
    static String IMG_PREVIA = "img_previa";
    static String ID_CATEGORIAS ="id_categoria";
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        new DownloadJSON().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_categorias, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.action_settings) {
            intent = new Intent(this, ConfiguracionActivity.class);
            startActivity(intent);
        }else{
        if(id == R.id.action_favorite){
            intent = new Intent (this, FavoritoActivity.class);
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class DownloadJSON extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(CategoriasActivity.this);
            mProgressDialog.setTitle("Donde Compras...");
            mProgressDialog.setMessage("Cargando...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            arraylist = new ArrayList<HashMap<String, String>>();
            List params = new ArrayList();
            //params.add(new BasicNameValuePair("",""));
            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(_URL, "GET",
                    params);
            Log.d("Json resultado", json.toString());
            try {
                jsonarray = json.getJSONArray("Categorias");
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    json = jsonarray.getJSONObject(i);
                    map.put("categoria", json.getString("nombre"));
                    map.put("img_previa", json.getString("ruta_imagen"));
                    map.put("id_categoria", json.getString("id_categoria"));
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
            adapter = new ListViewAdapter(CategoriasActivity.this, arraylist);
            listview.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }
}