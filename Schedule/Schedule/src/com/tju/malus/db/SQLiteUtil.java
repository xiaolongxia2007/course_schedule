package com.tju.malus.db;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteUtil {
	
	public static final String SQLite_MASTER_TABLE = "sqlite_master";
	private static SQLiteUtil mInstance = new SQLiteUtil();
	private  static final String TAG = "SQLiteUtil";
	
	private SQLiteUtil()
	{
		
	}
	
	/**
	 * @return instance of SQLiteUtil
	 */
	public static synchronized SQLiteUtil getInstance()
	{
		
		return SQLiteUtil.mInstance;
	}
	
	/**
	 * open or create a database with the given daName;
	 * /data/data/packagename/databases/databasefilename
	 * /sdcard/databasefilename
	 * 
	 * @param dbName
	 * @return SQLiteDatabase
	 */
	private synchronized SQLiteDatabase openDB(String dbName)
	{
		File file = new File(dbName);
		
		if(file.exists())
		{
			
			return SQLiteDatabase.openDatabase(dbName, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		}
		else
		{
			File dataFileDir = file.getParentFile();
			if(!dataFileDir.exists())
			{
				dataFileDir.mkdirs();
			}
			
			return SQLiteDatabase.openOrCreateDatabase(dbName, null);
		}
	}
	
	/**
	 * close database
	 * @param db
	 */
	private synchronized void closeDB(SQLiteDatabase db)
	{
		db.close();
	}
	
	/**
	 * delete database
	 * @param dbName
	 * @return
	 */
	public boolean deleteDB(String dbName)
	{
		File file = new File(dbName);
		if(file.exists())
		{
			return file.delete();
		}
		return true;
	}
	
	/**
	 * execute sql command
	 * @param dbName
	 * @param sql
	 */
	public void execQuery(String dbName, String sql)
	{
		SQLiteDatabase db = openDB(dbName);
		db.execSQL(sql);
		closeDB(db);
	}
	
	/**
	 * execute sql command and return Cursor
	 * @param dbName
	 * @param tableName
	 * @param condStr
	 * @return
	 */
	public Cursor openQuery(String dbName, String tableName, String condStr)
	{
		SQLiteDatabase db = openDB(dbName);
		Cursor cursor = db.query(tableName, null, condStr, null, null, null, null);
		closeDB(db);
		return (cursor);
	}
	
	/**
	 * execute sql command and return Cursor
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public Cursor openQuery(String dbName, String sql)
	{
		SQLiteDatabase db = null;
		try
		{
			Log.v(TAG, sql);
			db = openDB(dbName);
			Cursor cursor = db.rawQuery(sql, null);
			Log.v(TAG, "Result Count: " + cursor.getCount());
			return cursor;
		}
		catch(RuntimeException ex)
		{
			Log.v(TAG, ex.getMessage());
			throw ex;
		}
		finally
		{
			closeDB(db);
		}
	}
	
/*	private void deallocCachedSqlStatements()
	{
		try
		{
			class c = class.
		}
	}*/
	
	public int getRowsCount(Cursor cursor)
	{
		return cursor.getCount();
	}
	
	public int getColumnCount(Cursor cursor)
	{
		return cursor.getColumnCount();
	}
	
	public String getColumnNameByIndex(Cursor cursor, int index)
	{
		return cursor.getColumnName(index);
	}
	
	public boolean isBOF(Cursor cursor)
	{
		return cursor.isBeforeFirst();
	}
	
	public boolean isEOF(Cursor cursor)
	{
		return cursor.isAfterLast();
	}
	
	public boolean moveNext(Cursor cursor)
	{
		return cursor.moveToNext();
	}
	
	public String getField(Cursor cursor, int index)
	{
		return cursor.getString(index);
	}
	
	public void closeQuery(Cursor cursor)
	{
		cursor.close();
	}
	
	public boolean isTableExists(String dbName, String tableName)
	{
		Cursor cursor = openQuery(dbName, SQLite_MASTER_TABLE, "(tb1_name='" + tableName + "')");
		int recordCount = cursor.getCount();
		cursor.close();
		return (recordCount > 0);
	}
	
	public boolean isDatabaseExists(String dbName)
	{
		return new File(dbName).exists();
	}
	
	public static void copyDatabase(Context context, String dbName, int resourceId)
	{
		try
		{
			File file = new File(dbName);
			if(!file.exists())
			{
				InputStream inputStream = context.getResources().openRawResource(resourceId);
				FileOutputStream fos = new FileOutputStream(dbName);
				/*byte[] buffer = new byte[1024];
				int count = 0;
				while((count = inputStream.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}*/
				IOUtils.copy(inputStream, fos);
				
				fos.close();
				inputStream.close();
			}
		}
		catch(Exception ex)
		{
			Log.v(TAG, ex.getMessage());
		}
	}

}
