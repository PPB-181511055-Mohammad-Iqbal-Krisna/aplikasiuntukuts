/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.aplikasiuntukuts;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aplikasiuntukuts.data.Cheese;
import com.example.aplikasiuntukuts.data.CheeseViewModel;
import com.example.aplikasiuntukuts.provider.SampleContentProvider;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final int LOADER_CHEESES = 1;

    private CheeseViewModel vm;
    private CheeseAdapter mCheeseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        mCheeseAdapter = new CheeseAdapter();
        list.setAdapter(mCheeseAdapter);

        vm = new ViewModelProvider(this).get(CheeseViewModel.class);
        vm.getmAllCheese().observe(this, new Observer<List<Cheese>>() {
            @Override
            public void onChanged(List<Cheese> cheeses) {
                mCheeseAdapter.setCheeses(cheeses);
            }
        });

//        LoaderManager.getInstance(this).initLoader(LOADER_CHEESES, null, mLoaderCallbacks);
    }

    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                @NonNull
                public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                    return new CursorLoader(getApplicationContext(),
                            SampleContentProvider.URI_CHEESE,
                            new String[]{Cheese.COLUMN_NAME},
                            null, null, null);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                    ArrayList<Cheese> dataCheese = new ArrayList<>();

                    do {
                        long id = data.getLong(data.getColumnIndex(Cheese.COLUMN_ID));
                        String name = data.getString(data.getColumnIndex(Cheese.COLUMN_NAME));

                        dataCheese.add(new Cheese(id, name));
                    }
                    while (data.moveToNext());

                    mCheeseAdapter.setCheeses(dataCheese);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                    mCheeseAdapter.setCheeses(null);
                }

            };

    private static class CheeseAdapter extends RecyclerView.Adapter<CheeseAdapter.ViewHolder> {

//        private Cursor mCursor;

        private List<Cheese> mCheese;

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (mCheese.get(position) != null) {
                Cheese current = mCheese.get(position);
                holder.mText.setText(current.getName());
            }
        }

        @Override
        public int getItemCount() {
            return mCheese == null ? 0 : mCheese.size();
        }

        void setCheeses(List<Cheese> cheeses) {
            mCheese = cheeses;
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mText;

            ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_list_item_1, parent, false));
                mText = itemView.findViewById(android.R.id.text1);
            }

        }

    }

}