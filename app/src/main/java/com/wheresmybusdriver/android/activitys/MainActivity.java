package com.wheresmybusdriver.android.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.wheresmybusdriver.android.R;
import com.wheresmybusdriver.android.utils.Auth;

import java.util.logging.ConsoleHandler;

public class MainActivity extends AppCompatActivity {

    Auth auth = new Auth();

    EditText editEmail, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
    }

    public void login(View view){
        auth.signIn(MainActivity.this, editEmail.getText().toString(),
                editPassword.getText().toString());
    }

    public void navigateToRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
