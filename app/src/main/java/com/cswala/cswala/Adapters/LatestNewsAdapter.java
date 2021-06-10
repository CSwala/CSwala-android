package com.cswala.cswala.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cswala.cswala.Activities.LoginActivity;
import com.cswala.cswala.Activities.WebActivity;
import com.cswala.cswala.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LatestNewsAdapter extends RecyclerView.Adapter<LatestNewsAdapter.ViewHolder> {
    private Context context;
    private JSONArray results;

    public LatestNewsAdapter(Context context, JSONArray results){
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_news_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        JSONObject obj = null;
        try {
            obj = results.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject finalObj = obj;
        holder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.blink_anim);
                holder.readMore.startAnimation(animation);
                Intent external_link = null;
                String url;
                try {
                    url=finalObj.getString("url");
                    external_link = new Intent(context,
                            WebActivity.class);
                    external_link.putExtra("URL",url);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                context.startActivity(external_link);
            }
        });

        if(obj!=null){
            try {
                holder.title.setText(obj.getString("title"));
                holder.description.setText(obj.getString("abstract"));
                String date_raw = obj.getString("updated_date");
                holder.date.setText(date_raw.substring(0, 10).concat("  ")
                        .concat(date_raw.substring(11, 16)));
                Glide.with(context)
                        .load(obj.getJSONArray("multimedia")
                                .getJSONObject(2)
                                .getString("url")).into(holder.imageView);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public int getItemCount() {
        return results.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, description, date;
        public ImageView imageView;
        public Button readMore;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.textView1_row_hf);
            description = itemView.findViewById(R.id.textView2_row_hf);
            date = itemView.findViewById(R.id.textView3_row_hf);
            imageView = itemView.findViewById(R.id.image_row_hf);
            readMore = itemView.findViewById(R.id.read_more_hf);

        }
    }

}
