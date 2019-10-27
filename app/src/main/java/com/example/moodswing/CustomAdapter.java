package com.example.moodswing;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;


import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<MoodEvent> {

    private ArrayList<MoodEvent> moods;
    private Context context;

    public CustomAdapter(Context context, ArrayList<MoodEvent> moods) {
        //Customize the list
        super(context, 0, moods);
        this.moods = moods;
        this.context = context;
    }

    public View getView(final int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        //Customize the view
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mood_list_content, parent, false);
        }

        MoodEvent mood = moods.get(position);

        TextView moodType = view.findViewById(R.id.mood_list_text);
        moodType.setText(String.valueOf(mood.getMoodType()));

        CheckBox checkBox = view.findViewById(R.id.checkBox);
        checkBox.setTag(position);

        return view;
    }
}