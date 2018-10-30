package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by melvinrivera on 14/4/18.
 */

public class AEODbHelper extends SQLiteOpenHelper {

    private static final String TAG = AEODbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "aeo_2018.db";
    private static final int DATABASE_VERSION = 1;
    Context context;

    public AEODbHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PERFILES_TABLE = "CREATE TABLE " +PerfilesContract.ContactosEntry.TABLE_NAME+"("+
                PerfilesContract.ContactosEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                PerfilesContract.ContactosEntry.COLUMN_PERFILID+" INTEGER UNIQUE NOT NULL,"+
                PerfilesContract.ContactosEntry.COLUMN_NOMBRE+" TEXT NOT NULL, " +
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO +" TEXT," +
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR +" TEXT," +
                PerfilesContract.ContactosEntry.COLUMN_DIRECCION +" TEXT," +
                PerfilesContract.ContactosEntry.COLUMN_E_MAIL +" TEXT," +
                PerfilesContract.ContactosEntry.COLUMN_DESCRIPCION +" TEXT," +
                PerfilesContract.ContactosEntry.COLUMN_CATEGORIA +" INTEGER," +
                PerfilesContract.ContactosEntry.COLUMN_NOMBRE_REGION +" TEXT NOT NULL," +
                PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH + " TEXT," +
                PerfilesContract.ContactosEntry.COLUMN_ID_REGION + " INTEGER, " +
                PerfilesContract.ContactosEntry.COLUMN_LATITUD + " REAL," +
                PerfilesContract.ContactosEntry.COLUMN_LONGITUD + " REAL," +
                PerfilesContract.ContactosEntry.COLUMN_ESTADO + " INTEGER); ";

        final String SQL_CREATE_CATEGORIAS_TABLE = "CREATE TABLE " +CategoriasContract.CategoriasEntry.TABLE_NAME+"("+
                CategoriasContract.CategoriasEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA+" INTEGER UNIQUE NOT NULL,"+
                CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA+" TEXT NOT NULL, " +
                CategoriasContract.CategoriasEntry.COLUMN_CANTIDAD+" TEXT NOT NULL, " +
                CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA +" TEXT);";

        final String SQL_CREATE_REGIONES_TABLE = "CREATE TABLE " +RegionesContract.RegionesEntry.TABLE_NAME+"("+
                CategoriasContract.CategoriasEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                RegionesContract.RegionesEntry.COLUMN_ID_REGION+" INTEGER UNIQUE NOT NULL,"+
                RegionesContract.RegionesEntry.COLUMN_NOMBRE_REGION +" TEXT);";

        db.execSQL(SQL_CREATE_PERFILES_TABLE);
        db.execSQL(SQL_CREATE_REGIONES_TABLE);
        db.execSQL(SQL_CREATE_CATEGORIAS_TABLE);


        Log.d(TAG,"Database Created Successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PerfilesContract.ContactosEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoriasContract.CategoriasEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RegionesContract.RegionesEntry.TABLE_NAME);
        onCreate(db);

    }
}
