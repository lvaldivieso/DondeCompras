package compras.donde.lvr.com.dondecompras;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Leonardo on 28/01/2016.
 */
public class ListViewAdapterDetalle extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public ListViewAdapterDetalle(Context context,
                                  ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);
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

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.activity_categorias_lista_detalle_lista, parent, false);
        // Get the position
        resultp = data.get(position);

        nombre_establecimiento = (TextView) itemView.findViewById(R.id.establecimiento);
        img_previa= (ImageView) itemView.findViewById(R.id.imagen_detalle);
        direccion = (TextView) itemView.findViewById(R.id.direccion_establecimiento);
        descripcion = (TextView) itemView.findViewById(R.id.descripcion_esta);

        nombre_establecimiento.setText(resultp.get(CategoriasDetalle.NOMBRE_ESTABLECIMIENTO));
        direccion.setText(resultp.get(CategoriasDetalle.DIRECCION));
        descripcion.setText(resultp.get(CategoriasDetalle.DESCRIPCION));
        imageLoader.DisplayImage(resultp.get(CategoriasDetalle.IMG_PREVIA), img_previa);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                resultp = data.get(position);
                Intent intent = new Intent(context, ComercioMapaActivity.class);
                intent.putExtra("id_comercio", resultp.get(CategoriasDetalle.ID));
                intent.putExtra("comercio", resultp.get(CategoriasDetalle.NOMBRE_ESTABLECIMIENTO));
                intent.putExtra("direccion", resultp.get(CategoriasDetalle.DIRECCION));
                intent.putExtra("descripcion", resultp.get(CategoriasDetalle.DESCRIPCION));
                intent.putExtra("latitud", resultp.get(CategoriasDetalle.LATITUD));
                intent.putExtra("longitud", resultp.get(CategoriasDetalle.LONGITUD));
                context.startActivity(intent);
            }
        });
        return itemView;
    }
}
