package com.example.user.bookstore.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bookstore.BookList.BookListAdapter;
import com.example.user.bookstore.BookList.BookRow;
import com.example.user.bookstore.Database.Action;
import com.example.user.bookstore.Database.Database;
import com.example.user.bookstore.MainActivity;
import com.example.user.bookstore.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View rootView;
    //    private List<BookRow> bookslist = new ArrayList<BookRow>();
    private RecyclerView mRecyclerView;
    private BookListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.booksContainer);
        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BookListAdapter(getActivity(), MainActivity.bookList);
        mRecyclerView.setAdapter(adapter);

        adapter.clearAdapter();

        try {
//            if (MainActivity.bookList.size() == 0) {
            Log.d("UPDATE LIST", "HEY");
            updateList();
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rootView;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_items, menu);


        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<BookRow> newList = new ArrayList<BookRow>();

        for (BookRow book : MainActivity.bookList) {
            String title = book.getTitle().toLowerCase();
            String author = book.getAuthor().toLowerCase();
            String publisher = book.getPublisher().toLowerCase();
            if (title.contains(newText) || author.contains(newText) || publisher.contains(newText)) {
                newList.add(book);
            }
        }
        adapter.setFilter(newList);
        return false;
    }

    public void updateList() throws InterruptedException {
        try {
            Database database = new Database();
            String result = database.execute(Action.GETBOOKS.toString()).get();
            Log.d("MainActivity", result);
            String[] booksString = result.split("<br>");
            Log.d("TOTAL BOOKS", booksString.length + "");
            for (String book : booksString) {
                BookRow bookRow = new BookRow();
                String[] book_info = book.split(";");
                if (book_info.length == 6) {
                    bookRow.setTitle(book_info[0]);
                    bookRow.setAuthor(book_info[1]);
                    bookRow.setPublisher(book_info[2]);
                    bookRow.setPrice(Double.parseDouble(book_info[3]));
                    bookRow.setStock(Integer.parseInt(book_info[4]));
                    bookRow.setUrl(book_info[5]);
                    if (MainActivity.shoppingBag.containsKey(bookRow)) {
                        Log.d("HERE", MainActivity.shoppingBag.get(bookRow) + "");
                        bookRow.setTotal(MainActivity.shoppingBag.get(bookRow));
                    } else {
                        bookRow.setTotal(0);
                    }
                    MainActivity.bookList.add(bookRow);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
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
