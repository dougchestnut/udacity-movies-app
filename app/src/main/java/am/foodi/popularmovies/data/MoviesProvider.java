package am.foodi.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MoviesProvider extends ContentProvider {

    private MoviesDbHelper mOpenHelper;
//    private Context mContext;

    public static final int MOVIES = 101;
    public static final int FAVORITEMOVIES = 102;
    public static final int MOVIES_ID = 103;

    // The URI Matcher used by this content provider.
    private static final UriMatcher sURIMatcher = buildUriMatcher();
//    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//    static {
//        sURIMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,
//                MoviesContract.PATH_MOVIES,
//                MOVIES);
//        sURIMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,
//                MoviesContract.PATH_FAVORITEMOVIES,
//                FAVORITEMOVIES);
//        sURIMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,
//                MoviesContract.PATH_MOVIES + "/#",
//                MOVIES_ID);
//    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MoviesContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", MOVIES_ID);
        return matcher;
    }

    public MoviesProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mOpenHelper.getWritableDatabase();
        int rowsAffected = 0;
        switch (uriType) {
            case MOVIES:
                rowsAffected = sqlDB.delete(MoviesContract.MoviesEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sURIMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;
            case MOVIES_ID:
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case FAVORITEMOVIES:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sURIMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.MoviesEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
//        mContext = getContext();
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MoviesContract.MoviesEntry.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case MOVIES_ID:
                queryBuilder.appendWhere(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "="
                        + uri.getLastPathSegment());
                break;
            case FAVORITEMOVIES:
                queryBuilder.appendWhere(MoviesContract.MoviesEntry.COLUMN_FAVORITE + "=1");
                break;
            case MOVIES:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mOpenHelper.getWritableDatabase();
        int rowsAffected = 0;
        switch (uriType) {
            case MOVIES:
                rowsAffected = sqlDB.update(MoviesContract.MoviesEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(MoviesContract.MoviesEntry.TABLE_NAME, null, value, db.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
