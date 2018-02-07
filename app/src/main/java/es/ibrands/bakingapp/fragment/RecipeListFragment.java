package es.ibrands.bakingapp.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.developers.coolprogressviews.DoubleArcProgress;

import es.ibrands.bakingapp.R;
import es.ibrands.bakingapp.activity.RecipeListActivity;
import es.ibrands.bakingapp.adapter.RecipeAdapter;
import es.ibrands.bakingapp.model.Recipe;
import es.ibrands.bakingapp.util.BakingApiInterface;
import es.ibrands.bakingapp.util.SimpleIdlingResource;
import es.ibrands.bakingapp.util.Utility;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

/**
 * Created by pablomoreno on 27/01/18.
 */
public class RecipeListFragment extends Fragment
{
    private static final String TAG = RecipeListFragment.class.getSimpleName();
    private static final String API_URL = "http://d17h27t6h515a5.cloudfront.net";
    private static Retrofit retrofit = null;

    SimpleIdlingResource idlingResource;

    @BindView(R.id.recipe_recycler_view)
    RecyclerView recipeRecyclerView;

    @BindView(R.id.double_progress_arc)
    DoubleArcProgress doubleArcProgress;

    private BakingApiInterface bakingApiInterface;
    private List<Recipe> mRecipeList;
    private boolean mTwoPane;
    private RecipeAdapter recipeAdapter;

    public RecipeListFragment()
    {
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.recipe_list_fragment, container, false);
        ButterKnife.bind(this, view);

        bakingApiInterface = createRetrofit();
        idlingResource = (SimpleIdlingResource) ((RecipeListActivity) getActivity()).getIdlingResource();
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        doubleArcProgress.setVisibility(View.VISIBLE);
        if (Utility.isConnected(getActivity())) {
            mRecipeList = getRecipeList();
        }

        return view;
    }

    public List<Recipe> getRecipeList()
    {
        bakingApiInterface.getList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<Recipe>>()
            {
                Disposable mDisposable;

                @Override
                public void onSubscribe(Disposable disposable)
                {
                    mDisposable = disposable;
                }

                @Override
                public void onNext(List<Recipe> recipeList)
                {
                    mRecipeList = recipeList;
                }

                @Override
                public void onError(Throwable e)
                {
                    String message = e.getMessage();
                    Log.d(TAG, message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete()
                {
                    if (!mDisposable.isDisposed()) {
                        mDisposable.dispose();
                    }

                    recipeAdapter = new RecipeAdapter(getActivity(), mRecipeList);
                    mTwoPane = RecipeListActivity.getPanelMode();

                    if (mTwoPane) {
                        // tablet mode
                        int noOfColumns = calculateGridNoOfColumns();

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                            getActivity(),
                            noOfColumns
                        );

                        recipeRecyclerView.setLayoutManager(gridLayoutManager);
                    } else {
                        // mobile mode
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                            getActivity()
                        );

                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recipeRecyclerView.setLayoutManager(linearLayoutManager);
                    }

                    recipeRecyclerView.setAdapter(recipeAdapter);
                    idlingResource.setIdleState(true);

                    doubleArcProgress.setVisibility(View.GONE);
                }

                private int calculateGridNoOfColumns()
                {
                    Resources resources = getActivity().getResources();
                    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
                    int scalingFactor = 180;
                    return (int) (dpWidth / scalingFactor);
                }
            });

        return mRecipeList;
    }

    public static BakingApiInterface createRetrofit()
    {
        if (retrofit == null) {
            retrofit = new Retrofit
                .Builder()
                .baseUrl(API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }

        return retrofit.create(BakingApiInterface.class);
    }
}
