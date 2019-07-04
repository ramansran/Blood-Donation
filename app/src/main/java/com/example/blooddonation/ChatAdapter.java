package com.example.blooddonation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    public static final int MSG_TYPE_LEFT =  0 ;
    public static final int MSG_TYPE_RIGHT = 1 ;

    public Context context;
    public List<ChatMainData> list;;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;


    public ChatAdapter(Context context, List<ChatMainData> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
          View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,viewGroup,false);
          return new ChatAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,viewGroup,false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder viewHolder, int i) {

        ChatMainData chatMainData = list.get(i);

        viewHolder.showMessage.setText(chatMainData.getMessage());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView showMessage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.MI);

        }


    }

    @Override
    public int getItemViewType(int position) {

     firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
     if (list.get(position).getSender().equals(firebaseUser.getUid())){
         return MSG_TYPE_RIGHT;
     }
     else {
         return  MSG_TYPE_LEFT;
     }



    }
}









