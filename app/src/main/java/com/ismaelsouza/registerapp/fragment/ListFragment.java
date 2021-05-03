package com.ismaelsouza.registerapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ismaelsouza.registerapp.MainActivity;
import com.ismaelsouza.registerapp.R;
import com.ismaelsouza.registerapp.model.Book;
import com.ismaelsouza.registerapp.view.adapter.BookAdapter;
import com.ismaelsouza.registerapp.view.listener.OnItemClickListener;
import com.ismaelsouza.registerapp.viewmodel.BookModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private List<Book> books;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference("Livros");
    private FloatingActionButton fab;
    private RecyclerView rvBooks;
    private BookAdapter adapter;

    public ListFragment() {
        // Required empty public constructor
    }

    private void changeFragment(Fragment fragment){
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra("fragment", "registerBook");
        startActivity(i);
        getActivity().finish();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        fab = v.findViewById(R.id.fab);
        rvBooks = v.findViewById(R.id.rvBooks);
        books = new ArrayList<>();

        ViewModelProviders.of(this)
                .get(BookModel.class)
                .getBooks()
                .observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
                    @Override
                    public void onChanged(@Nullable List<Book> books) {
                        adapter.setList(books);
                        rvBooks.getAdapter().notifyDataSetChanged();
                    }
                });

        rvBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookAdapter(books, deleteClick, editClick);
        rvBooks.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new RegisterBookFragment());
            }
        });

        return v;
    }

    private OnItemClickListener editClick = new OnItemClickListener() {
        @Override
        public void onClick(int position) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            i.putExtra("fragment", "registerBook");
            i.putExtra("obj", adapter.getBook(position));
            startActivity(i);
            getActivity().finish();
        }
    };

    private OnItemClickListener deleteClick = new OnItemClickListener() {
        @Override
        public void onClick(int position) {
            Query query = dbReference.orderByChild("id").equalTo(adapter.getBook(position).getId());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        data.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    };

}