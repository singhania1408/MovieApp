package movieapp.singhania.com.movieapp.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import movieapp.singhania.com.movieapp.R;
import movieapp.singhania.com.movieapp.model.ReviewModel;

/**
 * Created by mrsinghania on 1/3/17.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private ReviewModel[] reviewModelsList;
    private boolean mTwoPane = false;
    Context mContext;

    public ReviewListAdapter(ReviewModel[] items, Context context) {
        reviewModelsList = items;
        this.mContext = context;
        mTwoPane = false;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.reviewModel = reviewModelsList[position];
        holder.authorName.setText(holder.reviewModel.getAuthor());
        holder.content.setText(holder.reviewModel.getContent());
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
                watchContentUrl(holder.reviewModel.getUrl());
            }
        });
    }

    public void watchContentUrl(String link) {
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            mContext.startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (reviewModelsList != null)
            return reviewModelsList.length;
        else
            return 0;
    }

    public ReviewModel[] swapCursor(ReviewModel[] c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (reviewModelsList == c) {
            return null; // bc nothing has changed
        }
        ReviewModel[] temp = reviewModelsList;
        this.reviewModelsList = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView authorName, content;

        public ReviewModel reviewModel;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            authorName = (TextView) view.findViewById(R.id.authorName);
            content = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + authorName.getText() + "'";
        }
    }
}