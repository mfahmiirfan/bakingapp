package com.example.mfahmii.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mfahmii.bakingapp.utility.JsonUtils;

import java.util.ArrayList;

/**
 * Created by mfahmii on 8/9/2017.
 */

public class RecipeCard implements Parcelable {
    private int id;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private int servings;
    private String image;

    public RecipeCard(int id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, int servings, String image){
        this.id=id;
        this.name=name;
        this.ingredients=ingredients;
        this.steps=steps;
        this.servings=servings;
        this.image=image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.ingredients);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeList(this.steps);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
    }

    protected RecipeCard(Parcel in) {
        this.ingredients = new ArrayList<Ingredient>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());
        this.id = in.readInt();
        this.name = in.readString();
        this.steps = new ArrayList<Step>();
        in.readList(this.steps, Step.class.getClassLoader());
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<RecipeCard> CREATOR = new Parcelable.Creator<RecipeCard>() {
        @Override
        public RecipeCard createFromParcel(Parcel source) {
            return new RecipeCard(source);
        }

        @Override
        public RecipeCard[] newArray(int size) {
            return new RecipeCard[size];
        }
    };
}
