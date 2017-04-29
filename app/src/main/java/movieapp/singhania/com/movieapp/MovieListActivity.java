package movieapp.singhania.com.movieapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import movieapp.singhania.com.movieapp.adapter.MovieListAdapter;
import movieapp.singhania.com.movieapp.dao.MovieProvider;
import movieapp.singhania.com.movieapp.model.MovieModel;
import movieapp.singhania.com.movieapp.utils.NetworkUtils;
import movieapp.singhania.com.movieapp.utils.ParseJSONResponse;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    List<MovieModel> movieModelList;
    private boolean mTwoPane;
    public static final int MOVIE_API_LOADER = 0;
    public static final int MOVIE_FAV_LOADER = 1;

    public static final int MOVIE_POPULAR = 0;
    public static final int MOVIE_TOP_RATED = 1;
    public static final int MOVIE_FAV = 2;

    public static final int SPAN_COUNT_MOVIE_LIST = 2;
    private GridLayoutManager gridLayoutManager;
    private MovieListAdapter movieListAdapter;
    private ProgressBar mLoadingIndicator;
    private Spinner sortSpinner;
    int sortOrder = MOVIE_POPULAR;
    boolean firstCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;

        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT_MOVIE_LIST);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        sortSpinner = (Spinner) findViewById(R.id.sort_spinner);

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getResources().getStringArray(R.array.sort_category));
        sortSpinner.setAdapter(sortAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                LoaderManager loaderManager = getSupportLoaderManager();
                if (i == 2) {
                    Loader<String> loader = loaderManager.getLoader(MOVIE_FAV_LOADER);
                    if (loader == null) {
                        loaderManager.initLoader(MOVIE_FAV_LOADER, bundle, localFavMovieCallBack);
                    } else {
                        loaderManager.restartLoader(MOVIE_FAV_LOADER, bundle, localFavMovieCallBack);
                    }
                } else {
                    if (i == 0)
                        sortOrder = MOVIE_POPULAR;
                    else if (i == 1)
                        sortOrder = MOVIE_TOP_RATED;

                    Loader<String> loaderCall = loaderManager.getLoader(MOVIE_API_LOADER);
                    if (loaderCall == null) {
                        loaderManager.initLoader(MOVIE_API_LOADER, bundle, MovieListActivity.this);
                    } else {
                        loaderManager.restartLoader(MOVIE_API_LOADER, bundle, MovieListActivity.this);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setupRecyclerView((RecyclerView) recyclerView);

        Bundle bundle = new Bundle();
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(MOVIE_API_LOADER);

        if (loader == null) {
            loaderManager.initLoader(MOVIE_API_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(MOVIE_API_LOADER, bundle, this);
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(gridLayoutManager);
        movieListAdapter = new MovieListAdapter(movieModelList, this, mTwoPane);
        recyclerView.setAdapter(movieListAdapter);
    }


    LoaderManager.LoaderCallbacks<Cursor> localFavMovieCallBack = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(MovieListActivity.this, MovieProvider.FavMovies.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            //mCursorAdapter.swapCursor(data);
            movieListAdapter.addFavData(data);


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // mCursorAdapter.swapCursor(null);
        }

    };

    @Override
    public Loader<String> onCreateLoader(int i, final Bundle bundle) {


        return new AsyncTaskLoader<String>(MovieListActivity.this) {

            @Override
            protected void onStartLoading() {
                if (bundle == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                URL url = NetworkUtils.buildUrl(NetworkUtils.MOVIE_POPULAR);

                switch (sortOrder) {
                    case MOVIE_POPULAR:
                        url = NetworkUtils.buildUrl(NetworkUtils.MOVIE_POPULAR);
                        break;
                    case MOVIE_TOP_RATED:
                        url = NetworkUtils.buildUrl(NetworkUtils.MOVIE_TOP_RATED);


                }
                try {
                    return NetworkUtils.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String response) {
        mLoadingIndicator.setVisibility(View.GONE);
        if (response != null) {
            String parseResponse = ParseJSONResponse.parseJSONResponse(response);
            movieModelList = ParseJSONResponse.parseResultObjectForMovieList(parseResponse);
            movieListAdapter.swapCursor(movieModelList);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

}
