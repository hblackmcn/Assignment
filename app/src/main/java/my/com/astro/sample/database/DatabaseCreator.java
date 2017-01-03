package my.com.astro.sample.database;

/**
 * Created by hossein on 12/31/16.
 */

import android.app.Activity;
import android.content.Context;
import java.io.File;
import java.util.Calendar;
import java.util.Random;


public class DatabaseCreator {

    public DbHelper_Channel    Db_channel;


    private Context mContext;


    public DatabaseCreator(Context ct)
    {
        Db_channel = new DbHelper_Channel(ct);

        mContext=ct;
    }

    public void reCreateDatabase(){
        Db_channel.reCreateTable();

        ApplyChangesToTables();//apply add new column if any
    }

    public void CreateDatabaseIfNotExist(){

        File dbFile;
        try {
            int n = Db_channel.numberOfRows();
            if (n==0){
                reCreateDatabase();
            }
        } catch (Exception e) {
            reCreateDatabase();
        }
        ApplyChangesToTables();
    }

    private void ApplyChangesToTables(){

    }


}
