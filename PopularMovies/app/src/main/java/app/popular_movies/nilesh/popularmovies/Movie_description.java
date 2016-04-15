package app.popular_movies.nilesh.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;

import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class Movie_description extends AppCompatActivity {
    ImageView poster,title;
    TextView year,average,synopsis,mTrailer,mReview,moTitle;
    FloatingActionButton share;
    String mTitle,mBackdrop_Image,mOverview,mVote,mRelease_Date,mPoster_Image;

    RatingBar r;
    float rate;
    double d;
    ScrollView v;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_description);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        poster= (ImageView) findViewById(R.id.poster);
        year= (TextView) findViewById(R.id.year);
        average= (TextView) findViewById(R.id.average);
        title= (ImageView) findViewById(R.id.mtitle);
        synopsis= (TextView) findViewById(R.id.synopsis);
        share= (FloatingActionButton) findViewById(R.id.fab);
        r= (RatingBar) findViewById(R.id.rating);

        moTitle= (TextView) findViewById(R.id.motitle);
        v= (ScrollView) findViewById(R.id.sview);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {


           mTitle = extras.getString("title");
           mBackdrop_Image =extras.getString("b_img");
           mOverview = extras.getString("overview");
           mVote = extras.getString("vote");
            mRelease_Date = extras.getString("r_date");
            mPoster_Image = extras.getString("p_img");
        }
        Glide.with(getApplicationContext()).load(Uri.parse(mPoster_Image)).error(R.drawable.placeholder).into(poster);
        Glide.with(getApplicationContext()).load(Uri.parse(mBackdrop_Image)).error(R.drawable.placeholder).into(title);
        year.setText(mRelease_Date);
        average.setText(mVote);
        moTitle.setText(mTitle);
        synopsis.setText(mOverview);
        d=Double.parseDouble(mVote);
        rate=(float)d;
        Log.i("test", String.valueOf(rate));
        r.setRating(rate / 2);
share.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTitle);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mOverview);
        startActivity(Intent.createChooser(sharingIntent,"Share Via"));
    }
});
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
