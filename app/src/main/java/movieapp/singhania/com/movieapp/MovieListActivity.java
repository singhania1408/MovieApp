package movieapp.singhania.com.movieapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
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


import movieapp.singhania.com.movieapp.adapter.MovieListAdapter;
import movieapp.singhania.com.movieapp.model.MovieModel;
import movieapp.singhania.com.movieapp.utils.NetworkUtils;
import movieapp.singhania.com.movieapp.utils.ParseJSONResponse;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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
    public static final int  MOVIE_API_LOADER=0;
    public static final int  MOVIE_POPULAR=0;
    public static final int  MOVIE_TOP_RATED=1;

    public static final int SPAN_COUNT_MOVIE_LIST=2;
    private GridLayoutManager gridLayoutManager;
    private MovieListAdapter movieListAdapter;
    private ProgressBar mLoadingIndicator;
    private Spinner sortSpinner;
    int sortOrder=MOVIE_POPULAR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());



        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;

        gridLayoutManager=new GridLayoutManager(this,SPAN_COUNT_MOVIE_LIST);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        sortSpinner=(Spinner) findViewById(R.id.sort_spinner);

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.sort_category));
        sortSpinner.setAdapter(sortAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                  @Override
                                                  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                      if(i==0)
                                                          sortOrder=MOVIE_POPULAR;
                                                      else if(i==1)
                                                          sortOrder=MOVIE_TOP_RATED;

                                                      Bundle bundle=new Bundle();
                                                      LoaderManager loaderManager=getSupportLoaderManager();
                                                      Loader<String> loader=loaderManager.getLoader(MOVIE_API_LOADER);

                                                      if(loader==null){
                                                          loaderManager.initLoader(MOVIE_API_LOADER,bundle,MovieListActivity.this);
                                                      }
                                                      else {
                                                          loaderManager.restartLoader(MOVIE_API_LOADER,bundle,MovieListActivity.this);
                                                      }
                                                  }

                                                  @Override
                                                  public void onNothingSelected(AdapterView<?> adapterView) {

                                                  }
                                              });
                setupRecyclerView((RecyclerView) recyclerView);

        Bundle bundle=new Bundle();
        LoaderManager loaderManager=getSupportLoaderManager();
        Loader<String> loader=loaderManager.getLoader(MOVIE_API_LOADER);

        if(loader==null){
            loaderManager.initLoader(MOVIE_API_LOADER,bundle,this);
        }
        else {
            loaderManager.restartLoader(MOVIE_API_LOADER,bundle,this);
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(gridLayoutManager);
        movieListAdapter=new MovieListAdapter(movieModelList,this,mTwoPane);
        recyclerView.setAdapter(movieListAdapter);
    }

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
                URL url=NetworkUtils.buildUrl(NetworkUtils.MOVIE_POPULAR);

                switch (sortOrder){
                    case MOVIE_POPULAR:
                        url=NetworkUtils.buildUrl(NetworkUtils.MOVIE_POPULAR);
                        break;
                    case MOVIE_TOP_RATED:
                        url=NetworkUtils.buildUrl(NetworkUtils.MOVIE_TOP_RATED);

                }
                try{
                    String response=NetworkUtils.getResponseFromHttpUrl(url);
                    return response;
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
        if(response!=null) {
            String parseResponse = ParseJSONResponse.parseJSONResponse(response);
            movieModelList = ParseJSONResponse.parseResultObjectForMovieList(parseResponse);
            movieListAdapter.swapCursor(movieModelList);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
