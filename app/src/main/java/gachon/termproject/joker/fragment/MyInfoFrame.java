package gachon.termproject.joker.fragment;

import android.graphics.Color;
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
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.fragment.PostMyInfo;

public class MyInfoFrame extends Fragment {

    private View view;
    private FragmentManager fm;
    private PostMyInfo post;
    private CommentMyInfo comment;
    //private PortfolioMyInfo portfolio;
    TabLayout tabs;
    Button selectCommunityMode;
    boolean i = true;

    // 닉네임 변경 부분
    /*
    private CollectionReference collectionReference;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userId = UserInfo.userId;

     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frame_myinfo, container, false);

        tabs = view.findViewById(R.id.myinfo_tabs);
        tabs.setSelectedTabIndicatorHeight(0);
        fm = getChildFragmentManager();

        if (post == null) {
            post = new PostMyInfo();
            fm.beginTransaction().add(R.id.myinfo_frame, post).commit();
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabs.getSelectedTabPosition()) {
                    case 0 :
                        // tabs.getChildAt(0).setBackgroundColor(Color.parseColor("#4B3D5A"));
                        if (comment != null) fm.beginTransaction().hide(comment).commit();
                        //if (portfolio != null) fm.beginTransaction().hide(portfolio).commit();
                        fm.beginTransaction().show(post).commit();
                        break;
                    case 1 :
                        if (comment == null) {
                            comment = new CommentMyInfo();
                            fm.beginTransaction().add(R.id.myinfo_frame, comment).commit();
                        }
                        //if (portfolio != null) fm.beginTransaction().hide(portfolio).commit();
                        fm.beginTransaction().hide(post).commit();
                        fm.beginTransaction().show(comment).commit();
                        break;
                        /*
                    case 2 :
                        if (portfolio == null) {
                            portfolio = new PortfolioMyInfo();
                            fm.beginTransaction().add(R.id.myinfo_frame, portfolio).commit();
                        }
                        if (comment != null) fm.beginTransaction().hide(comment).commit();
                        fm.beginTransaction().hide(post).commit();
                        fm.beginTransaction().show(portfolio).commit();
                        break;

                         */
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        
        // 설정 구현되면 설정 안에 이 내용 넣기 - 닉네임 변경
        /*Button changeButton = view.findViewById(R.id.change_nick);
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

        }); */
        return view;
    }
}