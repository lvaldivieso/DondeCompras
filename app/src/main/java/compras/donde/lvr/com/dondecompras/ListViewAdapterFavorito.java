package compras.donde.lvr.com.dondecompras;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Leonardo on 28/01/2016.
 */
public class ListViewAdapterFavorito extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();
    ProgressDialog mProgressDialog;
    JSONParser jsonParser = new JSONParser();
    ArrayList<HashMap<String, String>> arraylist;
    int pos;

    private static final String _URL = "http://tiny-alien.com.ar/api/v1/dondecompras/favorito";


    public ListViewAdapterFavorito(Context context,
                                   ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);

    }

    public ListViewAdapterFavorito() {

    }

    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        TextView nombre_establecimiento;
        TextView direccion;
        ImageView img_previa;
        TextView descripcion;

        ImageButton borrarFav;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.activity_favorito_lista, parent, false);

        resultp = data.get(position);


        nombre_establecimiento = (TextView) itemView.findViewById(R.id.establecimiento);
        img_previa= (ImageView) itemView.findViewById(R.id.imagen_detalle);
        direccion = (TextView) itemView.findViewById(R.id.direccion_establecimiento);
        descripcion = (TextView) itemView.findViewById(R.id.descripcion_esta);

        borrarFav = (ImageButton) itemView.findViewById(R.id.imageButton);
        borrarFav.setTag(new Integer(position));
        borrarFav.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View view)
            {
                new BorrarFavorito().execute();
                pos = position;
            }
        });


        nombre_establecimiento.setText(resultp.get(CategoriasDetalle.NOMBRE_ESTABLECIMIENTO));
        direccion.setText(resultp.get(CategoriasDetalle.DIRECCION));
        descripcion.setText(resultp.get(CategoriasDetalle.DESCRIPCION));
        imageLoader.DisplayImage(resultp.get(CategoriasDetalle.IMG_PREVIA), img_previa);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                resultp = data.get(position);
                Intent intent = new Intent(context, ComercioMapaActivity.class);
                intent.putExtra("id_comercio", resultp.get(FavoritoActivity.ID));
                intent.putExtra("comercio", resultp.get(FavoritoActivity.NOMBRE_ESTABLECIMIENTO));
                intent.putExtra("direccion", resultp.get(FavoritoActivity.DIRECCION));
                intent.putExtra("descripcion", resultp.get(FavoritoActivity.DESCRIPCION));
                intent.putExtra("latitud", resultp.get(FavoritoActivity.LATITUD));
                intent.putExtra("longitud", resultp.get(FavoritoActivity.LONGITUD));
                intent.putExtra("id_comercio", resultp.get(FavoritoActivity.ID_COMERCIO));
                intent.putExtra("favorito", resultp.get(FavoritoActivity.ES_FAVORITO));
                context.startActivity(intent);
            }
        });
        return itemView;
    }
    private class BorrarFavorito extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("Donde Compras...");
            mProgressDialog.setMessage("Borrando...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            arraylist = new ArrayList<HashMap<String, String>>();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("id_comercio",resultp.get(FavoritoActivity.ID_COMERCIO)));
            params.add(new BasicNameValuePair("id_usuario", FavoritoActivity.id_usuario));
            Log.d("request!", "starting");
            JSONObject json = jsonParser.makeHttpRequest(_URL, "DELETE", params);
            Log.d("Json resultado", json.toString());

            return null;
        }

        @Override
        protected void onPostExecute(String args) {
            mProgressDialog.dismiss();
            data.remove(pos);
            notifyDataSetChanged();
            CustomToast miToast = new CustomToast(context, Toast.LENGTH_LONG);
            miToast.show("Favorito Borrado");
        }
    }

}
