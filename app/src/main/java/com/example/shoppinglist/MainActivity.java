package com.example.shoppinglist;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.MoreObjects;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                        }

                        //save shopping list item
                    }
                });


            }
        });

    }
}
