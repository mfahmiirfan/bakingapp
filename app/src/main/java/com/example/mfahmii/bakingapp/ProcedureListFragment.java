package com.example.mfahmii.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.mfahmii.bakingapp.model.Ingredient;
import com.example.mfahmii.bakingapp.model.RecipeCard;
import com.example.mfahmii.bakingapp.model.Step;
import com.example.mfahmii.bakingapp.utility.SimpleDividerItemDecoration;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mfahmii on 8/10/2017.
 */

public class ProcedureListFragment extends Fragment implements ProcedureListAdapter.ProcedureListAdapterOnClickHandler{
    @BindView(R.id.procedure_list_recycler_view) RecyclerView mRecyclerView;
    private Unbinder unbinder;

    private RecyclerView.LayoutManager mLayoutManager;
    private ProcedureListAdapter mProcedureListAdapter;

    OnListItemSelected mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback=(OnListItemSelected)context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement OnListItemSelected");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_procedure_list, container, false);

//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.procedure_list_recycler_view);
        ButterKnife.bind(this, rootView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<String> procedure=new ArrayList<String>();
        procedure.add("Ingredients");

        RecipeCard recipeCard=getActivity().getIntent().getParcelableExtra("test");

        ArrayList<Step> steps=recipeCard.getSteps();
        for(int i=0;i<steps.size();i++){
            procedure.add(steps.get(i).getShortDescription());
        }

        // specify an adapter (see also next example)
        mProcedureListAdapter = new ProcedureListAdapter(this,procedure);

        mRecyclerView.setAdapter(mProcedureListAdapter);

        return rootView;
    }

    @Override
    public void onClick(int stepPosition) {
        mCallback.ShowProcedureDetail(stepPosition);
    }

    public interface OnListItemSelected{
        void ShowProcedureDetail(int stepPosition);
    }
}
