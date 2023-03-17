package sh.surge.hammad.bankmobile.Common.LoginSignup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import sh.surge.hammad.bankmobile.Databases.SessionManager;
import sh.surge.hammad.bankmobile.Databases.UserHelper;
import sh.surge.hammad.bankmobile.R;

public class signup extends AppCompatActivity {

    EditText username, name, phone, password;
    RelativeLayout progressBar;
    String ref = "https://bank-mobile-ed289-default-rtdb.asia-southeast1.firebasedatabase.app";

    String _name, _phone, _pass, _un, finalPhoneNo;

    Random r1 = new Random();
    int cn1 = r1.nextInt(8000) + 1000;
    Random r2 = new Random();
    int cn2 = r2.nextInt(8000) + 1000;
    Random r3 = new Random();
    int cn3 = r3.nextInt(8000) + 1000;
    Random r4 = new Random();
    int cn4 = r4.nextInt(8000) + 1000;

    String cardNum = cn1+"-"+cn2+"-"+cn3+"-"+cn4;

    Random random = new Random();
    int CVC = random.nextInt(300) + 100;

    String cvc = CVC + "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);


        username = findViewById(R.id.signup_username);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.signup_password);
        progressBar = findViewById(R.id.signupProgress);
    }

    public void signUp(View view)
    {
        progressBar.setVisibility(View.VISIBLE);

        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }

        _un = username.getText().toString().trim();
        _un = _un.replace(" ", "");
        _name = name.getText().toString().trim();
        _pass = password.getText().toString().trim();
        _phone = phone.getText().toString().trim();

        if (_un.isEmpty() || _name.isEmpty() || _phone.isEmpty() || _pass.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(signup.this, "All fields are required!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (_phone.charAt(0) == '0') {
                _phone = _phone.substring(1);
            }
            finalPhoneNo = "+92" + _phone;

            Query checkUser = FirebaseDatabase.getInstance(ref).getReference("Users").orderByChild("username").equalTo(_un);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(signup.this, "User already exists!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        storeNewUserData();
                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(signup_otp.this, "Verification Completed!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), accountCreated.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(signup.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void alreadyAccount(View view)
    {
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
    }

    private void storeNewUserData()
    {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance(ref);
        DatabaseReference reference = rootNode.getReference("Users");

        UserHelper newUser = new UserHelper(_name, _un, _pass, finalPhoneNo, "0", cardNum, cvc);

        reference.child(_un).setValue(newUser);
    }
}