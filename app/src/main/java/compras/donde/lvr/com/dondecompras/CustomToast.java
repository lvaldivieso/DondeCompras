package compras.donde.lvr.com.dondecompras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Leonardo on 02/03/2016.
 */
public class CustomToast extends Toast {

    private Context context;

    /**
     * constructor que recibe el contexto y la duración del Toast y
     * que se lo pasamos al método setDuracion de la clase de la que hereda
     */

    public CustomToast(Context cont, int duration) {
        super(cont);
        context = cont;
        this.setDuration(duration);
    }

    /**
     * método donde inflamos nuestro layout personalizado y le asignamos
     * el texto que recibirá cuando instanciemos el objeto.
     * Además llamamos al método show de la clase de la que hereda para que se muestre por pantalla
     */
    public void show(CharSequence text) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = (View) li.inflate(R.layout.custom_layout_toast, null);

        TextView tv = (TextView) vi.findViewById(R.id.text_toast);
        this.setView(vi);
        tv.setText(text);
        super.show();
    }

    {
    }
}