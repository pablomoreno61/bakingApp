package es.ibrands.bakingapp.activity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import es.ibrands.bakingapp.adapter.RecipeAdapter;
import es.ibrands.bakingapp.fragment.RecipeDetailFragment;
import es.ibrands.bakingapp.R;
import es.ibrands.bakingapp.fragment.VideoFragment;
import es.ibrands.bakingapp.model.Recipe;
import es.ibrands.bakingapp.widget.RecipeAppWidgetProvider;

/**
 * Created by pablomoreno on 30/01/18.
 */
public class RecipeDetailActivity extends AppCompatActivity
{
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String RECIPE_DETAIL_ROTATION = "recipeDetailRotation";
    private boolean mRecipeDetailRotation;
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.recipe_detail_title);
        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            mRecipeDetailRotation = savedInstanceState.getBoolean(RECIPE_DETAIL_ROTATION);
        }

        if (findViewById(R.id.video_player_tablet) != null) {
            twoPane = true;

            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.video_player_tablet, new VideoFragment()).commit();
        }

        if (savedInstanceState == null) {
            String stepJson = getIntent().getStringExtra(RecipeAdapter.STEPS);
            String ingredientJson = getIntent().getStringExtra(RecipeAdapter.INGREDIENTS);
            Bundle bundle = new Bundle();
            bundle.putString(RecipeDetailFragment.STEPS_DATA, stepJson);
            bundle.putString(RecipeDetailFragment.INGREDIENTS_DATA, ingredientJson);
            bundle.putBoolean(RecipeDetailFragment.PANE, twoPane);

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(
                R.id.recipe_detail_fragment,
                recipeDetailFragment
            ).commit();

            mRecipeDetailRotation = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.add_widget_action) {
            SharedPreferences sharedPreferences = getSharedPreferences(
                "preferences",
                Context.MODE_PRIVATE
            );

            Gson gson = new Gson();
            Recipe recipe = gson.fromJson(
                sharedPreferences.getString(RecipeAdapter.RECIPE_LIST, null),
                Recipe.class
            );

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            Bundle bundle = new Bundle();
            int appWidgetId = bundle.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            );

            RecipeAppWidgetProvider.updateAppWidget(
                this,
                appWidgetManager,
                appWidgetId,
                recipe.getName(),
                recipe.getIngredients()
            );

            Toast.makeText(
                this,
                recipe.getName() + " " + getString(R.string.added_to_widget_message),
                Toast.LENGTH_SHORT
            ).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.getBoolean(RECIPE_DETAIL_ROTATION, mRecipeDetailRotation);
    }
}
