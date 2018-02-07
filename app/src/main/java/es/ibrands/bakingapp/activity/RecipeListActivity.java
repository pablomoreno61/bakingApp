package es.ibrands.bakingapp.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import es.ibrands.bakingapp.R;
import es.ibrands.bakingapp.util.SimpleIdlingResource;

/**
 * Created by pablomoreno on 28/01/18.
 */
public class RecipeListActivity extends AppCompatActivity
{
    private static boolean mTwoPane;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource()
    {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }

        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list_activity);
        getSupportActionBar().setTitle(R.string.recipe_list_title);

        mTwoPane = false;
        if (findViewById(R.id.recipe_list_tablet_container) != null) {
            mTwoPane = true;
        }

        getIdlingResource();
    }

    public static boolean getPanelMode()
    {
        return mTwoPane;
    }
}
