package com.example.prjyummyjoy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import model.Item;
import model.Review;
import util.ItemStatus;

public class ReserveMealProductActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener, OnCompleteListener {

    TextView tvItemName, tvPlaceName, tvAddress, tvPostalCode, tvPriceItem, tvNameUser,
            tvItemNumber, tvItemTime, tvStatusItem, tvPlaceEmail, tvPlacePhone;
    EditText edReview;
    Button btnReserve,btnReview;
    DatabaseReference objDatabase;
    String objId,userIdValue,tempUserIdValue;
    String[] itemDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_meal_product);
        initialize();
    }

    private void initialize() {
        tvItemName = findViewById(R.id.tvNameItem);
        tvItemNumber = findViewById(R.id.tvItemAvailability);
        tvAddress = findViewById(R.id.tvPlaceAddress);
        tvItemTime = findViewById(R.id.tvTimeItem);
        tvPriceItem = findViewById(R.id.tvPriceItem);
        tvPostalCode = findViewById(R.id.tvPostalCode);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvPlaceEmail = findViewById(R.id.tvPlaceEmail);
        tvPlacePhone = findViewById(R.id.tvPlacePhone);
        tvStatusItem= findViewById(R.id.tvItemStatus);
        tvNameUser = findViewById(R.id.tvNameUser);
        edReview = findViewById(R.id.edItemReview);
        btnReserve = findViewById(R.id.btnReserve);
        btnReserve.setOnClickListener(this);
        btnReview = findViewById(R.id.btnWriteReview);
        btnReview.setOnClickListener(this);
        userIdValue = getIntent().getStringExtra("userId");

        if (userIdValue.contains("ID")){
            itemDetails = userIdValue.split("\t");
            if (itemDetails[itemDetails.length-1].contains("Review")){
                if (itemDetails[5].contains(ItemStatus.Status.RESERVED.toString())){
                    btnReserve.setVisibility(View.INVISIBLE);
                    btnReview.setVisibility(View.VISIBLE);
                    edReview.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(this, "Please reserve the item before writing a review", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }else{
                btnReserve.setVisibility(View.VISIBLE);
                btnReview.setVisibility(View.INVISIBLE);
                edReview.setVisibility(View.INVISIBLE);
            }
            userIdValue = itemDetails[itemDetails.length-2];
            objDatabase = FirebaseDatabase
                    .getInstance()
                    .getReference(String.valueOf(ItemStatus.TypeUser.user));

            //DatabaseReference itemChild = objDatabase.child(userIdValue);
            objDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        if (snapshot1.getKey().equals(userIdValue)){

                            tvItemName.setText(tvItemName.getText()+itemDetails[3]);
                            tvItemNumber.setText(tvItemNumber.getText()+itemDetails[7]);
                            tvItemTime.setText(tvItemTime.getText()+itemDetails[9]);
                            tvPlaceName.setText(tvPlaceName.getText()+snapshot1.child("name").getValue().toString());
                            tvAddress.setText(tvAddress.getText()+snapshot1.child("address").getValue().toString());
                            tvPostalCode.setText(tvPostalCode.getText()+snapshot1.child("postalCode").getValue().toString());
                            tvPlaceEmail.setText(tvPlaceEmail.getText()+snapshot1.child("email").getValue().toString());
                            tvPlacePhone.setText(tvPlacePhone.getText()+snapshot1.child("phoneNumber").getValue().toString());
                            tvStatusItem.setText(tvStatusItem.getText()+itemDetails[5]);
                            tvPriceItem.setText(tvPriceItem.getText()+itemDetails[11]);
                            objId = itemDetails[1];
                            tempUserIdValue = itemDetails[1];

                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReserve:
                reserveMeal();
                break;
            case R.id.btnWriteReview:
                if (!edReview.getText().toString().equals(""))
                    reviewMeal();
                else
                    Toast.makeText(this, "Please enter a review", Toast.LENGTH_SHORT).show();
                break;
        }



    }

    private void reviewMeal() {
        objDatabase = FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf("review"));

        //DatabaseReference itemChild = objDatabase.child(userIdValue);
        objDatabase.addValueEventListener(this);
    }

    private void reserveMeal() {

        if (!getIntent().getStringExtra("userId").equals("")){

            objDatabase = FirebaseDatabase
                    .getInstance()
                    .getReference(String.valueOf(ItemStatus.TypeUser.item));

            //DatabaseReference itemChild = objDatabase.child(userIdValue);
            objDatabase.addValueEventListener(this);
        }else
            Toast.makeText(this, "There is not a place related to this meal or product to publish", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {

            if (tvStatusItem.getText().toString().contains(ItemStatus.Status.RESERVED.toString())){

                ArrayList<Integer> objIds = new ArrayList<Integer>();

                if(!snapshot.getKey().equals(itemDetails[12]))
                    snapshot.child(itemDetails[12]);

                for (DataSnapshot snapshot1 : snapshot.child(itemDetails[12]).getChildren()) {
                    objIds.add(Integer.valueOf((snapshot1.getKey())));
                }
                if (!objIds.isEmpty())
                    objId = String.valueOf(Collections.max(objIds) + 1);
                else
                    objId = "1";

                Review review = new Review(edReview.getText().toString());

                objDatabase.child(itemDetails[12]).child(objId).setValue(review);


            }else{



                String item = itemDetails[1] + "\t" + userIdValue + "\t" + itemDetails[3] + "\t" +
                        itemDetails[9] + "\t" + itemDetails[11] + "\t" +
                        itemDetails[7] + "\t" + ItemStatus.Status.PUBLISHED.toString()
                        + "/" + ItemStatus.Status.RESERVED.toString();


                objDatabase.child(objId).setValue(item);


            }
            objDatabase.removeEventListener(this);
            Toast.makeText(this, "Item reserved successfully", Toast.LENGTH_SHORT).show();


            Intent intent
                    = new Intent(this,
                    ClientProfileActivity.class);
            intent.putExtra("userId",userIdValue);
            startActivity(intent);
        }

    }




    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onComplete(@NonNull Task task) {

    }
}