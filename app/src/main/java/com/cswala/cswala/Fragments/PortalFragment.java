package com.cswala.cswala.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.Adapters.PortalListAdapter;
import com.cswala.cswala.Models.PortalListElement;
import com.cswala.cswala.R;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PortalFragment extends Fragment {
    FirebaseFirestore firestore;
    //    CollectionReference reference;
    Query reference;
    RecyclerView portalListView;
    SearchView searchTech;
    RecyclerView.LayoutManager manager;
    PortalListAdapter adapter;
    private DocumentSnapshot lastVisible;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;
    private ProgressBar progressBar_loadMorePortals;
    ArrayList<PortalListElement> data = new ArrayList<>();
    ArrayList<PortalListElement> filteredData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_portal, container, false);
        portalListView = view.findViewById(R.id.list);
        progressBar_loadMorePortals = view.findViewById(R.id.paginationLoadingPortal);
        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("Portal");
        manager = new LinearLayoutManager(getContext());
        portalListView.setHasFixedSize(true);
        portalListView.setLayoutManager(manager);

        fetchData();

        searchTech = (SearchView) view.findViewById(R.id.portal_search);
        setSearchViewParameters();
        return view;
    }

    private void fetchData() {
        Query query = reference.orderBy("Title", Query.Direction.ASCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(25)
                .setPrefetchDistance(2)
                .setPageSize(15)
                .build();

        FirestorePagingOptions<PortalListElement> options = new FirestorePagingOptions.Builder<PortalListElement>()
                .setLifecycleOwner(this)
                .setQuery(query, config, new SnapshotParser<PortalListElement>() {
                    @NonNull
                    @Override
                    public PortalListElement parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        String title = snapshot.get("Title").toString();
                        String link = snapshot.getString("Link");
                        boolean isCompany = snapshot.getBoolean("iscompany");
                        PortalListElement portal = new PortalListElement(title, link, isCompany);
                        data.add(portal);
                        return portal;
                    }
                })
                .build();

        adapter = new PortalListAdapter(
                getContext(), data, options, progressBar_loadMorePortals);

        portalListView.setAdapter(adapter);
    }

    private void setSearchViewParameters() {
        TextView text = (TextView) searchTech.findViewById(androidx.appcompat.R.id.search_src_text);
        Typeface type = ResourcesCompat.getFont(getContext(), R.font.press_start_2p);
        text.setTypeface(type);
        searchTech.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
                searchPortalResults(newText);
                return false;
            }
        });
    }

    private void searchPortalResults(String text) {
//        ArrayList<PortalListElement> sample = new ArrayList<>();
//        if (text == null || text.length() == 0) {
//            sample.addAll(data);
//        } else {
//            String pattern = text.toLowerCase().trim();
//            for (PortalListElement portal : data) {
//                String item = portal.getTitle();
//                if (item.toLowerCase().contains(pattern)) {
//                    sample.add(portal);
//                }
//            }
//        }

        if (text != null && text.length() != 0) {
            text = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
        }

        Query filteredQuery = reference
                .whereGreaterThanOrEqualTo("Title", text)
                .whereLessThan("Title", text + 'z')
                .orderBy("Title", Query.Direction.ASCENDING);


//        if(sample != null) {
//            for (PortalListElement port : sample) {
//                Log.i("SAMPLE LIST", port.getTitle());
//                filteredQuery = filteredQuery.whereEqualTo("Title", port.getTitle());
//            }
//        }

        PagedList.Config filteredConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(25)
                .setPrefetchDistance(2)
                .setPageSize(15)
                .build();

//        FirestorePagingOptions<PortalListElement> newOpt = new FirestorePagingOptions.Builder<PortalListElement>()
//                .setDiffCallback()
        FirestorePagingOptions<PortalListElement> newOptions = new FirestorePagingOptions.Builder<PortalListElement>()
                .setQuery(filteredQuery, filteredConfig, new SnapshotParser<PortalListElement>() {
                    @NonNull
                    @Override
                    public PortalListElement parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        String title = snapshot.get("Title").toString();
                        String link = snapshot.getString("Link");
                        boolean isCompany = snapshot.getBoolean("iscompany");
                        PortalListElement portal = new PortalListElement(title, link, isCompany);
                        filteredData.add(portal);
                        return portal;
                    }
                })
                .build();

        adapter.notifyDataSetChanged();
        adapter.updateOptions(newOptions);
        portalListView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    //Stop Listening Adapter
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}