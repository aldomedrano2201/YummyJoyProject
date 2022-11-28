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

import util.ItemStatus;

public class PublishMealProductActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener, OnCompleteListener {

    EditText edItemName, edItemNumber, edItemTime, edItemPrice;
    TextView tvItemStatus;
    Button btnPublish;
    DatabaseReference objDatabase;
    String objId,userIdValue,tempUserIdValue;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_meal_product);
        initialize();
    }

    private void initialize() {
        edItemName = findViewById(R.id.name);
        edItemNumber = findViewById(R.id.email);
        edItemPrice = findViewById(R.id.passwd);
        edItemTime = findViewById(R.id.address);
        tvItemStatus = findViewById(R.id.tvItemStatus);
        btnPublish = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressbar);
        btnPublish.setOnClickListener(this);
        userIdValue = getIntent().getStringExtra("userId");

        if (userIdValue.contains("ID")){
            String[] itemDetails = userIdValue.split(" ");
            userIdValue = itemDetails[1];
            objDatabase = FirebaseDatabase
                    .getInstance()
                    .getReference(String.valueOf(ItemStatus.TypeUser.item));

            //DatabaseReference itemChild = objDatabase.child(userIdValue);
            objDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        if (snapshot1.getKey().equals(userIdValue)){
                            String[] itemDetails = snapshot1.getValue().toString().split("\t");
                            edItemName.setText(itemDetails[2]);
                            edItemNumber.setText(itemDetails[5]);
                            edItemTime.setText(itemDetails[3]);
                            edItemPrice.setText(itemDetails[4]);
                            tvItemStatus.setText(tvItemStatus.getText()+itemDetails[itemDetails.length-1]);
                            objId = itemDetails[0];
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
        publishMeal();

    }

    private void publishMeal() {

        if (!getIntent().getStringExtra("userId").equals("")){



                if (TextUtils.isEmpty(edItemName.getText())) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter the name of the product",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }else if (TextUtils.isEmpty(edItemPrice.getText())) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter the price",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }else if (TextUtils.isEmpty(edItemNumber.getText())) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter the number of products or meals to sell",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }else if (TextUtils.isEmpty(edItemTime.getText())) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter the time to pick up",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }


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

            String item;

            if (getIntent().getStringExtra("userId").contains("ID")){
                userIdValue = tempUserIdValue;
            }else {

                // show the visibility of progress bar to show loading
                progressBar.setVisibility(View.VISIBLE);
                ArrayList<Integer> objIds = new ArrayList<Integer>();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    objIds.add(Integer.valueOf((snapshot1.getKey())));
                }
                if (!objIds.isEmpty())
                    objId = String.valueOf(Collections.max(objIds) + 1);
                else
                    objId = "1";
            }



            if (edItemNumber.getText().toString().equals("0")) {
                item = objId + "\t" + userIdValue + "\t" + edItemName.getText().toString() + "\t" +
                        edItemTime.getText().toString() + "\t" + edItemPrice.getText().toString() + "\t" +
                        edItemNumber.getText().toString() + "\t" + ItemStatus.Status.ALL_DELIVERED.toString();
                Toast.makeText(this, "All items were delivered!", Toast.LENGTH_SHORT).show();
            }else if(tvItemStatus.getText().toString().contains(ItemStatus.Status.RESERVED.toString())){
                item = objId + "\t" + userIdValue + "\t" + edItemName.getText().toString() + "\t" +
                        edItemTime.getText().toString() + "\t" + edItemPrice.getText().toString() + "\t" +
                        edItemNumber.getText().toString() + "\t" + tvItemStatus.getText().toString();
            }

            else{
                item = objId + "\t" + userIdValue + "\t" + edItemName.getText().toString() + "\t" +
                        edItemTime.getText().toString() + "\t" + edItemPrice.getText().toString() + "\t" +
                        edItemNumber.getText().toString() + "\t" + ItemStatus.Status.PUBLISHED.toString();

            }



            objDatabase.child(objId).setValue(item);
            progressBar.setVisibility(View.GONE);
            objDatabase.removeEventListener(this);
            Toast.makeText(this, "Item added/updated successfully", Toast.LENGTH_SHORT).show();
            clearWidgets();
            Intent intent
                    = new Intent(this,
                    PlaceProfileActivity.class);
            intent.putExtra("userId",userIdValue);
            startActivity(intent);
        }

    }

    private void clearWidgets() {
        edItemTime.setText("");
        edItemPrice.setText("");
        edItemName.setText("");
        edItemNumber.setText("");
    }


    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onComplete(@NonNull Task task) {

    }
}