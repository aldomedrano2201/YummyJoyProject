package com.example.prjyummyjoy;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;
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

import util.ItemStatus;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener, OnCompleteListener<AuthResult> {

    private EditText emailTextView, passwordTextView;
    private Button Btn,BtnSignUp;
    private ProgressBar progressbar;
    DatabaseReference objDatabase;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();


    }

    private void initialize() {


        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.login);
        BtnSignUp = findViewById(R.id.signUp);
        progressbar = findViewById(R.id.progressBar);
        BtnSignUp.setOnClickListener(this);
        // Set on Click Listener on Sign-in button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount();
            }
        });

    }

    private void loginUserAccount()
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.signUp:
                Intent intent
                        = new Intent(LoginActivity.this,
                        RegistrationActivity.class);
                intent.putExtra("userId", "");
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){
            if (snapshot.child("userType").getValue().toString().equals(ItemStatus.TypeUser.provider.toString())){
                Intent intent
                        = new Intent(LoginActivity.this,
                        PlaceProfileActivity.class);
                intent.putExtra("userId",mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }else{
                Intent intent
                        = new Intent(LoginActivity.this,
                        ClientProfileActivity.class);
                intent.putExtra("userId",mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(),
                                "Login successful!!",
                                Toast.LENGTH_LONG)
                        .show();

                // hide the progress bar
                progressbar.setVisibility(View.GONE);

                // if sign-in is successful
                // intent to home activity
                objDatabase = FirebaseDatabase
                        .getInstance()
                        .getReference(String.valueOf(ItemStatus.TypeUser.user));
                DatabaseReference personChild = objDatabase.child(mAuth.getCurrentUser().getUid());
                personChild.addValueEventListener(this);

            }

            else {

                // sign-in failed
                Toast.makeText(getApplicationContext(),
                                "Login failed!!",
                                Toast.LENGTH_LONG)
                        .show();

                // hide the progress bar
                progressbar.setVisibility(View.GONE);
            }
    }

}