package com.example.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//it is important to say that some overriden methods works for each view item

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListHolder> {

    private ArrayList<ShoppingList> shoppingLists;
    private Context context;

    public ShoppingListAdapter(ArrayList<ShoppingList> shoppingLists, Context context){
        this.shoppingLists = shoppingLists;
        this.context = context;
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
    public void onBindViewHolder(@NonNull ShoppingListHolder holder, final int position) {
        final ShoppingList shoppingList = shoppingLists.get(position);

        // Set the data to the views here
        holder.setShoppingListName(shoppingList.getName());
        holder.imageViewEdit.setBackgroundResource(R.drawable.ic_edit_black_24dp);
        holder.imageViewDelete.setBackgroundResource(R.drawable.ic_delete_black_24dp);

        holder.shoppingListNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "shoppingListItemPressed", Toast.LENGTH_SHORT).show();
            }
        });

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "shoppingListItemPressed", Toast.LENGTH_SHORT).show();
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "shoppingListItemPressed", Toast.LENGTH_SHORT).show();
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

        public ShoppingListHolder(View itemView) {
            super(itemView);
            shoppingListNameTextView =(TextView) itemView.findViewById(R.id.textView_shoopingListName);
            imageViewEdit = (ImageView) itemView.findViewById(R.id.imageViewEdit);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.imageViewDelete);

        }

        public void setShoppingListName(String name){
            shoppingListNameTextView.setText(name);
        }
    }

}
