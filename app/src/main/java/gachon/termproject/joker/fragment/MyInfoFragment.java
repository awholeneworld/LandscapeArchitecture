package gachon.termproject.joker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.CheckPasswordActivity;
import gachon.termproject.joker.activity.SettingActivity;

public class MyInfoFragment extends Fragment {
    private View view;
    private MyInfoPostFragment post;
    private MyInfoCommentFragment comment;
    private ViewGroup portfolioLayout;
    private FragmentManager fm;
    private TabLayout tabs;
    static String locationStr;

    /* 닉네임 변경 부분
    private CollectionReference collectionReference;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userId = UserInfo.userId;
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myinfo, container, false);

        setHasOptionsMenu(true); //action bar menu

        // 프사 설정
        ImageView profileImg = view.findViewById(R.id.profileImage);
        if (!UserInfo.profileImg.equals("None"))
            Glide.with(getActivity()).load(UserInfo.profileImg).into(profileImg);

        // 닉네임 설정
        TextView nickname = view.findViewById(R.id.myInfoNickname);
        nickname.setText(UserInfo.nickname);

        // 지역 설정
        locationStr = "";
        for (String item : UserInfo.location) {
            locationStr += item + " ";
        }
        TextView location = view.findViewById(R.id.myInfoLocation);
        location.setText(locationStr);

        // 한줄 소개 설정 -> 설정 구현되면 마저 작성할 것임
        TextView intro = view.findViewById(R.id.myInfoMessage);



        // 포트폴리오 창 설정
        portfolioLayout = view.findViewById(R.id.portfolioLayout);
        portfolioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "포트폴리오 창 이동", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MyInfoPortfolioFragment.class));
            }
        });

        // 탭 설정
        fm = getChildFragmentManager();
        tabs = view.findViewById(R.id.myinfo_tabs);

        if (post == null) {
            post = new MyInfoPostFragment();
            fm.beginTransaction().add(R.id.myinfo_frame, post).commit();
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabs.getSelectedTabPosition()) {
                    case 0:
                        if (comment != null) fm.beginTransaction().hide(comment).commit();
                        fm.beginTransaction().show(post).commit();
                        break;
                    case 1:
                        if (comment == null) {
                            comment = new MyInfoCommentFragment();
                            fm.beginTransaction().add(R.id.myinfo_frame, comment).commit();
                        }
                        fm.beginTransaction().hide(post).commit();
                        fm.beginTransaction().show(comment).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        /*
        portfolioText = view.findViewById(R.id.portfolioText);
        portfolioText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getContext(), PortfolioMyInfo.class));
            }
        });

         */
        /* 설정 구현되면 설정 안에 이 내용 넣기 - 닉네임 변경
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
        */

        return view;
    }

    // action bar menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_setting_app_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId()) {
            case R.id.setting:
                getActivity().startActivity(new Intent(getContext(), CheckPasswordActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}