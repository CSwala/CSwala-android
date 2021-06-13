package com.cswala.cswala.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.Activities.WebActivity;
import com.cswala.cswala.Models.PortalListElement;
import com.cswala.cswala.R;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PortalListAdapter extends FirestorePagingAdapter<PortalListElement, PortalListAdapter.PortalHolder> {
    ArrayList<PortalListElement> data;
    ArrayList<PortalListElement> dataFull;
    Context ctx;
    private ProgressBar progressBar_morePortalData;

    public PortalListAdapter(Context ctx, ArrayList<PortalListElement> data,
                             FirestorePagingOptions<PortalListElement> options,
                             ProgressBar loadMorePortals) {
        super(options);
        this.data = data;
        this.ctx = ctx;
        dataFull = new ArrayList<>(data);
        progressBar_morePortalData = loadMorePortals;
    }

    @NonNull
    @Override
    public PortalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.portal_list_element, parent, false);
        return new PortalHolder(myView);
    }

    @Override
    protected void onBindViewHolder(@NonNull PortalHolder holder, final int position, final PortalListElement portalList) {
//        PortalHolder portalHolder = (PortalHolder) holder;
        holder.companyBtn.setText(portalList.getTitle());
        holder.companyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent external_link = null;

                external_link = new Intent(ctx,
                        WebActivity.class);
                external_link.putExtra("URL",portalList.getLink().trim());

                ctx.startActivity(external_link);

            }
        });
    }

    @Override
    protected void onError(@NonNull Exception e) {
        super.onError(e);
        Log.e("JOB PORTAL ADAPTER", e.getMessage());
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state) {
            case LOADING_INITIAL:
                Log.i("JOB PORTAL ADAPTER", "Loading initial data");
                break;
            case LOADING_MORE:
                Log.i("JOB PORTAL ADAPTER", "Loading more data");
                progressBar_morePortalData.setVisibility(View.VISIBLE);
                break;
            case LOADED:
                Log.i("JOB PORTAL ADAPTER", "Loaded data");
                progressBar_morePortalData.setVisibility(View.GONE);
                break;
            case ERROR:
                Log.e("JOB PORTAL ADAPTER", "An error occurred");
                break;
            case FINISHED:
                progressBar_morePortalData.setVisibility(View.GONE);
                Log.i("JOB PORTAL ADAPTER", "No more portals");
                break;
        }
    }


    public class PortalHolder extends RecyclerView.ViewHolder {
        Button companyBtn;

        public PortalHolder(@NonNull View itemView) {
            super(itemView);
            companyBtn = itemView.findViewById(R.id.name);
        }
    }
}
