package es.ibrands.bakingapp.util;

import es.ibrands.bakingapp.model.Recipe;

import io.reactivex.Observable;

import java.util.List;

import retrofit2.http.GET;

public interface BakingApiInterface
{
    @GET("topher/2017/May/59121517_baking/baking.json")
    Observable<List<Recipe>> getList();
}
