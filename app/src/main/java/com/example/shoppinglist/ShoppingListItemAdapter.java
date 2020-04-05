package com.example.shoppinglist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//it is important to say that some overriden methods works for each view item

public class ShoppingListItemAdapter extends RecyclerView.Adapter<ShoppingListItemAdapter.ShoppingListItemHolder> {

    private ArrayList<ShoppingListItem> shoppingListItems;
    private Context context;
    private String shoppingListId;

    private FirebaseUser firebaseUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public ShoppingListItemAdapter(ArrayList<ShoppingListItem> shoppingListItems, Context context, String shoppingListId){
        this.shoppingListItems = shoppingListItems;
        this.context = context;
        this.shoppingListId = shoppingListId;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("shoppingLists");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    public ShoppingListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_shopping_list_item, parent, false);
        return new ShoppingListItemHolder(view);
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull final ShoppingListItemHolder holder, final int position) {
        final ShoppingListItem shoppingListItem = shoppingListItems.get(position);

        if(shoppingListItem.checked == true){
            holder.checkboxShoppingList.setChecked(true);
        }
        else{
            holder.checkboxShoppingList.setChecked(false);
        }
        // Set the data to the views here
        holder.setShoppingListItemName(shoppingListItem.getItem());
        holder.imageViewEdit.setBackgroundResource(R.drawable.ic_edit_black_24dp);
        holder.imageViewDelete.setBackgroundResource(R.drawable.ic_delete_black_24dp);

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_shopping_list_name);
                final EditText shoppingListNameText = (EditText) dialog.findViewById(R.id.shopping_list_name);
                shoppingListNameText.setText(shoppingListItem.item);
                dialog.show();


                Button cancelButton = (Button) dialog.findViewById(R.id.cancel_dialog);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button editButton = (Button) dialog.findViewById(R.id.update);

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String shoppingListName = shoppingListNameText.getText().toString();
                        if((shoppingListName!= null && shoppingListName.trim().isEmpty()) || shoppingListName == null){
                            Toast.makeText(context, "Shopping List item name is empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // 1. save the data
                            String UserUID = firebaseUser.getUid();

                            databaseReference.child(UserUID).child(shoppingListId).child("shoppingListItems").child(String.valueOf(position)).child("item").setValue(shoppingListName);
                            dialog.dismiss();
                        }
                    }
                });

            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.delete_shopping_list);
                dialog.show();

                Button deleteButton = (Button)dialog.findViewById(R.id.delete);
                Button cancelButton = (Button)dialog.findViewById(R.id.cancel_dialog);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String UserUID = firebaseUser.getUid();


                        databaseReference.child(UserUID).child(shoppingListId).child("shoppingListItems").addListenerForSingleValueEvent(new ValueEventListener() {
                            ArrayList<ShoppingListItem> shoppingListItems = new ArrayList<>();
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot shoppingListItemSnapshot : dataSnapshot.getChildren()){
                                    shoppingListItems.add(shoppingListItemSnapshot.getValue(ShoppingListItem.class));
                                }
                                shoppingListItems.remove(position);
                                databaseReference.child(UserUID).child(shoppingListId).child("shoppingListItems").removeValue();
                                databaseReference.child(UserUID).child(shoppingListId).child("shoppingListItems").setValue(shoppingListItems);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        dialog.dismiss();
                    }
                });
            }
        });

        holder.checkboxShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkboxShoppingList.isChecked()){
                    shoppingListItem.checked = true;
                }
                else{
                    shoppingListItem.checked = false;
                }
                databaseReference.child(firebaseUser.getUid()).child(shoppingListId).child("shoppingListItems").child(String.valueOf(position)).child("checked").setValue(shoppingListItem.checked);
            }
        });
        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    @Override
    public int getItemCount() {
        return shoppingListItems == null? 0: shoppingListItems.size();
    }

    // This is your ViewHolder class that helps to populate data to the view
    public class ShoppingListItemHolder extends RecyclerView.ViewHolder{

        public TextView shoppingListNameTextView;
        public ImageView imageViewEdit;
        public ImageView imageViewDelete;
        public CheckBox checkboxShoppingList;

        public ShoppingListItemHolder(View itemView) {
            super(itemView);
            shoppingListNameTextView =(TextView) itemView.findViewById(R.id.textView_shoopingListName);
            imageViewEdit = (ImageView) itemView.findViewById(R.id.imageViewEdit);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.imageViewDelete);
            checkboxShoppingList = (CheckBox) itemView.findViewById(R.id.checkBox_shoppingList);

        }

        public void setShoppingListItemName(String name){
            shoppingListNameTextView.setText(name);
        }
    }

}

