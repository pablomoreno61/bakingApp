package es.ibrands.bakingapp.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import es.ibrands.bakingapp.fragment.VideoFragment;
import es.ibrands.bakingapp.R;

/**
 * Created by pablomoreno on 02/02/18.
 */
public class VideoPlayerActivity extends AppCompatActivity
{
    private static final String TAG = VideoPlayerActivity.class.getSimpleName();
    private static final String VIDEO_ROTATION = "videoRotation";

    private boolean isFragmentCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.video_player_title);

        if (savedInstanceState != null) {
            isFragmentCreated = savedInstanceState.getBoolean(VIDEO_ROTATION);
        }

        if (!isFragmentCreated) {
            Bundle bundle = getIntent().getExtras();

            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setArguments(bundle);

            isFragmentCreated = true;

            getSupportFragmentManager().beginTransaction().replace(
                R.id.video_fragment,
                videoFragment
            ).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(VIDEO_ROTATION, isFragmentCreated);
    }
}
