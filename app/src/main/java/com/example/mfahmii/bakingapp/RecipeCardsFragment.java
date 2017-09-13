package com.example.mfahmii.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mfahmii.bakingapp.model.Ingredient;
import com.example.mfahmii.bakingapp.model.RecipeCard;
import com.example.mfahmii.bakingapp.utility.JsonUtils;
import com.example.mfahmii.bakingapp.utility.NetworkUtils;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mfahmii on 8/5/2017.
 */

public class RecipeCardsFragment extends Fragment implements RecipeCardsAdapter.RecipeCardsAdapterOnClickHandler {

    @BindView(R.id.recipe_cards_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    private Unbinder unbinder;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecipeCardsAdapter mRecipeCardsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =inflater.inflate(R.layout.fragment_recipe_cards, container, false);

//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_cards_recycler_view);
//        mErrorMessageDisplay = (TextView) rootView.findViewById(R.id.tv_error_message_display);
//        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);
        ButterKnife.bind(this, rootView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        if(getActivity().getResources().getBoolean(R.bool.isTablet)){
            Log.d("tag","ini tablet");
            mLayoutManager = new GridLayoutManager(getContext(),3);
            mRecyclerView.setLayoutManager(mLayoutManager);

        }else{
            Log.d("tag","ini dudu tablet");
            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
        }



        // specify an adapter (see also next example)
        mRecipeCardsAdapter = new RecipeCardsAdapter(this);
        mRecyclerView.setAdapter(mRecipeCardsAdapter);

        LoadRecipeCardsData();

        // Inflate the layout for this fragment
        return rootView;
    }

    private void LoadRecipeCardsData() {
        ShowRecipeCardsDataView();

        new FetchMoviesTask().execute();
    }

    private void ShowRecipeCardsDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void ShowErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(RecipeCard recipeCard) {
        Context context = getActivity();

        Class destinationClass = RecipeDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        intentToStartDetailActivity.putExtra("test",recipeCard);
        startActivity(intentToStartDetailActivity);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<RecipeCard>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<RecipeCard> doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
//            if (params.length == 0) {
//                return null;
//            }

            URL recipeCardsRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonRecipeCardsResponse = NetworkUtils
                        .getResponseFromHttpUrl(recipeCardsRequestUrl);

                ArrayList<RecipeCard> recipeCardsData = JsonUtils
                        .getRecipeCardsFromJson(getActivity(), jsonRecipeCardsResponse);

//                if(Boolean.valueOf(params[2])){
//
//                    String[] simpleMoviesData=new String[getContentResolver().query(
//                            FavMovieContract.FavMovieEntry.CONTENT_URI,
//                            null,
//                            FavMovieContract.FavMovieEntry.COLUMN_SORTBY + " = " + params[0],
//                            null,
//                            null
//                    ).getCount()];
//
//                    int i=0;
//                    for (String favMov: simpleJsonMoviesData
//                            ) {
//                        Cursor cursor = getContentResolver().query(
//                                FavMovieContract.FavMovieEntry.CONTENT_URI,
//                                null,
//                                FavMovieContract.FavMovieEntry.COLUMN_ID + " = " +favMov.split("\\^")[0].substring(0,favMov.split("\\^")[0].length()-1),
//                                null,
//                                null
//                        );
//                        if (cursor != null) {
//                            if(cursor.getCount() != 0){
//                                simpleMoviesData[i]=favMov;
//
//                                i++;
//                            }
//                            cursor.close();
//                        }
//                    }
//                    return simpleMoviesData;
//                }

                return recipeCardsData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<RecipeCard> recipeCardsData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (recipeCardsData != null) {
                ShowRecipeCardsDataView();
                mRecipeCardsAdapter.setmRecipeCards(recipeCardsData);
                String recipeName="";
                String recipeId="";
                String recipeIgStr="";
                ArrayList<Ingredient> recipeIg=new ArrayList<Ingredient>();
                for(int i=0;i<recipeCardsData.size();i++){
                    recipeName=recipeName+"^"+recipeCardsData.get(i).getName();
                    recipeId=recipeId+"^"+recipeCardsData.get(i).getId();
                    recipeIg=recipeCardsData.get(i).getIngredients();
//                    Log.d("tug","----------");
                    String rIg="";
                    for(int j=0;j<recipeIg.size();j++){
                        rIg=rIg+recipeIg.get(j).getIngredient()+"("+recipeIg.get(j).getQuantity()+" "+recipeIg.get(j).getMeasure()+")\n";
//                        Log.d("tug",recipeIg.get(j).getIngredient()+"("+recipeIg.get(j).getQuantity()+" "+recipeIg.get(j).getMeasure()+")\n");
                    }
                    recipeIgStr=recipeIgStr+"^"+rIg;
                }

                    Log.d("tug",recipeIgStr);
//                Log.d("tug",datas);
//                Log.d("tug",datas.split("\\^")[1]);
                SharedPreferences.Editor editor =getContext().getSharedPreferences("namaewa",Context.MODE_PRIVATE).edit();
                editor.putString("RECIPE_NAME",recipeName);
                editor.putString("RECIPE_ID",recipeId);
                editor.putString("RECIPE_INGREDIENTS",recipeIgStr);
                editor.apply();
//                mMoviesAdapter.setWeatherData(moviesData);
//                mRecyclerView.setVerticalScrollbarPosition(scrollPosition);
//                mRecyclerView.scrollToPosition(scrollPosition);
            } else {
                ShowErrorMessage();
            }
        }
    }

    public class ObjectWrapperForBinder extends Binder {

        private final Object mData;

        public ObjectWrapperForBinder(Object data) {
            mData = data;
        }

        public Object getData() {
            return mData;
        }
    }
}
