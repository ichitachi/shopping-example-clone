package com.teikaiz.quiz.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.teikaiz.quiz.R;
import com.teikaiz.quiz.model.Products;
import com.teikaiz.quiz.ui.holder.ProductViewHolder;

@RequiresApi(api = Build.VERSION_CODES.M)
public class HomeFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final RecyclerView recyclerView = root.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = homeViewModel.getAdapter();
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        return root;
    }
}