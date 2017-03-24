package movieapp.singhania.com.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import movieapp.singhania.com.movieapp.ItemDetailActivity;
import movieapp.singhania.com.movieapp.ItemDetailFragment;
import movieapp.singhania.com.movieapp.R;
import movieapp.singhania.com.movieapp.model.MovieModel;
import movieapp.singhania.com.movieapp.model.TrailerMovieModel;
import movieapp.singhania.com.movieapp.utils.Constants;

/**
 * Created by mrsinghania on 1/3/17.
 */

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder> {

    private List<TrailerMovieModel> trailerMovieModelList;
    private boolean mTwoPane=false;
    Context mContext;

    public TrailerListAdapter(List<TrailerMovieModel> items, Context context) {
        trailerMovieModelList = items;
        this.mContext=context;
        mTwoPane=false;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.trailerMovieModel = trailerMovieModelList.get(position);
        //holder.posterView.setText(mValues.get(position).id);
        /*String imageUrl=Constants.IMAGE_BASE_URL + Constants.IMAGE_GRID_STRING + holder.trailerMovieModel.getPosterPath();
        holder.titleView.setText(holder.trailerMovieModel.getTitle());

        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.color.faded_color)
                .error(R.color.faded_color)
                .into(holder.posterView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.movieModel);
                    context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        if(trailerMovieModelList !=null)
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
        public final ImageView posterView;
        public final TextView titleView;
        public TrailerMovieModel trailerMovieModel;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            posterView = (ImageView) view.findViewById(R.id.posterMovie);
            titleView = (TextView) view.findViewById(R.id.titleMovie);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleView.getText() + "'";
        }
    }
}