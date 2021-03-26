package com.cswala.cswala.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.Adapters.PortalListAdapter;
import com.cswala.cswala.Models.PortalListElement;
import com.cswala.cswala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PortalFragment extends Fragment {
    FirebaseFirestore firestore;
    CollectionReference reference;
    RecyclerView portalListView;
    RecyclerView.LayoutManager manager;
    PortalListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_portal, container, false);
        portalListView = view.findViewById(R.id.list);
        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("Portal");
        manager = new LinearLayoutManager(getContext());
        portalListView.setLayoutManager(manager);
        fetchData();
        return view;
    }

    private void fetchData() {
        final ArrayList<PortalListElement> data = new ArrayList<>();
        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    String title = snapshot.get("Title").toString();
                    String link = snapshot.getString("Link");
                    boolean isCompany = snapshot.getBoolean("iscompany");
                    PortalListElement p = new PortalListElement(title, link, isCompany);
                    data.add(p);
                }
                adapter = new PortalListAdapter(getContext(), data);
                portalListView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Some error occurred !", Toast.LENGTH_SHORT).show();
            }
        });
    }

}