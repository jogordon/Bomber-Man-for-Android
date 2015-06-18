package edu.udel.jogordon.bombsgoboom.android;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.udel.jogordon.bombsgoboom.GameRecord;

public class GameDatabase {
    public static final String DATABASE_NAME = "BombsGoBoom!";
    public static final String TABLE_NAME = "Game_Records";

    private Context context;
    private SQLiteDatabase database;
    
    /**
     * Creates a database for the TicTacToe application. Creates/opens a connection
     * to the underlying Android database.
     * 
     * @param context
     */
    public GameDatabase(Context context) {
        super();
        this.context = context;
        this.database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        
        createGameRecordsTable();
    }
    
    public void close() {
        this.database.close();
    }
    
    /**
     * Creates a table in the database if it does not exist already.
     */
    private void createGameRecordsTable() {
        database.execSQL("CREATE TABLE IF NOT EXISTS "
                        + TABLE_NAME
                        + " (Player TEXT,"
                        + " Score INTEGER, Date INTEGER);");
    }
    
    /**
     * Resets the database table by dropping the entire table.
     * This can be useful to delete all data, but also useful if the
     * definition of what columns make up the table change.
     */
    public void deleteGameRecords() {
        database.execSQL("DROP TABLE " + TABLE_NAME);
        createGameRecordsTable();
    }
    
    /**
     * Inserts a single record (row) into the database table.
     * 
     * @param record
     */
    public void insertGameRecord(GameRecord record) {
        database.execSQL(getInsertGameRecordQuery(record));       
    }

    /**
     * Queries the GameRecords table to find the highest score game played
     * by any player and returns that record.
     * 
     * @return
     */
    public ArrayList<GameRecord> getFiveHighestScoreRecords() {
    	String query = "SELECT Player, Score, Date" +
    				   " FROM " + TABLE_NAME + 
    				   " ORDER BY Score DESC" +
    				   " LIMIT 0, 5";
    	Cursor c = database.rawQuery(query, null);
    	try {
    		GameRecord record = null;

    		/* Get the indices of the Columns we will need */
    		int playerColumn = c.getColumnIndex("Player");
    		int scoreColumn = c.getColumnIndex("Score");
    		int dateColumn = c.getColumnIndex("Date");
    
    		// get the first result
    		ArrayList<GameRecord> records = new ArrayList<GameRecord>();
    		boolean isNext = c.moveToFirst();
    		while(isNext) {
    			record = new GameRecord(
    			c.getString(playerColumn),
    			c.getInt(scoreColumn),
    			c.getLong(dateColumn));
    			records.add(record);
    			isNext = c.moveToNext();
    		}
    	System.out.println("Records size:" + records.size());
    	return records;
    	}
    	finally {
            c.close();
        }
    }
    public GameRecord getHighestScoreGameRecord() {
        String query = "SELECT Player, Score, Date" +
                        " FROM " + TABLE_NAME + 
                        " ORDER BY Score DESC" +
                        " LIMIT 0, 1";
        Cursor c = database.rawQuery(query, null);
        try {
            GameRecord record = null;

            /* Get the indices of the Columns we will need */
            int playerColumn = c.getColumnIndex("Player");
            int scoreColumn = c.getColumnIndex("Score");
            int dateColumn = c.getColumnIndex("Date");
            
            // get the first result
            if (c.moveToFirst()) {
                record = new GameRecord(
                    c.getString(playerColumn),
                    c.getInt(scoreColumn),
                    c.getLong(dateColumn));
            }
            
            return record;
        }
        finally {
            c.close();
        }
    }
    
    public String getInsertGameRecordQuery(GameRecord record) {
        return "INSERT INTO "
                + TABLE_NAME
                + " (Player, Score, Date)"
                + " VALUES ('" + record.getPlayer()
                + "', " + record.getScore()
                + ", " + record.getDate() + ")";
    }
}
