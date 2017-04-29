package movieapp.singhania.com.movieapp.dao;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by mrsinghania on 9/4/17.
 */
@Database(
        version = MovieDatabase.VERSION)
public class MovieDatabase {
    private MovieDatabase() {
    }

    public static final int VERSION = 3;

    @Table(MovieColumns.class)
    public static final String FAV_MOVIES = "fav_movies";
}
