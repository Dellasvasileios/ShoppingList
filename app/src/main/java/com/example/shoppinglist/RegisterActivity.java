package com.example.shoppinglist;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    Button buttonRegister;
    EditText editTextUserName,editTextPassword;

    private AwesomeValidation awesomeValidation;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonRegister = (Button) findViewById(R.id.register);
        editTextUserName = (EditText) findViewById(R.id.editText_username);
        editTextPassword = (EditText) findViewById(R.id.editText_password);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.editText_username, Patterns.EMAIL_ADDRESS, R.string.validEmail);
        awesomeValidation.addValidation(this, R.id.editText_password, ".{6,}", R.string.validPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    Toast.makeText(RegisterActivity.this, "Validation was Successfull", Toast.LENGTH_SHORT).show();

                    String email = editTextUserName.getText().toString();
                    String password = editTextPassword.getText().toString();

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Failed To create Account check to see if the E-mail is valid.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

    }
}
