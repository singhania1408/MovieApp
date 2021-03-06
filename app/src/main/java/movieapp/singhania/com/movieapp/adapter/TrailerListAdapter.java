package movieapp.singhania.com.movieapp.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import movieapp.singhania.com.movieapp.R;
import movieapp.singhania.com.movieapp.model.TrailerMovieModel;

/**
 * Created by mrsinghania on 1/3/17.
 */

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder> {

    private List<TrailerMovieModel> trailerMovieModelList;
    private boolean mTwoPane = false;
    Context mContext;

    public TrailerListAdapter(List<TrailerMovieModel> items, Context context) {
        trailerMovieModelList = items;
        this.mContext = context;
        mTwoPane = false;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.trailerMovieModel = trailerMovieModelList.get(position);
        holder.trailerTitle.setText(holder.trailerMovieModel.getName());
        /*String imageUrl=Constants.IMAGE_BASE_URL + Constants.IMAGE_GRID_STRING + holder.trailerMovieModel.getPosterPath();
        holder.titleView.setText(holder.trailerMovieModel.getTitle());

        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.color.faded_color)
                .error(R.color.faded_color)
                .into(holder.posterView);

        h*/
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo(holder.trailerMovieModel.getKey());
            }
        });
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            mContext.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            mContext.startActivity(webIntent);
        }
    }

    @Override
    public int getItemCount() {
        if (trailerMovieModelList != null)
            return trailerMovieModelList.size();
        else
            return 0;
    }

    public List<TrailerMovieModel> swapCursor(List<TrailerMovieModel> c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (trailerMovieModelList == c) {
            return null; // bc nothing has changed
        }
        List<TrailerMovieModel> temp = trailerMovieModelList;
        this.trailerMovieModelList = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView trailerIcon;
        public final TextView trailerTitle;
        public TrailerMovieModel trailerMovieModel;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            trailerIcon = (ImageView) view.findViewById(R.id.trailerIcon);
            trailerTitle = (TextView) view.findViewById(R.id.trailerName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + trailerTitle.getText() + "'";
        }
    }
}