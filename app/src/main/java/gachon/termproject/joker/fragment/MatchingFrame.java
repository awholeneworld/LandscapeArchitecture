package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import gachon.termproject.joker.ExpertListContent;
import gachon.termproject.joker.R;

public class MatchingFrame extends Fragment {
    private FragmentManager fm;
    private View view;
    private ExpertList Elist;
    public static ArrayList<ExpertListContent> expertList = new ArrayList<ExpertListContent>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frame_matching, container, false);
        fm = getChildFragmentManager();

        Button nextButton = view.findViewById(R.id.expertList);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore fStore;
                CollectionReference collectionReference;

                fStore = FirebaseFirestore.getInstance();
                collectionReference = fStore.collection("users");
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            List<DocumentSnapshot> list = querySnapshot.getDocuments();

                            for (int i = 0; i < list.size(); i++) {
                                DocumentSnapshot snapshot = list.get(i);
                                Boolean publicCheck = snapshot.getBoolean("isPublic");
                                if (!publicCheck) {
                                    expertList.add(new ExpertListContent(snapshot.getId(), snapshot.getString("nickname")));
                                }
                            }
                        }
                    }
                });
            }
        });
        return view;
    }
}