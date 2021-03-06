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
        location_tv.setText(" "); //????????? ?????? ????????? ??????
        
        // ????????? ???????????? ???????????????!!!!
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
                //?????????????????? ????????? ?????? relative layout -> ???????????? ?????? ?????? ???????????? ?????????
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
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    locationSelected.add("??????");
                    location_tv.setText("");
                }
                else {
                    locationSelected = checklocation();
                    location_tv.setText(locStr(locationSelected));
                }
                
                // ???????????? ?????? ?????? ????????? ????????????.
                LinearLayout LL = view.findViewById(R.id.post_select_location);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) LL.getLayoutParams();
                lp.addRule(RelativeLayout.BELOW, R.id.refresh_layout);
                lp.addRule(RelativeLayout.ABOVE, 0);
                LL.setLayoutParams(lp);

                // ????????? ????????? ????????? ??????
                collectionReference.limit(50).get().addOnCompleteListener(onCompleteListener2);
            }
        });
        
        // ?????? 50???????????? ???????????? ?????? ????????? ???????????? 50??? ??? ???????????? ?????? ??? ??????
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
        //????????? ????????? ????????? ?????????
        ArrayList<String> location = new ArrayList<>();

        if (SU.isChecked()) location.add("??????");
        if (IC.isChecked()) location.add("??????");
        if (DJ.isChecked()) location.add("??????");
        if (GJ.isChecked()) location.add("??????");
        if (DG.isChecked()) location.add("??????");
        if (US.isChecked()) location.add("??????");
        if (BS.isChecked()) location.add("??????");
        if (JJ.isChecked()) location.add("??????");
        if (GG.isChecked()) location.add("??????");
        if (GW.isChecked()) location.add("??????");
        if (CB.isChecked()) location.add("??????");
        if (CN.isChecked()) location.add("??????");
        if (GB.isChecked()) location.add("??????");
        if (GN.isChecked()) location.add("??????");
        if (JB.isChecked()) location.add("??????");
        if (JN.isChecked()) location.add("??????");
        if (SJ.isChecked()) location.add("??????");

        return location;
    }

    public String locStr(ArrayList<String> location){
        String temp = "";
        String result = "";

        for (int i = 0;  i < location.size(); i++) {
            if (location.get(i).equals("SU")) {
                temp = "??????";
            } else if (location.get(i).equals("IC")) {
                temp = "??????";
            } else if (location.get(i).equals("JD")) {
                temp = "??????";
            } else if (location.get(i).equals("GJ")) {
                temp = "??????";
            } else if (location.get(i).equals("DG")) {
                temp = "??????";
            } else if (location.get(i).equals("US")) {
                temp = "??????";
            } else if (location.get(i).equals("BS")) {
                temp = "??????";
            } else if (location.get(i).equals("JJ")) {
                temp = "?????????";
            } else if (location.get(i).equals("GG")) {
                temp = "?????????";
            } else if (location.get(i).equals("GW")) {
                temp = "?????????";
            } else if (location.get(i).equals("CB")) {
                temp = "????????????";
            } else if (location.get(i).equals("CN")) {
                temp = "????????????";
            } else if (location.get(i).equals("GB")) {
                temp = "????????????";
            } else if (location.get(i).equals("GN")) {
                temp = "????????????";
            } else if (location.get(i).equals("JB")) {
                temp = "????????????";
            } else if (location.get(i).equals("JN")) {
                temp = "????????????";
            } else if (location.get(i).equals("SJ")) {
                temp = "??????";
            } else {
                temp = location.get(i);
            }

            result += temp + " | ";
        }
        
        // ?????? ?????? ??? ??? ??????
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
                
                idList.add("test"); //null????????? ????????????

                expertList.clear();
                for (int i = 0; i < list.size(); i++) {
                    DocumentSnapshot snapshot = list.get(i);
                    boolean isPublic = snapshot.getBoolean("isPublic");
                    String userId = snapshot.getId();

                    if (!userId.equals(UserInfo.getUserId()) && !isPublic) {
                        ArrayList<String> location = (ArrayList<String>) snapshot.get("location");

                        // ?????? ?????? ??????
                        for (int j = 0; j < locationSelected.size(); j++){
                            for (int k = 0; k < location.size(); k++){
                                if (locationSelected.get(j).equals(location.get(k))){
                                    if (!idList.contains(userId)){ //?????? ??????
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
