package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by melvinrivera on 14/4/18.
 */

public class AEOContentProvider extends ContentProvider {
    /* TAG for the log message */
    public static final String LOG_TAG = AEOContentProvider.class.getSimpleName();

    private static final int PERFILES = 100;

    private static final int PERFIL_ID = 101;

    private static final int CATEGORIAS = 1;

    private static final int CATEGORIA_ID = 2;

    private static final int REGION_ID = 3;

    private static final int REGIONES= 4;



    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PerfilesContract.CONTENT_AUTHORITY, PerfilesContract.PATH_PERFILES, PERFILES);
        sUriMatcher.addURI(PerfilesContract.CONTENT_AUTHORITY, PerfilesContract.PATH_PERFILES + "/#", PERFIL_ID);
        sUriMatcher.addURI(CategoriasContract.CONTENT_AUTHORITY, CategoriasContract.PATH_CATEGORIAS, CATEGORIAS);
        sUriMatcher.addURI(CategoriasContract.CONTENT_AUTHORITY, CategoriasContract.PATH_CATEGORIAS + "/#", CATEGORIA_ID);
        sUriMatcher.addURI(RegionesContract.CONTENT_AUTHORITY, RegionesContract.PATH_REGIONES, REGIONES);
        sUriMatcher.addURI(RegionesContract.CONTENT_AUTHORITY, RegionesContract.PATH_REGIONES + "/#", REGION_ID);
    }

    private AEODbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new AEODbHelper(getContext());
        return true;
    }


    public String getType(@NonNull Uri uri) {

        // Find the MIME type of the results... multiple results or a single result
        switch (sUriMatcher.match(uri)) {
            case PERFILES:
                return PerfilesContract.ContactosEntry.CONTENT_TYPE;
            case PERFIL_ID:
                return PerfilesContract.ContactosEntry.CONTENT_ITEM_TYPE;
            case CATEGORIAS:
                return CategoriasContract.CategoriasEntry.CONTENT_TYPE;
            case  CATEGORIA_ID:
                return CategoriasContract.CategoriasEntry.CONTENT_ITEM_TYPE;
            case REGIONES:
                return RegionesContract.RegionesEntry.CONTENT_TYPE;
            case  REGION_ID:
                return RegionesContract.RegionesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Invalid URI!");
        }
    }



    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor c;
        switch (sUriMatcher.match(uri)) {
            // Query for multiple article results
            case PERFILES:
                c = database.query(PerfilesContract.ContactosEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            // Query for single article result
            case PERFIL_ID:
                long _id = ContentUris.parseId(uri);
                c = database.query(PerfilesContract.ContactosEntry.TABLE_NAME,
                        projection,
                        PerfilesContract.ContactosEntry._ID + "=?",
                        new String[] { String.valueOf(_id) },
                        null,
                        null,
                        sortOrder);
                break;

            case CATEGORIAS:
                c = database.query(CategoriasContract.CategoriasEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CATEGORIA_ID:
                long id_caso_categoria = ContentUris.parseId(uri);
                c = database.query(CategoriasContract.CategoriasEntry.TABLE_NAME,
                        projection,
                        CategoriasContract.CategoriasEntry._ID + "=?",
                        new String[] { String.valueOf(id_caso_categoria) },
                        null,
                        null,
                        sortOrder);
                break;

            case REGIONES:
                c = database.query(RegionesContract.RegionesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case REGION_ID:
                long id_caso_region = ContentUris.parseId(uri);
                c = database.query(RegionesContract.RegionesEntry.TABLE_NAME,
                        projection,
                        RegionesContract.RegionesEntry._ID + "=?",
                        new String[] { String.valueOf(id_caso_region) },
                        null,
                        null,
                        sortOrder);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");
        }

        // Tell the cursor to register a content observer to observe changes to the
        // URI or its descendants.
        assert getContext() != null;
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;

    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Uri returnUri;
        long _id;

        switch (sUriMatcher.match(uri)) {
            case PERFILES:
                _id = database.insert(PerfilesContract.ContactosEntry.TABLE_NAME, null, values);
                returnUri = ContentUris.withAppendedId(PerfilesContract.ContactosEntry.CONTENT_URI, _id);
                break;
            case CATEGORIAS:
                _id = database.insert(CategoriasContract.CategoriasEntry.TABLE_NAME, null, values);
                returnUri = ContentUris.withAppendedId(CategoriasContract.CategoriasEntry.CONTENT_URI, _id);
                break;
            case REGIONES:
                _id = database.insert(RegionesContract.RegionesEntry.TABLE_NAME, null, values);
                returnUri = ContentUris.withAppendedId(RegionesContract.RegionesEntry.CONTENT_URI, _id);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");
        }

        // Notify any observers to update the UI
        assert getContext() != null;
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case PERFILES:
                rows = database.delete(PerfilesContract.ContactosEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORIAS:
                rows = database.delete(CategoriasContract.CategoriasEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REGIONES:
                rows = database.delete(RegionesContract.RegionesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");
        }

        // Notify any observers to update the UI
        if (rows != 0) {
            assert getContext() != null;
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case PERFILES:
                rows = database.update(PerfilesContract.ContactosEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CATEGORIAS:
                rows = database.update(CategoriasContract.CategoriasEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REGIONES:
                rows = database.update(RegionesContract.RegionesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");
        }

        // Notify any observers to update the UI
        if (rows != 0) {
            assert getContext() != null;
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;

    }
}