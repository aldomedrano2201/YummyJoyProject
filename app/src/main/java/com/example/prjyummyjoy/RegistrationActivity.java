package com.example.prjyummyjoy;


import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import model.Client;
import model.Provider;
import util.ItemStatus;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener, OnCompleteListener<AuthResult> {

    private EditText emailTextView, passwordTextView,
            addressTextView, postalCodeTextView, phoneNumberTextView, nameTextView;
    private TextView tvRegister,tvUserType;
    private Button btnRegister;
    private RadioButton rbClient, rbProvider;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    DatabaseReference objDatabase;
    String userIdValue;
    private RadioGroup rgUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        phoneNumberTextView = findViewById(R.id.phone);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressbar);
        addressTextView = findViewById(R.id.address);
        nameTextView = findViewById(R.id.name);
        postalCodeTextView = findViewById(R.id.postalCode);
        rbClient = findViewById(R.id.rbClient);
        rbProvider = findViewById(R.id.rbProvider);
        rbClient.setOnClickListener(this);
        rbProvider.setOnClickListener(this);
        rgUserType = findViewById(R.id.rgUserRole);
        tvRegister = findViewById(R.id.tvRegister);
        tvUserType = findViewById(R.id.tvUserType);

        // Set on Click Listener on Registration button
        btnRegister.setOnClickListener(this);
        userIdValue = getIntent().getStringExtra("userId");
        if (!getIntent().getStringExtra("userId").equals("")){
            objDatabase = FirebaseDatabase
                    .getInstance()
                    .getReference(String.valueOf(ItemStatus.TypeUser.user));

            DatabaseReference personChild = objDatabase.child(userIdValue);
            personChild.addValueEventListener(this);
        }




    }

    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        //Update a user profile by credentials authentication
        if (!getIntent().getStringExtra("userId").equals(""))
            mAuth.signInWithEmailAndPassword(emailTextView.getText().toString(), passwordTextView.getText().toString())
                .addOnCompleteListener(this);

        else{

            // create new user or register new user
            mAuth
                    .createUserWithEmailAndPassword(emailTextView.getText().toString(),
                            passwordTextView.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful()) {

                                userIdValue = mAuth.getCurrentUser().getUid();
                                addRegister();
                                progressBar.setVisibility(View.GONE);

                                if (addressTextView.getVisibility() == View.VISIBLE){

                                    Intent intent
                                            = new Intent(RegistrationActivity.this,
                                            PlaceProfileActivity.class);
                                    intent.putExtra("userId",userIdValue);
                                    startActivity(intent);
                                }else{

                                    Intent intent
                                            = new Intent(RegistrationActivity.this,
                                            ClientProfileActivity.class);
                                    intent.putExtra("userId",userIdValue);
                                    startActivity(intent);
                                }

                            }
                            else {

                                // Registration failed
                                Toast.makeText(
                                                getApplicationContext(),
                                                "Registration failed!!"
                                                        + " Please try again later",
                                                Toast.LENGTH_LONG)
                                        .show();

                                // hide the progress bar
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }


    }

    private boolean validateFields() {


        String email, password, name, postalCode, address,phone;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        name = nameTextView.getText().toString();
        postalCode = postalCodeTextView.getText().toString();
        address = addressTextView.getText().toString();
        phone = phoneNumberTextView.getText().toString();


        // Validations for input email and password
        if (isFieldEmpty(email,emailTextView.getResources().getResourceEntryName(emailTextView.getId()))) return true;
        if (isFieldEmpty(password, passwordTextView.getResources().getResourceEntryName(passwordTextView.getId()))) return true;
        if (isFieldEmpty(name, nameTextView.getResources().getResourceEntryName(nameTextView.getId()))) return true;
        if (isFieldEmpty(phone, phoneNumberTextView.getResources().getResourceEntryName(phoneNumberTextView.getId()))) return true;

        if(rbProvider.isChecked() == false && rbClient.isChecked() == false){
            Toast.makeText(this,"Please select a type of user",Toast.LENGTH_LONG).show();
            return true;
        }

        if(addressTextView.getVisibility() == View.VISIBLE){

            if (isFieldEmpty(address, addressTextView.getResources().getResourceEntryName(addressTextView.getId()))) return true;
            if (isFieldEmpty(postalCode, postalCodeTextView.getResources().getResourceEntryName(postalCodeTextView.getId()))) return true;
        }

        return false;
    }

    private boolean isFieldEmpty(String textField, String resourceName) {

        if (TextUtils.isEmpty(textField)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter "+ resourceName,
                            Toast.LENGTH_LONG)
                    .show();
            return true;
        }
        return false;
    }

    private void addRegister() {



        if (getIntent().getExtras().getString("userId").equals("")) {


            if(addressTextView.getVisibility() == View.VISIBLE){
                objDatabase = FirebaseDatabase
                        .getInstance()
                        .getReference(String.valueOf(ItemStatus.TypeUser.user));

                Provider provider = new Provider(phoneNumberTextView.getText().toString(),
                        nameTextView.getText().toString(),emailTextView.getText().toString(),
                        postalCodeTextView.getText().toString(),addressTextView.getText().toString(),null);


                objDatabase.child(mAuth.getCurrentUser().getUid()).setValue(provider);
                Toast.makeText(this,"The place has" +
                        " been registered successfully", Toast.LENGTH_LONG).show();
            }else{
                objDatabase = FirebaseDatabase
                        .getInstance()
                        .getReference(String.valueOf(ItemStatus.TypeUser.user));
                Client client = new Client(phoneNumberTextView.getText().toString(),
                        nameTextView.getText().toString(),emailTextView.getText().toString());
                objDatabase.child(mAuth.getCurrentUser().getUid()).setValue(client);
                Toast.makeText(this,"The client has" +
                        " been registered successfully", Toast.LENGTH_LONG).show();
            }


        }


         else {

             if(objDatabase != null){

                 if(addressTextView.getVisibility() == View.VISIBLE){
                     objDatabase.child(userIdValue).child("name").setValue(nameTextView.getText().toString());
                     objDatabase.child(userIdValue).child("phoneNumber").setValue(phoneNumberTextView.getText().toString());
                     objDatabase.child(userIdValue).child("address").setValue(addressTextView.getText().toString());
                     objDatabase.child(userIdValue).child("postalCode").setValue(postalCodeTextView.getText().toString());

                     Toast.makeText(this,"The place has" +
                             " been updated successfully", Toast.LENGTH_LONG).show();
                 }else{
                     objDatabase.child(userIdValue).child("name").setValue(nameTextView.getText().toString());
                     objDatabase.child(userIdValue).child("phoneNumber").setValue(phoneNumberTextView.getText().toString());
                     Toast.makeText(this,"The client has" +
                             " been updated successfully", Toast.LENGTH_LONG).show();
                 }





             }

        }





    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

            case R.id.rbClient:
                postalCodeTextView.setVisibility(View.INVISIBLE);
                addressTextView.setVisibility(View.INVISIBLE);
                break;
            case R.id.rbProvider:
                postalCodeTextView.setVisibility(View.VISIBLE);
                addressTextView.setVisibility(View.VISIBLE);
                break;
            case R.id.btnRegister:
                //Validate fields
                if (validateFields()) return;
                registerNewUser();


        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){
            nameTextView.setText(snapshot.child("name").getValue().toString());
            emailTextView.setText(snapshot.child("email").getValue().toString());
            phoneNumberTextView.setText(snapshot.child("phoneNumber").getValue().toString());
            rgUserType.setVisibility(View.INVISIBLE);
            tvRegister.setText("Update your name and phone number");
            emailTextView.setEnabled(false);
            btnRegister.setText("Update Profile");
            if(!snapshot.child("userType").getValue().toString().equals("provider")){
                tvUserType.setVisibility(View.INVISIBLE);
                rbClient.setChecked(true);

            }
            else{
                rbProvider.setChecked(true);
                tvUserType.setText("Update your address and postal code");
                postalCodeTextView.setVisibility(View.VISIBLE);
                addressTextView.setVisibility(View.VISIBLE);
                postalCodeTextView.setText(snapshot.child("postalCode").getValue().toString());
                addressTextView.setText(snapshot.child("address").getValue().toString());

            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            addRegister();


            // hide the progress bar
            progressBar.setVisibility(View.GONE);

            // if sign-in is successful
            // intent to home activity
            if (addressTextView.getVisibility() == View.VISIBLE){

                Intent intent
                        = new Intent(RegistrationActivity.this,
                        PlaceProfileActivity.class);
                intent.putExtra("userId",mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }else{

                Intent intent
                        = new Intent(RegistrationActivity.this,
                        ClientProfileActivity.class);
                intent.putExtra("userId",mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }

        }

        else {

            // sign-in failed
            Toast.makeText(getApplicationContext(),
                            "Updated failed!!",
                            Toast.LENGTH_LONG)
                    .show();

            // hide the progress bar
            progressBar.setVisibility(View.GONE);
        }
    }
}


