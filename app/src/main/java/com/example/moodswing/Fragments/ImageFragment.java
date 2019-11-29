package com.example.moodswing.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.moodswing.EditMoodActivity;
import com.example.moodswing.MainActivity;
import com.example.moodswing.NewMoodActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ImageFragment extends DialogFragment {
    FloatingActionButton closeBtn;
    FloatingActionButton galleryBtn;
    FloatingActionButton cameraBtn;


    String activity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null)
            activity = args.getString("activity");
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_prompt, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // link elements
        closeBtn = view.findViewById(R.id.imagePrompt_close);
        galleryBtn = view.findViewById(R.id.imagePrompt_gallery);
        cameraBtn = view.findViewById(R.id.imagePrompt_camera);

        // listeners
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                if (activity == "Edit"){
                    EditMoodActivity callingActivity = (EditMoodActivity) getActivity();
                    callingActivity.takeimage();
                }
                if (activity == "New") {
                    NewMoodActivity callingActivity = (NewMoodActivity) getActivity();
                    callingActivity.takeimage();
                }
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select photo from gallery
                dismiss();
                if (activity == "Edit"){
                    EditMoodActivity callingActivity = (EditMoodActivity) getActivity();
                    callingActivity.pickFromGallery();
                }
                if (activity == "New") {
                    NewMoodActivity callingActivity = (NewMoodActivity) getActivity();
                    callingActivity.pickFromGallery();
                }

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
