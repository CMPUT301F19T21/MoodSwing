package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodswing.customDataTypes.AddMoodAdapter;
import com.example.moodswing.customDataTypes.DateJar;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.MoodType;
import com.example.moodswing.customDataTypes.MoodTypeAdapter;
import com.example.moodswing.customDataTypes.TimeJar;
import com.example.moodswing.navigationFragments.HomeFragment;
import com.google.protobuf.Empty;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class EditMoodActivity extends AppCompatActivity {
    TextView timeText;
    EditText minuteText;
    EditText hourText;
    EditText reasonEditText;
    ImageButton confirmButton;

    MoodEvent moodEvent;
    private int moodType;
    private DateJar date;
    private TimeJar time;
    private String UID;
    private String reason;
    private Integer socialSituation;
    private ArrayList<MoodType> moodTypes;

    private RecyclerView moodView;
    private RecyclerView.Adapter moodAdapter;
    private RecyclerView.LayoutManager manager;

    FirestoreUserDocCommunicator communicator;
    String username;

    //@Override
    //public void onItemClick(View view, int position) {
     //   Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
     //   moodType = position;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_mood);
        communicator = FirestoreUserDocCommunicator.getInstance();
        Intent intent = getIntent();
        UID = intent.getStringExtra("MoodUID");
        moodEvent = communicator.grabMoodEvent(UID);


        timeText = findViewById(R.id.timeText);
        confirmButton = findViewById(R.id.confirmNewMood);


        //moodTypes = initial();

        date = moodEvent.getDate();
        time = moodEvent.getTime();
        int Hr = time.getHr();
        int Min = time.getMin();
        timeText.setText(Hr+":"+Min);
        int year = date.getYear();
        int Day = date.getDay();
        int month = date.getMonth();
        timeText.setText(getMonth(month)+","+Day+","+year);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                communicator.updateMoodEvent(moodEvent);
                finish();
            }
        });
    }
    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
    public ArrayList<MoodType> initial(){
        moodTypes = new ArrayList<>();
        //moodTypes.add(new MoodType(R.drawable.happymood, "happy",1));
        moodTypes.add(new MoodType(R.drawable.ic_add_black_24dp,"angry",2));

        moodView = findViewById(R.id.mood_RecyclerView);
        moodView.setHasFixedSize(true);
        manager =  new LinearLayoutManager(this);
        moodAdapter = new MoodTypeAdapter(moodTypes);
        moodView.setLayoutManager(manager);
        moodView.setAdapter(moodAdapter);
        return moodTypes;
    }
}
