package com.salidasnow.salidasnow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by berenson on 26/09/2016.
 */

public class AdaptadorListViewRestaurantes extends ArrayAdapter<Restaurantes>{
    ArrayList<Restaurantes> restaurants;
    Context context;
    Usuarios usuarioActual;

    public AdaptadorListViewRestaurantes(Context context,int resource, ArrayList<Restaurantes> restaurants, Usuarios usuario) {
        super(context,resource,restaurants);
        this.context = context;
        this.restaurants = restaurants;
        this.usuarioActual=usuario;
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
            convertView = inflater.inflate(R.layout.list_item_restaurant, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }


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
        holder.btnDelete.setOnClickListener(onDeleteListener(position, holder));
        holder.estrellasTV.setText("Estrellas: " + p.get_Estrellas());

        return convertView;
    }


    private View.OnClickListener onLikeListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (position!=0)
                {
                    UsuariosRestaurantes usuariosRestaurantes=new UsuariosRestaurantes();
                   int idRestaurant = restaurants.get(position-1).get_IdRestaurant();
                    int idUsuario = usuarioActual.get_idUsuario();

                    usuariosRestaurantes.setIdRestaurant(idRestaurant);
                    usuariosRestaurantes.setIdUsuario(idUsuario);

                        //TODO fijarse php

                }

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

        public ViewHolder(View v) {
            nombreTV = (TextView) v.findViewById(R.id.nombre);
            swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout);
            btnDelete = v.findViewById(R.id.delete);
            btnEdit = v.findViewById(R.id.edit_query);
            direccionTV = (TextView)v.findViewById(R.id.direc);
            precioTV1 = (TextView)v.findViewById(R.id.txvwLVPrecio1);
            precioTV2 = (TextView)v.findViewById(R.id.txvwLVPrecio2);
            precioTV3 = (TextView)v.findViewById(R.id.txvwLVPrecio3);
            precioTV4 = (TextView)v.findViewById(R.id.txvwLVPrecio4);
            precioTV5 = (TextView)v.findViewById(R.id.txvwLVPrecio5);
            estrellasTV = (TextView)v.findViewById(R.id.txvwLVestrellas);

            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        }
    }

    public void senddatatoserver(UsuariosRestaurantes usuRes) {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();

        try {
            post_dict.put("idUsuario", usuRes.getIdUsuario());
            post_dict.put("idRestaurant", usuRes.getIdRestaurant());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (post_dict.length() > 0) {
            new SendJsonDataToServer().execute(String.valueOf(post_dict));
        }
    }

    class SendJsonDataToServer extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://salidasnow.hol.es/insertar_usuario.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
// json data
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
//input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
//response data
                Log.i("adapterListView", JsonResponse);

//send to post execute

                return JsonResponse;


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("adapterListView", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("adapterListView", s);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
