package com.example.user.bookstore.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bookstore.Database.Action;
import com.example.user.bookstore.Database.Database;
import com.example.user.bookstore.Login;
import com.example.user.bookstore.R;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private View rootview;
    private TextView username, fullname, address, phonenumber, password, creditcard, hello_msg;
    private Button submit;
    private OnFragmentInteractionListener mListener;

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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        try {
            Database database = new Database();
            String info_string = database.execute(Action.GETPROFILE.toString(), Login.USERNAME).get();
            String[] info = info_string.split(";");
            submit = (Button) rootview.findViewById(R.id.submit);
            username = (TextView) rootview.findViewById(R.id.username);
            fullname = (TextView) rootview.findViewById(R.id.full_name);
            address = (TextView) rootview.findViewById(R.id.address);
            phonenumber = (TextView) rootview.findViewById(R.id.phone_number);
            password = (TextView) rootview.findViewById(R.id.password);
            creditcard = (TextView) rootview.findViewById(R.id.credit_card);
            hello_msg = (TextView) rootview.findViewById(R.id.hello_msg);

            String[] name = info[0].split(" ");
            hello_msg.setText("Hello " + name[0]);
            fullname.setText(info[0]);
            username.setText(info[1]);
            password.setText(info[2]);
            creditcard.setText(info[3]);
            address.setText(info[4]);
            phonenumber.setText(info[5]);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String fullname_str = fullname.getText().toString();
                        String username_str = username.getText().toString();
                        String password_str = password.getText().toString();
                        String address_str = address.getText().toString();
                        String phone_str = phonenumber.getText().toString();
                        String creditCard_str = creditcard.getText().toString();

                        if (!password_str.equals("")) {
                            Database database = new Database();
                            String result = database.execute(Action.REGISTER.toString(), fullname_str, username_str, password_str,
                                    address_str, phone_str, creditCard_str).get();
                            Log.d("UPDATE PROFILE", result);
                            Toast.makeText(getActivity().getApplicationContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    } catch (
                            InterruptedException e
                            )

                    {
                        e.printStackTrace();
                    } catch (
                            ExecutionException e
                            ) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (
                InterruptedException e
                )

        {
            e.printStackTrace();
        } catch (
                ExecutionException e
                )

        {
            e.printStackTrace();
        }

        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
