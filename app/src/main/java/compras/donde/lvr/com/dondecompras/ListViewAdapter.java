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
public class ListViewAdapter extends BaseAdapter {

    // Declaro Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapter(Context context,
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

        TextView categoria;
        ImageView img_previa;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.activity_categorias_lista, parent, false);
        // Get the position
        resultp = data.get(position);

        categoria = (TextView) itemView.findViewById(R.id.categoria);
        img_previa= (ImageView) itemView.findViewById(R.id.list_image);

        categoria.setText(resultp.get(CategoriasActivity.CATEGORIA));

        imageLoader.DisplayImage(resultp.get(CategoriasActivity.IMG_PREVIA), img_previa);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                resultp = data.get(position);
                Intent intent = new Intent(context, CategoriasDetalle.class);
                intent.putExtra("posicion", resultp.get(CategoriasActivity.ID_CATEGORIAS));
                context.startActivity(intent);
            }
        });
        return itemView;
    }
}
