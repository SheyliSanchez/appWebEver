package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilesBreves;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.PerfilDeLaOrganizacion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.PerfilesContract;

/**
 * Created by melvinrivera on 22/2/18.
 */

public class AdaptadorPerfilBreve extends RecyclerView.Adapter<AdaptadorPerfilBreve.ViewHolder> {
    /**********************************************************************************************
     *                                       DECLARACIÓN DE VARIABLES
     **********************************************************************************************/
    private  String BASE_URL=new ipLocalhost().getIp().substring(0,19);
    Cursor dataCursor;
    Context context;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE=1;

    /**********************************************************************************************
     *             ViewHolder que inicializa los componentes visuales
     **********************************************************************************************/


    public  class ViewHolder extends RecyclerView.ViewHolder{
        public    CircleImageView imagenPerfilBreve;
        public    TextView nombrePerfilBreve;
        public    TextView numeroTelefonoPerfilBreve;
        public    TextView direccionPerfilBreve;
        public    TextView id_perfilBreve;

        public ViewHolder(View itemView ){
            super(itemView);
            imagenPerfilBreve =  itemView.findViewById(R.id.imagen_organizacion);
            nombrePerfilBreve = (TextView) itemView.findViewById(R.id.nombre_organizacion);
            numeroTelefonoPerfilBreve = (TextView) itemView.findViewById(R.id.numero_telefono_organizacion);
            direccionPerfilBreve = (TextView) itemView.findViewById(R.id.direccion_organizacion);
            id_perfilBreve = (TextView) itemView.findViewById(R.id.id_Deorganizacion);

            /**********************************************************************************************
             *                                      Asignación de listeners
             **********************************************************************************************/

            numeroTelefonoPerfilBreve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(Intent.ACTION_DIAL);
                    String p = "tel:" + numeroTelefonoPerfilBreve.getText().toString();
                    i.setData(Uri.parse(p));
                    context.startActivity(i);

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context,PerfilDeLaOrganizacion.class);
                        intent.putExtra("id_organizacion", getItem(pos).id);
                        intent.putExtra("nombre_organizacion", getItem(pos).nombre);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }

    }

    /**********************************************************************************************
     *                                       Constructor del adaptador
     **********************************************************************************************/
    public AdaptadorPerfilBreve(Activity mContext, Cursor cursor) {
        dataCursor = cursor;
        context = mContext;
    }

    /**********************************************************************************************
     *          MÉTODO DE CREACIÓN DEL VIEWHOLDER INFLA LA PLANTILLA PARA LOS ELEMENTOS
     **********************************************************************************************/

    @Override
    public AdaptadorPerfilBreve.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_layout_item_lista_contactos,parent,false);
        return new ViewHolder(vista);
    }

    /**********************************************************************************************
     *      OBTIENE LOS ELEMENTOS DE LA BASE DE DATOS Y LOS ASIGNA A LOS COMPONENTES VISUALES
     **********************************************************************************************/

    @Override
    public void onBindViewHolder(AdaptadorPerfilBreve.ViewHolder holder, int position) {

        dataCursor.moveToPosition(position);


        holder.nombrePerfilBreve.setText(dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE)));
        holder.direccionPerfilBreve.setText(dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE_REGION)));
        if(dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO)).isEmpty()){
            holder.numeroTelefonoPerfilBreve.setText(dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR)));
        }else{
            holder.numeroTelefonoPerfilBreve.setText(dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO)));
        }

        holder.id_perfilBreve.setText(""+dataCursor.getInt(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_PERFILID)));

    if (!dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH)).equals("null")){
        Picasso.get().
                load(BASE_URL+
                        dataCursor.getString(
                                dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH))).
            memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.wait).
        into(holder.imagenPerfilBreve);

    }else {
        Picasso.get().
                load(R.drawable.iconocontactowhite).
                memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).
        into(
                holder.imagenPerfilBreve
        );
    }

    }
    /**********************************************************************************************
     *                             CAMBIA EL CURSOR
     **********************************************************************************************/

    public Cursor swapCursor(Cursor cursor){
        if(dataCursor == cursor){
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if(cursor != null){
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public int getItemCount() {
        return (dataCursor==null)?0:dataCursor.getCount();
    }

    /**********************************************************************************************
     *                                 OBTIENE EL ITEM
     **********************************************************************************************/

    public PerfilBreve getItem(int position){
        if(position < 0 || position >= getItemCount()){
            throw new IllegalArgumentException("Posicion del Item fuera del rango de adaptador");
        }else if(dataCursor.moveToPosition(position)){
            return new PerfilBreve(dataCursor);
        }
        return  null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        animateCircularReveal(holder.itemView);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateCircularReveal(View view){
        int centerX=0, centery=0, startRadius=0, endRadius = Math.max(view.getWidth(),view.getHeight());
        Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, centery,startRadius,endRadius);
        view.setVisibility(View.VISIBLE);
        animator.start();
    }

}

