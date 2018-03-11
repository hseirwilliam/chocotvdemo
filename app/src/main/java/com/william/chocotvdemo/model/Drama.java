package com.william.chocotvdemo.model;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.william.chocotvdemo.common.DBA;


@Table(name = DBA.Table.DRAMA)
public class Drama extends Model {

    @Column(name = "_id")
    public long id;
    @Column(name = DBA.Field.DRAMA_ID, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int drama_id;
    @Column
    public String name;
    @Column
    public int total_views;
    @Column
    public String created_at;
    @Column
    public String thumb;
    @Column
    public float rating;

    public static Cursor fetchResultCursor() {
        String tableName = Cache.getTableInfo(Drama.class).getTableName();
        // Query all items without any conditions
        String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").
                from(Drama.class).toSql();
        // Execute query on the underlying ActiveAndroid SQLite database
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        return resultCursor;
    }

}
