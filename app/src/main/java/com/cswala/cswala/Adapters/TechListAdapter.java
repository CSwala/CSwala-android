package com.cswala.cswala.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TechListAdapter extends RecyclerView.Adapter<TechListAdapter.TechHolder> implements Filterable {

    ArrayList<String> data=new ArrayList<>();
    ArrayList<String> dataFull;

    techItemClicked listener;
    public TechListAdapter(ArrayList<String> data,techItemClicked listener) {
        this.data = data;
        this.listener=listener;
        dataFull=new ArrayList<>(data);
    }

    @Override
    public TechHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.tech_list_element,parent,false);
        final TechHolder holder=new TechHolder(myView);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(data.get(holder.getAdapterPosition()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TechHolder holder, int position) {
        holder.data.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String> sample=new ArrayList<>();
            if(constraint==null || constraint.length()==0)
            {
                sample.addAll(dataFull);
            }
            else {
                String pattern=constraint.toString().toLowerCase().trim();
                for(String item: dataFull)
                {
                    if(item.toLowerCase().contains(pattern))
                    {
                        sample.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=sample;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class TechHolder extends RecyclerView.ViewHolder {
        TextView data;
        public TechHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            data=itemView.findViewById(R.id.tech_data);
        }
    }
}

