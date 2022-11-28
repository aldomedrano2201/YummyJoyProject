package com.example.prjyummyjoy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import util.ItemStatus;

public class ClientProfileActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    TextView tvClientName, tvClientEmail;
    Button btnClientProfile, btnFindMealProduct,
            btnClientItemReviews, btnClientFAQ, btnLogOut;
    DatabaseReference objDatabase;
    String userIdValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        initialize();
    }

    private void initialize() {
        tvClientName = findViewById(R.id.tvClientName);
        tvClientEmail = findViewById(R.id.tvClientEmail);
        btnClientProfile = findViewById(R.id.clientProfile);
        btnFindMealProduct = findViewById(R.id.findMealProduct);
        btnClientItemReviews = findViewById(R.id.writeReview);
        btnClientFAQ = findViewById(R.id.clientFAQ);
        btnClientProfile.setOnClickListener(this);
        btnClientFAQ.setOnClickListener(this);
        btnClientItemReviews.setOnClickListener(this);
        btnFindMealProduct.setOnClickListener(this);
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
            case R.id.clientProfile:
                launchActivity(RegistrationActivity.class);
                break;
            case R.id.findMealProduct:
                userIdValue = "Reserve";
                launchActivity(ListItemsActivity.class);
                break;
            case R.id.writeReview:
                userIdValue = "Review";
                launchActivity(ListItemsActivity.class);
                break;
            case R.id.clientFAQ:
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
                = new Intent(ClientProfileActivity.this,
                generalActivity);
        intent.putExtra("userId",userIdValue);
        startActivity(intent);

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){
            tvClientName.setText("Welcome " + snapshot.child("name").getValue().toString());
            tvClientEmail.setText(snapshot.child("email").getValue().toString());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}