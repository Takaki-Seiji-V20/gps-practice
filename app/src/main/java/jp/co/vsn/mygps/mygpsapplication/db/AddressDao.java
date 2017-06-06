package jp.co.vsn.mygps.mygpsapplication.db;

import static jp.co.vsn.mygps.mygpsapplication.db.Address.*;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class AddressDao {
    private DatabaseHelper mDatabaseHelper;

    public AddressDao(Context context) {
        mDatabaseHelper = DatabaseHelper.getInstance(context);
        mDatabaseHelper.open();
    }

    public List<AddressEntity> selectAll() {
        List<AddressEntity> list = new ArrayList<AddressEntity>();
        Cursor cursor = mDatabaseHelper.execSelect("SELECT * FROM " + ADDRESS);
        AddressEntity addressEntity;
        while (cursor.moveToNext()) {
            addressEntity = new AddressEntity();
            int idIndex = cursor.getColumnIndex(ID.toString());
            if (idIndex >= 0) {
                addressEntity.setId(cursor.getInt(idIndex));
            }
            int stateIndex = cursor.getColumnIndex(STATE.toString());
            if (stateIndex >= 0) {
                addressEntity.setState(cursor.getString(stateIndex));
            }
            int compIndex = cursor.getColumnIndex(COMPLETE_FLG.toString());
            if (compIndex >= 0) {
                addressEntity.setComp(cursor.getInt(compIndex));
            }
            list.add(addressEntity);
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public void updateCompWhereState(String state) {
        String sql = "UPDATE " + ADDRESS +
                    " SET " + COMPLETE_FLG + " = 1 " +
                    " WHERE " + STATE + " = '" + state + "' ";
        mDatabaseHelper.execSql(sql);
    }
}
