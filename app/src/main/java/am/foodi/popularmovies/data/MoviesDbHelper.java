/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package am.foodi.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import am.foodi.popularmovies.data.MoviesContract.MoviesEntry;

/**
 * Manages a local database for weather data.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "movies.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY," +
                MoviesContract.MoviesEntry.COLUMN_ADULT + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_LANGUAGE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_DATE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_VIDEO + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_FAVORITE + " INTEGER);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
