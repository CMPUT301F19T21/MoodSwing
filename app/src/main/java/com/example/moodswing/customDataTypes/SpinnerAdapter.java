package com.example.moodswing.customDataTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.moodswing.R;

import java.util.ArrayList;

//https://codinginflow.com/tutorials/android/custom-spinner
//This is the adapter for the social situation spinner(a dropdown menu).
// this class is not in use. (not in release)

public class SpinnerAdapter extends ArrayAdapter<SocialSituationItem> {

    private ImageView spinnerImage;
    private TextView spinnerText;
    private SocialSituationItem current;

    /**
     * @param context The activity context
     * @param situationList The arraylist of situation items for the adapter to show
     */

    public SpinnerAdapter(Context context, ArrayList<SocialSituationItem> situationList) {
        super(context,0,situationList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    /**
     * Each entry in the dropdown menu will have an image and text. see SocialSituationItem
     */
    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.social_situation_spinner,parent,false);

        }
        spinnerImage = convertView.findViewById(R.id.spinner_image);
        spinnerText = convertView.findViewById(R.id.spinner_text);

        current = getItem(position);
        spinnerImage.setImageResource(current.getSitImage());
        spinnerText.setText(current.getSituation());

        return convertView;
    }
}
