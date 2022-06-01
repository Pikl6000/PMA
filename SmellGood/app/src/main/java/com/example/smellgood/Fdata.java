package com.example.smellgood;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class Fdata {
    private DatabaseReference databaseReference;
    public Fdata(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Users");
    }

    public Task<Void> add(Player p){
        //return databaseReference.push().setValue(p);
        return databaseReference.child(p.getNickname()).setValue(p);
    }

    public Task<Void> update(String key, HashMap<String ,Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key)
    {
        return databaseReference.child(key).removeValue();
    }

    public Query get()
    {
        return databaseReference;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}
