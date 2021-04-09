package com.cswala.cswala.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.Models.PortalListElement;
import com.cswala.cswala.R;

import java.util.ArrayList;

public class PortalListAdapter extends RecyclerView.Adapter<PortalListAdapter.PortalHolder> {
    ArrayList<PortalListElement> data;
    Context ctx;

    public PortalListAdapter(Context ctx, ArrayList<PortalListElement> data) {
        this.data = data;
        this.ctx = ctx;
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

    public class PortalHolder extends RecyclerView.ViewHolder {
        Button companyBtn;

        public PortalHolder(@NonNull View itemView) {
            super(itemView);
            companyBtn = itemView.findViewById(R.id.name);
        }
    }
}
