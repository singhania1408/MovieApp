package movieapp.singhania.com.movieapp.dao;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by mrsinghania on 9/4/17.
 */

public interface MovieColumns {


    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    public static final String _ID =
            "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String NAME_TITLE = "title";

    @DataType(DataType.Type.TEXT)
    public static final String NAME_OVERVIEW = "overview";

    @DataType(DataType.Type.TEXT)
    public static final String NAME_ADULT = "adult";

    @DataType(DataType.Type.TEXT)
    public static final String NAME_RELEASE_DATE = "release_date";

    @DataType(DataType.Type.INTEGER)
    public static final String NAME_VOTE_AVERAGE = "vote_average";

    @DataType(DataType.Type.TEXT)
    public static final String NAME_POSTERPATH = "posterPath";
    //
}
