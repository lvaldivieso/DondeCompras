package compras.donde.lvr.com.dondecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    EditText nombre, calle, telefono, descripcion;
    Button guardar;
    Spinner localidades;
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
        localidades = (Spinner) findViewById(R.id.SpinnerLocalidades);
        telefono = (EditText) findViewById(R.id.NC_telefono);
        descripcion = (EditText) findViewById(R.id.NC_descripcion);
        guardar = (Button) findViewById(R.id.NC_guardar);

        guardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                guardarComercio();
            }
        });

        ArrayAdapter adapter = ArrayAdapter.createFromResource( this, R.array.localidades , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        localidades.setAdapter(adapter);
        localidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View vies,
                                       int position, long id) {
                String barrio = String.valueOf(localidades.getSelectedItem());
                Toast.makeText(getApplicationContext(), barrio, Toast.LENGTH_SHORT).show();
                // Aquí se codifica la lógica que se ejecutará al seleccionar un elemento del Spinner.
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {

            }
        });
    }

    public void guardarComercio(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id_categoria", idCategoria);
        params.put("nombre", nombre.getText().toString());
        params.put("calle",calle.getText().toString());
       // params.put("localidad",localidad.getText().toString());
        params.put("telefono",telefono.getText().toString());
        params.put("descripcion",descripcion.getText().toString());
        params.put("latitud","0");
        params.put("longitud","0");
        aq.ajax(_URL, params, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d("json", json.toString());
                Toast.makeText(getApplicationContext(), "Nuevo Comercio Añadido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
