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
    private ArrayList<String> locationSelected;
    private boolean selected = false;
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
        collectionReference = FirebaseFirestore.getInstance().collection("users");
        
        content.setLayoutManager(new LinearLayoutManager(getActivity()));
        content.setHasFixedSize(true);
        content.setAdapter(expertListAdapter);

        location_btn = view.findViewById(R.id.button_location);
        location_tv = view.findViewById(R.id.textview_location);
        location_select_OK_btn = view.findViewById(R.id.btn_post_select_location);
        location_tv.setText(" "); //초반에 지역 텍스트 삭제
        
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
                locationSelected = new ArrayList<>();
                
                if (!SU.isChecked() && !IC.isChecked() && !DJ.isChecked() &&
                        !GJ.isChecked() && !DG.isChecked() && !US.isChecked() && !BS.isChecked() &&
                        !JJ.isChecked() && !GG.isChecked() && !GW.isChecked() && !CB.isChecked() &&
                        !CN.isChecked() && !GB.isChecked() && !GN.isChecked() && !JB.isChecked() &&
                        !JN.isChecked() && !SJ.isChecked()){
                    locationSelected.add("서울");
                    locationSelected.add("인천");
                    locationSelected.add("대전");
                    locationSelected.add("광주");
                    locationSelected.add("대구");
                    locationSelected.add("울산");
                    locationSelected.add("부산");
                    locationSelected.add("제주");
                    locationSelected.add("경기");
                    locationSelected.add("강원");
                    locationSelected.add("충북");
                    locationSelected.add("충남");
                    locationSelected.add("경북");
                    locationSelected.add("경남");
                    locationSelected.add("전북");
                    locationSelected.add("전남");
                    locationSelected.add("세종");
                    location_tv.setText("");
                }
                else {
                    locationSelected = checklocation();
                    location_tv.setText(locStr(locationSelected));
                }
                
                // 지역선택 뷰를 다시 밑으로 내립니다.
                LinearLayout LL = view.findViewById(R.id.post_select_location);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) LL.getLayoutParams();
                lp.addRule(RelativeLayout.BELOW, R.id.refresh_layout);
                lp.addRule(RelativeLayout.ABOVE, 0);
                LL.setLayoutParams(lp);

                // 선택한 지역의 전문가 표시
                collectionReference.limit(50).get().addOnCompleteListener(onCompleteListener2);
            }
        });
        
        // 일단 50명까지만 가져오고 추후 아래로 스크롤시 50명 더 불러오는 코드 짤 것임
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

    public String locStr(ArrayList<String> location){
        String temp = "";
        String result = "";

        for (int i = 0;  i < location.size(); i++) {
            if (location.get(i).equals("SU")) {
                temp = "서울";
            } else if (location.get(i).equals("IC")) {
                temp = "인천";
            } else if (location.get(i).equals("JD")) {
                temp = "대전";
            } else if (location.get(i).equals("GJ")) {
                temp = "광주";
            } else if (location.get(i).equals("DG")) {
                temp = "대구";
            } else if (location.get(i).equals("US")) {
                temp = "울산";
            } else if (location.get(i).equals("BS")) {
                temp = "부산";
            } else if (location.get(i).equals("JJ")) {
                temp = "제주도";
            } else if (location.get(i).equals("GG")) {
                temp = "경기도";
            } else if (location.get(i).equals("GW")) {
                temp = "강원도";
            } else if (location.get(i).equals("CB")) {
                temp = "충청북도";
            } else if (location.get(i).equals("CN")) {
                temp = "충청남도";
            } else if (location.get(i).equals("GB")) {
                temp = "경상북도";
            } else if (location.get(i).equals("GN")) {
                temp = "경상남도";
            } else if (location.get(i).equals("JB")) {
                temp = "전라북도";
            } else if (location.get(i).equals("JN")) {
                temp = "전라남도";
            } else if (location.get(i).equals("SJ")) {
                temp = "세종";
            } else {
                temp = location.get(i);
            }

            result += temp + " | ";
        }
        
        // 글자 수가 클 때 자름
        if (result.length() >= 27)
            result = result.substring(0, 28) + "...";
        else
            result = result.substring(0, result.length() - 2);

        return result;
    }

    OnCompleteListener onCompleteListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<DocumentSnapshot> list = querySnapshot.getDocuments();

                if (selected) {
                    collectionReference.limit(50).get().addOnCompleteListener(onCompleteListener2);
                } else {
                    expertList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        DocumentSnapshot snapshot = list.get(i);

                        String userId = snapshot.getId();
                        boolean isPublic = snapshot.getBoolean("isPublic");

                        if (!userId.equals(UserInfo.getUserId()) && !isPublic) {
                            String nickname = snapshot.getString("nickname");
                            String introduction = snapshot.getString("introduction");
                            String profileImg = snapshot.getString("profileImg");
                            String portfolioImg = snapshot.getString("portfolioImg");
                            String portfolioWeb = snapshot.getString("portfolioWeb");
                            String pushToken = snapshot.getString("pushToken");
                            ArrayList<String> location = (ArrayList<String>) snapshot.get("location");
                            expertList.add(new ExpertListContent(userId, nickname, profileImg, portfolioImg, portfolioWeb, pushToken, introduction, location));
                        }
                    }
                    expertListAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    OnCompleteListener onCompleteListener2 = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<DocumentSnapshot> list = querySnapshot.getDocuments();
                
                ArrayList<String> idList = new ArrayList<>();
                
                idList.add("test"); //null에러를 막기위함

                expertList.clear();
                for (int i = 0; i < list.size(); i++) {
                    DocumentSnapshot snapshot = list.get(i);
                    boolean isPublic = snapshot.getBoolean("isPublic");
                    String userId = snapshot.getId();

                    if (!userId.equals(UserInfo.getUserId()) && !isPublic) {
                        ArrayList<String> location = (ArrayList<String>) snapshot.get("location");

                        // 위치 정보 비교
                        for (int j = 0; j < locationSelected.size(); j++){
                            for (int k = 0; k < location.size(); k++){
                                if (locationSelected.get(j).equals(location.get(k))){
                                    if (!idList.contains(userId)){ //중복 체크
                                        idList.add(userId);
                                        String nickname = snapshot.getString("nickname");
                                        String introduction = snapshot.getString("introduction");
                                        String profileImg = snapshot.getString("profileImg");
                                        String portfolioImg = snapshot.getString("portfolioImg");
                                        String portfolioWeb = snapshot.getString("portfolioWeb");
                                        String pushToken = snapshot.getString("pushToken");
                                        expertList.add(new ExpertListContent(userId, nickname, profileImg, portfolioImg, portfolioWeb, pushToken, introduction, location));
                                    }
                                }
                            }
                        }
                        expertListAdapter.notifyDataSetChanged();
                        selected = true;
                    }
                }
            }
        }
    };
}
