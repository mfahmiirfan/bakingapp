package com.example.mfahmii.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mfahmii on 8/9/2017.
 */

public class Ingredient implements Parcelable {
    private int quantity;
    private String measure;
    private String ingredient;

    public Ingredient(int quantity, String measure, String ingredient){
        this.quantity=quantity;
        this.measure=measure;
        this.ingredient=ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    protected Ingredient(Parcel in) {
        this.quantity = in.readInt();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
