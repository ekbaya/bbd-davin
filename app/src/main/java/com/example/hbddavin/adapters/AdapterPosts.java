package com.example.hbddavin.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbddavin.R;
import com.example.hbddavin.modals.ModelPost;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder>{

    Context context;
    List<ModelPost> postList;

    public AdapterPosts(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // inflate layout row_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_post, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String id = postList.get(position).getId();
        String name = postList.get(position).getName();
        String message = postList.get(position).getMessage();
        String time = postList.get(position).getTime();
        String image = postList.get(position).getImage();

        //Converting timeStamp to dd/mm/yyyy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(time));
        String pTime = DateFormat.format("dd/MM/yyyy  hh:mm aa", calendar).toString();

        //set data
        holder.nameTv.setText(name);
        holder.timeTv.setText(pTime);
        holder.messageTv.setText(message);

        try {
            Picasso.get().load(image).into(holder.imageIv);

        }catch (Exception e){
            Log.e("IMAGE LOAD ERROR", e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    // view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        //views from row_post.xml
        ImageView imageIv;
        TextView nameTv, timeTv, messageTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            imageIv = itemView.findViewById(R.id.imageIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            messageTv = itemView.findViewById(R.id.messageTv);

        }
    }
}
