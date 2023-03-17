package sh.surge.hammad.bankmobile.User;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import sh.surge.hammad.bankmobile.Databases.SessionManager;
import sh.surge.hammad.bankmobile.R;

public class UserDashboard extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        navigationView = findViewById(R.id.menu_nav);

        String balance, name, phone, password, username, cardNo, CVC;

        //To get all data of a login user
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSession();

        username = userDetails.get(SessionManager.KEY_USERNAME);
        name = userDetails.get(SessionManager.KEY_NAME);
        password = userDetails.get(SessionManager.KEY_PASSWORD);
        phone = userDetails.get(SessionManager.KEY_PHONE);
        balance = userDetails.get(SessionManager.KEY_BALANCE);
        cardNo = userDetails.get(SessionManager.KEY_CARD);
        CVC = userDetails.get(SessionManager.KEY_CVC);

        Bundle updateDb = getIntent().getExtras();
        if (updateDb != null) {
            balance = updateDb.getString("newBal");
            name = updateDb.getString("newName");
            phone = updateDb.getString("newPhone");
            password = updateDb.getString("newPass");
        }

//        balance = "Rs. " + balance +"/-";

        String firstName = name.replaceAll("\\s.*", "");
        firstName = "Hello, " + firstName;

        Bundle bundle = new Bundle();

        bundle.putString("un",username);
        bundle.putString("bal",balance);
        bundle.putString("cn",cardNo);
        bundle.putString("nm",name);
        bundle.putString("fn",firstName);
        bundle.putString("pass", password);
        bundle.putString("ph", phone);

        HomeFragment homeFragment1 = new HomeFragment();
        homeFragment1.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, homeFragment1).commit();
        navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        fragment.setArguments(bundle);
                        break;
                    case R.id.nav_profile:
                        fragment = new ProfileFragment();
                        fragment.setArguments(bundle);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                return true;
            }
        });
    }
}