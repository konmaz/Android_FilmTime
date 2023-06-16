package gr.auth.csd.filmtime.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

/**
 * A helper class for managing interactions with an SQLite database used to store Scenes and CrewMembers (Assets).
 * It extends the SQLiteOpenHelper class to provide methods for creating, upgrading, and managing the database.
 */
public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "filmtime.db";
    public static final String TABLE_SCENES = "scenes";
    public static final String TABLE_CREW_MEMBERS = "crew_members";
    public static final String TABLE_SCENE_CREW_MEMBERS = "scene_crew_members";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SCENE_ID = "scene_id";
    public static final String COLUMN_MEMBER_ID = "member_id";

    public static final String COLUMN_AVAILABILITIES = "availabilities";

    /**
     * Create a new database handler.
     * @param context The context from which the database was created.
     */
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Serializes a TreeSet and returns a bytes array
     * @param set THe TreeSet to serialize
     * @return A byte array containing the serialized TreeSet; Return null if there was an error
     */
    private byte[] serialize(TreeSet<LocalDate> set) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(set);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deserializes a bytes array and returns the TreeSet
     * @param bytes A byte array containing the serialized TreeSet
     * @return The TreeSet
     */
    private TreeSet<LocalDate> deserialize(byte[] bytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (TreeSet<LocalDate>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Created the database
     * @param db The database.
     */
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
                "job TEXT," +
                COLUMN_AVAILABILITIES + " BLOB"+
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
                TABLE_CREW_MEMBERS + "(" + COLUMN_ID + ")," +
                "UNIQUE(" + COLUMN_SCENE_ID + ", " + COLUMN_MEMBER_ID + ")" +
                ")";
        db.execSQL(CREATE_SCENE_CREW_MEMBERS_TABLE);
    }

    /** On database update delete everything and start oven.
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
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

    /** Returns the CrewMembers that are joining a scene.
     * @param sceneId The scene ID.
     * @return All the crew members objects that are joining a scene.
     */
    public HashSet<CrewMember> getCrewMembersForScene(long sceneId) {
        HashSet<CrewMember> crewMembers = new HashSet<>();

        String query = "SELECT " + TABLE_CREW_MEMBERS + "." + COLUMN_ID +
                ", " + TABLE_CREW_MEMBERS + ".name" +
                ", " + TABLE_CREW_MEMBERS + ".job" +
                ", " + TABLE_CREW_MEMBERS + "."+COLUMN_AVAILABILITIES +
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
                byte[] serializedAvailabilities = cursor.getBlob(3);
                TreeSet<LocalDate> availabilities = deserialize(serializedAvailabilities);

                CrewMember crewMember = new CrewMember(crewMemberId, name, job);
                crewMember.setAvailabilities(availabilities);
                crewMembers.add(crewMember);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return crewMembers;
    }

    /** Creates a Crew Member and save it to the DataBase
     * @param crewMember The crew member
     * @return The id of the Crew Member as it saved in the DataBase
     */
    public long createCrewMember(@NonNull CrewMember crewMember) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", crewMember.getName());
        values.put("job", crewMember.getJob());
        values.put(COLUMN_AVAILABILITIES, serialize(crewMember.getAvailabilities()));
        long crewMemberId = db.insert(TABLE_CREW_MEMBERS, null, values);
        db.close();
        return crewMemberId;
    }

    /** Creates a scene and save it to the DataBase
     * @param scene The crew member
     * @return The id of the Scene as it saved in the DataBase
     */
    public long createScene(@NonNull Scene scene) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", scene.getName());
        long sceneId = db.insert(TABLE_SCENES, null, values);
        db.close();
        ArrayList<Long> sceneCrewMembersIDs = new ArrayList<>(scene.getCrewMembersSize());
        for (CrewMember item : scene.getCrewMembers()) {sceneCrewMembersIDs.add(item.getID());}
        updateSceneCrewMembers(sceneId, sceneCrewMembersIDs);

        return sceneId;
    }

//    public void debug_open_database_and_dont_close_it(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.rawQuery("SELECT * FROM "+TABLE_SCENES,null);
//    }

    /**
     * Fetch a scene from the database, giving the id of the scene
     * @param id The id of the Scene
     * @return The scene object
     */
    public Scene getScene(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the scenes table to retrieve the scene with the given id
        Cursor cursor = db.query(TABLE_SCENES, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        Scene scene = null;

        if (cursor.moveToFirst()) {
            String name = cursor.getString(1);
            scene = new Scene(id, name, getCrewMembersForScene(id));
        }

        cursor.close();
        db.close();

        return scene;
    }

    /**
     * @return A array list containing all the scenes that are saved in the database
     */
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

    /**
     * Fetch a crew member from the database, giving the id of it
     * @param id The id of the Crew member
     * @return The scene object
     */
    public CrewMember getCrewMember(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CREW_MEMBERS, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        CrewMember crewMember = null;

        if (cursor.moveToFirst()) {
            String name = cursor.getString(1);
            String job = cursor.getString(2);

            byte[] serializedAvailabilities = cursor.getBlob(3);
            TreeSet<LocalDate> availabilities = deserialize(serializedAvailabilities);

            crewMember = new CrewMember(id, name, job);
            crewMember.setAvailabilities(availabilities);
        }

        cursor.close();
        db.close();

        return crewMember;
    }

    /**
     * @return A linked hash set containing all the Crew Members that are saved in the database
     */
    public LinkedHashSet<CrewMember> getCrewMembers() {
        LinkedHashSet<CrewMember> crew_members = new LinkedHashSet<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CREW_MEMBERS, null, null, null, null, null, "name ASC");

        if (cursor.moveToFirst()) {
            do {
                CrewMember crewMember = new CrewMember(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2));
                byte[] serializedAvailabilities = cursor.getBlob(3);
                TreeSet<LocalDate> availabilities = deserialize(serializedAvailabilities);
                crewMember.setAvailabilities(availabilities);


                crew_members.add(crewMember);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return crew_members;
    }

    /**
     * Make sure that the scene you are updating has been added to the database first
     * @param scene Overwrites all the information of the scene saving all the information
     * @param updateSceneCrewMembers If true it also updates all the Crew Members that are in the scene
     */
    public void updateScene(Scene scene, boolean updateSceneCrewMembers) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", scene.getName());
        db.update(TABLE_SCENES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(scene.getID())});
        db.close();
        if (updateSceneCrewMembers) {
            ArrayList<Long> sceneCrewMembersIDs = new ArrayList<>(scene.getCrewMembersSize());
            for (CrewMember item : scene.getCrewMembers()) {
                sceneCrewMembersIDs.add(item.getID());
            }
            updateSceneCrewMembers(scene.getID(), sceneCrewMembersIDs);
        }
    }

    /**
     * Make sure that the crew member you are updating has been added to the database first
     * @param crewMember Overwrites all the information of the crew member saving all the information
     */
    public void updateCrewMember(CrewMember crewMember) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", crewMember.getName());
        values.put("job", crewMember.getJob());
        values.put(COLUMN_AVAILABILITIES, serialize(crewMember.getAvailabilities()));
        db.update(TABLE_CREW_MEMBERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(crewMember.getID())});
        db.close();
    }

    /**
     * Deletes a scene given the ID
     * @param sceneId The scene ID
     */
    public void deleteScene(long sceneId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SCENE_CREW_MEMBERS, COLUMN_SCENE_ID + " = ?", new String[]{String.valueOf(sceneId)}); // also delete all the participants in the scene
        db.delete(TABLE_SCENES, COLUMN_ID + " = ?", new String[]{String.valueOf(sceneId)});
        db.close();
    }

    /**
     * Deletes a crew member given the ID, also it deletes all the partisipition of the crew member
     * @param crewMemberId The Crew Member ID
     */
    public void deleteCrewMember(long crewMemberId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SCENE_CREW_MEMBERS, COLUMN_MEMBER_ID + " = ?", new String[]{String.valueOf(crewMemberId)});
        db.delete(TABLE_CREW_MEMBERS, COLUMN_ID + " = ?", new String[]{String.valueOf(crewMemberId)});
        db.close();
    }
    /**
     * Update the participation list of a scene
     * @param sceneId The scene ID
     * @param crewMemberIds A list of the ID's of all the members that are participating in the scene
     */
    public void updateSceneCrewMembers(long sceneId, List<Long> crewMemberIds) {
        SQLiteDatabase db = getWritableDatabase();

        // First, delete existing scene-crew-member relationships for the given scene ID
        db.delete(TABLE_SCENE_CREW_MEMBERS, COLUMN_SCENE_ID + " = ?", new String[]{String.valueOf(sceneId)});

        // Insert new scene-crew-member relationships for the given crew member IDs
        for (Long crewMemberId : crewMemberIds) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_SCENE_ID, sceneId);
            values.put(COLUMN_MEMBER_ID, crewMemberId);
            db.insert(TABLE_SCENE_CREW_MEMBERS, null, values);
        }

        db.close();
    }



}
