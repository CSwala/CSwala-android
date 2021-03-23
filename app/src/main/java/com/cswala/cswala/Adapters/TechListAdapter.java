package com.cswala.cswala.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.Activities.TechDataActivity;
import com.cswala.cswala.Models.TechListElement;
import com.cswala.cswala.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TechListAdapter extends RecyclerView.Adapter<TechListAdapter.TechHolder> implements Filterable {
    ArrayList<TechListElement> data=new ArrayList<>();
    ArrayList<TechListElement> dataFull;

    techItemClicked listener;
    public TechListAdapter(ArrayList<TechListElement> data,techItemClicked listener) {
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
                listener.onItemClicked(data.get(holder.getAdapterPosition()).getTechName());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TechHolder holder, int position) {
        holder.data.setText(data.get(position).getTechName());
        holder.tag.setText(data.get(position).getTechTag());
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
            ArrayList<TechListElement> sample=new ArrayList<>();
            if(constraint==null || constraint.length()==0)
            {
                sample.addAll(dataFull);
            }
            else {
                String pattern=constraint.toString().toLowerCase().trim();
                for(TechListElement data: dataFull)
                {
                    String item=data.getTechName();
                    if(item.toLowerCase().contains(pattern))
                    {
                        sample.add(data);
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
        TextView tag;
        public TechHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            data=itemView.findViewById(R.id.tech_data);
            tag=itemView.findViewById(R.id.tag_element);
        }
    }
}

