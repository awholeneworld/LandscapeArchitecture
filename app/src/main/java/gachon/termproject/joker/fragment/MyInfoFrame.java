package gachon.termproject.joker.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;

public class MyInfoFrame extends Fragment {

    private View view;
    private CollectionReference collectionReference;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userId = UserInfo.userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frame_myinfo, container, false);

        Button changeButton = view.findViewById(R.id.change_nick);
        EditText nicknameText = view.findViewById(R.id.text_change_nick);

        changeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //닉네임 입력 받기 + firebase 중복 확인 + 변경완료
                

                String temp = nicknameText.getText().toString();

                fStore = FirebaseFirestore.getInstance();
                collectionReference = fStore.collection("users");
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            List<DocumentSnapshot> list = querySnapshot.getDocuments();

                            DocumentReference documentReference = fStore.collection("users").document(userId);
                            //LoginActivity에 있는 setUserInfo 의 documentReference를가져와야함.

                            Map<String, Object> user = new HashMap<>();
                            user.put("nickname", temp);
                            documentReference.update(user);

                            for (int i = 0; i < list.size(); i++) {
                                DocumentSnapshot snapshot = list.get(i);
                                String nicknameCheck = snapshot.getString("nickname");
                                if (temp.compareTo(nicknameCheck) == 0) {
                                    Toast.makeText(view.getContext(), "중복된 닉네임 입니다", Toast.LENGTH_SHORT).show();

                                    break;
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