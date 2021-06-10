package com.cswala.cswala.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cswala.cswala.Activities.TechDataActivity;
import com.cswala.cswala.Adapters.BestPracticeAdapter;
import com.cswala.cswala.Adapters.TechListAdapter;
import com.cswala.cswala.Adapters.techItemClicked;
import com.cswala.cswala.MainActivity;
import com.cswala.cswala.Models.Begineer;
import com.cswala.cswala.Models.TechListElement;
import com.cswala.cswala.R;
import com.cswala.cswala.utils.IntentHelper;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ExploreFragment extends Fragment implements techItemClicked {
    RecyclerView techListView;
    RecyclerView rvBestPractice;
    RecyclerView.LayoutManager manager;
    SearchView searchTech;
    FirebaseFirestore firestore;
    Query reference;
    TechListAdapter adapter;
    BestPracticeAdapter bestPracticeAdapter;
    List<Begineer> begineerList;
    ImageView ivClose;
    LinearLayout llbeginner;
    private ProgressBar progressBar_loadMoreTechLists;
    private ArrayList<TechListElement> data = new ArrayList<>();
    private ArrayList<TechListElement> filteredData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_explore, container, false);

        firestore=FirebaseFirestore.getInstance();
        reference=firestore.collection("Dictionary");
        techListView=(RecyclerView) view.findViewById(R.id.tech_list);
        progressBar_loadMoreTechLists = view.findViewById(R.id.paginationLoadingTechList);
        rvBestPractice=view.findViewById(R.id.rvBestPractice);
        manager=new LinearLayoutManager(getContext());
        techListView.setHasFixedSize(true);
        techListView.setLayoutManager(manager);
        ivClose=view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llbeginner.setVisibility(View.GONE);
            }
        });
        llbeginner=view.findViewById(R.id.llBeginner);
        llbeginner.setVisibility(View.VISIBLE);
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_fall_down);
        techListView.setLayoutAnimation(layoutAnimationController);

        fetchData();

        searchTech=(SearchView) view.findViewById(R.id.tech_search);
        setSearchViewParameters();
        setBeginnerOptions();

        return view;
    }

    private void setBeginnerOptions() {
        begineerList=new ArrayList<>();
        Begineer begineer=new Begineer();
        begineer.setTitle("Best Practice");
        begineerList.add(begineer);
        Begineer begineer1=new Begineer();
        begineer1.setTitle("Beginner friendly");
        begineerList.add(begineer1);
        bestPracticeAdapter=new BestPracticeAdapter(getContext(),begineerList);
        rvBestPractice.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        rvBestPractice.setAdapter(bestPracticeAdapter);
    }

    private void fetchData() {
        Query query = reference.orderBy("Title", Query.Direction.ASCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(25)
                .setPrefetchDistance(2)
                .setPageSize(15)
                .build();

        FirestorePagingOptions<TechListElement> options = new FirestorePagingOptions.Builder<TechListElement>()
                .setLifecycleOwner(this)
                .setQuery(query, config, new SnapshotParser<TechListElement>() {
                    @NonNull
                    @Override
                    public TechListElement parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        String tech = snapshot.getId();
                        String tag = snapshot.getString("Tag");
                        if(tag != null && tag.length() > 12) {
                            tag = tag.substring(0,12)+"...";
                        }
                        TechListElement techList = new TechListElement(tech, tag);
                        data.add(techList);
                        return techList;
                    }
                })
                .build();

        adapter = new TechListAdapter(
                getContext(), data, options, progressBar_loadMoreTechLists, ExploreFragment.this);

        techListView.setAdapter(adapter);
        techListView.scheduleLayoutAnimation();

    }

    private void setSearchViewParameters() {
        TextView text=(TextView) searchTech.findViewById(androidx.appcompat.R.id.search_src_text);
        Typeface type= ResourcesCompat.getFont(getContext(),R.font.press_start_2p);
        text.setTypeface(type);
        searchTech.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchTechListResults(newText);
                return false;
            }
        });
    }

    private void searchTechListResults(String text) {
//        if (text != null && text.length() != 0) {
//            text = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
//        }

        Query filteredQuery = reference
                .whereGreaterThanOrEqualTo("Search_title", text)
                .whereLessThan("Search_title", text + 'z')
                .orderBy("Search_title");

        PagedList.Config filteredConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(25)
                .setPrefetchDistance(2)
                .setPageSize(15)
                .build();

        FirestorePagingOptions<TechListElement> newOptions = new FirestorePagingOptions.Builder<TechListElement>()
                .setQuery(filteredQuery, filteredConfig, new SnapshotParser<TechListElement>() {
                    @NonNull
                    @Override
                    public TechListElement parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        String tech = snapshot.getId();
                        String tag = snapshot.getString("Tag");
                        if(tag != null && tag.length() > 12) {
                            tag = tag.substring(0,12)+"...";
                        }
                        TechListElement techList = new TechListElement(tech, tag);
                        filteredData.add(techList);
                        return techList;
                    }
                })
                .build();

        adapter.notifyDataSetChanged();
        adapter.updateOptions(newOptions);
        techListView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(String item) {
        IntentHelper transfer=new IntentHelper(getContext());
        transfer.GoToTechData(item);
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