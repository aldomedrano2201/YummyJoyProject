package com.example.prjyummyjoy;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import util.ItemStatus;

public class ListItemsActivity extends AppCompatActivity implements ValueEventListener {

    // Array of strings...
    ListView simpleList;
    ArrayList<String> itemsList= new ArrayList<>();
    ActivityResultLauncher<Intent> activityResultLauncher;
    DatabaseReference objDatabase;
    String userIdValue, tempUserIdValue;



    @Override   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        initialize();






    }

    private void initialize() {




            objDatabase = FirebaseDatabase
                    .getInstance()
                    .getReference(String.valueOf(ItemStatus.TypeUser.item));
            userIdValue = getIntent().getStringExtra("userId");


            objDatabase.addValueEventListener(this);







        simpleList = (ListView)findViewById(R.id.itemsListView);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView2, itemsList);
        simpleList.setAdapter(arrayAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);


                Intent intent;

                    if (userIdValue.equals("Reserve") || userIdValue.equals("Review")){
                        intent = new Intent(ListItemsActivity.this, ReserveMealProductActivity.class);
                        intent.putExtra("userId",selectedItem +tempUserIdValue+"\t"+ userIdValue);
                    }else{
                        intent = new Intent(ListItemsActivity.this, PublishMealProductActivity.class);
                        intent.putExtra("userId",selectedItem);
                    }
                    startActivity(intent);







            }
        });




        /*userIdValue = getIntent().getStringExtra("userId");
       */

    }


    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){

            if (userIdValue.equals("Reserve")|| userIdValue.equals("Review")){
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    String itemVal = snapshot1.getValue().toString();
                    String[] itemDetails = itemVal.split("\t");
                    if (snapshot1.getValue().toString().contains(ItemStatus.Status.PUBLISHED.toString())) {
                        itemsList.add("ID: " + "\t" +itemDetails[0] + "\t" +" Meal or product: " + "\t" +itemDetails[2] +
                                "\t" +" - Status: " + "\t" +itemDetails[itemDetails.length - 1] + "\t" +" - Items available: "
                                + "\t" +itemDetails[5] + "\t" +" - Hour: " + "\t" + itemDetails[3]+ "\t" +
                                " - Price: " + "\t" + itemDetails[4]);
                        tempUserIdValue = "\t" + itemDetails[1];

                    }
                }

            }else{
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        String itemVal = snapshot1.getValue().toString();
                        String[] itemDetails = itemVal.split("\t");
                        if (snapshot1.getValue().toString().contains(userIdValue)) {
                            itemsList.add("ID: " + itemDetails[0] + " Name: " + itemDetails[2] + " - Status: " + itemDetails[itemDetails.length - 1]);
                        }
                    }

            }











        }

    }



    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}