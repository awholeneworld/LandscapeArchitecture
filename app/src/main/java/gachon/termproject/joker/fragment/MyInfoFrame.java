package gachon.termproject.joker.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Comment;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.fragment.PostMyInfo;

public class MyInfoFrame extends Fragment {

    private StorageReference storageReference;
    private View view;
    private FragmentManager fm;
    private PostMyInfo post;
    private CommentMyInfo comment;
    private PortfolioMyInfo portfolio;
    TabLayout tabs;
    Button selectCommunityMode;
    boolean i = true;
    Uri file;
    StorageReference fReference;
    FirebaseStorage fStore;
    ImageView profile_Album;


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
        getFirebaseProfileImage(UserInfo.userId);


        tabs = view.findViewById(R.id.myinfo_tabs);
        tabs.setSelectedTabIndicatorHeight(0);
        fm = getChildFragmentManager();

        profile_Album = view.findViewById(R.id.go_album);
        //사진 눌렀을 때, 앨범으로 이동
        view.findViewById(R.id.go_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "사진을 선택하세요."), 0);
            }
        });


        if (post == null) {
            post = new PostMyInfo();
            fm.beginTransaction().add(R.id.myinfo_frame, post).commit();
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabs.getSelectedTabPosition()) {
                    case 0:
                        // tabs.getChildAt(0).setBackgroundColor(Color.parseColor("#4B3D5A"));
                        if (comment != null) fm.beginTransaction().hide(comment).commit();
                        if (portfolio != null) fm.beginTransaction().hide(portfolio).commit();
                        fm.beginTransaction().show(post).commit();
                        break;
                    case 1:
                        if (comment == null) {
                            comment = new CommentMyInfo();
                            fm.beginTransaction().add(R.id.myinfo_frame, comment).commit();
                        }
                        if (portfolio != null) fm.beginTransaction().hide(portfolio).commit();
                        fm.beginTransaction().hide(post).commit();
                        fm.beginTransaction().show(comment).commit();
                        break;
                    case 2:
                        if (portfolio == null) {
                            portfolio = new PortfolioMyInfo();
                            fm.beginTransaction().add(R.id.myinfo_frame, portfolio).commit();
                        }
                        if (comment != null) fm.beginTransaction().hide(comment).commit();
                        fm.beginTransaction().hide(post).commit();
                        fm.beginTransaction().show(portfolio).commit();
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

    //profile 이미지 불러오는 method
    private void downloadImg(String ID) {
        fStore = FirebaseStorage.getInstance();
        fReference = fStore.getReference();
        fReference.child("profile_img/" + "profile" + ID + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(profile_Album);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }


    //프로필 이미지 부르는 햄수(이거쓰세연)
    private void getFirebaseProfileImage(String ID) {
        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profile_img");

        if(!file.isDirectory()){
            file.mkdir();
        }
        downloadImg(ID);
    }

    //이미지 넣는거
    private void createProfile_Photo(String ID){
        fStore = FirebaseStorage.getInstance();
        fReference = fStore.getReference();
        String filename = "profile" + ID + ".jpg";

        StorageReference riverRef = fReference.child("profile_img/" + filename);
        UploadTask uploadTask = riverRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "업로드 실패", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "프로필 이미지가 설정/변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //이미지 삭제하는거를 만들었지만 이미 변경까지 되어버려서 사실상 무쓸모
    //하지만 혹시 모르니 keep
    private void profile_delete(String ID){
        fStore = FirebaseStorage.getInstance();
        fReference = fStore.getReference();

        StorageReference desertRef = fReference.child("profile_img/" + "profile" + ID + ".jpg");

        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e){
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data!= null && data.getData()!= null) {
            file = data.getData(); // 파일 가져옴

            if (file == null) {//파일 선택안한경우
                Toast.makeText(view.getContext(), "사진을 선택하라고 했잖아요", Toast.LENGTH_SHORT).show();
            } else { //파일 선택한 경우
                String ID = UserInfo.userId;
                createProfile_Photo(ID);
                profile_Album.setImageURI(file);
            }
        }
    }
}