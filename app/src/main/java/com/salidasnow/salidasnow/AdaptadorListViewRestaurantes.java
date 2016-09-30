package com.salidasnow.salidasnow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

/**
 * Created by berenson on 26/09/2016.
 */

public class AdaptadorListViewRestaurantes extends ArrayAdapter<Restaurantes>{
    ArrayList<Restaurantes> restaurants;
    Context context;

    public AdaptadorListViewRestaurantes(Context context,int resource, ArrayList<Restaurantes> restaurants) {
        super(context,resource,restaurants);
        this.context = context;
        this.restaurants = restaurants;
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
        holder.btnEdit.setOnClickListener(onEditListener(position, holder));
        holder.btnDelete.setOnClickListener(onDeleteListener(position, holder));
        holder.estrellasTV.setText("Estrellas: " + p.get_Estrellas());

        return convertView;
    }


    private View.OnClickListener onEditListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
