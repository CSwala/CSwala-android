package com.cswala.cswala.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.Activities.WebActivity;
import com.cswala.cswala.Activities.contestCalendar;
import com.cswala.cswala.Models.contest_Results;
import com.cswala.cswala.R;


public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ViewHolder> {
    contest_Results[] result;
    Context context;


    public ContestAdapter(contest_Results[] result, contestCalendar activity) {
        this.result = result;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contest_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final contest_Results resultList = result[position];
        holder.contest_name.setText(resultList.getName());
        holder.contest_start_time.setText(resultList.getStart_time());
        holder.contest_end_time.setText(resultList.getEnd_time());
        holder.contest_duration.setText(resultList.getDuration());
        holder.contest_status.setText(resultList.getStatus());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            Intent external_link = null;
            String url=null;
            @Override
            public void onClick(View v) {
                try {
                    url=resultList.getUrl();
                    external_link = new Intent(context,
                            WebActivity.class);
                    external_link.putExtra("URL",url);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application can handle this request." + " Please install a web browser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                context.startActivity(external_link);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contest_name, contest_start_time, contest_end_time, contest_duration, contest_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contest_name = itemView.findViewById(R.id.contest_name);
            contest_start_time = itemView.findViewById(R.id.contest_start_time);
            contest_end_time = itemView.findViewById(R.id.contest_end_time);
            contest_duration = itemView.findViewById(R.id.contest_duration);
            contest_status = itemView.findViewById(R.id.contest_status);
        }
    }




}
