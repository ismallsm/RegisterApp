package com.ismaelsouza.registerapp;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.ismaelsouza.registerapp.model.Book;
import com.ismaelsouza.registerapp.view.adapter.BookAdapter;
import com.ismaelsouza.registerapp.view.listener.OnItemClickListener;
import com.ismaelsouza.registerapp.viewmodel.BookModel;

public class TesteActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView rvBoks;
    private BookAdapter adapter;
    private List<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        rvBoks = (RecyclerView) findViewById(R.id.rvBooks);

        books = new ArrayList<>();

        ViewModelProviders.of(this)
                .get(BookModel.class)
                .getBooks()
                .observe(this, new Observer<List<Book>>() {
                    @Override
                    public void onChanged(@Nullable List<Book> books) {
                        adapter.setList(books);
                        rvBoks.getAdapter().notifyDataSetChanged();
                    }
                });
        rvBoks.setLayoutManager(new LinearLayoutManager(this));
        rvBoks.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // TarefaDialog dialog = new TarefaDialog();
                //dialog.show(getFragmentManager(), "CriarTarefa");
            }
        });

    }

    private OnItemClickListener deleteClick = new OnItemClickListener() {
        @Override
        public void onClick(int position) {
            //
        }
    };

}
