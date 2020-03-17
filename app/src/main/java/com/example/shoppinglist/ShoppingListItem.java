package com.example.shoppinglist;

import java.io.Serializable;

public class ShoppingListItem implements Serializable {

    public String item;
    public String itemId;

    public ShoppingListItem(){

    }

    public ShoppingListItem(String item, String itemId){
        this.item = item;
        this.itemId = itemId;
    }

    public String getItem() {
        return item;
    }

    public String getItemId() {
        return itemId;
    }
}
