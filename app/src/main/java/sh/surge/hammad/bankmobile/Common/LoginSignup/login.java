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

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import sh.surge.hammad.bankmobile.Databases.SessionManager;
import sh.surge.hammad.bankmobile.R;

public class login extends AppCompatActivity {

    EditText username, password;
    RelativeLayout progress;
    String ref = "https://bank-mobile-ed289-default-rtdb.asia-southeast1.firebasedatabase.app";
    String un;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        progress = findViewById(R.id.signinOtpProgress);
    }

    public void signIn(View view)
    {
        // Check if no view has focus:
        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }

        un = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        un = un.replace(" ", "");

        if (un.isEmpty() || pass.isEmpty())
        {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progress.setVisibility(View.VISIBLE);
            Query checkUser = FirebaseDatabase.getInstance(ref).getReference("Users").orderByChild("username").equalTo(un);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if (snapshot.exists())
                    {
                        String sysPass = snapshot.child(un).child("password").getValue(String.class);

                        if (sysPass.equals(pass))
                        {
                            String _username = snapshot.child(un).child("username").getValue(String.class);
                            String _name = snapshot.child(un).child("name").getValue(String.class);
                            String _password = snapshot.child(un).child("password").getValue(String.class);
                            String _phone = snapshot.child(un).child("phone").getValue(String.class);
                            String _balance = snapshot.child(un).child("balance").getValue(String.class);
                            String _cardNo = snapshot.child(un).child("cardNo").getValue(String.class);
                            String _cvc = snapshot.child(un).child("cvc").getValue(String.class);

//                            progress.setVisibility(View.GONE);

                            SessionManager sessionManager = new SessionManager(login.this);
                            sessionManager.createLoginSession(_username,_name,_password,_phone,_balance,_cardNo,_cvc);
//                            startActivity(new Intent(getApplicationContext(), UserDashboard.class));

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    _phone,
                                    60,
                                    TimeUnit.SECONDS,
                                    login.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            progress.setVisibility(View.GONE);
                                            Toast.makeText(login.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                              progress.setVisibility(View.GONE);
                                              Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            progress.setVisibility(View.GONE);
                                            Intent intent = new Intent(getApplicationContext(), login_otp.class);
                                            intent.putExtra("phoneNo", _phone);
                                            intent.putExtra("verificationID", verificationID);
                                            startActivity(intent);
                                        }
                                    }
                            );

                        }
                        else
                        {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(login.this, "User doesn't exists!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void noAccount(View view)
    {
        Intent intent = new Intent(getApplicationContext(), signup.class);
        startActivity(intent);
    }
}