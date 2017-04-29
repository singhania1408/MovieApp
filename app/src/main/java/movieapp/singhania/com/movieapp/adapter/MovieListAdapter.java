package movieapp.singhania.com.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import movieapp.singhania.com.movieapp.ItemDetailActivity;
import movieapp.singhania.com.movieapp.ItemDetailFragment;
import movieapp.singhania.com.movieapp.R;
import movieapp.singhania.com.movieapp.dao.MovieColumns;
import movieapp.singhania.com.movieapp.model.MovieModel;
import movieapp.singhania.com.movieapp.utils.Constants;

/**
 * Created by mrsinghania on 1/3/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private List<MovieModel> movieModelList;
    private boolean mTwoPane = false;
    Context mContext;

    public MovieListAdapter(List<MovieModel> items, Context context) {
        movieModelList = items;
        this.mContext = context;
        mTwoPane = false;

    }

    public MovieListAdapter(List<MovieModel> items, Context context, boolean mTwoPane) {
        movieModelList = items;
        this.mContext = context;
        this.mTwoPane = mTwoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.movieModel = movieModelList.get(position);
        //holder.posterView.setText(mValues.get(position).id);
        String imageUrl = Constants.IMAGE_BASE_URL + Constants.IMAGE_GRID_STRING + holder.movieModel.getPosterPath();
        holder.titleView.setText(holder.movieModel.getTitle());

        if (holder.movieModel.isFav()) {
            holder.favIcon.setVisibility(View.VISIBLE);
        } else
            holder.favIcon.setVisibility(View.GONE);

        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.color.faded_color)
                .error(R.color.faded_color)
                .into(holder.posterView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable(ItemDetailFragment.ARG_ITEM_ID, holder.movieModel);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    if (mContext instanceof AppCompatActivity) {
                        ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    }
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.movieModel);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movieModelList != null)
            return movieModelList.size();
        else
            return 0;
    }

    public List<MovieModel> addFavData(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)

        if (c == null)
            return null;
        List<MovieModel> temp = new ArrayList<>();
        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                MovieModel movieModel = new MovieModel();
                movieModel.setId(c.getLong(c.getColumnIndex(MovieColumns._ID)));
                movieModel.setTitle(c.getString(c.getColumnIndex(MovieColumns.NAME_TITLE)));
                movieModel.setOverView(c.getString(c.getColumnIndex(MovieColumns.NAME_OVERVIEW)));
                movieModel.setPosterPath(c.getString(c.getColumnIndex(MovieColumns.NAME_POSTERPATH)));
                movieModel.setVote_average(c.getDouble(c.getColumnIndex(MovieColumns.NAME_VOTE_AVERAGE)));
                movieModel.setReleaseDate(c.getString(c.getColumnIndex(MovieColumns.NAME_RELEASE_DATE)));
                movieModel.setFav(true);
                temp.add(movieModel);
            }
        }
        List<MovieModel> tempReturn = movieModelList;
        this.movieModelList = temp;

       /* if(movieModelList==null)
            movieModelList=temp;
        else {
            movieModelList.addAll(temp);
        }*/
        this.notifyDataSetChanged();
        return tempReturn;
    }

    public List<MovieModel> swapCursor(List<MovieModel> c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (movieModelList == c) {
            return null; //  nothing has changed
        }
        List<MovieModel> temp = movieModelList;
        this.movieModelList = c; // new cursor value assigned

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

  /*  public List<MovieModel> addCursor(List<MovieModel> c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (movieModelList == c) {
            return null; //  nothing has changed
        }
        List<MovieModel> temp = movieModelList;
        this.movieModelList.addAll(c); // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView posterView, favIcon;
        public final TextView titleView;
        public MovieModel movieModel;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            favIcon = (ImageView) view.findViewById(R.id.favIcon);
            posterView = (ImageView) view.findViewById(R.id.posterMovie);
            titleView = (TextView) view.findViewById(R.id.titleMovie);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleView.getText() + "'";
        }
    }
}