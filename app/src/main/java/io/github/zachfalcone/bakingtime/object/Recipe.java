package io.github.zachfalcone.bakingtime.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable {
    private int mId;
    private String mName;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;
    private int mServings;
    private String mImage;

    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, String image) {
        mId = id;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
        mServings = servings;
        mImage = image;
    }

    private Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        in.readTypedList(mIngredients, Ingredient.CREATOR);
        in.readTypedList(mSteps, Step.CREATOR);
        mServings = in.readInt();
        mImage = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeTypedList(mIngredients);
        dest.writeTypedList(mSteps);
        dest.writeInt(mServings);
        dest.writeString(mImage);
    }

    public String getName() {
        return mName;
    }

    public int getServings() {
        return mServings;
    }

    public int getNumberOfSteps() {
        return mSteps.size();
    }
}
