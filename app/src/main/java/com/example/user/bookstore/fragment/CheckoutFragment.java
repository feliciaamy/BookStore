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
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.bookstore.BookList.BookRow;
import com.example.user.bookstore.Database.Action;
import com.example.user.bookstore.Database.Database;
import com.example.user.bookstore.Login;
import com.example.user.bookstore.MainActivity;
import com.example.user.bookstore.PlaceOrder.PlaceOrderAdapter;
import com.example.user.bookstore.R;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckoutFragment extends Fragment {
    private View rootView;
    private ListView orderList;
    private PlaceOrderAdapter adapter;
    private Button pay;

    private OnFragmentInteractionListener mListener;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    public CheckoutFragment newInstance(Map<BookRow, Integer> shoppingbag) {
        CheckoutFragment fragment = new CheckoutFragment();
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

        rootView = inflater.inflate(R.layout.fragment_checkout, container, false);
        orderList = (ListView) rootView.findViewById(R.id.orderList);
        pay = (Button) rootView.findViewById(R.id.pay_button);
        adapter = new PlaceOrderAdapter(getActivity(), MainActivity.shoppingBag);
        orderList.setAdapter(adapter);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (MainActivity.shoppingBag.size() > 0) {
                        Database database = new Database();
                        String result = database.execute(Action.PLACEORDER.toString(), Login.USERNAME, "SUCCESSFUL", shoppingListToString(MainActivity.shoppingBag)).get();
                        if (!result.contains("Error")) {
                            MainActivity.shoppingBag.clear();
                            Toast.makeText(getActivity().getApplicationContext(), "Payment is successful!", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Payment is not successful!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("NOTHING IN CART", "AAAA!!!");
                        Toast.makeText(getActivity().getApplicationContext(), "You have no items in your shopping bag", Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    private String shoppingListToString(Map<BookRow, Integer> shoppingList) {
        String shoppingList_str = "";
        for (BookRow book : shoppingList.keySet()) {
            if (!shoppingList_str.equals("")) {
                shoppingList_str = shoppingList_str + ",";
            }
            shoppingList_str = shoppingList_str + book.getUrl() + "=" + book.getTotal();
        }
        return shoppingList_str;
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
