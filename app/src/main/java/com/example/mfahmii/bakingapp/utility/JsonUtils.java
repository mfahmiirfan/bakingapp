package com.example.mfahmii.bakingapp.utility;

import android.content.Context;
import android.util.Log;

import com.example.mfahmii.bakingapp.model.Ingredient;
import com.example.mfahmii.bakingapp.model.RecipeCard;
import com.example.mfahmii.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mfahmii on 8/5/2017.
 */

public class JsonUtils {



    private static final String TAG = JsonUtils.class.getSimpleName();

    public static ArrayList<RecipeCard> getRecipeCardsFromJson(Context context, String jsonStr)
            throws JSONException {

        final String TAG_ID="id";
        final String TAG_NAME="name";

        final String TAG_INGREDIENTS="ingredients";
        final String TAG_QUANTITY="quantity";
        final String TAG_MEASURE="measure";
        final String TAG_INGREDIENT="ingredient";

        final String TAG_STEPS="steps";
        final String TAG_STEP_ID="id";
        final String TAG_SHORT_DESCRIPTION="shortDescription";
        final String TAG_DESCRIPTION="description";
        final String TAG_VIDEO_URL="videoURL";
        final String TAG_THUMBNAIL_URL="thumbnailURL";

        final String TAG_SERVINGS="servings";
        final String TAG_IMAGE="image";

        /* String array to hold each day's weather String */
        ArrayList<RecipeCard> parsedRecipeCardsData=null;

        JSONArray json=new JSONArray(jsonStr);
        json.get(0).toString();

        ArrayList<RecipeCard> recipeCards=new ArrayList<RecipeCard>();
        RecipeCard recipeCard;

        for(int i=0;i<json.length();i++){
            int id;
            String name;
            int servings;
            String image;

            JSONArray ingredientsJSONArray;
            JSONArray stepsJSONArray;

            id=((JSONObject)json.get(i)).getInt(TAG_ID);
            name=((JSONObject)json.get(i)).getString(TAG_NAME);
            ingredientsJSONArray=((JSONObject)json.get(i)).getJSONArray(TAG_INGREDIENTS);
            stepsJSONArray=((JSONObject)json.get(i)).getJSONArray(TAG_STEPS);

            ArrayList<Ingredient> ingredients=new ArrayList<Ingredient>();
            Ingredient ingredient;

            for(int j=0;j<ingredientsJSONArray.length();j++){
                int qty;
                String msr;
                String igr;

                qty=((JSONObject)ingredientsJSONArray.get(j)).getInt(TAG_QUANTITY);
                msr=((JSONObject)ingredientsJSONArray.get(j)).getString(TAG_MEASURE);
                igr=((JSONObject)ingredientsJSONArray.get(j)).getString(TAG_INGREDIENT);

                ingredient=new Ingredient(qty,msr,igr);
                ingredients.add(ingredient);
            }

            ArrayList<Step> steps=new ArrayList<Step>();
            Step step;

            for(int j=0;j<stepsJSONArray.length();j++){
                int stpId;
                String srtDsc;
                String dsc;
                String vidURL;
                String tmbURL;

                stpId=((JSONObject)stepsJSONArray.get(j)).getInt(TAG_STEP_ID);
                srtDsc=((JSONObject)stepsJSONArray.get(j)).getString(TAG_SHORT_DESCRIPTION);
                dsc=((JSONObject)stepsJSONArray.get(j)).getString(TAG_DESCRIPTION);
                vidURL=((JSONObject)stepsJSONArray.get(j)).getString(TAG_VIDEO_URL);
                tmbURL=((JSONObject)stepsJSONArray.get(j)).getString(TAG_THUMBNAIL_URL);

                step=new Step(stpId,srtDsc,dsc,vidURL,tmbURL);
                steps.add(step);
            }

            servings=((JSONObject)json.get(i)).getInt(TAG_SERVINGS);
            image=((JSONObject)json.get(i)).getString(TAG_IMAGE);

            recipeCard=new RecipeCard(id,name,ingredients,steps,servings,image);
            recipeCards.add(recipeCard);
            Log.d("tag",recipeCard.getName());

        }

        parsedRecipeCardsData=new ArrayList<RecipeCard>(recipeCards);

        return parsedRecipeCardsData;
    }
}
