package com.example.shoppinglist;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingList implements Serializable {
    public String name;
    public String shoppingListId;
    public ArrayList<ShoppingListItem> shoppingListItems;

    public ShoppingList(){

    }

    public ShoppingList(String name, String shoppingListId){
        this.name = name;
        this.shoppingListId = shoppingListId;
        shoppingListItems = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getShoppingListId() {
        return shoppingListId;
    }

    public ArrayList<ShoppingListItem> getShoppingListItems() {
        return shoppingListItems;
    }
}
