package com.cswala.cswala.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.Models.PortalListElement;
import com.cswala.cswala.R;

import java.util.ArrayList;
import java.util.List;

public class PortalListAdapter extends RecyclerView.Adapter<PortalListAdapter.PortalHolder> implements Filterable {
    ArrayList<PortalListElement> data;
    ArrayList<PortalListElement> dataFull;
    Context ctx;

    public PortalListAdapter(Context ctx, ArrayList<PortalListElement> data) {
        this.data = data;
        this.ctx = ctx;
        dataFull = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public PortalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.portal_list_element, parent, false);
        return new PortalHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull PortalHolder holder, final int position) {
        holder.companyBtn.setText(data.get(position).getTitle());
        holder.companyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.startActivity(
                        new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(data.get(position).getLink())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<PortalListElement> sample = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                sample.addAll(dataFull);
            } else {
                String pattern = constraint.toString().toLowerCase().trim();
                for (PortalListElement data : dataFull) {
                    String item = data.getTitle();
                    if (item.toLowerCase().contains(pattern)) {
                        sample.add(data);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = sample;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class PortalHolder extends RecyclerView.ViewHolder {
        Button companyBtn;

        public PortalHolder(@NonNull View itemView) {
            super(itemView);
            companyBtn = itemView.findViewById(R.id.name);
        }
    }
}
