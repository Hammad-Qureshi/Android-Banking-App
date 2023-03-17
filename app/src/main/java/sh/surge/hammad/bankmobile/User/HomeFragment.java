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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import sh.surge.hammad.bankmobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    LinearLayout showAddPop, showSendPop;
    EditText amountInput, sendAmountInput, sendUserInput;
    int totalAmount;
    String ref = "https://bank-mobile-ed289-default-rtdb.asia-southeast1.firebasedatabase.app";

    String _balance, _username, _cardNo, _name, _phone, _pass;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        showAddPop = view.findViewById(R.id.addMoneyPopup);
        showSendPop = view.findViewById(R.id.sendMoneyPopup);
        amountInput = view.findViewById(R.id.amountToAddTxt);
        sendAmountInput = view.findViewById(R.id.amountToSendTxt);
        sendUserInput = view.findViewById(R.id.usernameSendTxt);

        TextView cardNo, name, firstN, bal;
        Button add, send, addPopBtn, sendPopBtn;

        _username = this.getArguments().getString("un");
        _balance = this.getArguments().getString("bal");
        _cardNo = this.getArguments().getString("cn");
        _name = this.getArguments().getString("nm");
        String _firstName = this.getArguments().getString("fn");
        _phone = this.getArguments().getString("ph");
        _pass = this.getArguments().getString("pass");


        String balFormat = "Rs. " + _balance + "/-";

        cardNo = view.findViewById(R.id.cardNum);
        cardNo.setText(_cardNo);

        name = view.findViewById(R.id.cardName);
        name.setText(_name);

        firstN = view.findViewById(R.id.name);
        firstN.setText(_firstName);

        bal = view.findViewById(R.id.money);
        bal.setText(balFormat);
        
        //Add button

        add = view.findViewById(R.id.addMoney);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPop.setVisibility(View.VISIBLE);
            }
        });
        addPopBtn = view.findViewById(R.id.addMoneyBtn);
        addPopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                addMoney(_username, _balance, bal);
            }
        });
        
        //Send button

        send = view.findViewById(R.id.sendMoney);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSendPop.setVisibility(View.VISIBLE);
            }
        });
        sendPopBtn = view.findViewById(R.id.sendMoneyBtn);
        sendPopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                sendMoney(_username, _balance, bal);
            }
        });

        return view;
    }

    private void addMoney(String un, String dbBal, TextView bal)
    {
        View view2 = getActivity().getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }

        String userAmount = amountInput.getText().toString();

        if (userAmount.equals(""))
        {
            Toast.makeText(getActivity(), "Amount can't be null!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            totalAmount = Integer.parseInt(userAmount) + Integer.parseInt(dbBal);

            String totalAmountString = String.valueOf(totalAmount);

            Query updateBal = FirebaseDatabase.getInstance(ref).getReference("Users").orderByChild("username").equalTo(un);
            updateBal.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        FirebaseDatabase.getInstance(ref).getReference().child("Users/"+un+"/balance").setValue(totalAmountString);
                        Intent temp = new Intent(getActivity(), UserDashboard.class);
                        temp.putExtra("newBal",totalAmount+"");
                        temp.putExtra("newName", _name);
                        temp.putExtra("newPhone", _phone);
                        temp.putExtra("newPass", _pass);
                        startActivity(temp);
                        Toast.makeText(getActivity(), "Money added successfully :)", Toast.LENGTH_SHORT).show();
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
    }

    private void sendMoney(String currentUser, String currentUserBal, TextView currentUserTV) 
    {

        View view2 = getActivity().getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }

        String newUserAmount = sendAmountInput.getText().toString();
        String newUserName = sendUserInput.getText().toString();
        
        if (newUserAmount.equals("") || newUserName.equals(""))
        {
            Toast.makeText(getActivity(), "Enter complete details!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (newUserName.equals(currentUser))
            {
                Intent temp = new Intent(getActivity(), UserDashboard.class);
                temp.putExtra("newBal",currentUserBal);
                temp.putExtra("newName", _name);
                temp.putExtra("newPhone", _phone);
                temp.putExtra("newPass", _pass);
                startActivity(temp);
                Toast.makeText(getActivity(), "Can't transact between same user :)", Toast.LENGTH_LONG).show();
            }
            else
            {
                int totalAmountTemp = Integer.parseInt(currentUserBal) - Integer.parseInt(newUserAmount);
                if (totalAmountTemp < 0)
                {
                    Intent temp = new Intent(getActivity(), UserDashboard.class);
                    temp.putExtra("newBal",currentUserBal);
                    temp.putExtra("newName", _name);
                    temp.putExtra("newPhone", _phone);
                    temp.putExtra("newPass", _pass);
                    startActivity(temp);
                    Toast.makeText(getActivity(), "Insufficient Balance!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    totalAmount = totalAmountTemp;
                    Query checkNewUser = FirebaseDatabase.getInstance(ref).getReference("Users").orderByChild("username").equalTo(newUserName);
                    checkNewUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                Query updateNewUserBal = FirebaseDatabase.getInstance(ref).getReference("Users").orderByChild("username").equalTo(newUserName);
                                updateNewUserBal.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot)
                                    {
                                        String tempBal = snapshot.child(newUserName).child("balance").getValue(String.class);
                                        int newUserBal = Integer.parseInt(tempBal);
                                        newUserBal = newUserBal + Integer.parseInt(newUserAmount);
                                        tempBal = newUserBal + "";
                                        FirebaseDatabase.getInstance(ref).getReference().child("Users/"+newUserName+"/balance").setValue(tempBal);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error)
                                    {
                                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                String totalAmountString = String.valueOf(totalAmount);

                                Query updateCurrentUserBal = FirebaseDatabase.getInstance(ref).getReference("Users").orderByChild("username").equalTo(currentUser);
                                updateCurrentUserBal.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        FirebaseDatabase.getInstance(ref).getReference().child("Users/"+currentUser+"/balance").setValue(totalAmountString);
                                        Intent temp = new Intent(getActivity(), UserDashboard.class);
                                        temp.putExtra("newBal",totalAmount+"");
                                        temp.putExtra("newName", _name);
                                        temp.putExtra("newPhone", _phone);
                                        temp.putExtra("newPass", _pass);
                                        startActivity(temp);
                                        Toast.makeText(getActivity(), "Transaction successful :)", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "User doesn't exists!", Toast.LENGTH_SHORT).show();
                                Intent temp = new Intent(getActivity(), UserDashboard.class);
                                temp.putExtra("newBal",currentUserBal);
                                temp.putExtra("newName", _name);
                                temp.putExtra("newPhone", _phone);
                                temp.putExtra("newPass", _pass);
                                startActivity(temp);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }
}