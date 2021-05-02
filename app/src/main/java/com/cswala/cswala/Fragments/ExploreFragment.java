package com.cswala.cswala.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
    CollectionReference reference;
    TechListAdapter adapter;
    BestPracticeAdapter bestPracticeAdapter;
    List<Begineer> begineerList;
    ImageView ivClose;
    LinearLayout llbeginner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_explore, container, false);

        firestore=FirebaseFirestore.getInstance();
        reference=firestore.collection("Dictionary");
        techListView=(RecyclerView) view.findViewById(R.id.tech_list);
        rvBestPractice=view.findViewById(R.id.rvBestPractice);
        manager=new LinearLayoutManager(getContext());
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
                if(!newText.isEmpty()) {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }

    private void fetchData() {
        final ArrayList<TechListElement> data=new ArrayList<>();
        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull @NotNull QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                {
                    String tech= snapshot.getId();
                    String tag=snapshot.getString("Tag");
                    if(tag != null && tag.length()>12)
                    {
                        tag=tag.substring(0,12)+"...";
                    }
                    data.add(new TechListElement(tech,tag));
                }
                adapter=new TechListAdapter(data,ExploreFragment.this);
                techListView.setAdapter(adapter);
                techListView.scheduleLayoutAnimation();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d("EXPLOREFRAGMENT", "onFailure: "+e.getMessage());
                Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onItemClicked(String item) {
        IntentHelper transfer=new IntentHelper(getContext());
        transfer.GoToTechData(item);
    }
}