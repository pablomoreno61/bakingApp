package es.ibrands.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import es.ibrands.bakingapp.R;
import es.ibrands.bakingapp.adapter.RecipeAdapter;
import es.ibrands.bakingapp.model.Ingredient;
import es.ibrands.bakingapp.model.Recipe;

import java.util.List;

/**
 * Created by pablomoreno on 03/02/18.
 */
public class RecipeAppWidgetProvider extends AppWidgetProvider
{
    SharedPreferences sharedPreferences;

    public static void updateAppWidget(
        Context context,
        AppWidgetManager appWidgetManager,
        int appWidgetId,
        String recipeName,
        List<Ingredient> ingredientList
    ) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_app);
        remoteViews.setTextViewText(R.id.recipe_name_text_view, recipeName);
        remoteViews.removeAllViews(R.id.ingredients_widget);

        for (Ingredient ingredient : ingredientList) {
            RemoteViews ingredientView = new RemoteViews(
                context.getPackageName(),
                R.layout.widget_recipe_list_item
            );

            String text = ingredient.getQuantity() + " " + ingredient.getMeasure()
                + " " + ingredient.getIngredient();

            ingredientView.setTextViewText(
                R.id.ingredient_text_view,
                text
            );

            remoteViews.addView(R.id.ingredients_widget, ingredientView);
        }

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        sharedPreferences = context.getSharedPreferences(
            "preferences",
            Context.MODE_PRIVATE
        );

        String jsonRecipe = sharedPreferences.getString(RecipeAdapter.RECIPE_LIST, null);
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId,
                recipe.getName(),
                recipe.getIngredients()
            );
        }
    }

    @Override
    public void onEnabled(Context context)
    {
        // not implemented
    }

    @Override
    public void onDisabled(Context context)
    {
        // not implemented
    }
}

