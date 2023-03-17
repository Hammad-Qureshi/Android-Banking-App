package sh.surge.hammad.bankmobile.Common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sh.surge.hammad.bankmobile.Common.LoginSignup.login;
import sh.surge.hammad.bankmobile.Common.LoginSignup.signup;
import sh.surge.hammad.bankmobile.Databases.SessionManager;
import sh.surge.hammad.bankmobile.R;
import sh.surge.hammad.bankmobile.User.UserDashboard;

public class SplashScreen extends AppCompatActivity {

    public static int SPLASH_TIME = 2500;

    Animation topAnim, bottomAnim;
    ImageView icon;
    TextView logo_text;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        icon = findViewById(R.id.icon);
        logo_text = findViewById(R.id.logo_text);

        icon.setAnimation(topAnim);
        logo_text.setAnimation(bottomAnim);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
//                if (user!=null)
//                {
//                    intent = new Intent(getApplicationContext(), UserDashboard.class);
//                }
//                else
//                {
//                    intent = new Intent(getApplicationContext(), login.class);
//                }
                intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME);
    }
}