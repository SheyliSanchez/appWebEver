package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.CategoriasContract;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.PerfilesContract;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.RegionesContract;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.web.Categoria;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.web.CategoriaParser;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.web.Perfil;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.web.PerfilParser;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.web.Region;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.web.RegionParser;

/**
 * Created by melvinrivera on 14/4/18.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SYNC_ADAPTER";

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    /**
     * This gives us access to our local data source.
     */
    private final ContentResolver resolver;


    public SyncAdapter(Context c, boolean autoInit) {
        this(c, autoInit, false);
    }

    public SyncAdapter(Context c, boolean autoInit, boolean parallelSync) {
        super(c, autoInit, parallelSync);
        this.resolver = c.getContentResolver();
    }

    /**
     * This method is run by the Android framework, on a new Thread, to perform a sync.
     * @param account Current account
     * @param extras Bundle extras
     * @param authority Content authority
     * @param provider {@link ContentProviderClient}
     * @param syncResult Object to write stats to
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.w(TAG, "Starting synchronization...");

        try {
            // Synchronize our news feed
            syncNewsFeed(syncResult);

            // Add any other things you may want to sync

        } catch (IOException ex) {
            Log.e(TAG, "Error synchronizing!", ex);
            syncResult.stats.numIoExceptions++;
        } catch (JSONException ex) {
            Log.e(TAG, "Error synchronizing!", ex);
            syncResult.stats.numParseExceptions++;
        } catch (RemoteException|OperationApplicationException ex) {
            Log.e(TAG, "Error synchronizing!", ex);
            syncResult.stats.numAuthExceptions++;
        }

        Log.w(TAG, "Finished synchronization!");


    }

    /**
     * Performs synchronization of our pretend news feed source.
     * @param syncResult Write our stats to this
     */
    private void syncNewsFeed(SyncResult syncResult) throws IOException, JSONException, RemoteException, OperationApplicationException {

        /* **********************************************************************************
        *                       OPERACIONES PARA SINCRONIZAR PERFILES                        *
        *************************************************************************************/
        final String rssFeedEndpoint = "http://aeo.web-hn.com/WebServices/ParaSincronizarPerfiles.php";


        // We need to collect all the network items in a hash table
        Log.i(TAG, "Fetching server entries...");
        Map<String, Perfil> networkEntries = new HashMap<>();

        // Parse the pretend json news feed
        String jsonFeed = download(rssFeedEndpoint);
        JSONArray perfilesArray = new JSONArray(jsonFeed);


        for (int i = 0; i < perfilesArray.length(); i++) {
            Perfil perfil = PerfilParser.parse(perfilesArray.optJSONObject(i));
            networkEntries.put(perfil.getPERFILID(), perfil);
        }

        // Create list for batching ContentProvider transactions
        ArrayList<ContentProviderOperation> batch = new ArrayList<>();

        // Compare the hash table of network entries to all the local entries
        Log.i(TAG, "Fetching local entries...");
        Cursor c = resolver.query(PerfilesContract.ContactosEntry.CONTENT_URI, null, null, null, null, null);
        assert c != null;
        c.moveToFirst();

        String nombre, numeroTelefono, numeroCelular,direccion , e_mail, descripcion, nombreRegion, imagenPath;
        String id, categoria, idRegion, idEstado;
        String latitud, longitud;
        for (int i = 0; i < c.getCount(); i++) {
            syncResult.stats.numEntries++;

            // Create local article entry
            nombre = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE));
            numeroTelefono = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO));
            numeroCelular = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR));
            id = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_PERFILID));
            nombreRegion =c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE_REGION));
            direccion = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_DIRECCION));
            e_mail = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_E_MAIL));
            descripcion = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_DESCRIPCION));
            categoria = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_CATEGORIA));
            idRegion = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_ID_REGION));
            idEstado = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_ESTADO));
            latitud = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_LATITUD));
            longitud = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_LONGITUD));
            imagenPath = c.getString(c.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_PERFILID));
            Perfil found;

            // Try to retrieve the local entry from network entries
            found = networkEntries.get(id);
            if (found != null) {
                // The entry exists, remove from hash table to prevent re-inserting it
                networkEntries.remove(id);

                // Check to see if it needs to be updated
                if (!nombre.equals(found.getNOMBRE())
                        || !numeroTelefono.equals(found.getNUMERO_TELEFONO())
                        || !numeroCelular.equals(found.getNUMERO_CELULAR())
                        || !nombreRegion.equals(found.getNOMBRE_REGION())
                        || !direccion.equals(found.getDIRECCION())
                        || !e_mail.equals(found.getE_MAIL())
                        || !descripcion.equals(found.getDESCRIPCION())
                        || !categoria.equals(found.getCATEGORIA())
                        || !idRegion.equals(found.getID_REGION())
                        || !idEstado.equals(found.getESTADO())
                        || !latitud.equals(found.getLATITUD())
                        || !latitud.equals(found.getLATITUD())
                        || !longitud.equals(found.getLONGITUD())
                        || !imagenPath.equals(found.getIMAGEN_PATH())
                        ) {
                    // Batch an update for the existing record
                    Log.i(TAG, "Scheduling update: " + nombre);
                    batch.add(ContentProviderOperation.newUpdate(PerfilesContract.ContactosEntry.CONTENT_URI)
                            .withSelection(PerfilesContract.ContactosEntry.COLUMN_PERFILID + "='" + id + "'", null)
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_NOMBRE,found.getNOMBRE())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO, found.getNUMERO_TELEFONO())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR, found.getNUMERO_CELULAR())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_NOMBRE_REGION, found.getNOMBRE_REGION())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH, found.getIMAGEN_PATH())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_E_MAIL, found.getE_MAIL())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_DIRECCION, found.getDIRECCION())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_DESCRIPCION, found.getDESCRIPCION())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_CATEGORIA, found.getCATEGORIA())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_ID_REGION,found.getID_REGION())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_ESTADO,found.getESTADO())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_LATITUD,found.getLATITUD())
                            .withValue(PerfilesContract.ContactosEntry.COLUMN_LONGITUD,found.getLONGITUD())
                            .build());
                    syncResult.stats.numUpdates++;

                }
            } else {
                // Entry doesn't exist, remove it from the local database
                Log.i(TAG, "Scheduling delete: " + nombre);
                batch.add(ContentProviderOperation.newDelete(PerfilesContract.ContactosEntry.CONTENT_URI)
                        .withSelection(PerfilesContract.ContactosEntry.COLUMN_PERFILID + "='" + id + "'", null)
                        .build());
                syncResult.stats.numDeletes++;
            }
            c.moveToNext();
        }
        c.close();

        // Add all the new entries
        for (Perfil perfil : networkEntries.values()) {
            Log.i(TAG, "Scheduling insert: " + perfil.getNOMBRE());
            batch.add(ContentProviderOperation.newInsert(PerfilesContract.ContactosEntry.CONTENT_URI)
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_PERFILID,perfil.getPERFILID())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_NOMBRE,perfil.getNOMBRE())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO, perfil.getNUMERO_TELEFONO())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR, perfil.getNUMERO_CELULAR())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_NOMBRE_REGION, perfil.getNOMBRE_REGION())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH, perfil.getIMAGEN_PATH())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_E_MAIL, perfil.getE_MAIL())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_DIRECCION, perfil.getDIRECCION())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_DESCRIPCION, perfil.getDESCRIPCION())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_CATEGORIA, perfil.getCATEGORIA())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_ID_REGION,perfil.getID_REGION())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_ESTADO,perfil.getESTADO())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_LATITUD,perfil.getLATITUD())
                    .withValue(PerfilesContract.ContactosEntry.COLUMN_LONGITUD,perfil.getLONGITUD())
                    .build());
            syncResult.stats.numInserts++;
        }

        // Synchronize by performing batch update
        Log.i(TAG, "Merge solution ready, applying batch update...");
        resolver.applyBatch(PerfilesContract.CONTENT_AUTHORITY, batch);
        resolver.notifyChange(PerfilesContract.ContactosEntry.CONTENT_URI, // URI where data was modified
                null, // No local observer
                false); // IMPORTANT: Do not sync to network

         /* **********************************************************************************
        *                       OPERACIONES PARA SINCRONIZAR CATEGORIAS                     *
        *************************************************************************************/
        final String rssFeedEndpointCategorias = "http://aeo.web-hn.com/WebServices/ParaSincronizarCategorias.php?estados=2";
        Map<String, Categoria> networkEntriesCategoria = new HashMap<>();

        // Parse the pretend json news feed
        String jsonFeedCategoria = download(rssFeedEndpointCategorias);
        JSONArray categoriasArray = new JSONArray(jsonFeedCategoria);


        for (int i = 0; i < categoriasArray.length(); i++) {
            Categoria categoria1 = CategoriaParser.parse(categoriasArray.optJSONObject(i));
            networkEntriesCategoria.put(categoria1.getID_CATEGORIA(), categoria1);
        }

        // Create list for batching ContentProvider transactions
        ArrayList<ContentProviderOperation> batch1 = new ArrayList<>();

        // Compare the hash table of network entries to all the local entries
        Log.i(TAG, "Fetching local entries...");
        Cursor c1 = resolver.query(CategoriasContract.CategoriasEntry.CONTENT_URI, null, null, null, null, null);
        assert c1 != null;
        c1.moveToFirst();

        String id_categoria, nombre_categoria, imagen_categoria, cantidad;

        for (int i = 0; i < c1.getCount(); i++) {
            syncResult.stats.numEntries++;

            // Create local article entry
            id_categoria = c1.getString(c1.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA));
            nombre_categoria = c1.getString(c1.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA));
            imagen_categoria = c1.getString(c1.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA));
            cantidad = c1.getString(c1.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_CANTIDAD));

            Categoria found1;

            // Try to retrieve the local entry from network entries
            found1 = networkEntriesCategoria.get(id_categoria);
            if (found1 != null) {
                // The entry exists, remove from hash table to prevent re-inserting it
                networkEntriesCategoria.remove(id_categoria);

                // Check to see if it needs to be updated
                if (!nombre_categoria.equals(found1.getNOMBRE_CATEGORIA())
                        || !imagen_categoria.equals(found1.getIMAGEN_CATEGORIA())
                        || !cantidad.equals(found1.getCANTIDAD())
                        ) {
                    // Batch an update for the existing record
                    Log.i(TAG, "Scheduling update: " + nombre_categoria);
                    batch1.add(ContentProviderOperation.newUpdate(CategoriasContract.CategoriasEntry.CONTENT_URI)
                            .withSelection(CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA + "='" + id_categoria + "'", null)
                            .withValue(CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA,found1.getNOMBRE_CATEGORIA())
                            .withValue(CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA,found1.getIMAGEN_CATEGORIA())
                            .withValue(CategoriasContract.CategoriasEntry.COLUMN_CANTIDAD,found1.getCANTIDAD())
                            .build());
                    syncResult.stats.numUpdates++;

                }
            } else {
                // Entry doesn't exist, remove it from the local database
                Log.i(TAG, "Scheduling delete: " + nombre_categoria);
                batch1.add(ContentProviderOperation.newDelete(CategoriasContract.CategoriasEntry.CONTENT_URI)
                        .withSelection(CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA + "='" + id_categoria + "'", null)
                        .build());
                syncResult.stats.numDeletes++;
            }
            c1.moveToNext();
        }
        c1.close();

        // Add all the new entries
        for (Categoria categoria_recorrer : networkEntriesCategoria.values()) {
            Log.i(TAG, "Scheduling insert: " + categoria_recorrer.getNOMBRE_CATEGORIA());
            batch1.add(ContentProviderOperation.newInsert(CategoriasContract.CategoriasEntry.CONTENT_URI)
                    .withValue(CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA,categoria_recorrer.getID_CATEGORIA())
                    .withValue(CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA,categoria_recorrer.getNOMBRE_CATEGORIA())
                    .withValue(CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA,categoria_recorrer.getIMAGEN_CATEGORIA())
                    .withValue(CategoriasContract.CategoriasEntry.COLUMN_CANTIDAD,categoria_recorrer.getCANTIDAD())
                    .build());
            syncResult.stats.numInserts++;
        }

        // Synchronize by performing batch update
        Log.i(TAG, "Merge solution ready, applying batch update...");
        resolver.applyBatch(CategoriasContract.CONTENT_AUTHORITY, batch1);
        resolver.notifyChange(CategoriasContract.CategoriasEntry.CONTENT_URI, // URI where data was modified
                null, // No local observer
                false); // IMPORTANT: Do not sync to network

         /* **********************************************************************************
        *                       OPERACIONES PARA SINCRONIZAR REGIONES                      *
        *************************************************************************************/
        final String rssFeedEndpointRegiones= "http://aeo.web-hn.com/WebServices/ParaSincronizarRegiones.php";
        Map<String, Region> networkEntriesRegion = new HashMap<>();

        // Parse the pretend json news feed
        String jsonFeedRegion = download(rssFeedEndpointRegiones);
        JSONArray regionesArray = new JSONArray(jsonFeedRegion);


        for (int i = 0; i < regionesArray.length(); i++) {
            Region region = RegionParser.parse(regionesArray.optJSONObject(i));
            networkEntriesRegion.put(region.getID_REGION(), region);
        }

        // Create list for batching ContentProvider transactions
        ArrayList<ContentProviderOperation> batch2 = new ArrayList<>();

        // Compare the hash table of network entries to all the local entries
        Log.i(TAG, "Fetching local entries...");
        Cursor c2 = resolver.query(RegionesContract.RegionesEntry.CONTENT_URI, null, null, null, null, null);
        assert c2 != null;
        c2.moveToFirst();

        String id_region, nombre_region;

        for (int i = 0; i < c2.getCount(); i++) {
            syncResult.stats.numEntries++;

            // Create local article entry
            id_region = c2.getString(c2.getColumnIndex(RegionesContract.RegionesEntry.COLUMN_ID_REGION));
            nombre_region = c2.getString(c2.getColumnIndex(RegionesContract.RegionesEntry.COLUMN_NOMBRE_REGION));

            Region found2;

            // Try to retrieve the local entry from network entries
            found2 = networkEntriesRegion.get(id_region);
            if (found2 != null) {
                // The entry exists, remove from hash table to prevent re-inserting it
                networkEntriesRegion.remove(id_region);

                // Check to see if it needs to be updated
                if (!nombre_region.equals(found2.getNOMBRE_REGION())
                        ) {
                    // Batch an update for the existing record
                    Log.i(TAG, "Scheduling update: " + nombre_region);
                    batch1.add(ContentProviderOperation.newUpdate(RegionesContract.RegionesEntry.CONTENT_URI)
                            .withSelection(RegionesContract.RegionesEntry.COLUMN_ID_REGION + "='" + id_region + "'", null)
                            .withValue(RegionesContract.RegionesEntry.COLUMN_NOMBRE_REGION,found2.getNOMBRE_REGION())
                            .build());
                    syncResult.stats.numUpdates++;

                }
            } else {
                // Entry doesn't exist, remove it from the local database
                Log.i(TAG, "Scheduling delete: " + nombre_region);
                batch1.add(ContentProviderOperation.newDelete(RegionesContract.RegionesEntry.CONTENT_URI)
                        .withSelection(RegionesContract.RegionesEntry.COLUMN_ID_REGION + "='" + id_region + "'", null)
                        .build());
                syncResult.stats.numDeletes++;
            }
            c2.moveToNext();
        }
        c2.close();

        // Add all the new entries
        for (Region region_recorrer : networkEntriesRegion.values()) {
            Log.i(TAG, "Scheduling insert: " + region_recorrer.getNOMBRE_REGION());
            batch2.add(ContentProviderOperation.newInsert(RegionesContract.RegionesEntry.CONTENT_URI)
                    .withValue(RegionesContract.RegionesEntry.COLUMN_ID_REGION,region_recorrer.getID_REGION())
                    .withValue(RegionesContract.RegionesEntry.COLUMN_NOMBRE_REGION,region_recorrer.getNOMBRE_REGION())
                    .build());
            syncResult.stats.numInserts++;
        }

        // Synchronize by performing batch update
        Log.i(TAG, "Merge solution ready, applying batch update...");
        resolver.applyBatch(RegionesContract.CONTENT_AUTHORITY, batch2);
        resolver.notifyChange(RegionesContract.RegionesEntry.CONTENT_URI, // URI where data was modified
                null, // No local observer
                false); // IMPORTANT: Do not sync to network

    }


    /**
     * A blocking method to stream the server's content and build it into a string.
     * @param url API call
     * @return String response
     */
    private String download(String url) throws IOException {
        // Ensure we ALWAYS close these!
        HttpURLConnection client = null;
        InputStream is = null;

        try {
            // Connect to the server using GET protocol
            URL server = new URL(url);
            client = (HttpURLConnection)server.openConnection();
            client.connect();

            // Check for valid response code from the server
            int status = client.getResponseCode();
            is = (status == HttpURLConnection.HTTP_OK)
                    ? client.getInputStream() : client.getErrorStream();

            // Build the response or error as a string
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            for (String temp; ((temp = br.readLine()) != null);) {
                sb.append(temp);
            }

            return sb.toString();
        } finally {
            if (is != null) { is.close(); }
            if (client != null) { client.disconnect(); }
        }
    }

    /**
     * Manual force Android to perform a sync with our SyncAdapter.
     */
    public static void performSync(Context context) {
        Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(getSyncAccount(context),
                PerfilesContract.CONTENT_AUTHORITY, b);
    }

    public static Account getSyncAccount(Context context){
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account("Agenda Electrónica Oriental","aplicacionagendaelectronicaoriental.aeo.proyecto.unah");

        if(null == accountManager.getPassword(newAccount)){
            if(!accountManager.addAccountExplicitly(newAccount,"",null)){
                return  null;
            }
            onAccountCreated(newAccount,context);
        }
        return newAccount;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime){
        Account account = getSyncAccount(context);
        String authority = context.getResources().getString(R.string.content_authority);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            SyncRequest request = new SyncRequest.Builder().
                    setSyncAdapter(account,authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        }else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(),syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context){
        SyncAdapter.configurePeriodicSync(context,SYNC_INTERVAL,SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(newAccount,context.getResources().getString(R.string.content_authority),true);

        syncImmediately(context);
    }

    public static void syncImmediately(Context context){
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED,true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
        ContentResolver.requestSync(getSyncAccount(context), context.getResources().getString(R.string.content_authority),bundle);
    }

    public  static void initializeSyncAdapter(Context context){ getSyncAccount(context); }


}
