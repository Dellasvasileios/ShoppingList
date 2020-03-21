package com.example.shoppinglist;

import java.io.Serializable;

public class ShoppingListItem implements Serializable {

    public String item;
    public String itemId;
    public boolean checked;

    public ShoppingListItem(){

    }

    public ShoppingListItem(String item, String itemId, boolean checked){
        this.item = item;
        this.itemId = itemId;
        this.checked = checked;
    }

    public String getItem() {
        return item;
    }

    public String getItemId() {
        return itemId;
    }

    public boolean getChecked() {
        return checked;
    }
}
