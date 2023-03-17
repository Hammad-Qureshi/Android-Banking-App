package sh.surge.hammad.bankmobile.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import sh.surge.hammad.bankmobile.Common.LoginSignup.login;
import sh.surge.hammad.bankmobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    String ref = "https://bank-mobile-ed289-default-rtdb.asia-southeast1.firebasedatabase.app";

    String usernameDb, nameDb, phoneDb, passDb, balDb;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView username;
        EditText name, phone, password;
        Button update, logout;

        usernameDb = this.getArguments().getString("un");
        nameDb = this.getArguments().getString("nm");
        phoneDb = this.getArguments().getString("ph");
        passDb = this.getArguments().getString("pass");
        balDb = this.getArguments().getString("bal");

        phoneDb = phoneDb.replace("+92", "0");

        username = view.findViewById(R.id.profileUsername);
        name = view.findViewById(R.id.profileName);
        phone = view.findViewById(R.id.profilePhone);
        password = view.findViewById(R.id.profilePassword);
        update = view.findViewById(R.id.profileUpdateBtn);
        logout = view.findViewById(R.id.profileLogoutBtn);

        username.setText(usernameDb);
        name.setText(nameDb);
        phone.setText(phoneDb);
        password.setText(passDb);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                v.setEnabled(false);

                View view2 = getActivity().getCurrentFocus();
                if (view2 != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }

                String newName = name.getText().toString();
                String newPhone = phone.getText().toString();
                String newPass = password.getText().toString();

                newPhone = "+92" + newPhone.substring(1);

                Query updateBal = FirebaseDatabase.getInstance(ref).getReference("Users").orderByChild("username").equalTo(usernameDb);
                String finalNewPhone = newPhone;
                updateBal.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            FirebaseDatabase.getInstance(ref).getReference().child("Users/"+usernameDb+"/name").setValue(newName);
                            FirebaseDatabase.getInstance(ref).getReference().child("Users/"+usernameDb+"/phone").setValue(finalNewPhone);
                            FirebaseDatabase.getInstance(ref).getReference().child("Users/"+usernameDb+"/password").setValue(newPass);
                            Intent temp = new Intent(getActivity(), UserDashboard.class);
                            temp.putExtra("newBal", balDb);
                            temp.putExtra("newName", newName);
                            temp.putExtra("newPhone", finalNewPhone);
                            temp.putExtra("newPass", newPass);
                            startActivity(temp);
                            Toast.makeText(getActivity(), "Profile updated successfully :)", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Couldn't find user.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        });

        return view;
    }
}