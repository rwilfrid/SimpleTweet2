package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.CircleTransform;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.Glide.with;

//import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private Context context;
    private List<Tweet> tweets;
   //pass in context and list of tweets

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    //for each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet,parent,false);
        return new ViewHolder(view);
    }

    //bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
       Tweet tweet = tweets.get(i);
        holder.tvBody.setText(tweet.body);
        holder.tvNom.setText(tweet.user.nom);
        holder.tvScreenName.setText(tweet.user.screenName);
        with(context)
                .load(tweet.user.profileImageUrl)
                .transform(new CircleTransform(context))
                .into(holder.ivProfileImage);
       holder.tvTimestamp.setText(TimeFormatter.getTimeDifference(tweet.createdAt));

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear(){
       tweets.clear();
       notifyDataSetChanged();
    }

    public void addTweets(List<Tweet> tweetList){
        tweets.addAll(tweetList);
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivProfileImage;
        public ImageView ivMedia;
        public TextView tvNom;
        public TextView  tvScreenName;
        public TextView  tvBody;
        public TextView  tvTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            //ivMedia = itemView.findViewById(R.id.ivMedia);
            tvScreenName   = itemView.findViewById(R.id.tvScreenName);
            tvNom          = itemView.findViewById(R.id.tvNom);
            tvBody         = itemView.findViewById(R.id.tvBody);
            tvTimestamp    = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}
