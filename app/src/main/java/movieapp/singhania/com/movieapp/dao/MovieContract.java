package movieapp.singhania.com.movieapp.dao;

/**
 * Created by mrsinghania on 25/03/17.
 * MovieContract for Fav Movies
 */

import android.provider.BaseColumns;

public final class MovieContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private MovieContract() {
    }

    /* Inner class that defines the table contents */
    public static class MovieFavEntry implements BaseColumns {
        public static final String TABLE_NAME = "movieFav";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_ADULT = "adult";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_VOTECOUNT = "vote_count";
        public static final String COLUMN_NAME_POSTERPATH = "posterPath";
        //public static final String COLUMN_NAME_OVERVIEW = "overview";

    }
}

