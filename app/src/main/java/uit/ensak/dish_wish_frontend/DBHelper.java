package uit.ensak.dish_wish_frontend;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBName="register.db";

    public DBHelper(@Nullable Context context) {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table users(email Text primary key,password Text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");

    }
    public boolean insertData(String email,String Password){
        SQLiteDatabase mydb =this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("email",email);
        contentValues.put("password",Password);
        long result=mydb.insert("users",null,contentValues);
        if(result==-1)return false;
        else return true;


    }
    public boolean checkemail(String email){
        SQLiteDatabase mydb=this.getReadableDatabase();
        Cursor cursor = mydb.rawQuery("select * from users where email =?",new String[]{email});
        if(cursor.getCount() >0)
            return true;
        else return false;
    }
    public boolean checkuser(String email,String password){
        SQLiteDatabase mydb=this.getWritableDatabase();
        Cursor cursor= mydb.rawQuery("select * from users where email=? and password=?",new String[]{email,password});
        if(cursor.getCount() >0)
            return true;
        else
            return false;
    }
}
