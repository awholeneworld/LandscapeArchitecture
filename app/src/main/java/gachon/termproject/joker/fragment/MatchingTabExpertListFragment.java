package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.ExpertListAdapter;
import gachon.termproject.joker.Content.ExpertListContent;

public class MatchingTabExpertListFragment extends Fragment {
    private View view;
    private RecyclerView content;
    private SwipeRefreshLayout refresher;
    private CollectionReference collectionReference;
    private ExpertListAdapter expertListAdapter;
    private ArrayList<ExpertListContent> expertList;
    private Button location_btn;
    private TextView location_tv;
    private CheckBox SU, IC, DJ, GJ, DG, US, BS, JJ, GG, GW, CB, CN, GB, GN, JB, JN, SJ;
    private Button location_select_OK_btn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.matching_expert_list, container, false);

        content = view.findViewById(R.id.content);
        refresher = view.findViewById(R.id.refresh_layout);

        expertList = new ArrayList<>();
        expertListAdapter = new ExpertListAdapter(getActivity(), expertList);

        content.setLayoutManager(new LinearLayoutManager(getActivity()));
        content.setHasFixedSize(true);
        content.setAdapter(expertListAdapter);

        location_btn = view.findViewById(R.id.button_location);
        location_tv = view.findViewById(R.id.textview_location);
        location_select_OK_btn = view.findViewById(R.id.btn_post_select_location);
        // 지역을 선택하는 부분입니다!!!!
        SU = view.findViewById(R.id.signup04_SU);
        IC = view.findViewById(R.id.signup04_IC);
        DJ = view.findViewById(R.id.signup04_DJ);
        GJ = view.findViewById(R.id.signup04_GJ);
        DG = view.findViewById(R.id.signup04_DG);
        US = view.findViewById(R.id.signup04_US);
        BS = view.findViewById(R.id.signup04_BS);
        JJ = view.findViewById(R.id.signup04_JJ);
        GG = view.findViewById(R.id.signup04_GG);
        GW = view.findViewById(R.id.signup04_GW);
        CB = view.findViewById(R.id.signup04_CB);
        CN = view.findViewById(R.id.signup04_CN);
        GB = view.findViewById(R.id.signup04_GB);
        GN = view.findViewById(R.id.signup04_GN);
        JB = view.findViewById(R.id.signup04_JB);
        JN = view.findViewById(R.id.signup04_JN);
        SJ = view.findViewById(R.id.signup04_SJ);

        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //지역선택하는 화면이 있는 relative layout -> 선택지를 화면 내로 끌고와서 보여줌
                LinearLayout LL = view.findViewById(R.id.post_select_location);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) LL.getLayoutParams();
                lp.addRule(RelativeLayout.BELOW, 0);
                LL.setLayoutParams(lp);


            }
        });

        location_select_OK_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //location_tv.setText(locationSelected.size() + "개 지역");

                //지역선택 뷰를 다시 밑으로 내립니다.
                LinearLayout LL = view.findViewById(R.id.post_select_location);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) LL.getLayoutParams();
                lp.addRule(RelativeLayout.BELOW, R.id.refresh_layout);
                lp.addRule(RelativeLayout.ABOVE, 0);
                LL.setLayoutParams(lp);

                //그리고 다시 query받아서 adapter를 구성해야 하는데,,,, 할수잇을까???

            }
        });


        OnCompleteListener onCompleteListener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> list = querySnapshot.getDocuments();
                    expertList.clear();

                    for (int i = 0; i < list.size(); i++) {
                        DocumentSnapshot snapshot = list.get(i);
                        Boolean isPublic = snapshot.getBoolean("isPublic");
                        String userId = snapshot.getId();
                        if (!isPublic && !userId.equals(UserInfo.userId)) {
                            String nickname = snapshot.getString("nickname");
                            String profileImg = snapshot.getString("profileImg");
                            String portfolioImg = snapshot.getString("portfolioImg");
                            String portfolioWeb = snapshot.getString("portfolioWeb");
                            ArrayList<String> location = (ArrayList<String>) snapshot.get("location");
                            expertList.add(new ExpertListContent(userId, nickname, profileImg, portfolioImg, portfolioWeb, location));
                        }
                    }

                    expertListAdapter.notifyDataSetChanged();
                }
            }
        };

        collectionReference = FirebaseFirestore.getInstance().collection("users");
        collectionReference.limit(50).get().addOnCompleteListener(onCompleteListener);

        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                collectionReference.limit(50).get().addOnCompleteListener(onCompleteListener);
                refresher.setRefreshing(false);
            }
        });

        return view;
    }
}
