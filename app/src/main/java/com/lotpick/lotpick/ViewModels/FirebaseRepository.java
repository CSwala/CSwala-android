package com.lotpick.lotpick.ViewModels;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.lotpick.lotpick.Models.Modelclass;

import java.util.List;
import java.util.Objects;

class FirebaseRepository {

    private OnFirestoreTaskComplete onFirestoreTaskComplete;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Query placeRef = firebaseFirestore.collection("Hosts");


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = firebaseAuth.getCurrentUser();
    String name = user != null ? user.getDisplayName() : "User";
    String uid = Objects.requireNonNull(user).getUid();
    private Query favplaceRef = firebaseFirestore.collection("Users")
            .document(name +" "+ uid).collection("favourites");

    FirebaseRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    void getPlaceData() {
        placeRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    onFirestoreTaskComplete.placesListDataAdded(Objects.requireNonNull(task.getResult()).toObjects(Modelclass.class));
                } else {
                    onFirestoreTaskComplete.onError(task.getException());
                }
            }
        });
    }

    void getFavPlaceData() {
        favplaceRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    onFirestoreTaskComplete.placesListDataAdded(Objects.requireNonNull(task.getResult()).toObjects(Modelclass.class));
                } else {
                    onFirestoreTaskComplete.onError(task.getException());
                }
            }
        });
    }

    public interface OnFirestoreTaskComplete {
        void placesListDataAdded(List<Modelclass> placeListModelsList);
        void onError(Exception e);
    }

}
