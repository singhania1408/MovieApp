package movieapp.singhania.com.movieapp;

import android.app.Activity;
import android.os.Build;
import android.renderscript.Double2;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import movieapp.singhania.com.movieapp.dummy.DummyContent;
import movieapp.singhania.com.movieapp.model.MovieModel;
import movieapp.singhania.com.movieapp.utils.Constants;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    MovieModel movieModel;
    TextView releaseDate;
    TextView userRating;

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

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            movieModel=(MovieModel)getArguments().getSerializable(ARG_ITEM_ID);

            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

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

        // Show the dummy content as text in a TextView.
        if (movieModel != null) {
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(movieModel.getOverView());

            userRating=(TextView) rootView.findViewById(R.id.movie_user_rating);
            releaseDate=(TextView) rootView.findViewById(R.id.movie_release_date);
            userRating.setText(Double.toString(movieModel.getVote_average()));
            userRating.append("/10");
            releaseDate.setText(movieModel.getReleaseDate());
        }

        return rootView;
    }
}
