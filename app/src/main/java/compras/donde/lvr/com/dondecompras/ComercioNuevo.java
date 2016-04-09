package compras.donde.lvr.com.dondecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leonardo on 09/04/2016.
 */
public class ComercioNuevo extends AppCompatActivity {

    String idCategoria;
    EditText nombre, calle, localidad, telefono, descripcion;
    Button guardar;
    AQuery aq = new AQuery(this);
    private static final String _URL = "http://tiny-alien.com.ar/api/v1/dondecompras/comercio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_comercio);

        idCategoria = (getIntent().getStringExtra("idCategoria"));
        //Toast.makeText(getApplicationContext(), idCategoria, Toast.LENGTH_SHORT).show();
        nombre = (EditText) findViewById(R.id.NC_nombre);
        calle = (EditText) findViewById(R.id.NC_calle);
        localidad = (EditText) findViewById(R.id.NC_localidad);
        telefono = (EditText) findViewById(R.id.NC_telefono);
        descripcion = (EditText) findViewById(R.id.NC_descripcion);
        guardar = (Button) findViewById(R.id.NC_guardar);

        guardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                guardarComercio();
            }
        });
    }
    public void guardarComercio(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id_categoria", idCategoria);
        params.put("nombre", nombre.getText().toString());
        params.put("calle",calle.getText().toString());
        params.put("localidad",localidad.getText().toString());
        params.put("telefono",telefono.getText().toString());
        params.put("descripcion",descripcion.getText().toString());
        aq.ajax(_URL, params, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                Log.d("json", json.toString());
                Toast.makeText(getApplicationContext(), "Nuevo Comercio Añadido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
