package com.example.moodswing.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moodswing.MainActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.MoodEventUtility;
import com.example.moodswing.customDataTypes.UserJar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class MoodDetailFollowingFragment extends Fragment{
    private FirestoreUserDocCommunicator communicator;
    UserJar userJar;
    MoodEvent moodEvent;

    private TextView dateText;
    private TextView timeText;
    private TextView moodText;
    private TextView reasonText;
    private TextView socialText;
    private TextView locationText;

    private TextView usernameText;

    private FloatingActionButton backButton;
    private ImageView moodImage;
    private ImageView locationImg;
    private ImageView socialIcon;
    private ImageView FollowingImage;
    private Fragment outerFragment;
    private String imagePath;

    public MoodDetailFollowingFragment(){}

    public MoodDetailFollowingFragment(int userJarPosition, Fragment outerFragment) {
        this.outerFragment = outerFragment;
        this.communicator = FirestoreUserDocCommunicator.getInstance();
        this.userJar = communicator.getUserJar(userJarPosition);
        this.moodEvent = userJar.getMoodEvent();
        // moodEvent
    }

    public MoodDetailFollowingFragment(int userJarPosition) {
        this.outerFragment = null;
        this.communicator = FirestoreUserDocCommunicator.getInstance();
        this.userJar = communicator.getUserJar(userJarPosition);
        this.moodEvent = userJar.getMoodEvent();
        // moodEvent

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mood_detail_fragment_following, container, false);

        // find view
        dateText = root.findViewById(R.id.moodDetail_following_dateText);
        timeText = root.findViewById(R.id.moodDetail_following_timeText);
        moodText = root.findViewById(R.id.moodDetail_following_moodText);
        reasonText = root.findViewById(R.id.detailedView_following_reasonText);
        backButton = root.findViewById(R.id.detailedView_following_back);
        moodImage = root.findViewById(R.id.detailedView_following_moodImg);
        socialText = root.findViewById(R.id.moodDetail_following_SocialText);
        locationImg = root.findViewById(R.id.moodDetail_following_locationImg);
        socialIcon = root.findViewById(R.id.moodDetail_following_socialSitIcon);
        locationText = root.findViewById(R.id.moodDetail_following_locationText);
        FollowingImage = root.findViewById(R.id.moodDetail_following_image_place_holder);
        usernameText = root.findViewById(R.id.moodDetail_following_username);

        initialElements();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFrag();
            }
        });



        return root;
    }

    private void closeFrag(){
        if (outerFragment== null){
            getFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .remove(this)
                    .commit();
        }else{
            ((MapFragment)getFragmentManager().findFragmentByTag("mapFrag")).initElements();
            getFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .remove(this)
                    .remove(outerFragment)
                    .commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            initialElements();
        }
    }


    private void initialElements(){
        dateText.setText(MoodEventUtility.getDateStr(moodEvent.getTimeStamp()));
        timeText.setText(MoodEventUtility.getTimeStr(moodEvent.getTimeStamp()));
        moodText.setText(MoodEventUtility.getMoodType(moodEvent.getMoodType()));
        setMoodImage(moodEvent.getMoodType());
        setReasonText();
        setSocialSituation();
        usernameText.setText(userJar.getUsername());
        locationText.setText("");
        if (userJar.getMoodEvent().getLatitude() == null) {
            locationImg.setImageResource(R.drawable.ic_location_off_grey_24dp);
        }else{
            locationImg.setImageResource(R.drawable.ic_location_on_accent_red_24dp);
            setLocationStrFromLocation();
        }
        String imageId = moodEvent.getImageId();
        if (checkImageExist(imageId) ==true) {
            // load local image
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            FollowingImage.setImageBitmap(myBitmap);
        }
        else {
            String userId = userJar.getUID();
            communicator.getPhoto(imageId,FollowingImage,userId);
        }
    }

    private void setLocationStrFromLocation(){
        communicator.getAsynchronousTask()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        updateLocationStr();
                    }
                });
    }

    private void updateLocationStr(){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        if (moodEvent.getLatitude() != null){
            try {
                List<Address> firstAddressList = geocoder.getFromLocation(moodEvent.getLatitude(),moodEvent.getLongitude(),1);
                if (firstAddressList != null){
                    if (firstAddressList.isEmpty()){
                        // error
                    }else{
                        //
                        Address address = firstAddressList.get(0);
                        String thoroughfare = address.getThoroughfare();
                        if (thoroughfare == null){
                            locationText.setText("nowhere!");
                        }else{
                            locationText.setText(thoroughfare);
                        }
                    }
                }else {
                    // error
                }
            } catch (Exception e) {
                // display error msg
                e.printStackTrace();
            }
        }else{
            //
        }
    }

    private void setReasonText(){
        if (moodEvent.getReason() != null){
            this.reasonText.setVisibility(View.VISIBLE);
            this.reasonText.setText(String.format(Locale.getDefault(), "\"%s\"",(moodEvent.getReason())));
        }else{
            this.reasonText.setVisibility(View.INVISIBLE);
        }
    }

    private void setSocialSituation(){
        Integer socialSituation = moodEvent.getSocialSituation();
        switch (socialSituation){
            case 0:
                this.socialText.setVisibility(View.INVISIBLE);
                this.socialIcon.setVisibility(View.INVISIBLE);
            case 1:
                this.socialText.setVisibility(View.VISIBLE);
                this.socialIcon.setVisibility(View.VISIBLE);
                this.socialText.setText("Alone");
                this.socialIcon.setImageResource(R.drawable.ic_person_black_24dp);
                break;
            case 2:
                this.socialText.setVisibility(View.VISIBLE);
                this.socialIcon.setVisibility(View.VISIBLE);
                this.socialText.setText("Company");
                this.socialIcon.setImageResource(R.drawable.ic_people_black_24dp);
                break;
            case 3:
                this.socialText.setVisibility(View.VISIBLE);
                this.socialIcon.setVisibility(View.VISIBLE);
                this.socialText.setText("Party");
                this.socialIcon.setImageResource(R.drawable.ic_account_group);
        }
    }

    private void setMoodImage(int moodType){
        switch(moodType){
            case 1:
                moodText.setText("HAPPY");
                moodImage.setImageResource(R.drawable.mood1);
                break;
            case 2:
                moodText.setText("SAD");
                moodImage.setImageResource(R.drawable.mood2);
                break;
            case 3:
                moodText.setText("ANGRY");
                moodImage.setImageResource(R.drawable.mood3);
                break;
            case 4:
                moodText.setText("EDMOTIONAL");
                moodImage.setImageResource(R.drawable.mood4);
                break;
            case 5:
                moodText.setText("HEART BROKEN");
                moodImage.setImageResource(R.drawable.mood5);
                break;
            case 6:
                moodText.setText("IN LOVE");
                moodImage.setImageResource(R.drawable.mood6);
                break;
            case 7:
                moodText.setText("SCARED");
                moodImage.setImageResource(R.drawable.mood7);
                break;
        }
    }

    private boolean checkImageExist(String imageName){
        String root = Environment.getExternalStorageDirectory().toString();
        File myFile = new File(root + "/MoodSwing/"+ imageName +".jpg");

        if(myFile.exists()){
            imagePath = myFile.getAbsolutePath();
            Log.d("testa","image exist");
            return true;
        }
        Log.d("testa","image not exist" + myFile.getAbsolutePath());
        return false;
    }
}