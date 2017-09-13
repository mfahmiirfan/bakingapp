package com.example.mfahmii.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.mfahmii.bakingapp.model.RecipeCard;

/**
 * Created by mfahmii on 8/10/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity implements ProcedureListFragment.OnListItemSelected{

    private boolean mTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        FragmentManager fm=getSupportFragmentManager();

        FragmentTransaction fragmentTransaction=fm.beginTransaction();

        if(findViewById(R.id.procedure_detail_fragment_container) != null){
            mTwoPane=true;

            if(savedInstanceState==null){
            fragmentTransaction.add(R.id.procedure_list_fragment_container, new ProcedureListFragment())
                        .add(R.id.procedure_detail_fragment_container, new ProcedureDetailFragment())
                        .commit();}

        }else{
            mTwoPane=false;

            if(savedInstanceState==null){
                fragmentTransaction.add(R.id.procedure_list_fragment_container, new ProcedureListFragment())
                        .commit();
            }

        }

    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
        super.onBackPressed();
    }

    @Override
    public void ShowProcedureDetail(int stepPosition) {
        if(mTwoPane){
            ProcedureDetailFragment procedureDetailFragment= (ProcedureDetailFragment) getSupportFragmentManager().findFragmentById(R.id.procedure_detail_fragment_container);
            procedureDetailFragment.UpdateView(stepPosition);
        }else{
            ProcedureDetailFragment procedureDetailFragment= new ProcedureDetailFragment();
            procedureDetailFragment.SetStepPosition(stepPosition);
            getSupportFragmentManager().beginTransaction().replace(R.id.procedure_list_fragment_container,procedureDetailFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

}
