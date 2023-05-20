package gr.auth.csd.filmtime.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "filmtime.db";
    public static final String TABLE_SCENES = "scenes";
    public static final String TABLE_CREW_MEMBERS = "crew_members";
    public static final String TABLE_SCENE_CREW_MEMBERS = "scene_crew_members";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SCENE_ID = "scene_id";
    public static final String COLUMN_MEMBER_ID = "member_id";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String CREATE_SCENES_TABLE = "CREATE TABLE " +
                TABLE_SCENES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                "name TEXT" +
                ")";
        db.execSQL(CREATE_SCENES_TABLE);

        String CREATE_CREW_MEMBERS_TABLE = "CREATE TABLE " +
                TABLE_CREW_MEMBERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                "name TEXT," +
                "job TEXT" +
                ")";
        db.execSQL(CREATE_CREW_MEMBERS_TABLE);

        String CREATE_SCENE_CREW_MEMBERS_TABLE = "CREATE TABLE " +
                TABLE_SCENE_CREW_MEMBERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_SCENE_ID + " INTEGER," +
                COLUMN_MEMBER_ID + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_SCENE_ID + ") REFERENCES " +
                TABLE_SCENES + "(" + COLUMN_ID + ")," +
                "FOREIGN KEY(" + COLUMN_MEMBER_ID + ") REFERENCES " +
                TABLE_CREW_MEMBERS + "(" + COLUMN_ID + ")" +
                ")";
        db.execSQL(CREATE_SCENE_CREW_MEMBERS_TABLE);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCENE_CREW_MEMBERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCENES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREW_MEMBERS);
        onCreate(db);
    }

    public void addCrewMemberToScene(long sceneId, long crewMemberId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCENE_ID, sceneId);
        values.put(COLUMN_MEMBER_ID, crewMemberId);
        db.insert(TABLE_SCENE_CREW_MEMBERS, null, values);
        db.close();
    }

    public void removeCrewMemberFromScene(long sceneId, long crewMemberId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCENE_CREW_MEMBERS,
                COLUMN_SCENE_ID + " = ? AND " + COLUMN_MEMBER_ID + " = ?",
                new String[]{String.valueOf(sceneId), String.valueOf(crewMemberId)});
        db.close();
    }

    public ArrayList<CrewMember> getCrewMembersForScene(long sceneId) {
        ArrayList<CrewMember> crewMembers = new ArrayList<>();

        String query = "SELECT " + TABLE_CREW_MEMBERS + "." + COLUMN_ID +
                ", " + TABLE_CREW_MEMBERS + ".name" +
                ", " + TABLE_CREW_MEMBERS + ".job" +
                " FROM " + TABLE_SCENE_CREW_MEMBERS +
                " JOIN " + TABLE_CREW_MEMBERS +
                " ON " + TABLE_SCENE_CREW_MEMBERS + "." + COLUMN_MEMBER_ID + " = " +
                TABLE_CREW_MEMBERS + "." + COLUMN_ID +
                " WHERE " + COLUMN_SCENE_ID + " = " + sceneId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int crewMemberId = cursor.getInt(0);
                String name = cursor.getString(1);
                String job = cursor.getString(2);
                CrewMember crewMember = new CrewMember(crewMemberId, name, job);
                crewMembers.add(crewMember);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return crewMembers;
    }

    public long createCrewMember(@NonNull CrewMember crewMember) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", crewMember.getName());
        values.put("job", crewMember.getJob());
        long crewMemberId = db.insert(TABLE_CREW_MEMBERS, null, values);
        db.close();
        return crewMemberId;
    }

    public long createScene(@NonNull Scene scene) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", scene.getName());
        long sceneId = db.insert(TABLE_SCENES, null, values);
        db.close();
        return sceneId;
    }

    public void debug_open_database_and_dont_close_it(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("SELECT * FROM "+TABLE_SCENES,null);
    }


    public ArrayList<Scene> getScenes() {
        ArrayList<Scene> scenes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCENES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                long sceneId = cursor.getLong(0);
                String name = cursor.getString(1);
                Scene scene = new Scene(sceneId, name, getCrewMembersForScene(sceneId));
                scenes.add(scene);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return scenes;
    }


}
