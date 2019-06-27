package com.example.knlgsharing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class SignActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 2326; //Random number
    FirebaseAuth auth;

    public static final String SHARED_PREFS = "sharedPrefs";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();


        auth = FirebaseAuth.getInstance();


        if(auth.getCurrentUser()!=null)
        {
            //user already signed
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            saveData(user.getEmail());
            nextActivity(user.getEmail());
        }
        else
        {

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(true)
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                saveData(user.getEmail());
                nextActivity(user.getEmail());

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }



    private void saveData(String email)
    {
        editor.putString("email", email);
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
    }

    private void nextActivity(final String user_email)
    {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child("Global").child("Admin").child("Email");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String adminEmail = dataSnapshot.getValue(String.class);
                Log.d("DEBUGG",adminEmail);

                if(user_email.equals(adminEmail))
                {
                    Intent intent = new Intent(SignActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(SignActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
