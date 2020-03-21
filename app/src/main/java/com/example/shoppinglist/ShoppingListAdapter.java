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

import com.google.common.base.MoreObjects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//it is important to say that some overriden methods works for each view item

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListHolder> {

    private ArrayList<ShoppingList> shoppingLists;
    private Context context;

    private FirebaseUser firebaseUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public ShoppingListAdapter(ArrayList<ShoppingList> shoppingLists, Context context){
        this.shoppingLists = shoppingLists;
        this.context = context;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("shoppingLists");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    public ShoppingListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_shoppim_list, parent, false);
        return new ShoppingListHolder(view);
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull final ShoppingListHolder holder, final int position) {
        final ShoppingList shoppingList = shoppingLists.get(position);

        if(shoppingList.checked == true){
            holder.checkboxShoppingList.setChecked(true);
        }
        else{
            holder.checkboxShoppingList.setChecked(false);
        }
        // Set the data to the views here
        holder.setShoppingListName(shoppingList.getName());
        holder.imageViewEdit.setBackgroundResource(R.drawable.ic_edit_black_24dp);
        holder.imageViewDelete.setBackgroundResource(R.drawable.ic_delete_black_24dp);

        holder.shoppingListNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ShoppingListItems.class);
                intent.putExtra("shoppingListId",shoppingList.shoppingListId);
                context.startActivity(intent);
            }
        });

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_shopping_list_name);
                final EditText shoppingListNameText = (EditText) dialog.findViewById(R.id.shopping_list_name);
                shoppingListNameText.setText(shoppingList.name);
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
                            Toast.makeText(context, "Shopping List name is empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // 1. save the data
                            String UserUID = firebaseUser.getUid();
                            shoppingList.name = shoppingListName;
                            databaseReference.child(UserUID).child(shoppingList.shoppingListId).setValue(shoppingList);
                            dialog.dismiss();
                        }
                    }
                });

            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "shoppingListItemPressed", Toast.LENGTH_SHORT).show();
            }
        });

        holder.checkboxShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkboxShoppingList.isChecked()){
                    shoppingList.checked = true;
                }
                else{
                    shoppingList.checked = false;
                }
                databaseReference.child(firebaseUser.getUid()).child(shoppingList.shoppingListId).setValue(shoppingList);
            }
        });
        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    @Override
    public int getItemCount() {
        return shoppingLists == null? 0: shoppingLists.size();
    }

    // This is your ViewHolder class that helps to populate data to the view
    public class ShoppingListHolder extends RecyclerView.ViewHolder{

        public TextView shoppingListNameTextView;
        public ImageView imageViewEdit;
        public ImageView imageViewDelete;
        public CheckBox checkboxShoppingList;

        public ShoppingListHolder(View itemView) {
            super(itemView);
            shoppingListNameTextView =(TextView) itemView.findViewById(R.id.textView_shoopingListName);
            imageViewEdit = (ImageView) itemView.findViewById(R.id.imageViewEdit);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.imageViewDelete);
            checkboxShoppingList = (CheckBox) itemView.findViewById(R.id.checkBox_shoppingList);

        }

        public void setShoppingListName(String name){
            shoppingListNameTextView.setText(name);
        }
    }

}
