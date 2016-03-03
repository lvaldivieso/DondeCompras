package compras.donde.lvr.com.dondecompras;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;

public class LoginGoogleActivity extends ActionBarActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener{

    private static final int PROFILE_PIC_SIZE = 400;
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mShouldResolve;
    private ConnectionResult connectionResult;
    private SignInButton signInButton;
    private Button signOutButton;
    private TextView tvName, tvMail, tvNotSignedIn;
    private ImageView imgProfilePic;
    private LinearLayout viewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google);

        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signOutButton = (Button) findViewById(R.id.sign_out_button);
        tvName = (TextView) findViewById(R.id.tvName);
        tvMail = (TextView) findViewById(R.id.tvMail);
        tvNotSignedIn = (TextView) findViewById(R.id.notSignedIn_tv);
        viewContainer = (LinearLayout) findViewById(R.id.text_view_container);

        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);

        SharedPreferences DondeComprasPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);

        if (DondeComprasPreferencias.getString("Usuario", "").isEmpty()) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .build();

        } else {
            //Toast.makeText(getApplicationContext(),
            //        "En este momento estas Logueado " + (DondeComprasPreferencias.getString("Usuario", "")), Toast.LENGTH_LONG).show();
            CustomToast miToast = new CustomToast(getApplicationContext(), Toast.LENGTH_LONG);
            miToast.show("En este momento estas Logueado "+ (DondeComprasPreferencias.getString("Usuario", "")));

            Intent openMainActivity = new Intent(getApplicationContext(), CategoriasActivity.class);
            startActivity(openMainActivity);
            finish();
        }
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    private void resolveSignInError() {
        if (connectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                connectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }
        if (!mIntentInProgress) {
            connectionResult = result;
            if (mShouldResolve) {
                resolveSignInError();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mShouldResolve = false;
            }
            mIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }
    @Override
    public void onConnected(Bundle arg0) {
        mShouldResolve = false;
           try {
                if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                    Person person = Plus.PeopleApi
                            .getCurrentPerson(mGoogleApiClient);
                    String personName = person.getDisplayName();
                    String personPhotoUrl = person.getImage().getUrl();
                    String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                    tvName.setText(personName);
                    tvMail.setText(email);

                    personPhotoUrl = personPhotoUrl.substring(0,
                            personPhotoUrl.length() - 2)
                            + PROFILE_PIC_SIZE;

                    new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

                    SharedPreferences DondeComprasPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = DondeComprasPreferencias.edit();
                    String nombre_usuario = personName;
                    String email_usuario = email;
                    String foto = personPhotoUrl;
                    editor.putString("Usuario", nombre_usuario);
                    editor.putString("Email", email_usuario);
                    editor.putString("Foto", personPhotoUrl);
                    editor.commit();

                    Toast.makeText(getApplicationContext(),
                            "En este momento estas Logueado " + personName, Toast.LENGTH_LONG).show();

                    Intent openMainActivity = new Intent(getApplicationContext(), CategoriasActivity.class);
                    startActivity(openMainActivity);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "No podemos ver tu información", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            signOutUI();
    }
    private void signOutUI() {
        signInButton.setVisibility(View.GONE);
        tvNotSignedIn.setVisibility(View.GONE);
        signOutButton.setVisibility(View.VISIBLE);
        viewContainer.setVisibility(View.VISIBLE);
    }
    private void signInUI() {
        signInButton.setVisibility(View.VISIBLE);
        tvNotSignedIn.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.GONE);
        viewContainer.setVisibility(View.GONE);
    }
    private void getProfileInformation() {

    }
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        signInUI();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                onSignInClicked();
                break;
            case R.id.sign_out_button:
                onSignOutClicked();
                break;
        }
    }
    private void onSignInClicked() {
        if (!mGoogleApiClient.isConnecting()) {
            mShouldResolve = true;
            resolveSignInError();
        }
    }
    public void onSignOutClicked() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            signInUI();
        }
    }
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
