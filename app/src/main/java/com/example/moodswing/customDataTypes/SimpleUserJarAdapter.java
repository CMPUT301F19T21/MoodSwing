package com.example.moodswing.customDataTypes;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.Fragments.ManageRequestFragment;
import com.example.moodswing.MainActivity;
import com.example.moodswing.R;

import java.util.ArrayList;

public class SimpleUserJarAdapter extends RecyclerView.Adapter<SimpleUserJarAdapter.MyViewHolder>{



    private ArrayList<UserJar> userJars;
    private Integer mode;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        ConstraintLayout requestLayout;

        public MyViewHolder(View view){
            super(view);
            this.usernameText = view.findViewById(R.id.request_username);
            this.requestLayout = view.findViewById(R.id.request_card);
        }
    }

    public SimpleUserJarAdapter(){}

    public SimpleUserJarAdapter(ArrayList<UserJar> userJars, int mode){
        // 0 -> request
        // 1 -> following

        this.userJars = userJars;
        this.mode = mode;
    }

    @NonNull
    @Override
    public SimpleUserJarAdapter.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_user_jar_content_requests, parent, false);
        return new SimpleUserJarAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView usernameText = holder.usernameText;
        usernameText.setText(userJars.get(position).getUsername());
        UserJar currentUserJar = userJars.get(position);

        holder.requestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == 1){
                    new ManageRequestFragment(currentUserJar)
                            .show(((MainActivity)v.getContext()).getSupportFragmentManager(), "manage_request");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userJars.size();
    }


    public void clearUserJars(){
        this.userJars.clear();
    }


    public void addToUserJars(UserJar userJar){
        this.userJars.add(userJar);
    }

    public ArrayList<UserJar> getUserJars() {
        return userJars;
    }
}
