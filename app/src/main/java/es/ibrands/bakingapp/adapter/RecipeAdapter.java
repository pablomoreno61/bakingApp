package es.ibrands.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import es.ibrands.bakingapp.R;
import es.ibrands.bakingapp.activity.RecipeDetailActivity;
import es.ibrands.bakingapp.model.Recipe;

import java.util.List;

/**
 * Created by pablomoreno on 28/01/18.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>
{
    public static final String RECIPE_LIST = "recipeList";
    public static final String INGREDIENTS = "ingredients";
    public static final String STEPS = "steps";

    private Context context;
    private List<Recipe> recipeList;

    private SharedPreferences sharedPreferences;

    public RecipeAdapter(Context context, List<Recipe> recipeList)
    {
        this.context = context;
        this.recipeList = recipeList;

        sharedPreferences = context.getSharedPreferences(
            "preferences",
            Context.MODE_PRIVATE
        );
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.recipe_name_list_text_view)
        TextView recipeNameText;

        @BindView(R.id.servings_text_view)
        TextView servingText;

        @BindView(R.id.card_view)
        CardView cardView;

        @BindView(R.id.recipe_thumbnail_image_view)
        AppCompatImageView thumbView;

        public RecipeViewHolder(View view)
        {
            super(view);

            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            Gson gson = new Gson();

            int position = getAdapterPosition();

            String jsonRecipe = gson.toJson(recipeList.get(position));
            sharedPreferences.edit().putString(RECIPE_LIST, jsonRecipe).apply();

            String jsonIngredientList = gson.toJson(recipeList.get(position).getIngredients());
            String jsonStepList = gson.toJson(recipeList.get(position).getSteps());

            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(INGREDIENTS, jsonIngredientList);
            intent.putExtra(STEPS, jsonStepList);
            context.startActivity(intent);
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(
            R.layout.recipe_item_list,
            viewGroup,
            false
        );

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder recipeViewHolder, final int position)
    {
        recipeViewHolder.recipeNameText.setText(recipeList.get(position).getName());

        String servings = String.valueOf(recipeList.get(position).getServings());
        recipeViewHolder.servingText.setText(servings + " " + context.getString(R.string.servings));

        String imageUrl = recipeList.get(position).getImage();

        // throws an error if empty path
        if (!imageUrl.equals("")) {
            Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(recipeViewHolder.thumbView);
        }
    }

    @Override
    public int getItemCount()
    {
        return recipeList.size();
    }
}
