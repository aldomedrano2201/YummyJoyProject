package com.example.prjyummyjoy;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import util.ItemStatus;

public class PlaceReviewActivity extends AppCompatActivity implements ValueEventListener {



    // Array of strings...
    ListView simpleList;
    ArrayList<String> itemsList= new ArrayList<>();
    ActivityResultLauncher<Intent> activityResultLauncher;
    DatabaseReference objDatabase;
    String userIdValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_review);
        initialize();
    }

    private void initialize() {

        objDatabase = FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf(ItemStatus.TypeUser.review));
        userIdValue = getIntent().getStringExtra("userId").toString();


        objDatabase.addValueEventListener(this);


        simpleList = (ListView)findViewById(R.id.itemsListView);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView2, itemsList);
        simpleList.setAdapter(arrayAdapter);


    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){


                for (DataSnapshot snapshot1 : snapshot.child(userIdValue).getChildren()) {


                    String itemVal = snapshot1.child("name").getValue().toString();

                        itemsList.add(itemVal);



                }


        }else{
            Toast.makeText(this, "Review for this place do not exist", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}