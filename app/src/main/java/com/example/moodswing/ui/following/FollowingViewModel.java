package com.example.moodswing.ui.following;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.FirestoreUserDocCommunicator;
import com.example.moodswing.MoodEvent;

import java.util.ArrayList;

public class FollowingViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<ArrayList<MoodEvent>> moodDataList;
    private MutableLiveData<RecyclerView.Adapter> moodListAdapter;
    private MutableLiveData<FirestoreUserDocCommunicator> communicator;

    public FollowingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");

        moodDataList = new MutableLiveData<>();
        moodListAdapter = new MutableLiveData<>();
        
    }

    public LiveData<String> getText() {
        return mText;
    }
}