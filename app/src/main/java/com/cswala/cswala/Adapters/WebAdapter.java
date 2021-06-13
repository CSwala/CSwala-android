
package com.cswala.cswala.Adapters;

import android.content.Context;
import android.util.Log;
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

import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.RichLinkListener;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewSkype;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

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
        final String URL= webModel.getWebUrl().trim();
        if(!URL.isEmpty()) {
            holder.richLinkView.setLink(URL, new ViewListener() {

                @Override
                public void onSuccess(boolean status) {
                    Toast.makeText(context, webModel.getWebUrl(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(Exception e) {

                }
            });
            holder.richLinkView.setDefaultClickListener(false);
            holder.richLinkView.setClickListener(new RichLinkListener() {
                @Override
                public void onClicked(View view, MetaData meta) {
                    IntentHelper intentHelper=new IntentHelper(context);
                    intentHelper.GoToWeb(URL);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return webModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //        CardView cardView;
        RichLinkViewSkype richLinkView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            cardView=itemView.findViewById(R.id.cardSmallView);
            richLinkView=(RichLinkViewSkype) itemView.findViewById(R.id.richLinkView);
        }
    }
}