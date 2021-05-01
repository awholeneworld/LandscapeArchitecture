package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.List;
import gachon.termproject.joker.R;
/*
public class MatchingFrame extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.matching_frame, container, false);

        return view;
    }
}
*/

public class MatchingFrame extends Fragment {


    private FirebaseFirestore fStore;
    private CollectionReference collectionReference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button nextButton = getActivity().findViewById(R.id.expertList);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                    //list에 snapshot 추가
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}