package com.wheresmybusdriver.android.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wheresmybusdriver.android.activitys.LocalizationActivity;

public class Auth {
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public String getUserId() {
        String uid = auth.getUid();

        if (uid != null) {
            return uid;
        } else {
            return null;
        }
    }

    public void signIn(final Context context, String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(context, LocalizationActivity.class);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Email ou senha incorretos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
