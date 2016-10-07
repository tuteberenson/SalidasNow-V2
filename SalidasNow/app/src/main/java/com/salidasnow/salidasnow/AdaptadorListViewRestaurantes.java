package com.salidasnow.salidasnow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by berenson on 26/09/2016.
 */

public class AdaptadorListViewRestaurantes extends ArrayAdapter<Restaurantes>{
    ArrayList<Restaurantes> restaurants;
    Context context;
    Usuarios usuarioActual;
    Integer DesdeDondeLlamo; // si DeDondeLlamo = 1 estoy llamando desde likeados si es = 2 lo estoy llamando de otro lado


    public AdaptadorListViewRestaurantes(Context context,int resource, ArrayList<Restaurantes> restaurants, Usuarios usuario, Integer desdeDondeLlamo) {
        super(context,resource,restaurants);
        this.context = context;
        this.restaurants = restaurants;
        this.usuarioActual=usuario;
        this.DesdeDondeLlamo=desdeDondeLlamo;
    }

  /*  @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int i) {
        return restaurants.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            if (DesdeDondeLlamo==1)
            {
                convertView = inflater.inflate(R.layout.list_item_restaurants_likeados, parent, false);
            }
            else {
                convertView = inflater.inflate(R.layout.list_item_restaurant, parent, false);
            }
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        Log.d("Contexto",context+"");
        Restaurantes p = this.restaurants.get(position);
        holder.nombreTV.setText(p.get_Nombre());
        holder.direccionTV.setText(p.get_Direccion());
        switch (p.get_Precio()) {
            case 1:
                holder.precioTV1.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV2.setTextColor(Color.rgb(124, 124, 124));
                holder.precioTV3.setTextColor(Color.rgb(124, 124, 124));
                holder.precioTV4.setTextColor(Color.rgb(124, 124, 124));
                holder.precioTV5.setTextColor(Color.rgb(124, 124, 124));
                break;
            case 2:
                holder.precioTV1.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV2.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV3.setTextColor(Color.rgb(124, 124, 124));
                holder.precioTV4.setTextColor(Color.rgb(124, 124, 124));
                holder.precioTV5.setTextColor(Color.rgb(124, 124, 124));
                break;
            case 3:
                holder.precioTV1.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV2.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV3.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV4.setTextColor(Color.rgb(124, 124, 124));
                holder.precioTV5.setTextColor(Color.rgb(124, 124, 124));
                break;
            case 4:
                holder.precioTV1.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV2.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV3.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV4.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV5.setTextColor(Color.rgb(124, 124, 124));
                break;
            case 5:
                holder.precioTV1.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV2.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV3.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV4.setTextColor(Color.rgb(0, 0, 0));
                holder.precioTV5.setTextColor(Color.rgb(0, 0, 0));
                break;
        }
        //handling buttons event
        holder.btnEdit.setOnClickListener(onLikeListener(position, holder));
        if (DesdeDondeLlamo!=1) {
            holder.btnDelete.setOnClickListener(onDeleteListener(position, holder));
        }
        holder.estrellasTV.setText("Estrellas: " + p.get_Estrellas());

        if (p.is_Likeado())
        {
            holder.iconLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_black_24dp));
        }
        else
        {
            holder.iconLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
        }

        return convertView;
    }


    private View.OnClickListener onLikeListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //if (position!=0)
                //{
                    UsuariosRestaurantes usuariosRestaurantes=new UsuariosRestaurantes();
                    int idRestaurant = restaurants.get(position).get_IdRestaurant();
                    int idUsuario = usuarioActual.get_idUsuario();

                    usuariosRestaurantes.setIdRestaurant(idRestaurant);
                    usuariosRestaurantes.setIdUsuario(idUsuario);
                    String url = "http://salidasnow.hol.es/UsuariosRestaurantes/setLike.php?idUsuario="+idUsuario+"&idRestaurant="+idRestaurant+"&like=1";
                    new insertLikeAsync().execute(url);



                    if (holder.iconLike.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_star_border_black_24dp).getConstantState())
                    {
                        holder.iconLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_black_24dp));
                        Toast.makeText(context, "Me gusta!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        holder.iconLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                        Toast.makeText(context, "No me gusta :(", Toast.LENGTH_SHORT).show();
                    }


                //}

             //   showEditDialog(position, holder);
            }
        };
    }

    /**
     * Editting confirm dialog
     * @param position
     * @param holder
     */
   /* private void showEditDialog(final int position, final ViewHolder holder) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setTitle("EDIT ELEMENT");
        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setText(friends.get(position));
        input.setLayoutParams(lp);
        alertDialogBuilder.setView(input);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result edit text
                                friends.set(position, input.getText().toString().trim());

                                //notify data set changed
                                activity.updateAdapter();
                                holder.swipeLayout.close();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog and show it
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
*/

    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // friends.remove(position);
               // holder.swipeLayout.close();
                // activity.updateAdapter();
            }
        };
    }

    private class ViewHolder {
        private View btnDelete;
        private View btnEdit;
        private SwipeLayout swipeLayout;
        private TextView nombreTV, direccionTV,precioTV1,precioTV2,precioTV3,precioTV4,precioTV5,estrellasTV;
        private ImageView iconLike;

        public ViewHolder(View v) {
            nombreTV = (TextView) v.findViewById(R.id.nombre);
            swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout);
            if (DesdeDondeLlamo!=1) {
                btnDelete = v.findViewById(R.id.delete);
            }
            btnEdit = v.findViewById(R.id.icon_likeLV);
            direccionTV = (TextView)v.findViewById(R.id.direc);
            precioTV1 = (TextView)v.findViewById(R.id.txvwLVPrecio1);
            precioTV2 = (TextView)v.findViewById(R.id.txvwLVPrecio2);
            precioTV3 = (TextView)v.findViewById(R.id.txvwLVPrecio3);
            precioTV4 = (TextView)v.findViewById(R.id.txvwLVPrecio4);
            precioTV5 = (TextView)v.findViewById(R.id.txvwLVPrecio5);
            estrellasTV = (TextView)v.findViewById(R.id.txvwLVestrellas);
            iconLike = (ImageView)v.findViewById(R.id.icon_likeLV);

            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        }
    }
    private class insertLikeAsync extends AsyncTask<String, Void,String>
    {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String resultado;

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response=client.newCall(request).execute();

                return response.body().string();
            }
            catch (IOException e)
            {
                Log.d("Error",e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String JsonResponse) {

            int estado=0;
            try {
               estado = parsearResultado(JsonResponse);

            }
            catch (JSONException e)
            {
                Log.d("error",e.getMessage());
            }
            switch (estado)
            {
                case 0:
                    Log.d("errorSwitch","Fue por el catch");
                    break;
                case 1:
                    Log.d("ResultadoLike","Ya existia el like, se actualizo sin problemas");
                    break;
                case 2:
                    Log.d("ResultadoLike","No se actualizo el registro");
                    break;
                case 3:
                    Log.d("ResultadoLike","No existia el like, se creo sin problemas");
                    break;
                case 4:
                    Log.d("ResultadoLike","No se creo el registro");
                    break;
                case 5:
                    Log.d("ResultadoLike","Excepcion sql: "+ JsonResponse);
                    break;
                default:
                    Log.d("errorSwitch","Ups");
                    break;
            }
        }

        private int parsearResultado(String string) throws JSONException
        {
            JSONObject json=new JSONObject(string);

            return json.getInt("estado");
        }

    }
}
