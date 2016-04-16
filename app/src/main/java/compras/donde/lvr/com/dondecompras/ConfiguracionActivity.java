package compras.donde.lvr.com.dondecompras;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.androidquery.AQuery;


/**
 * Created by Leonardo on 17/02/2016.
 */
public class ConfiguracionActivity extends AppCompatActivity {
    int Pro = 100;
    private CheckBox notificacion;
    private SeekBar distancia;
    private Button guardar;
    private TextView cuadras, usuario, email;
    ImageView imagen;
    RoundImage roundedImage;
    AQuery aq = new AQuery(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        notificacion = (CheckBox) findViewById(R.id.check_notificarme);
        distancia = (SeekBar) findViewById(R.id.barra_distancia);
        guardar = (Button) findViewById(R.id.btn_guardar);
        cuadras = (TextView) findViewById(R.id.txt_distancia_valor);
        usuario = (TextView) findViewById(R.id.Usuario);
        email = (TextView) findViewById(R.id.Email_usuario);
        imagen = (ImageView) findViewById(R.id.imagen_foto);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.temp_img);
        roundedImage = new RoundImage(bm);
        imagen.setImageDrawable(roundedImage);

                distancia.setMax(2000);
        distancia.incrementProgressBy(100);
        distancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Pro = progress;
                Pro = Pro / 100;
                Pro = Pro * 100;
                cuadras.setText(String.valueOf(Pro));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        CargarPreferencias();
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarPreferencias();
            }
        });

    }

    public void CargarPreferencias() {
        SharedPreferences DondeComprasPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        notificacion.setChecked(DondeComprasPreferencias.getBoolean("checked", false));
        distancia.setProgress(DondeComprasPreferencias.getInt("metros", 1));
        cuadras.setText(DondeComprasPreferencias.getString("distancia", "1"));
        usuario.setText(DondeComprasPreferencias.getString("Usuario", ""));
        email.setText(DondeComprasPreferencias.getString("Email", ""));
        AQuery aq = new AQuery(this);
        aq.id(R.id.imagen_foto).image(DondeComprasPreferencias.getString("Foto", ""), false, false);
    }

    public void GuardarPreferencias(){
        SharedPreferences DondeComprasPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = DondeComprasPreferencias.edit();
        boolean valor = notificacion.isChecked();
        int metros = distancia.getProgress();
        String distancia_m = cuadras.getText().toString();
        editor.putBoolean("checked", valor);
        editor.putInt("metros", metros);
        editor.putString("distancia", distancia_m);
        editor.commit();
    }

}
