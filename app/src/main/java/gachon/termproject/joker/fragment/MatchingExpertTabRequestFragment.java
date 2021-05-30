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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gachon.termproject.joker.Content.MatchingPostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.MatchingPostAdapter;

public class MatchingExpertTabRequestFragment extends Fragment { //매칭요청
    private View view;
    private SwipeRefreshLayout refresher;
    private RecyclerView contents;
    private Button location_btn;
    private TextView location_tv;
    private CheckBox SU, IC, DJ, GJ, DG, US, BS, JJ, GG, GW, CB, CN, GB, GN, JB, JN, SJ;
    private Button location_select_OK_btn;
    public static DatabaseReference databaseReference;
    public static ValueEventListener postEventListener;
    FirebaseDatabase firebaseDatabase;
    ArrayList<MatchingPostContent> postContentList;
    MatchingPostContent postContent;
    MatchingPostAdapter matchingpostAdapter;
    ArrayList<String> locationSelected = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.matching_expert_tab_request, container, false);

        contents = view.findViewById(R.id.content_community);
        refresher = view.findViewById(R.id.refresh_layout);
        location_btn = view.findViewById(R.id.button_location);
        location_tv = view.findViewById(R.id.textview_location);
        location_select_OK_btn = view.findViewById(R.id.btn_post_select_location);
        location_tv.setText("");

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
                locationSelected = checkLocation();
                String locationStr = "";

                for (String item : locationSelected) {
                    locationStr += item + " | ";
                }

                if(locationSelected.isEmpty()){
                    location_tv.setText("");
                }
                else{
                    location_tv.setText(locationStr.substring(0, locationStr .length()-3));
                }

                //지역선택 뷰를 다시 밑으로 내립니다.
                LinearLayout LL = view.findViewById(R.id.post_select_location);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) LL.getLayoutParams();
                lp.addRule(RelativeLayout.BELOW, R.id.refresh_layout);
                lp.addRule(RelativeLayout.ABOVE, 0);
                LL.setLayoutParams(lp);

                //그리고 다시 query받아서 adapter를 구성해야 하는데,,,, 할수잇을까???
                databaseReference.addListenerForSingleValueEvent(postEventListener);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Matching/userRequests");

        postContentList = new ArrayList<>();
        matchingpostAdapter = new MatchingPostAdapter(getActivity(), postContentList, "needed");

        contents.setLayoutManager(new LinearLayoutManager(getActivity()));
        contents.setHasFixedSize(true);
        contents.setAdapter(matchingpostAdapter);

        postEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postContentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    postContent = snapshot.getValue(MatchingPostContent.class);
                    if (!postContent.getIsMatched()){
                        //아직 게시글은 매칭이 안되었는데
                        //매칭 대기 목록에 앗 내가 없네 => 그럼 매칭 신청 탭에
                        if(!snapshot.child("requests/" + UserInfo.userId).exists()){

                            //근데 혹시 지역조건이 있다면 넣어주거라
                            if(locationSelected.isEmpty()){
                                postContentList.add(0, postContent);
                            }
                            else{ //지역 조건이 존재한다면?
                                for(String loc : postContent.getLocation()){
                                    if(locationSelected.contains(loc)){ //만약 글이 가지고 있는 지역이 선택범위에 있다면
                                        postContentList.add(0, postContent);
                                    }
                                }

                            }

                        }
                    }
                }
                matchingpostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addListenerForSingleValueEvent(postEventListener);

        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                databaseReference.addListenerForSingleValueEvent(postEventListener);
                refresher.setRefreshing(false);
            }
        });

        return view;
    }

    public ArrayList<String> checkLocation() {
        //선택된 지역을 저장할 리스트
        ArrayList<String> location = new ArrayList<>();

        if (SU.isChecked()) location.add("서울");
        if (IC.isChecked()) location.add("인천");
        if (DJ.isChecked()) location.add("대전");
        if (GJ.isChecked()) location.add("광주");
        if (DG.isChecked()) location.add("대구");
        if (US.isChecked()) location.add("울산");
        if (BS.isChecked()) location.add("부산");
        if (JJ.isChecked()) location.add("제주");
        if (GG.isChecked()) location.add("경기");
        if (GW.isChecked()) location.add("강원");
        if (CB.isChecked()) location.add("충북");
        if (CN.isChecked()) location.add("충남");
        if (GB.isChecked()) location.add("경북");
        if (GN.isChecked()) location.add("경남");
        if (JB.isChecked()) location.add("전북");
        if (JN.isChecked()) location.add("전남");
        if (SJ.isChecked()) location.add("세종");

        return location;
    }
}
