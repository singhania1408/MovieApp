package movieapp.singhania.com.movieapp;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import movieapp.singhania.com.movieapp.adapter.MovieListAdapter;
import movieapp.singhania.com.movieapp.adapter.TrailerListAdapter;
import movieapp.singhania.com.movieapp.dummy.DummyContent;
import movieapp.singhania.com.movieapp.model.MovieModel;
import movieapp.singhania.com.movieapp.model.TrailerMovieModel;
import movieapp.singhania.com.movieapp.utils.Constants;
import movieapp.singhania.com.movieapp.utils.NetworkUtils;
import movieapp.singhania.com.movieapp.utils.ParseJSONResponse;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final int  MOVIE_TRAILER=0;
    public static final int  MOVIE_REVIEWS=1;
    MovieModel movieModel;
    TrailerMovieModel trailerMovieModel;
    List<TrailerMovieModel> trailerMovieModelList;
    TrailerListAdapter trailerListAdapter;
    TextView releaseDate;
    TextView userRating;
    RecyclerView trailerListView;
    private ProgressBar mLoadingIndicator;


    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if(savedInstanceState!=null){

            if (getArguments().containsKey(ARG_ITEM_ID)) {

                movieModel=(MovieModel)getArguments().getSerializable(ARG_ITEM_ID);

                Activity activity = this.getActivity();

                final CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null && movieModel!=null) {
                    appBarLayout.setTitle(movieModel.getTitle());
                    String imageUrl=Constants.IMAGE_BASE_URL+Constants.IMAGE_POSTER_STRING+movieModel.getPosterPath();


                    final ImageView imageView=new ImageView(getContext());
                    Picasso.with(activity)
                            .load(imageUrl)
                            .placeholder(R.color.faded_color)
                            .error(R.color.faded_color)
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        appBarLayout.setBackground(imageView.getDrawable());
                                    }
                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
            }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        if (movieModel != null) {
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(movieModel.getOverView());

            userRating=(TextView) rootView.findViewById(R.id.movie_user_rating);
            releaseDate=(TextView) rootView.findViewById(R.id.movie_release_date);
            userRating.setText(Double.toString(movieModel.getVote_average()));
            userRating.append("/10");
            trailerListView=(RecyclerView) rootView.findViewById(R.id.trailerList);
            setupRecyclerView(trailerListView);
            releaseDate.setText(movieModel.getReleaseDate());

            LoaderManager loaderManager=getActivity().getSupportLoaderManager();
            Loader<String> loader=loaderManager.getLoader(MOVIE_TRAILER);
            Bundle bundle=new Bundle();

            if(loader==null){
                loaderManager.initLoader(MOVIE_TRAILER,bundle,this);
            }
            else {
                loaderManager.restartLoader(MOVIE_TRAILER,bundle,this);
            }
        }

        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        trailerListAdapter=new TrailerListAdapter(trailerMovieModelList,getActivity());
        recyclerView.setAdapter(trailerListAdapter);
    }
    @Override
    public Loader<String> onCreateLoader(final int i, final Bundle bundle) {


        return new AsyncTaskLoader<String>(getActivity()) {

            @Override
            protected void onStartLoading() {
                if (bundle == null) {
                    return;
                }
                //mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                URL url= NetworkUtils.buildUrl(NetworkUtils.MOVIE_POPULAR);
                url=NetworkUtils.buildUrl(NetworkUtils.MOVIE_TRAILER);
                switch (i){
                    case MOVIE_REVIEWS:
                        url=NetworkUtils.buildUrl(movieModel.getId()+NetworkUtils.MOVIE_REVIEW);
                        break;
                    case MOVIE_TRAILER:
                        url=NetworkUtils.buildUrl(movieModel.getId()+NetworkUtils.MOVIE_TRAILER);
                        break;
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
        //mLoadingIndicator.setVisibility(View.GONE);
        if(response!=null) {
            String parseResponse;
            parseResponse = ParseJSONResponse.parseJSONResponse(response);

            switch (loader.getId()){
                case MOVIE_TRAILER:
                    trailerMovieModelList = ParseJSONResponse.parseResultObjectForTrailerList(parseResponse);

                    break;
                case MOVIE_REVIEWS:
                    break;
            }
            trailerListAdapter.swapCursor(trailerMovieModelList);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

}
