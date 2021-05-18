package com.cswala.cswala.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.Models.Begineer;
import com.cswala.cswala.R;
import com.cswala.cswala.utils.IntentHelper;

import java.util.List;

public class BestPracticeAdapter extends RecyclerView.Adapter<BestPracticeAdapter.ViewHolder> {

    Context context;
    List<Begineer> begineerList;

    public BestPracticeAdapter(Context context, List<Begineer> begineerList) {
        this.context = context;
        this.begineerList = begineerList;
    }

    @NonNull
    @Override
    public BestPracticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.bestpractice_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestPracticeAdapter.ViewHolder holder, int position) {
        final Begineer begineer=begineerList.get(position);
        holder.title.setText(begineer.getTitle());
        holder.cardViewBegineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper intentHelper=new IntentHelper(context);
                intentHelper.GoToBegineer(begineer.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return begineerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        CardView cardViewBegineer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewBegineer=itemView.findViewById(R.id.cardViewBegineer);
            title=itemView.findViewById(R.id.titleBeginner);
        }
    }
}
