package compras.donde.lvr.com.dondecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Leonardo on 09/04/2016.
 */
public class ComercioNuevo extends AppCompatActivity {

    String idCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_comercio);

        idCategoria = (getIntent().getStringExtra("idCategoria"));
        Toast.makeText(getApplicationContext(), idCategoria, Toast.LENGTH_SHORT).show();

    }
}
