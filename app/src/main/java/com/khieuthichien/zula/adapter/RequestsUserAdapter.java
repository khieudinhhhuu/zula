package com.khieuthichien.zula.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.khieuthichien.zula.ui.MessageActivity;
import com.khieuthichien.zula.R;
import com.khieuthichien.zula.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsUserAdapter extends RecyclerView.Adapter<RequestsUserAdapter.RequestsUserHolder>{

    private Context context;
    private List<User> userList;

    public RequestsUserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public RequestsUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_requests_user, parent, false);
        return new RequestsUserAdapter.RequestsUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsUserHolder holder, int position) {
        final User user = userList.get(position);
        holder.tvusername.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(user.getImageURL()).into(holder.profileImage);
        }

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userList == null){
            return 0;
        }
        return userList.size();
    }

    public class RequestsUserHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView tvusername;

        public RequestsUserHolder(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            tvusername = itemView.findViewById(R.id.tvusername);
        }

    }
}
