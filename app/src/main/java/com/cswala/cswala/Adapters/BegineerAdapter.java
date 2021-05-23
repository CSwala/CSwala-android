package com.cswala.cswala.Adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.R;
import com.cswala.cswala.utils.IntentHelper;

import java.util.List;

import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.RichLinkListener;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewSkype;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class BegineerAdapter extends RecyclerView.Adapter<BegineerAdapter.ViewHolder>{

    Context context;
    List<Pair<String,String>> stringList;

    public BegineerAdapter(Context context, List<Pair<String,String>> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public BegineerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.begineer_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BegineerAdapter.ViewHolder holder, final int position) {
            String A=String.valueOf(position+1);
            holder.tvLink.setText(A + ". " + stringList.get(position).first);

//            holder.tvLink.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    IntentHelper intentHelper=new IntentHelper(context);
//                    intentHelper.GoToWeb(stringList.get(position).second);
//                }
//            });

        holder.richLinkViewSkype.setLink(stringList.get(position).second, new ViewListener() {

            @Override
            public void onSuccess(boolean status) {
                Toast.makeText(context, stringList.get(position).second, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        holder.richLinkViewSkype.setDefaultClickListener(false);
        holder.richLinkViewSkype.setClickListener(new RichLinkListener() {
            @Override
            public void onClicked(View view, MetaData meta) {
                IntentHelper intentHelper=new IntentHelper(context);
                intentHelper.GoToWeb(stringList.get(position).second);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvLink;
        RichLinkViewSkype richLinkViewSkype;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLink=itemView.findViewById(R.id.tvLink);
            richLinkViewSkype=itemView.findViewById(R.id.richLink);
        }
    }

}
