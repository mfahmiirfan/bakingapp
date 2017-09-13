package com.example.mfahmii.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mfahmii.bakingapp.model.RecipeCard;
import com.example.mfahmii.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mfahmii on 8/11/2017.
 */

public class ProcedureListAdapter extends RecyclerView.Adapter<ProcedureListAdapter.ViewHolder> {
    ArrayList<String> mProcedureList;

    private final ProcedureListAdapter.ProcedureListAdapterOnClickHandler mClickHandler;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        @BindView(R.id.tv_procedure_list) TextView mTextView;
        public ViewHolder(View v) {
            super(v);
//            mTextView = (TextView) v.findViewById(R.id.tv_procedure_list);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            mClickHandler.onClick(adapterPosition-1);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProcedureListAdapter(ProcedureListAdapter.ProcedureListAdapterOnClickHandler mClickHandler, ArrayList<String> mProcedureList) {
        this.mClickHandler=mClickHandler;
        this.mProcedureList=mProcedureList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProcedureListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.procedure_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ProcedureListAdapter.ViewHolder vh = new ProcedureListAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProcedureListAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(position==0)
            holder.mTextView.setText(mProcedureList.get(position));
        else
            holder.mTextView.setText(position-1 +". "+mProcedureList.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mProcedureList==null) return 0;
        return mProcedureList.size();

    }

    public interface ProcedureListAdapterOnClickHandler {
        void onClick(int position);
    }
}
