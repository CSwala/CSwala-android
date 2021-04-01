package com.cswala.cswala.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.Activities.LoginActivity;
import com.cswala.cswala.Models.WebModel;
import com.cswala.cswala.R;
import com.cswala.cswala.utils.IntentHelper;

import java.util.List;

public class WebAdapter extends RecyclerView.Adapter<WebAdapter.ViewHolder> {
    List<WebModel> webModelList;
    Context context;

    public WebAdapter(List<WebModel> webModelList, Context context) {
        this.webModelList = webModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.small_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final WebModel webModel=webModelList.get(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper intentHelper=new IntentHelper(context);
                intentHelper.GoToWeb(webModel.getWebUrl().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return webModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardSmallView);
        }
    }
}
