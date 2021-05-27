package gachon.termproject.joker.fragment;

import android.media.audiofx.LoudnessEnhancer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
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
    private Button location_cancel_OK_btn;
    private FirebaseFirestore fStore;

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
        location_cancel_OK_btn = view.findViewById(R.id.btn_post_cancel_location);
        // 지역을 선택하는 부분입니다!!!!
        SU = view.findViewById(R.id.SU);
        IC = view.findViewById(R.id.IC);
        DJ = view.findViewById(R.id.DJ);
        GJ = view.findViewById(R.id.GJ);
        DG = view.findViewById(R.id.DG);
        US = view.findViewById(R.id.US);
        BS = view.findViewById(R.id.BS);
        JJ = view.findViewById(R.id.JJ);
        GG = view.findViewById(R.id.GG);
        GW = view.findViewById(R.id.GW);
        CB = view.findViewById(R.id.CB);
        CN = view.findViewById(R.id.CN);
        GB = view.findViewById(R.id.GB);
        GN = view.findViewById(R.id.GN);
        JB = view.findViewById(R.id.JB);
        JN = view.findViewById(R.id.JN);
        SJ = view.findViewById(R.id.SJ);

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
                ArrayList<String> locationSelected = checklocation();

                location_tv.setText(locationSelected.size() + "개 지역");

                //지역선택 뷰를 다시 밑으로 내립니다.
                LinearLayout LL = view.findViewById(R.id.post_select_location);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) LL.getLayoutParams();
                lp.addRule(RelativeLayout.BELOW, R.id.refresh_layout);
                lp.addRule(RelativeLayout.ABOVE, 0);
                LL.setLayoutParams(lp);

                //그리고 다시 query받아서 adapter를 구성해야 하는데,,,, 할수잇을까???

                fStore = FirebaseFirestore.getInstance();
                collectionReference = fStore.collection("users");
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            List<DocumentSnapshot> list = querySnapshot.getDocuments();
                            boolean next = false;
                            ArrayList<String> idList = new ArrayList<String>();
                            idList.add("test");

                            //델꼬온나
                            expertList.clear();
                            for (int i = 0; i < list.size(); i++) {
                                DocumentSnapshot snapshot = list.get(i);
                                boolean publicMan = snapshot.getBoolean("isPublic");
                                if (!publicMan) { //일단 전문가여야함
                                    ArrayList<String> location = (ArrayList<String>) snapshot.get("location");
                                    //선택된 위치정보와 fireStore에 위치 정보를 비교
                                    for(int a = 0; a < locationSelected.size(); a++){
                                        for(int b = 0; b < location.size(); b++){
                                            if(locationSelected.get(a).equals(location.get(b))){
                                                String userId = snapshot.getId();

                                                for(int ab = 0; ab < idList.size(); ab++){ //이부분을 통과했다면 중복이 없다는 뜻
                                                    if(!idList.contains(userId)){
                                                        idList.add(userId);
                                                        next = true;
                                                    }
                                                }
                                                if(next) { //중복이 없을때 expertList에 추가
                                                    String nickname = snapshot.getString("nickname");
                                                    String profileImg = snapshot.getString("profileImg");
                                                    String portfolioImg = snapshot.getString("portfolioImg");
                                                    String portfolioWeb = snapshot.getString("portfolioWeb");
                                                    expertList.add(new ExpertListContent(userId, nickname, profileImg, portfolioImg, portfolioWeb, location));
                                                    next = false;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                    expertListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
            }
        });

        location_cancel_OK_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //지역선택 뷰를 다시 밑으로 내립니다.
                LinearLayout LL = view.findViewById(R.id.post_select_location);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) LL.getLayoutParams();
                lp.addRule(RelativeLayout.BELOW, R.id.refresh_layout);
                lp.addRule(RelativeLayout.ABOVE, 0);
                LL.setLayoutParams(lp);

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
                            location_tv.setText("지역 넣기");
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
    public ArrayList<String> checklocation() {
        //선택된 지역을 저장할 리스트
        ArrayList<String> location = new ArrayList<>();

        if(SU.isChecked()) location.add("서울");
        if(IC.isChecked()) location.add("인천");
        if(DJ.isChecked()) location.add("대전");
        if(GJ.isChecked()) location.add("광주");
        if(DG.isChecked()) location.add("대구");
        if(US.isChecked()) location.add("울산");
        if(BS.isChecked()) location.add("부산");
        if(JJ.isChecked()) location.add("제주");
        if(GG.isChecked()) location.add("경기");
        if(GW.isChecked()) location.add("강원");
        if(CB.isChecked()) location.add("충북");
        if(CN.isChecked()) location.add("충남");
        if(GB.isChecked()) location.add("경북");
        if(GN.isChecked()) location.add("경남");
        if(JB.isChecked()) location.add("전북");
        if(JN.isChecked()) location.add("전남");
        if(SJ.isChecked()) location.add("세종");

        return location;
    }
}
