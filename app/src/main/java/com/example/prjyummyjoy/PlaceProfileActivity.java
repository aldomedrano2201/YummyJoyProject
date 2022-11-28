package com.example.prjyummyjoy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import util.ItemStatus;

public class PlaceProfileActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    TextView tvPlaceName, tvPlaceEmail;
    Button btnPlaceProfile, btnPublishMealProduct,
            btnManageListItems, btnItemReviews, btnPlaceFAQ, btnLogOut;
    DatabaseReference objDatabase;
    String userIdValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_profile);
        initialize();
    }

    private void initialize() {
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvPlaceEmail = findViewById(R.id.tvPlaceEmail);
        btnPlaceProfile = findViewById(R.id.placeProfile);
        btnPublishMealProduct = findViewById(R.id.publishMealProduct);
        btnManageListItems = findViewById(R.id.listItems);
        btnItemReviews = findViewById(R.id.itemReviews);
        btnPlaceFAQ = findViewById(R.id.placeFAQ);
        btnPlaceProfile.setOnClickListener(this);
        btnPlaceFAQ.setOnClickListener(this);
        btnItemReviews.setOnClickListener(this);
        btnManageListItems.setOnClickListener(this);
        btnPublishMealProduct.setOnClickListener(this);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(this);
        objDatabase = FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf(ItemStatus.TypeUser.user));
        userIdValue = getIntent().getStringExtra("userId");
        DatabaseReference personChild = objDatabase.child(userIdValue);
        personChild.addValueEventListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.placeProfile:
                launchActivity(RegistrationActivity.class);
                break;
            case R.id.publishMealProduct:
                launchActivity(PublishMealProductActivity.class);
                break;
            case R.id.listItems:
                launchActivity(ListItemsActivity.class);
                break;
            case R.id.itemReviews:
                launchActivity(PlaceReviewActivity.class);
                break;
            case R.id.placeFAQ:
                launchActivity(PlaceFAQActivity.class);
                break;
            case R.id.btnLogOut:
                userIdValue = "";
                launchActivity(LoginActivity.class);
                break;

        }
    }



    private void launchActivity(Class<?> generalActivity) {

        Intent intent
                = new Intent(PlaceProfileActivity.this,
                generalActivity);
        intent.putExtra("userId",userIdValue);
        startActivity(intent);

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){
            tvPlaceName.setText("Welcome " + snapshot.child("name").getValue().toString());
            tvPlaceEmail.setText(snapshot.child("email").getValue().toString());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}