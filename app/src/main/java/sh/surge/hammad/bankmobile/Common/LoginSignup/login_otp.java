package sh.surge.hammad.bankmobile.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import sh.surge.hammad.bankmobile.R;
import sh.surge.hammad.bankmobile.User.UserDashboard;

public class login_otp extends AppCompatActivity {

    RelativeLayout progress;
    TextView phoneNo;
    PinView userPin;
    String sysCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        progress = findViewById(R.id.signinOtpProgress);
        phoneNo = findViewById(R.id.phoneNoTxtSignin);
        phoneNo.setText(getIntent().getStringExtra("phoneNo"));

        userPin = findViewById(R.id.pin_view_Signin);
    }

    public void verify_Signin(View view)
    {
        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
//        progress.setVisibility(View.VISIBLE);
        String userCode = userPin.getText().toString();
        sysCode = getIntent().getStringExtra("verificationID");

        if (sysCode!=null)
        {
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                    sysCode,
                    userCode
            );
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
//                                progress.setVisibility(View.GONE);
                                Toast.makeText(login_otp.this, "Verification Completed!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
//                                progress.setVisibility(View.GONE);
                                Toast.makeText(login_otp.this, "Invalid Code Entered!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            progress.setVisibility(View.GONE);
            Toast.makeText(this, "No code fetched!", Toast.LENGTH_SHORT).show();
        }
    }
}