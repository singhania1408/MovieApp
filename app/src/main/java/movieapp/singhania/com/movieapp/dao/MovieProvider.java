package movieapp.singhania.com.movieapp.dao;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by mrsinghania on 9/4/17.
 */
@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public class MovieProvider {
    public static final String AUTHORITY =
            "movieapp.singhania.com.movieapp.dao.MovieProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String FAVMOVIES = "fav_movies";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.FAV_MOVIES)
    public static class FavMovies {
        @ContentUri(
                path = Path.FAVMOVIES,
                type = "vnd.android.cursor.dir/movies",
                defaultSort = MovieColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.FAVMOVIES);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.FAVMOVIES + "/#",
                type = "vnd.android.cursor.item/movies",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.FAVMOVIES, String.valueOf(id));
        }
    }
}
