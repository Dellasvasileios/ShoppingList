package com.example.shoppinglist;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShoppingListItemsActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;

    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    ArrayList<ShoppingListItem> shoppingListItems = new ArrayList();
    ShoppingList shoppingList;
    ShoppingListItemAdapter  shoppingListItemAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String shoppingListId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppin_list_items);

        Intent intent = getIntent();
        shoppingListId = intent.getStringExtra("shoppingListId");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("shoppingLists");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        shoppingListItems = new ArrayList<>();
        shoppingListItemAdapter = new ShoppingListItemAdapter(shoppingListItems, this,shoppingListId);
        recyclerView.setAdapter(shoppingListItemAdapter);

        databaseReference.child(firebaseUser.getUid()).child(shoppingListId).child("shoppingListItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shoppingListItems.clear();
                for(DataSnapshot shoppingListItemSnapshot : dataSnapshot.getChildren()){
                    shoppingListItems.add(shoppingListItemSnapshot.getValue(ShoppingListItem.class));
                }
                shoppingListItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ShoppingListItemsActivity.this);
                dialog.setContentView(R.layout.add_shopping_list_item);
                dialog.show();

                final EditText shoppingListName = dialog.findViewById(R.id.shopping_list_item_name);
                Button createButton = dialog.findViewById(R.id.create);
                Button cancelButton = dialog.findViewById(R.id.cancel_dialog);

                createButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String shoppingListItemName = shoppingListName.getText().toString();
                        if((shoppingListItemName!= null && shoppingListItemName.trim().isEmpty()) || shoppingListItemName == null){
                            Toast.makeText(ShoppingListItemsActivity.this, "Shopping List item name is empty.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String uniqId = databaseReference.push().getKey();
                            final ShoppingListItem shoppingListItem = new ShoppingListItem(shoppingListItemName, uniqId ,false);


                            databaseReference.child(firebaseUser.getUid()).child(shoppingListId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);


                                    if(shoppingList!=null){
                                        if(shoppingList.shoppingListItems == null){
                                            shoppingList.shoppingListItems = new ArrayList();
                                        }
                                        shoppingList.shoppingListItems.add(shoppingListItem);
                                        databaseReference.child(firebaseUser.getUid()).child(shoppingListId).setValue(shoppingList);

                                    }

                                    dialog.dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
