package com.khieuthichien.zula.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.khieuthichien.zula.R;
import com.khieuthichien.zula.model.Chat;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    FirebaseUser firebaseUser;

    private Context context;
    private List<Chat> chatList;
    private String imageurl;

    public MessageAdapter(Context context, List<Chat> chatList, String imageurl) {
        this.context = context;
        this.chatList = chatList;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_right, parent, false);
            return new MessageAdapter.MessageHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_left, parent, false);
            return new MessageAdapter.MessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.tvShowMessage.setText(chat.getMessage());
        if (imageurl.equals("default")){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context).load(imageurl).into(holder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {

        public CircleImageView profileImage;
        public TextView tvShowMessage;

        public MessageHolder(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            tvShowMessage = itemView.findViewById(R.id.tv_show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
