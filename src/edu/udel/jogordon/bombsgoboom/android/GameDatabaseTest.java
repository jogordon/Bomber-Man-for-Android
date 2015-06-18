package edu.udel.jogordon.bombsgoboom.android;

import android.content.Context;
import android.test.AndroidTestCase;
import edu.udel.jogordon.bombsgoboom.GameRecord;

public class GameDatabaseTest extends AndroidTestCase {
    
    
    public void test_TicTacToeDatabaseQueries() {
        Context context = getContext();
        
        GameDatabase database = new GameDatabase(context);
        
        GameRecord testRecord = new GameRecord(
            "FOO", 100, Long.valueOf(1));
        
        assertEquals("INSERT INTO Game_Records (Player, Score, Date) VALUES ('FOO', 100, 1)",
            database.getInsertGameRecordQuery(testRecord));
    }
    
    public void test_TicTacToeDatabase() {
        Context context = getContext();
        
        GameDatabase database = new GameDatabase(context);
        // delete game records to start
        database.deleteGameRecords();

        Long date = System.currentTimeMillis();
        
        database.insertGameRecord(new GameRecord("FOO", 100, date+0));
        database.insertGameRecord(new GameRecord("ACE", 200, date+1));
        database.insertGameRecord(new GameRecord("FOO", 50, date+2));
        database.insertGameRecord(new GameRecord("ACE", 60, date+3));
        
        
        GameRecord record = database.getHighestScoreGameRecord();
        
        assertEquals("ACE", record.getPlayer());
        assertEquals(200, record.getScore().intValue());
    }
    
}
