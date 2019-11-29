package com.example.moodswing.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.moodswing.EditMoodActivity;
import com.example.moodswing.MainActivity;
import com.example.moodswing.NewMoodActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.moodswing.NewMoodActivity.CAMERA_REQUEST_CODE;
import static com.example.moodswing.NewMoodActivity.LOCATION_FOR_CAMERA_REQUEST_CODE;

/**
 * This is the dialog that shows up when the user wants to upload an image to their moodEvent
 * gives the user the choice of gallery, camera, or cancel
 */

public class ImageFragment extends DialogFragment {
    FloatingActionButton closeBtn;
    FloatingActionButton galleryBtn;
    FloatingActionButton cameraBtn;
    FloatingActionButton clearBtn;


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
        clearBtn = view.findViewById(R.id.imagePrompt_clearBtn);

        // listeners
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                    }

                }
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_FOR_CAMERA_REQUEST_CODE);
                }

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if (activity.equals("Edit")){
                            EditMoodActivity callingActivity = (EditMoodActivity) getActivity();
                            callingActivity.takeimage();
                        }
                        if (activity.equals("New")) {
                            NewMoodActivity callingActivity = (NewMoodActivity) getActivity();
                            callingActivity.takeimage();
                        }
                    }
                }
                dismiss();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.equals("Edit")){
                    EditMoodActivity callingActivity = (EditMoodActivity) getActivity();
                    callingActivity.clearImage();
                }
                if (activity.equals("New")) {
                    NewMoodActivity callingActivity = (NewMoodActivity) getActivity();
                    callingActivity.clearImage();
                }
                dismiss();
            }
        });


        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select photo from gallery
                dismiss();
                if (activity.equals("Edit")){
                    EditMoodActivity callingActivity = (EditMoodActivity) getActivity();
                    callingActivity.pickFromGallery();
                }
                if (activity.equals("New")) {
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
