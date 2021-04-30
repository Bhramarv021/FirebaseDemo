package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText name;
    private Button addName;
    private Button logout;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        addName = findViewById(R.id.add_name);
        logout = findViewById(R.id.logout);
        listView = findViewById(R.id.list);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
            }
        });

        /*
        To add single value in firebase realtime data
        */
//        FirebaseDatabase.getInstance().getReference("message").setValue("Hello msg");

        /*
        To add multiple data then use hashmap
        */
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("Name","Bhramar Virmani");
//        map.put("Email","bhramarv021@gmail.com");
//
//        FirebaseDatabase.getInstance().getReference().child("User Details").updateChildren(map);

        /*
        To add data input by user
        */
        addName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textName = name.getText().toString();
                if (textName.isEmpty())
                    Toast.makeText(MainActivity.this, "No Name Entered", Toast.LENGTH_SHORT).show();
                else {
                    FirebaseDatabase.getInstance().getReference().child("User Details").push().child("Name").setValue(textName);
                }
            }
        });

        ArrayList<String> arraylist = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item);
        listView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User Details");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arraylist.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    arraylist.add(snapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}