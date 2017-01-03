package my.com.astro.sample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import my.com.astro.sample.Entities.Channel;

import static java.lang.Integer.parseInt;

/**
 * Created by hossein on 12/31/16.
 */

public class DbHelper_Channel extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Astro.db";
    public static final String TABLE_NAME = "channels";
    public static final String PRIMARY_KEY_NAME = "id";

    public static final int DB_VERSION = 1;
    Context context;


    public DbHelper_Channel(Context context)
    {
        super(context, DATABASE_NAME , null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME  +
                "(" + PRIMARY_KEY_NAME + " integer primary key,"  + "chName text,chNo integer,isFavorite integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }

    public void reCreateTable() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }

    public boolean insertRow(Channel obj) {

        if (getAllData("id=" + obj.getId() + "").size() == 0) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put("id", obj.getId());
            contentValues.put("chName", obj.getName());
            contentValues.put("chNo", obj.getNumber());
            if (obj.isFavorite()){
                contentValues.put("isFavorite", 1);
            }else{
                contentValues.put("isFavorite", 0);
            }

            db.insert(TABLE_NAME, null, contentValues);

            return true;
        } else {
            return false;
        }

    }

    public Cursor getAllRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME  , null );
        res.close();
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }
    public boolean updateRow (Channel obj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("chName", obj.getName());
        contentValues.put("chNo", obj.getNumber());
        if (obj.isFavorite()){
            contentValues.put("isFavorite", 1);
        }else{
            contentValues.put("isFavorite", 0);
        }

        db.update(TABLE_NAME, contentValues, PRIMARY_KEY_NAME + " = ? ", new String[] { Integer.toString(obj.getId()) } );
        return true;
    }

    public Integer deleteRow (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                PRIMARY_KEY_NAME + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public boolean deleteAllRows ()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        return true;
    }


  public List<Channel> getAllData(String Cond)
    {

        String Condition="";
        if (Cond!=null && Cond.equals("")!=true){
            Condition = " where " + Cond;
        }


        List<Channel> lstChannel = new ArrayList<Channel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME + Condition , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            Channel obj =new Channel();

            obj.setId(parseInt(res.getString(res.getColumnIndex("id"))));
            obj.setName(res.getString(res.getColumnIndex("chName")));
            obj.setNumber(parseInt(res.getString(res.getColumnIndex("chNo"))));

            int isFavorite = Integer.parseInt(res.getString(res.getColumnIndex("isFavorite")));

            if (isFavorite==1){
                obj.setFavorite(true);
            }else{
                obj.setFavorite(false);
            }

            lstChannel.add(obj);
            res.moveToNext();
        }

        res.close();

        return lstChannel;
    }

    public int getDataCount(String Cond)
    {
        String Condition="";
        if (Cond!=null && Cond.equals("")!=true){
            Condition = " where " + Cond;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select count(*) as cn from " + TABLE_NAME + Condition , null );
        res.moveToFirst();

        int count = 0;
        while(res.isAfterLast() == false){
            count = parseInt(res.getString(res.getColumnIndex("cn")));
            res.moveToNext();
        }

        res.close();
        return count;
    }



}