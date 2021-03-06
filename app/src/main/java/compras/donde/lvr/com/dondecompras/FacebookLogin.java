package compras.donde.lvr.com.dondecompras;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacebookLogin extends AppCompatActivity {
    CallbackManager callbackManager;
 //   Button share,details;
    ShareDialog shareDialog;
    LoginButton login;
    ProfilePictureView profile;
    Dialog details_dialog;
    TextView details_txt;
    int Pro = 100;
    private CheckBox notificacion;
    private SeekBar distancia;
    private Button guardar;
    private TextView cuadras, usuario, Email, notificarme, text_distancia;
    String logintrue = null;
    ArrayList<HashMap<String, String>> arraylist;
    JSONParser jsonParser = new JSONParser();
    JSONArray jsonarray;
    String id_usuario;
    String personName;
    String email;
    private static final String _URL = "http://tiny-alien.com.ar/api/v1/dondecompras/usuario";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.facebook_login);
        notificarme = (TextView) findViewById(R.id.txt_notificarme);
        notificacion = (CheckBox) findViewById(R.id.check_notificarme);
        distancia = (SeekBar) findViewById(R.id.barra_distancia);
        guardar = (Button) findViewById(R.id.btn_guardar);
        cuadras = (TextView) findViewById(R.id.txt_distancia_valor);
        text_distancia = (TextView) findViewById(R.id.txt_distancia_maxima);
        callbackManager = CallbackManager.Factory.create();
        login = (LoginButton)findViewById(R.id.login_button);
        profile = (ProfilePictureView)findViewById(R.id.picture);
        shareDialog = new ShareDialog(this);
       // share = (Button)findViewById(R.id.share);
      //  details = (Button)findViewById(R.id.details);
        usuario = (TextView) findViewById(R.id.txt_fbUser);
        Email = (TextView) findViewById(R.id.txt_fbEmail);
        login.setReadPermissions("public_profile email");
        Email.setVisibility(View.INVISIBLE);
        usuario.setVisibility(View.INVISIBLE);
//        share.setVisibility(View.INVISIBLE);
        //details.setVisibility(View.INVISIBLE);
        details_dialog = new Dialog(this);
        details_dialog.setContentView(R.layout.dialog_details);
        details_dialog.setTitle("Details");
        details_txt = (TextView)details_dialog.findViewById(R.id.details);
       /* details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                details_dialog.show();
            }
        });*/
        logintrue = (getIntent().getStringExtra("logintrue"));
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
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        if(AccessToken.getCurrentAccessToken() != null){
            RequestData();
            usuario.setVisibility(View.VISIBLE);
            usuario.setText(personName);
            Email.setVisibility(View.VISIBLE);
            Email.setText(email);
            notificacion.setVisibility(View.VISIBLE);
            distancia.setVisibility(View.VISIBLE);
            guardar.setVisibility(View.VISIBLE);
            cuadras.setVisibility(View.VISIBLE);
            notificarme.setVisibility(View.VISIBLE);
            text_distancia.setVisibility(View.VISIBLE);
     //       share.setVisibility(View.VISIBLE);
            //details.setVisibility(View.VISIBLE);
            CargarPreferencias();
            if(logintrue == null) {
                Intent openMainActivity = new Intent(getApplicationContext(), CategoriasActivity.class);
                startActivity(openMainActivity);
                finish();
            }
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccessToken.getCurrentAccessToken() != null) {
                  //  share.setVisibility(View.INVISIBLE);
                    //details.setVisibility(View.INVISIBLE);
                    usuario.setVisibility(View.INVISIBLE);
                    Email.setVisibility(View.INVISIBLE);
                    notificacion.setVisibility(View.INVISIBLE);
                    distancia.setVisibility(View.INVISIBLE);
                    guardar.setVisibility(View.INVISIBLE);
                    cuadras.setVisibility(View.INVISIBLE);
                    notificarme.setVisibility(View.INVISIBLE);
                    text_distancia.setVisibility(View.INVISIBLE);
                    BorrarPreferencias();
                    profile.setProfileId(null);
                }
            }
        });
     /*   share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent content = new ShareLinkContent.Builder().build();
                shareDialog.show(content);
            }
        });*/
        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(AccessToken.getCurrentAccessToken() != null){
                    RequestData();
                    notificacion.setVisibility(View.VISIBLE);
                    distancia.setVisibility(View.VISIBLE);
                    guardar.setVisibility(View.VISIBLE);
                    cuadras.setVisibility(View.VISIBLE);
                    notificarme.setVisibility(View.VISIBLE);
                    text_distancia.setVisibility(View.VISIBLE);

             //       share.setVisibility(View.VISIBLE);
                   // details.setVisibility(View.VISIBLE);

                  //  CustomToast miToast = new CustomToast(getApplicationContext(), Toast.LENGTH_LONG);
                  //  miToast.show("En este momento estas Logueado " + (DondeComprasPreferencias.getString("Usuario", "")));
                    Intent openMainActivity = new Intent(getApplicationContext(), CategoriasActivity.class);
                    startActivity(openMainActivity);
                    finish();
                }
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException exception) {
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarPreferencias();
            }
        });
    }
    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {
                JSONObject json = response.getJSONObject();
                try {
                    if(json != null){
                        String text = "<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")+"<br><br><b>Profile link :</b> "+json.getString("link");
                        details_txt.setText(Html.fromHtml(text));
                        profile.setProfileId(json.getString("id"));
                        personName = json.getString("name");
                        email = json.getString("email");
                        usuario.setVisibility(View.VISIBLE);
                        usuario.setText(personName);
                        Email.setVisibility(View.VISIBLE);
                        Email.setText(email);
                        new GuardarUsuario().execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void CargarPreferencias() {
        SharedPreferences DondeComprasPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        notificacion.setChecked(DondeComprasPreferencias.getBoolean("checked", false));
        distancia.setProgress(DondeComprasPreferencias.getInt("metros", 1));
        cuadras.setText(DondeComprasPreferencias.getString("distancia", "1"));
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
    public void BorrarPreferencias(){
        SharedPreferences DondeComprasPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = DondeComprasPreferencias.edit();
        editor.clear();
        editor.commit();

    }
    public class GuardarUsuario extends AsyncTask<String, String, String> {
        public GuardarUsuario() {
        }
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            arraylist = new ArrayList();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("nombre", personName));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("foto", "facebook"));

            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(_URL, "POST", params);
            Log.d("Json resultado", json.toString());

            try {
                jsonarray = json.getJSONArray("Usuario");
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap();
                    json = jsonarray.getJSONObject(i);
                    id_usuario = json.optString("id_usuario");
                    arraylist.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String args) {
            SharedPreferences DondeComprasPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = DondeComprasPreferencias.edit();
            editor.putString("id_usuario", id_usuario);
            editor.commit();
            CustomToast miToast = new CustomToast(getApplicationContext(), Toast.LENGTH_LONG);
            miToast.show("En este momento estas Logueado " + personName);
           /* Intent openMainActivity = new Intent(getApplicationContext(), CategoriasActivity.class);
            startActivity(openMainActivity);
            finish();*/
        }
    }

}
