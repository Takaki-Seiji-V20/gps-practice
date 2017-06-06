package jp.co.vsn.mygps.mygpsapplication.db;

import static jp.co.vsn.mygps.mygpsapplication.db.Address.*;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gps.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(context);
        }
        return mDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mSQLiteDatabase = db;
        createTable();
        insertFirstData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean open() {
        boolean flag = false;
        try {
            if (mSQLiteDatabase == null || !mSQLiteDatabase.isOpen()) {
                mSQLiteDatabase = getWritableDatabase();
            }
            flag = true;
        } catch (Exception ex) {
            flag = false;
        }
        return flag;
    }

    public void close() {
        if (mSQLiteDatabase != null) {
            mSQLiteDatabase.close();
        }
        mSQLiteDatabase = null;
    }

    public void createTable() {
        String createSqlAddress = "CREATE TABLE IF NOT EXISTS " + ADDRESS + " ("
                    + ID + "     INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + STATE + "  TEXT, "
                    + COMPLETE_FLG + "  INTEGER "
                    + ")";
        mSQLiteDatabase.execSQL(createSqlAddress);
    }

    public void insertFirstData() {
        insertAddressData(1, "北海道");
        insertAddressData(2, "青森県");
        insertAddressData(3, "岩手県");
        insertAddressData(4, "宮城県");
        insertAddressData(5, "秋田県");
        insertAddressData(6, "山形県");
        insertAddressData(7, "福島県");
        insertAddressData(8, "茨城県");
        insertAddressData(9, "栃木県");
        insertAddressData(10, "群馬県");
        insertAddressData(11, "埼玉県");
        insertAddressData(12, "千葉県");
        insertAddressData(13, "東京都");
        insertAddressData(14, "神奈川県");
        insertAddressData(15, "山梨県");
        insertAddressData(16, "新潟県");
        insertAddressData(17, "富山県");
        insertAddressData(18, "石川県");
        insertAddressData(19, "福井県");
        insertAddressData(20, "長野県");
        insertAddressData(21, "岐阜県");
        insertAddressData(22, "静岡県");
        insertAddressData(23, "愛知県");
        insertAddressData(24, "三重県");
        insertAddressData(25, "滋賀県");
        insertAddressData(26, "京都府");
        insertAddressData(27, "大阪府");
        insertAddressData(28, "兵庫県");
        insertAddressData(29, "奈良県");
        insertAddressData(30, "和歌山県");
        insertAddressData(31, "鳥取県");
        insertAddressData(32, "島根県");
        insertAddressData(33, "岡山県");
        insertAddressData(34, "広島県");
        insertAddressData(35, "山口県");
        insertAddressData(36, "徳島県");
        insertAddressData(37, "香川県");
        insertAddressData(38, "愛媛県");
        insertAddressData(39, "高知県");
        insertAddressData(40, "福岡県");
        insertAddressData(41, "佐賀県");
        insertAddressData(42, "長崎県");
        insertAddressData(43, "熊本県");
        insertAddressData(44, "大分県");
        insertAddressData(45, "宮崎県");
        insertAddressData(46, "鹿児島県");
        insertAddressData(47, "沖縄県");
    }

    public void insertAddressData(int id, String state) {
        String sql = getAdressInsertSql(id, state);
        mSQLiteDatabase.execSQL(sql);
    }

    public String getAdressInsertSql(int id, String stateStr) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(ADDRESS);
        sql.append(" ( ");
        sql.append(ID);
        sql.append(",");
        sql.append(STATE);
        sql.append(",");
        sql.append(COMPLETE_FLG);
        sql.append(" ) VALUES ( ");
        sql.append(id);
        sql.append(",'");
        sql.append(stateStr);
        sql.append("',");
        sql.append(0);
        sql.append(")");
        return sql.toString();
    }

    public Cursor execSelect(String sql) {
        Cursor cursor = null;
        try {
            cursor = mSQLiteDatabase.rawQuery(sql.toString(), null);
        } catch (Exception ex) {
            if (cursor != null)
                cursor.close();
        }
        return cursor;
    }

    public boolean execSql(String sqls) {
        mSQLiteDatabase.beginTransaction();
        try {
            mSQLiteDatabase.execSQL(sqls);
            mSQLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            return false;
        } finally {
            mSQLiteDatabase.endTransaction();
        }
    }

}