package futbolitoapp.apliko.co.futbolitoapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import futbolitoapp.apliko.co.futbolitoapp.objects.Grupo;
import futbolitoapp.apliko.co.futbolitoapp.objects.Miembro;

/**
 * Created by iosdeveloper on 25/08/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {


    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "futbolitoappdb.db";

    // Table Names
    private static final String TABLE_TOKEN = "Token";
    private static final String TABLE_LIGA = "Liga";
    private static final String TABLE_PRONOSTICO = "Pronostico";
    private static final String TABLE__GRUPO = "Grupo";
    private static final String TABLE__MIEMBRO = "Miembro";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // NOTES Table
    private static final String TOKEN = "token";
    private static final String LIGA = "Nombre";
    private static final String GOLES_LOCAL = "goles_local";
    private static final String GOLES_VISITANTE = "goles_visitante";
    private static final String ID_PARTIDO = "id_partido";
    private static final String NOMBRE_GRUPO = "nombre_grupo";
    private static final String DESCRIPCION = "descripcion";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String ID_GRUPO = "id_grupo";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_TOKEN = "CREATE TABLE "
            + TABLE_TOKEN + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TOKEN
            + " TEXT,"+ KEY_CREATED_AT
            + " DATETIME" + ")";

    private static final String CREATE_TABLE_LIGA = "CREATE TABLE "
            + TABLE_LIGA +"(" + KEY_ID + " INTEGER PRIMARY KEY," + LIGA
            + " TEXT,"+ KEY_CREATED_AT
            + " DATETIME" + ")";

    private static final String CREATE_TABLE_PRONOSTICO = "CREATE TABLE "
            + TABLE_PRONOSTICO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + GOLES_LOCAL
            + " INTEGER,"+ GOLES_VISITANTE + " INTEGER, " + ID_PARTIDO + " INTEGER, " + KEY_CREATED_AT
            + " DATETIME" + ")";

    private static final String CREATE_TABLE_GRUPO = "CREATE TABLE "
            + TABLE__GRUPO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NOMBRE_GRUPO
            + " TEXT,"+ DESCRIPCION + " TEXT, " + ID_PARTIDO + " INTEGER, " + KEY_CREATED_AT
            + " DATETIME" + ")";

    private static final String CREATE_TABLE_MIEMBRO = "CREATE TABLE "
            + TABLE__MIEMBRO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + FIRST_NAME
            + " TEXT,"+ LAST_NAME + " TEXT, " + USERNAME + " TEXT,"+ EMAIL + " TEXT,"+ ID_PARTIDO + " INTEGER, " + KEY_CREATED_AT
            + " DATETIME" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {

    // creating required tables
        db.execSQL(CREATE_TABLE_TOKEN);
        db.execSQL(CREATE_TABLE_LIGA);
        db.execSQL(CREATE_TABLE_PRONOSTICO);
        db.execSQL(CREATE_TABLE_GRUPO);
        db.execSQL(CREATE_TABLE_MIEMBRO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOKEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIGA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRONOSTICO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE__GRUPO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE__MIEMBRO);

     // create new tables
        onCreate(db);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /*
 * Creating a todo
 */
    public long createToDo(Token token) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TOKEN, token.getToken());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long todo_id = db.insert(TABLE_TOKEN, null, values);

        return todo_id;
    }

    public long createToDoLiga(Liga liga) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.i("ahgghbjhbjhbjhjhbjh",liga.getId()+" "+liga.getNombre());
        ContentValues values = new ContentValues();
        values.put(KEY_ID, liga.getId());
        values.put(LIGA, liga.getNombre());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long todo_id = db.insert(TABLE_LIGA, null, values);

        return todo_id;
    }

    public long createToDoPronostico(Pronostico pronostico) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, pronostico.getId());
        values.put(GOLES_LOCAL, pronostico.getGolesLocal());
        values.put(GOLES_VISITANTE, pronostico.getGolesVisitante());
        values.put(ID_PARTIDO, pronostico.getIdPartido());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long todo_id = db.insert(TABLE_PRONOSTICO, null, values);

        return todo_id;
    }

    public long createToDoGrupo(Grupo grupo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, grupo.getId());
        values.put(NOMBRE_GRUPO, grupo.getNombre());
        values.put(DESCRIPCION,grupo.getDescripcion());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long todo_id = db.insert(TABLE__GRUPO, null, values);

        return todo_id;
    }

    public long createToDoMiembro(Miembro miembro) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, miembro.getId());
        values.put(FIRST_NAME, miembro.getFirstName());
        values.put(LAST_NAME, miembro.getLastname());
        values.put(USERNAME, miembro.getUsername());
        values.put(EMAIL, miembro.getEmail());
        values.put(ID_GRUPO, miembro.getIdGrupo());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long todo_id = db.insert(TABLE__MIEMBRO, null, values);

        return todo_id;
    }

    public List<Grupo> getAllGrupos() {
        List<Grupo> coordenadaList = new ArrayList<Grupo>();
        String selectQuery = "SELECT * FROM " + TABLE__GRUPO;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Grupo td = new Grupo((c.getString(c.getColumnIndex(NOMBRE_GRUPO))), (c.getString(c.getColumnIndex(DESCRIPCION))) );
                //td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                // adding to todo list
                coordenadaList.add(td);
            } while (c.moveToNext());
        }

        return coordenadaList;
    }

    public List<Liga> getAllLigas() {
        List<Liga> coordenadaList = new ArrayList<Liga>();
        String selectQuery = "SELECT * FROM " + TABLE_LIGA;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Liga td = new Liga((c.getInt(c.getColumnIndex(KEY_ID))), (c.getString(c.getColumnIndex(LIGA))));
                //td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                // adding to todo list
                coordenadaList.add(td);
            } while (c.moveToNext());
        }

        return coordenadaList;
    }

    public List<Token> getAllTokens() {
        List<Token> coordenadaList = new ArrayList<Token>();
        String selectQuery = "SELECT * FROM " + TABLE_TOKEN;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Token td = new Token((c.getString(c.getColumnIndex(TOKEN))));
                //td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                // adding to todo list
                coordenadaList.add(td);
            } while (c.moveToNext());
        }

        return coordenadaList;
    }

    public void dropDB() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOKEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIGA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRONOSTICO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE__GRUPO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE__MIEMBRO);

        onCreate(db);
    }

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Liga getLiga(String nombreLiga) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_LIGA + " WHERE "
                + LIGA + " = '" + nombreLiga +"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Liga td = new Liga(c.getInt(c.getColumnIndex(KEY_ID)), c.getString(c.getColumnIndex(LIGA)));

        return td;
    }

}
