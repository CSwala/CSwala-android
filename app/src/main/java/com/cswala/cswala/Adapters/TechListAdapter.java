package com.cswala.cswala.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.Models.PortalListElement;
import com.cswala.cswala.Models.TechListElement;
import com.cswala.cswala.R;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TechListAdapter extends FirestorePagingAdapter<TechListElement, TechListAdapter.TechHolder> {
    ArrayList<TechListElement> data;
    ArrayList<TechListElement> dataFull;
    Context ctx;
    private ProgressBar progressBar_moreTechListData;
    techItemClicked listener;

    public TechListAdapter(Context ctx, ArrayList<TechListElement> data,
                           FirestorePagingOptions<TechListElement> options,
                           ProgressBar loadMoreTechListElements,
                           techItemClicked listener) {
        super(options);
        this.data = data;
        this.ctx = ctx;
        this.listener=listener;
        dataFull=new ArrayList<>(data);
        progressBar_moreTechListData = loadMoreTechListElements;
    }

    @NotNull
    @Override
    public TechHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.tech_list_element,parent,false);
        final TechHolder holder= new TechHolder(myView);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(data.get(holder.getAdapterPosition()).getTechName());
            }
        });
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull TechHolder holder, int position, TechListElement techList) {
        holder.data.setText(techList.getTechName());
        holder.tag.setText(techList.getTechTag());
    }

    @Override
    protected void onError(@NonNull Exception e) {
        super.onError(e);
        Log.e("TECH LIST ADAPTER", e.getMessage());
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state) {
            case LOADING_INITIAL:
                Log.i("TECH LIST ADAPTER", "Loading initial data");
                break;
            case LOADING_MORE:
                Log.i("TECH LIST ADAPTER", "Loading more data");
                progressBar_moreTechListData.setVisibility(View.VISIBLE);
                break;
            case LOADED:
                Log.i("TECH LIST ADAPTER", "Loaded data");
                progressBar_moreTechListData.setVisibility(View.GONE);
                break;
            case ERROR:
                Log.e("TECH LIST ADAPTER", "An error occurred");
                break;
            case FINISHED:
                progressBar_moreTechListData.setVisibility(View.GONE);
                Log.i("TECH LIST ADAPTER", "No more tech lists");
                break;
        }
    }


    public static class TechHolder extends RecyclerView.ViewHolder {
        TextView data;
        TextView tag;
        public TechHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            data=itemView.findViewById(R.id.tech_data);
            tag=itemView.findViewById(R.id.tag_element);
        }
    }
}

