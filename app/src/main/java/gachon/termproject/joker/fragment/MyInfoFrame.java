package gachon.termproject.joker.fragment;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;

public class MyInfoFrame extends Fragment {
    private StorageReference storageReference;
    private View view;
    private ImageView profileImg;
    private PostMyInfo post;
    private CommentMyInfo comment;
    private PortfolioMyInfo portfolio;
    private FragmentManager fm;
    private TabLayout tabs;
    private Uri file;
    private ViewGroup portfolioLayout;

    /* 닉네임 변경 부분
    private CollectionReference collectionReference;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userId = UserInfo.userId;
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frame_myinfo, container, false);


        fm = getChildFragmentManager();
        tabs = view.findViewById(R.id.myinfo_tabs);
        profileImg = view.findViewById(R.id.profileImage);
        portfolioLayout = view.findViewById(R.id.portfolioLayout);

        if (post == null) {
            post = new PostMyInfo();
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
                            comment = new CommentMyInfo();
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

        // 프사 눌렀을 때 이미지 파일 선택 창으로 이동
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "사진을 선택하세요."), 0);
            }
        });

        // 닉네임 설정
        TextView nickname = view.findViewById(R.id.myInfoNickname);
        nickname.setText(UserInfo.nickname);

        // 지역 설정
        String locationStr = "";
        for (String item : UserInfo.location) {
            locationStr += item + " ";
        }
        TextView location = view.findViewById(R.id.myInfoLocation);
        location.setText(locationStr);

        // 한줄 소개 설정 -> 설정 구현되면 마저 작성할 것임
        TextView intro = view.findViewById(R.id.myInfoMessage);


        // 포트폴리오 창으로 이동
        portfolioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getContext(), PortfolioMyInfo.class);
                //startActivity(intent);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data != null && data.getData() != null) { // 이미지 파일 선택하였다면
            file = data.getData();
            uploadProfileImage();
        }
    }

    // 이미지 넣는거
    private void uploadProfileImage(){
        storageReference = FirebaseStorage.getInstance().getReference().child("profileImages/" + UserInfo.userId + "/" + file.getLastPathSegment());
        storageReference.putFile(file).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return storageReference.getDownloadUrl(); // URL은 반드시 업로드 후 다운받아야 사용 가능
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() { // URL 다운 성공 시
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) { // URL을 포스트 내용 Class(postContent)와 DB에 업데이트
                    Uri downloadUrl = task.getResult();
                    String url = downloadUrl.toString();
                    UserInfo.profileImg = url;

                    profileImg.setBackground(new ShapeDrawable(new OvalShape()));
                    profileImg.setClipToOutline(true);
                    Glide.with(getActivity()).load(url).into(profileImg);

                    Toast.makeText(getContext(), "프로필 이미지가 설정/변경되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}