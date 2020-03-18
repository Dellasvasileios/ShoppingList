package com.example.shoppinglist;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.MoreObjects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatButton;
    RecyclerView recycler;
    ShoppingListAdapter shoppingListAdapter;
    ArrayList<ShoppingList> shoppingLists;

    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("shoppingLists");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recycler = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        shoppingLists = new ArrayList<>();
        shoppingListAdapter = new ShoppingListAdapter(shoppingLists, this);
        recycler.setAdapter(shoppingListAdapter);


        shoppingLists.add(new ShoppingList("lalala","dasdsa"));
        shoppingLists.add(new ShoppingList("lalala","dasdsa"));
        shoppingLists.add(new ShoppingList("lalala","dasdsa"));
        shoppingLists.add(new ShoppingList("lalala","dasdsa"));
        shoppingListAdapter.notifyDataSetChanged();

        floatButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_shopping_list_name);
                dialog.show();

                final EditText shoppingListNameText = (EditText) dialog.findViewById(R.id.shopping_list_name);

                Button cancelButton = (Button) dialog.findViewById(R.id.cancel_dialog);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button createButton = (Button) dialog.findViewById(R.id.create);
                createButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String shoppingListName = shoppingListNameText.getText().toString();
                        if((shoppingListName!= null && shoppingListName.trim().isEmpty()) || shoppingListName == null){
                            Toast.makeText(MainActivity.this, "Shopping List name is empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // 1. save the data
                            String UserUID = firebaseUser.getUid();
                            String uniqId = databaseReference.push().getKey();
                            ShoppingList shoppingList = new ShoppingList(shoppingListName,uniqId);
                            databaseReference.child(UserUID).child(uniqId).setValue(shoppingList);
                            dialog.dismiss();
                            // 2. and update the user Interface(make the recycler view)
                        }
                    }
                });


            }
        });

    }
}
