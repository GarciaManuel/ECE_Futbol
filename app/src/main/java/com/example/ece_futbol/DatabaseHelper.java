package com.example.ece_futbol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "beachVolleyball";

    // Table Names
    private static final String TABLE_MATCHGAME = "match_games";
    private static final String TABLE_PLAYER = "player";
    private static final String TABLE_TEAM = "team";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_GAMESETS0 = "game_sets0";
    private static final String KEY_GAMESETS1 = "game_sets1";
    private static final String KEY_GAMESETS2 = "game_sets2";
    private static final String KEY_GAMESETS3 = "game_sets3";
    private static final String KEY_GAMESETS4 = "game_sets4";

    // MatchGames Table - column names
    private static final String KEY_TEAMANAME = "teamA_name";
    private static final String KEY_TEAMBNAME = "teamB_name";

    // Teams Table - column names
    private static final String KEY_PLAYER1 = "player1";
    private static final String KEY_PLAYER2 = "player2";
    private static final String KEY_FOURHITS = "four_hits";
    private static final String KEY_SERVICEORDER = "service_order";

    // Players Table - column names
    private static final String KEY_ASSISTEDHIT = "assisted_hit";
    private static final String KEY_DOUBLECONTACT = "double_contact";
    private static final String KEY_CATCHLIFT = "catch_lift";
    private static final String KEY_FOOT = "foot";
    private static final String KEY_NETTOUCH = "net_touch";

    // MatchGame table create statement
    private static final  String CREATE_TABLE_MATCHGAME =
            "CREATE TABLE " + TABLE_MATCHGAME + " (" +
                    KEY_ID + "INTEGER PRIMARY KEY, " +
                    KEY_TEAMANAME + " TEXT, " +
                    KEY_TEAMBNAME + " TEXT, " +
                    KEY_GAMESETS0 + " INTEGER, " +
                    KEY_GAMESETS1 + " INTEGER, " +
                    KEY_GAMESETS2 + " INTEGER, " +
                    KEY_GAMESETS3 + " INTEGER, " +
                    KEY_GAMESETS4 + " INTEGER) ";

    // Team table create statement
    private static final  String CREATE_TABLE_TEAM =
            "CREATE TABLE " + TABLE_TEAM + " (" +
                    KEY_ID + "INTEGER PRIMARY KEY, " +
                    KEY_NAME + "TEXT, " +
                    KEY_PLAYER1 + " TEXT, " +
                    KEY_PLAYER2 + " TEXT, " +
                    KEY_FOURHITS + " INTEGER, " +
                    KEY_SERVICEORDER + " INTEGER, " +
                    KEY_GAMESETS0 + " INTEGER, " +
                    KEY_GAMESETS1 + " INTEGER, " +
                    KEY_GAMESETS2 + " INTEGER, " +
                    KEY_GAMESETS3 + " INTEGER, " +
                    KEY_GAMESETS4 + " INTEGER) ";

    // Player table create statement
    private static final  String CREATE_TABLE_PLAYER =
            "CREATE TABLE " + TABLE_PLAYER + " (" +
                    KEY_ID + "INTEGER PRIMARY KEY, " +
                    KEY_NAME + "TEXT, " +
                    KEY_ASSISTEDHIT + " INTEGER, " +
                    KEY_DOUBLECONTACT + " INTEGER, " +
                    KEY_CATCHLIFT + " INTEGER, " +
                    KEY_FOOT + " INTEGER, " +
                    KEY_NETTOUCH + " INTEGER) ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MATCHGAME);
        db.execSQL(CREATE_TABLE_TEAM);
        db.execSQL(CREATE_TABLE_PLAYER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHGAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
        onCreate(db);
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHGAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);

        db.execSQL(CREATE_TABLE_MATCHGAME);
        db.execSQL(CREATE_TABLE_TEAM);
        db.execSQL(CREATE_TABLE_PLAYER);
    }

    public long createMatchGame(MatchGame matchGame) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID, matchGame.getId());
        values.put(KEY_TEAMANAME, matchGame.getTeamAName());
        values.put(KEY_TEAMBNAME, matchGame.getTeamBName());
        values.put(KEY_GAMESETS0, matchGame.getGameSets0());
        values.put(KEY_GAMESETS1, matchGame.getGameSets1());
        values.put(KEY_GAMESETS2, matchGame.getGameSets2());
        values.put(KEY_GAMESETS3, matchGame.getGameSets3());
        values.put(KEY_GAMESETS4, matchGame.getGameSets4());

        return db.insert(TABLE_MATCHGAME, null, values);
    }

    public MatchGame getMatchGame(long matchGame_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_MATCHGAME +
                " WHERE " + KEY_ID + " = " + matchGame_id;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }

        MatchGame mg = new MatchGame();
        mg.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        mg.setTeamAName(c.getString(c.getColumnIndex(KEY_TEAMANAME)));
        mg.setTeamBName(c.getString(c.getColumnIndex(KEY_TEAMBNAME)));
        mg.setGameSets0(c.getInt(c.getColumnIndex(KEY_GAMESETS0)));
        mg.setGameSets1(c.getInt(c.getColumnIndex(KEY_GAMESETS1)));
        mg.setGameSets2(c.getInt(c.getColumnIndex(KEY_GAMESETS2)));
        mg.setGameSets3(c.getInt(c.getColumnIndex(KEY_GAMESETS3)));
        mg.setGameSets4(c.getInt(c.getColumnIndex(KEY_GAMESETS4)));

        return mg;
    }

    public void updateMatchGame(MatchGame matchGame) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID, matchGame.getId());
//        values.put(KEY_TEAMANAME, matchGame.getTeamAName());
//        values.put(KEY_TEAMBNAME, matchGame.getTeamBName());
        values.put(KEY_GAMESETS0, matchGame.getGameSets0());
        values.put(KEY_GAMESETS1, matchGame.getGameSets1());
        values.put(KEY_GAMESETS2, matchGame.getGameSets2());
        values.put(KEY_GAMESETS3, matchGame.getGameSets3());
        values.put(KEY_GAMESETS4, matchGame.getGameSets4());

        db.update(TABLE_MATCHGAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(matchGame.getId())});
    }

    public void createTeam(Team team) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, team.getName());
        values.put(KEY_PLAYER1, team.getPlayer1());
        values.put(KEY_PLAYER2, team.getPlayer2());
        values.put(KEY_GAMESETS0, team.getGameSets0());
        values.put(KEY_GAMESETS1, team.getGameSets1());
        values.put(KEY_GAMESETS2, team.getGameSets2());
        values.put(KEY_GAMESETS3, team.getGameSets3());
        values.put(KEY_GAMESETS4, team.getGameSets4());

        db.insert(TABLE_TEAM, null, values);
    }

    public Team getTeam(String team_name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TEAM +
                " WHERE " + KEY_NAME + " LIKE " + team_name;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }

        Team t = new Team();
        t.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        t.setPlayer1(c.getString(c.getColumnIndex(KEY_PLAYER1)));
        t.setPlayer2(c.getString(c.getColumnIndex(KEY_PLAYER2)));
        t.setGameSets0(c.getInt(c.getColumnIndex(KEY_GAMESETS0)));
        t.setGameSets1(c.getInt(c.getColumnIndex(KEY_GAMESETS1)));
        t.setGameSets2(c.getInt(c.getColumnIndex(KEY_GAMESETS2)));
        t.setGameSets3(c.getInt(c.getColumnIndex(KEY_GAMESETS3)));
        t.setGameSets4(c.getInt(c.getColumnIndex(KEY_GAMESETS4)));

        return t;
    }

    public void updateTeam(Team team) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, team.getName());
        values.put(KEY_PLAYER1, team.getPlayer1());
        values.put(KEY_PLAYER2, team.getPlayer2());
        values.put(KEY_GAMESETS0, team.getGameSets0());
        values.put(KEY_GAMESETS1, team.getGameSets1());
        values.put(KEY_GAMESETS2, team.getGameSets2());
        values.put(KEY_GAMESETS3, team.getGameSets3());
        values.put(KEY_GAMESETS4, team.getGameSets4());

        db.update(TABLE_TEAM, values, KEY_NAME + " LIKE ?",
                new String[] {team.getName()});
    }

    public void createPlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, player.getName());
        values.put(KEY_ASSISTEDHIT, player.getAssistedHit());
        values.put(KEY_DOUBLECONTACT, player.getDoubleContact());
        values.put(KEY_CATCHLIFT, player.getCatchLift());
        values.put(KEY_FOOT, player.getFoot());
        values.put(KEY_NETTOUCH, player.getNetTouch());

        db.insert(TABLE_PLAYER, null, values);
    }

    public Player getPlayer(String player_name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PLAYER +
                " WHERE " + KEY_NAME + " LIKE " + player_name;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }

        Player t = new Player();
        t.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        t.setAssistedHit(c.getInt(c.getColumnIndex(KEY_ASSISTEDHIT)));
        t.setDoubleContact(c.getInt(c.getColumnIndex(KEY_DOUBLECONTACT)));
        t.setCatchLift(c.getInt(c.getColumnIndex(KEY_CATCHLIFT)));
        t.setFoot(c.getInt(c.getColumnIndex(KEY_FOOT)));
        t.setNetTouch(c.getInt(c.getColumnIndex(KEY_NETTOUCH)));

        return t;
    }

    public void updatePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, player.getName());
        values.put(KEY_ASSISTEDHIT, player.getAssistedHit());
        values.put(KEY_DOUBLECONTACT, player.getDoubleContact());
        values.put(KEY_CATCHLIFT, player.getCatchLift());
        values.put(KEY_FOOT, player.getFoot());
        values.put(KEY_NETTOUCH, player.getNetTouch());

        db.update(TABLE_PLAYER, values, KEY_NAME + " LIKE ?",
                new String[] {player.getName()});
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
