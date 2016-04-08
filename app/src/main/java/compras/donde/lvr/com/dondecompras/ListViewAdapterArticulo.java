package compras.donde.lvr.com.dondecompras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Leonardo on 28/01/2016.
 */
public class ListViewAdapterArticulo extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapterArticulo(Context context,
                                   ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);
    }

    public ListViewAdapterArticulo() {
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

        TextView nombre;
        TextView marca;
      //  TextView descripcion;
        TextView valor;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.fragment_comercio_detalle, parent, false);
        // Get the position
        resultp = data.get(position);

        nombre = (TextView) itemView.findViewById(R.id.nombre_art);
        marca = (TextView) itemView.findViewById(R.id.marca_art);
     //   descripcion = (TextView) itemView.findViewById(R.id.descripcion_art);
        valor = (TextView) itemView.findViewById(R.id.valor_art);

        nombre.setText(resultp.get(ComercioMapaActivity.NOMBRE));
        marca.setText(resultp.get(ComercioMapaActivity.MARCA));
    //    descripcion.setText(resultp.get(ComercioMapaActivity.DESCRIPCION));
        valor.setText(resultp.get(ComercioMapaActivity.VALOR));

      return itemView;
    }
}
