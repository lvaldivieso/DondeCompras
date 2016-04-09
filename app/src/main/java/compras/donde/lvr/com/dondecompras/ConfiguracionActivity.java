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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;

/**
 * Created by Leonardo on 17/02/2016.
 */
public class ConfiguracionActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<People.LoadPeopleResult> {
    int Pro = 100;
    private CheckBox notificacion;
    private SeekBar distancia;
    private Button guardar, salir;
    private TextView cuadras, usuario, email;
    ImageView imagen;
    RoundImage roundedImage;
    AQuery aq = new AQuery(this);
    private GoogleApiClient mGoogleApiClient;
    private boolean mShouldResolve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        mGoogleApiClient.connect();

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
        salir = (Button) findViewById(R.id.sign_out_button_configuracion);

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

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                    onSignOutClicked();
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

    @Override
    public void onConnected(Bundle arg0) {
        mShouldResolve = false;
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        onSignOutClicked();
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
    }

    public void onSignOutClicked() {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onResult(People.LoadPeopleResult arg0) {
    }
}
