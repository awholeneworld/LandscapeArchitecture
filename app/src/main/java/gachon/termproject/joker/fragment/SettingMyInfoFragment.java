package gachon.termproject.joker.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.ChangePasswordActivity;
import gachon.termproject.joker.activity.CheckPasswordActivity;
import gachon.termproject.joker.activity.MainActivity;

public class SettingMyInfoFragment extends Fragment {
    private View view;
    private CheckBox SU, IC, DJ, GJ, DG, US, BS, JJ, GG, GW, CB, CN, GB, GN, JB, JN, SJ;
    private StorageReference storageReference;
    private ArrayList<String> locationSelected;
    private ArrayList<String> location;
    private ImageView profileImg;
    private ImageView changeProfileImageBack;
    private ImageView changeProfileImage;
    private EditText email;
    private EditText nickname;
    private EditText introMsg;
    private Button checkNickname;
    private Button save;
    private Button resetPwd;
    private String nicknameEdited;
    private String messageEdited;
    private Uri file;
    private int flag_profileImg_change = 0;
    private int flag_nickname_check = 0;
    private int flag_location = 0;
    private int flag_message = 0;
    private int successCount = 0;
    private int failCount = 0;
    private int postsCountFree = 0;
    private int postsCountReview = 0;
    private int postsCountTip = 0;
    private int finishCountFree = 0;
    private int finishCountReview = 0;
    private int finishCountTip = 0;
    private int finishCount = 0;
    private int matchingSuccessCount = 0;
    private int chatCount = 0;
    private int commentSuccessFree = 0;
    private int commentSuccessReview = 0;
    private int commentSuccessTip = 0;
    private int commentFailFree = 0;
    private int commentFailReview = 0;
    private int commentFailTip = 0;
    private int commentFinishFree = 0;
    private int commentFinishReview = 0;
    private int commentFinishTip = 0;
    private int freeNoComments = 0;
    private int reviewNoComments = 0;
    private int tipNoComments = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myinfo_setting, container, false);
        // 레이아웃 가져오기
        profileImg = view.findViewById(R.id.profileImage);
        changeProfileImageBack = view.findViewById(R.id.changeProfileImageBack);
        email = view.findViewById(R.id.setting_email);
        nickname = view.findViewById(R.id.setting_nickname);
        introMsg = view.findViewById(R.id.setting_message);
        checkNickname = view.findViewById(R.id.duplicateCheck);
        save = view.findViewById(R.id.setting_save_button);
        resetPwd = view.findViewById(R.id.setting_password_button);
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

        // 프사 설정
        profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        profileImg.setClipToOutline(true);
        if (!UserInfo.profileImg.equals("None"))
            Glide.with(getContext()).load(UserInfo.profileImg).into(profileImg);

        changeProfileImageBack.setBackground(new ShapeDrawable(new OvalShape()));
        changeProfileImageBack.setClipToOutline(true);

        // 이메일 설정
        email.setText(UserInfo.email);
        email.setTextColor(Color.parseColor("#000000"));

        // 닉네임 설정
        nickname.setText(UserInfo.nickname);
        nicknameEdited = UserInfo.nickname;

        // 한줄 메시지 설정
        introMsg.setText(UserInfo.introduction);

        // 지역 설정
        setLocation();

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

        changeProfileImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "사진을 선택하세요."), 0);
            }
        });

        // 닉네임 중복체크 버튼 눌렀을 때
        checkNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nicknameCheck();
            }
        });

        // 저장 눌렀을 때
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                save.setEnabled(false);
                saveChanges();
            }
        });

        // 비밀번호 변경 눌렀을 때
        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPwd.setEnabled(false);
                startActivity(new Intent(getContext(), CheckPasswordActivity.class));
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                getActivity().finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data != null && data.getData() != null) { // 이미지 파일 선택하였다면
            file = data.getData();
            flag_profileImg_change = 1;
            Glide.with(getContext()).load(file).into(profileImg);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resetPwd.setEnabled(true);
    }

    public void nicknameCheck() {
        //닉네임을 입력받음
        String temp = nickname.getText().toString();

        if (temp.length() == 0) {
            nickname.setText(UserInfo.nickname);
            nickname.setEnabled(false);
            checkNickname.setEnabled(false);
            Toast.makeText(getContext(), "변경할 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if (temp.length() > 6) {
            nickname.setText(UserInfo.nickname);
            nickname.setEnabled(false);
            checkNickname.setEnabled(false);
            Toast.makeText(getContext(), "닉네임은 6자 이하로 설정해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if (temp.equals(UserInfo.nickname)) {
            nickname.setEnabled(false);
            checkNickname.setEnabled(false);
            Toast.makeText(getContext(), "본인의 닉네임입니다.", Toast.LENGTH_SHORT).show();
        }
        else { //데이터베이스에서 중복되는 닉네임 있는지 확인!!!
            FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> list = querySnapshot.getDocuments();

                        for (int i = 0; i < list.size(); i++) {
                            DocumentSnapshot snapshot = list.get(i);
                            String nicknameCheck = snapshot.getString("nickname");
                            if (temp.compareTo(nicknameCheck) == 0) {
                                Toast.makeText(getContext(), "중복된 닉네임 입니다", Toast.LENGTH_SHORT).show();
                                break;
                            } else if (i == list.size() - 1) {
                                flag_nickname_check++;
                                nicknameEdited = temp;
                                nickname.setEnabled(false);
                                checkNickname.setEnabled(false);
                                Toast.makeText(getContext(), "사용 가능한 닉네임 입니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }
    }

    public void setLocation() {
        location = UserInfo.location;

        if (location.contains("서울")) SU.setChecked(true);
        if (location.contains("인천")) IC.setChecked(true);
        if (location.contains("대전")) DJ.setChecked(true);
        if (location.contains("광주")) GJ.setChecked(true);
        if (location.contains("대구")) DG.setChecked(true);
        if (location.contains("울산")) US.setChecked(true);
        if (location.contains("부산")) BS.setChecked(true);
        if (location.contains("제주")) JJ.setChecked(true);
        if (location.contains("경기")) GG.setChecked(true);
        if (location.contains("강원")) GW.setChecked(true);
        if (location.contains("충북")) CB.setChecked(true);
        if (location.contains("충남")) CN.setChecked(true);
        if (location.contains("경북")) GB.setChecked(true);
        if (location.contains("경남")) GN.setChecked(true);
        if (location.contains("전북")) JB.setChecked(true);
        if (location.contains("전남")) JN.setChecked(true);
        if (location.contains("세종")) SJ.setChecked(true);
    }

    public ArrayList<String> checkLocation() {
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

    // 이미지 넣는거
    private void updateChangesWithImg(){
        storageReference = FirebaseStorage.getInstance().getReference().child("profileImages/" + UserInfo.userId);
        storageReference.putFile(file).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return storageReference.getDownloadUrl(); // URL은 반드시 업로드 후 다운받아야 사용 가능
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) { // URL 다운 성공 시
                    Uri downloadUrl = task.getResult();
                    String url = downloadUrl.toString();

                    Map<String, Object> dataToUpdate = new HashMap<>();
                    dataToUpdate.put("profileImg", url);
                    if (flag_nickname_check == 1) dataToUpdate.put("nickname", nickname.getText().toString());
                    if (flag_location == 1) dataToUpdate.put("location", locationSelected);
                    if (flag_message == 1) dataToUpdate.put("introduction", messageEdited);

                    // 계정 정보 저장하는 DB 업데이트
                    FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update(dataToUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            UserInfo.profileImg = url;
                            MyInfoFragment.profileImg.setImageURI(downloadUrl);
                            if (flag_nickname_check == 1) UserInfo.nickname = nicknameEdited;
                            if (flag_location == 1) UserInfo.location = locationSelected;
                            if (flag_message == 1 ) UserInfo.introduction = messageEdited;

                            //Glide.with(getContext()).load(downloadUrl).override(1000).thumbnail(0.1f).into(MyInfoFragment.profileImg);
                            MyInfoFragment.nickname.setText(nicknameEdited);
                            String locationStr = "";
                            for (String item : UserInfo.location) {
                                locationStr += item + " ";
                            }
                            MyInfoFragment.location.setText(locationStr);
                            MyInfoFragment.intro.setText(messageEdited);

                            dbUpdate();
                        }
                    });
                }
            }
        });
    }

    public void saveChanges() {
        int finishCount = 0;

        // 프사 변경함?
        if (flag_profileImg_change == 1) finishCount++;

        // 닉네임 변경 시도함?
        if (flag_nickname_check == 0  && nicknameEdited.equals(UserInfo.nickname)) {
            flag_nickname_check = 0;
        } else if (flag_nickname_check == 0) {
            save.setEnabled(true);
            Toast.makeText(getContext(), "닉네임 중복 확인을 해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        finishCount++;

        // 지역 변경 시도함?
        locationSelected = checkLocation();
        if (locationSelected.isEmpty()) {
            save.setEnabled(true);
            Toast.makeText(getContext(), "지역을 하나 이상 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (!locationSelected.isEmpty()) // 체크 되어있으면
            if (!location.equals(locationSelected)) // 다르면
                flag_location++;

        finishCount++;

        // 한줄 메시지 변경 시도함?
        messageEdited = introMsg.getText().toString();
        if (messageEdited.length() > 30) {
            save.setEnabled(true);
            Toast.makeText(getContext(), "한줄 메시지는 30자 이하로 작성해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!messageEdited.equals(UserInfo.introduction))
            flag_message++;

        finishCount++;

        // 저장 경우의 수 총 16가지 = 8가지(프사 변경X) + 8가지(프사 변경O)
        if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 0 && flag_message == 0) {
            Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 0 && flag_message == 1) { // 한줄 메시지 소개만 변경한 경우
            FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update("introduction", messageEdited).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.introduction = messageEdited;
                    MyInfoFragment.intro.setText(messageEdited);
                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 1 && flag_message == 0) { // 지역만 변경한 경우
            FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update("location", locationSelected).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.location = locationSelected;

                    String locationStr = "";
                    for (String item : locationSelected) {
                        locationStr += item + " ";
                    }
                    MyInfoFragment.location.setText(locationStr);

                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 0 && flag_message == 0) { // 닉네임만 변경한 경우
            FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update("nickname", nicknameEdited).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.nickname = nicknameEdited;
                    MyInfoFragment.nickname.setText(nicknameEdited);
                    dbUpdate();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 1 && flag_message == 1) { // 지역과 소개 두개를 변경한 경우
            Map<String, Object> dataToUpdate = new HashMap<>();
            dataToUpdate.put("location", locationSelected);
            dataToUpdate.put("introduction", messageEdited);

            FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update(dataToUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.location = locationSelected;
                    UserInfo.introduction = messageEdited;

                    String locationStr = "";
                    for (String item : locationSelected) {
                        locationStr += item + " ";
                    }
                    MyInfoFragment.location.setText(locationStr);
                    MyInfoFragment.intro.setText(messageEdited);

                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 0 && flag_message == 1) { // 닉네임과 소개 두개를 변경한 경우
            Map<String, Object> dataToUpdate = new HashMap<>();
            dataToUpdate.put("nickname", nicknameEdited);
            dataToUpdate.put("introduction", messageEdited);

            FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update(dataToUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.nickname = nicknameEdited;
                    UserInfo.introduction = messageEdited;
                    MyInfoFragment.nickname.setText(nicknameEdited);
                    MyInfoFragment.intro.setText(messageEdited);
                    dbUpdate();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 1 && flag_message == 0) { // 닉네임과 지역 두개를 변경한 경우
            Map<String, Object> dataToUpdate = new HashMap<>();
            dataToUpdate.put("nickname", nicknameEdited);
            dataToUpdate.put("location", locationSelected);

            FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update(dataToUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.nickname = nicknameEdited;
                    UserInfo.location = locationSelected;

                    String locationStr = "";
                    for (String item : locationSelected) {
                        locationStr += item + " ";
                    }
                    MyInfoFragment.nickname.setText(nicknameEdited);
                    MyInfoFragment.location.setText(locationStr);

                    dbUpdate();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 1 && flag_message == 1) { // 닉네임과 지역, 소개 3개를 변경한 경우
            Map<String, Object> dataToUpdate = new HashMap<>();
            dataToUpdate.put("nickname", nicknameEdited);
            dataToUpdate.put("location", locationSelected);
            dataToUpdate.put("introduction", messageEdited);

            FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update(dataToUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.nickname = nicknameEdited;
                    UserInfo.location = locationSelected;
                    UserInfo.introduction = messageEdited;

                    String locationStr = "";
                    for (String item : locationSelected) {
                        locationStr += item + " ";
                    }
                    MyInfoFragment.nickname.setText(nicknameEdited);
                    MyInfoFragment.location.setText(locationStr);
                    MyInfoFragment.intro.setText(messageEdited);

                    dbUpdate();
                }
            });
        } else
            updateChangesWithImg();
    }

    public void dbUpdate() {
        finishCount = 0;
        FirebaseDatabase db = FirebaseDatabase.getInstance(); // DB
        DatabaseReference postsRef = db.getReference("Posts"); // 게시물 DB

        postsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() { // 게시물 DB 접근
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) { // DB에 커뮤니티 데이터가 하나도 없을 경우
                    // 내가 작성한 매칭 게시물 찾기
                    db.getReference("Matching/userRequests").orderByChild("userId").equalTo(UserInfo.userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                            Map<String, Object> userData = new HashMap<>(); // 업데이트 할 데이터 미리 만들기
                            if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.profileImg);
                            if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);

                            if (task.isSuccessful()) {
                                DataSnapshot matchingSnapshot = task.getResult();
                                if (!matchingSnapshot.exists()) { // 내가 쓴 매칭 게시물이 없다면 (전문가는 무조건 이곳에 해당)
                                    // 내 채팅 찾기
                                    db.getReference("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                        @Override
                                        public void onSuccess(DataSnapshot chatSnapshot) {
                                            if (!chatSnapshot.exists()) { // 나의 채팅이 존재하지 않으면
                                                Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                getActivity().finish();
                                            } else {
                                                chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                        // 채팅방 한개 업데이트하기
                                                        chatCount++;
                                                        myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                    // 끝내기
                                                                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                    getActivity().finish();
                                                                }
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                    }

                                                    @Override
                                                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                    }

                                                    @Override
                                                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else { // 내가 쓴 매칭 게시물이 있다면
                                    matchingSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 내가 쓴 매칭 게시물 하나씩 가져오기
                                        @Override
                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myMatchingSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                            // 글 하나 하나 정보 새로 업데이트
                                            matchingSuccessCount++;
                                            myMatchingSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    if (matchingSuccessCount == matchingSnapshot.getChildrenCount()) { // 매칭 글 다 업데이트하였다면
                                                        // 나의 채팅방 업데이트 하기
                                                        db.getReference().child("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DataSnapshot chatSnapshot) {
                                                                if (!chatSnapshot.exists()) { // 나의 채팅방이 존재하지 않으면
                                                                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                    getActivity().finish();
                                                                } else { // 나의 채팅방이 존재하면
                                                                    chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 채팅방 하나 하나 가져오기
                                                                        @Override
                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                            // 채팅방 한개 업데이트하기
                                                                            chatCount++;
                                                                            myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                        // 끝내기
                                                                                        Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                        getActivity().finish();
                                                                                    }
                                                                                }
                                                                            });
                                                                        }

                                                                        @Override
                                                                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                        }

                                                                        @Override
                                                                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                        }

                                                                        @Override
                                                                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        }
                    });
                } else { // DB에 커뮤니티 게시물이 하나라도 있을 경우
                    final long categoryNum = dataSnapshot.getChildrenCount(); // 먼저 글이 있는 게시판 카테고리 수 가져옴

                    dataSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 그 다음 게시물 DB에 접근해서 게시판의 각 카테고리 데이터를 하나씩 가져옴
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot categorySnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                            // 내가 쓴 게시물이 있는지 확인하고 업데이트. 다 완료되면 댓글->매칭->채팅 순으로 업데이트.
                            categorySnapshot.getRef().orderByChild("userId").equalTo(UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() { // 각 카테고리에서 내가 쓴 글이 있는지 확인
                                @Override
                                public void onSuccess(DataSnapshot postsSnapshot) {
                                    if (!postsSnapshot.exists()) { // 내가 쓴 글이 지금 카테고리에 하나도 존재하지 않으면 fail count 하나 추가
                                        failCount++;
                                        if (failCount == categoryNum) { // 글이 있는 모든 카테고리에 나의 글이 하나도 없다면
                                            failCount = 0;
                                            postsRef.getRef().addChildEventListener(new ChildEventListener() { // 댓글 찾기
                                                @Override
                                                public void onChildAdded(@NonNull @NotNull DataSnapshot categorySnapshot2, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                    categorySnapshot2.getRef().addChildEventListener(new ChildEventListener() { // 내가 작성한 댓글이 있는지 게시글 하나 하나씩 가져와서 확인
                                                        @Override
                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot commentsSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                            if (!commentsSnapshot.hasChild("comments")) { // 게시글에 댓글이 없다면
                                                                if (categorySnapshot2.getKey().equals("free")) freeNoComments++;
                                                                else if (categorySnapshot2.getKey().equals("review")) reviewNoComments++;
                                                                else if (categorySnapshot2.getKey().equals("tip")) tipNoComments++;

                                                                if ((categorySnapshot2.getKey().equals("free") && freeNoComments == categorySnapshot2.getChildrenCount()) || (categorySnapshot2.getKey().equals("review") && reviewNoComments == categorySnapshot2.getChildrenCount()) || (categorySnapshot2.getKey().equals("tip") && tipNoComments == categorySnapshot2.getChildrenCount())) { // 모든 카테고리에 댓글이 없다면
                                                                    if (categorySnapshot2.getKey().equals("free")) finishCount++;
                                                                    else if (categorySnapshot2.getKey().equals("review")) finishCount++;
                                                                    else if (categorySnapshot2.getKey().equals("tip")) finishCount++;
                                                                    if (finishCount == categoryNum) {
                                                                        finishCount = 0;
                                                                        // 내가 작성한 매칭 게시물 찾기
                                                                        db.getReference("Matching/userRequests").orderByChild("userId").equalTo(UserInfo.userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                                                                Map<String, Object> userData = new HashMap<>(); // 업데이트 할 데이터 미리 만들기
                                                                                if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.profileImg);
                                                                                if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);

                                                                                if (task.isSuccessful()) {
                                                                                    DataSnapshot matchingSnapshot = task.getResult();
                                                                                    if (!matchingSnapshot.exists()) { // 내가 쓴 매칭 게시물이 없다면 (전문가는 무조건 이곳에 해당)
                                                                                        // 내 채팅 찾기
                                                                                        db.getReference("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                            @Override
                                                                                            public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                if (!chatSnapshot.exists()) { // 나의 채팅이 존재하지 않으면
                                                                                                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                    getActivity().finish();
                                                                                                } else {
                                                                                                    chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                        @Override
                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                            // 채팅방 한개 업데이트하기
                                                                                                            chatCount++;
                                                                                                            myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Void unused) {
                                                                                                                    if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                        // 업뎃하고 끝내기
                                                                                                                        if (MyInfoFragment.post != null) MyInfoFragment.post.adapter.notifyDataSetChanged();
                                                                                                                        if (MyInfoFragment.comment != null) MyInfoFragment.comment.adapter.notifyDataSetChanged();
                                                                                                                        Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                        getActivity().finish();
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                    } else { // 내가 쓴 매칭 게시물이 있다면
                                                                                        matchingSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 내가 쓴 매칭 게시물 하나씩 가져오기
                                                                                            @Override
                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myMatchingSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                // 글 하나 하나 정보 새로 업데이트
                                                                                                matchingSuccessCount++;
                                                                                                myMatchingSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void unused) {
                                                                                                        if (matchingSuccessCount == matchingSnapshot.getChildrenCount()) { // 매칭 글 다 업데이트하였다면
                                                                                                            // 나의 채팅방 업데이트 하기
                                                                                                            db.getReference().child("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                    if (!chatSnapshot.exists()) { // 나의 채팅방이 존재하지 않으면
                                                                                                                        Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                        getActivity().finish();
                                                                                                                    } else { // 나의 채팅방이 존재하면
                                                                                                                        chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 채팅방 하나 하나 가져오기
                                                                                                                            @Override
                                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                // 채팅방 한개 업데이트하기
                                                                                                                                chatCount++;
                                                                                                                                myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                    @Override
                                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                                        if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                                            // 끝내기
                                                                                                                                            Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                            getActivity().finish();
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            }

                                                                                            @Override
                                                                                            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                            }

                                                                                            @Override
                                                                                            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                            }

                                                                                            @Override
                                                                                            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            }
                                                            else { // 게시글에 댓글이 포함되어 있다면
                                                                commentsSnapshot.getRef().child("comments").orderByChild("userId").equalTo(UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() { // 내가 쓴게 있는지 확인
                                                                    @Override
                                                                    public void onSuccess(DataSnapshot myCommentSnapshot) {
                                                                        if (!myCommentSnapshot.exists()) { // 현재 게시글에 내가 단 댓글이 없으면
                                                                            if (categorySnapshot2.getKey().equals("free")) commentFailFree++;
                                                                            else if (categorySnapshot2.getKey().equals("review")) commentFailReview++;
                                                                            else if (categorySnapshot2.getKey().equals("tip")) commentFailTip++;

                                                                            if ((categorySnapshot.getKey().equals("free") && commentFailFree + commentSuccessFree == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("review") && commentFailReview + commentSuccessReview == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("tip") && commentFailTip + commentSuccessTip == categorySnapshot.getChildrenCount())) { // 내가 단 댓글이 하나도 없다면
                                                                                finishCount++;
                                                                                if (finishCount == categoryNum) {
                                                                                    // 내가 작성한 매칭 게시물 찾기
                                                                                    db.getReference("Matching/userRequests").orderByChild("userId").equalTo(UserInfo.userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                                                                            Map<String, Object> userData = new HashMap<>(); // 업데이트 할 데이터 미리 만들기
                                                                                            if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.profileImg);
                                                                                            if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);

                                                                                            if (task.isSuccessful()) {
                                                                                                DataSnapshot matchingSnapshot = task.getResult();
                                                                                                if (!matchingSnapshot.exists()) { // 내가 쓴 매칭 게시물이 없다면 (전문가는 무조건 이곳에 해당)
                                                                                                    // 내 채팅 찾기
                                                                                                    db.getReference("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                            if (!chatSnapshot.exists()) { // 나의 채팅이 존재하지 않으면
                                                                                                                Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                getActivity().finish();
                                                                                                            } else {
                                                                                                                chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                    @Override
                                                                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                        // 채팅방 한개 업데이트하기
                                                                                                                        chatCount++;
                                                                                                                        myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(Void unused) {
                                                                                                                                if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                                    // 끝내기
                                                                                                                                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                    getActivity().finish();
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                                } else { // 내가 쓴 매칭 게시물이 있다면
                                                                                                    matchingSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 내가 쓴 매칭 게시물 하나씩 가져오기
                                                                                                        @Override
                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myMatchingSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                            // 글 하나 하나 정보 새로 업데이트
                                                                                                            matchingSuccessCount++;
                                                                                                            myMatchingSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Void unused) {
                                                                                                                    if (matchingSuccessCount == matchingSnapshot.getChildrenCount()) { // 매칭 글 다 업데이트하였다면
                                                                                                                        // 나의 채팅방 업데이트 하기
                                                                                                                        db.getReference().child("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                if (!chatSnapshot.exists()) { // 나의 채팅방이 존재하지 않으면
                                                                                                                                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                    getActivity().finish();
                                                                                                                                } else { // 나의 채팅방이 존재하면
                                                                                                                                    chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 채팅방 하나 하나 가져오기
                                                                                                                                        @Override
                                                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                            // 채팅방 한개 업데이트하기
                                                                                                                                            chatCount++;
                                                                                                                                            myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                @Override
                                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                                    if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                                                        // 끝내기
                                                                                                                                                        Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                                        getActivity().finish();
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        } else { // 내가 단 댓글이 있으면
                                                                            Map<String, Object> userData = new HashMap<>(); // 업데이트 할 데이터 만들기
                                                                            if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.profileImg);
                                                                            if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);

                                                                            if (categorySnapshot2.getKey().equals("free")  && myCommentSnapshot.getRef().child("userId").toString().equals(UserInfo.userId)) commentSuccessFree++;
                                                                            else if (categorySnapshot2.getKey().equals("review") && myCommentSnapshot.getRef().child("userId").toString().equals(UserInfo.userId)) commentSuccessReview++;
                                                                            else if (categorySnapshot2.getKey().equals("tip") && myCommentSnapshot.getRef().child("userId").toString().equals(UserInfo.userId)) commentSuccessTip++;

                                                                            // 댓글 업데이트하기
                                                                            myCommentSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    if ((categorySnapshot2.getKey().equals("free") && (commentFailFree + commentSuccessFree) == categorySnapshot2.getChildrenCount() && commentFinishFree == 0) || (categorySnapshot2.getKey().equals("review") && (commentFailReview + commentSuccessReview) == categorySnapshot2.getChildrenCount() && commentFinishReview == 0) || (categorySnapshot2.getKey().equals("tip") && (commentFailTip + commentSuccessTip) == categorySnapshot2.getChildrenCount() && commentFinishTip == 0)) { // 댓글이 있는 모든 게시물을 다 확인하였으면
                                                                                        if (categorySnapshot2.getKey().equals("free")) commentFinishFree++;
                                                                                        else if (categorySnapshot2.getKey().equals("review")) commentFinishReview++;
                                                                                        else if (categorySnapshot2.getKey().equals("tip")) commentFinishTip++;
                                                                                        if (commentFinishFree + commentFinishReview + commentFinishTip == categoryNum) {
                                                                                            // 내가 작성한 매칭 게시물 찾기
                                                                                            db.getReference("Matching/userRequests").orderByChild("userId").equalTo(UserInfo.userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        DataSnapshot matchingSnapshot = task.getResult();
                                                                                                        if (!matchingSnapshot.exists()) { // 내가 쓴 매칭 게시물이 없다면 (전문가는 무조건 이곳에 해당)
                                                                                                            // 내 채팅 찾기
                                                                                                            db.getReference("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                    if (!chatSnapshot.exists()) { // 나의 채팅이 존재하지 않으면
                                                                                                                        Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                        getActivity().finish();
                                                                                                                    } else {
                                                                                                                        chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                            @Override
                                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                // 채팅방 한개 업데이트하기
                                                                                                                                chatCount++;
                                                                                                                                myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                    @Override
                                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                                        if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                                            // 끝내기
                                                                                                                                            Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                            getActivity().finish();
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                        } else { // 내가 쓴 매칭 게시물이 있다면
                                                                                                            matchingSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 내가 쓴 매칭 게시물 하나씩 가져오기
                                                                                                                @Override
                                                                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                    // 글 하나 하나 정보 새로 업데이트
                                                                                                                    matchingSuccessCount++;
                                                                                                                    snapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(Void unused) {
                                                                                                                            if (matchingSuccessCount == matchingSnapshot.getChildrenCount()) { // 매칭 글 다 업데이트하였다면
                                                                                                                                // 나의 채팅방 업데이트 하기
                                                                                                                                db.getReference().child("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                    @Override
                                                                                                                                    public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                        if (!chatSnapshot.exists()) { // 나의 채팅방이 존재하지 않으면
                                                                                                                                            Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                            getActivity().finish();
                                                                                                                                        } else { // 나의 채팅방이 존재하면
                                                                                                                                            chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 채팅방 하나 하나 가져오기
                                                                                                                                                @Override
                                                                                                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                    // 채팅방 한개 업데이트하기
                                                                                                                                                    chatCount++;
                                                                                                                                                    myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onSuccess(Void unused) {
                                                                                                                                                            if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                                                                // 끝내기
                                                                                                                                                                Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                                                getActivity().finish();
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    });
                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                                            }
                                                                                                                        }
                                                                                                                    });
                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                        }

                                                        @Override
                                                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                        }

                                                        @Override
                                                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                }

                                                @Override
                                                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                }

                                                @Override
                                                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    } else { // 내가 쓴 글이 지금 카테고리에 하나라도 존재할 경우
                                        postsSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 각 카테고리의 내가 쓴 글을 하나씩 가져오기
                                            @Override
                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myPostSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                // 업데이트 할 데이터 만들기
                                                Map<String, Object> userData = new HashMap<>();
                                                if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.profileImg);
                                                if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);

                                                // 글 한개 업데이트하기
                                                if (postsSnapshot.getKey().equals("free") && myPostSnapshot.child("userId").getValue().toString().equals(UserInfo.userId)) postsCountFree++;
                                                else if (postsSnapshot.getKey().equals("review") && myPostSnapshot.child("userId").getValue().toString().equals(UserInfo.userId)) postsCountReview++;
                                                else if (postsSnapshot.getKey().equals("tip") && myPostSnapshot.child("userId").getValue().toString().equals(UserInfo.userId)) postsCountTip++;
                                                myPostSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        if ((postsSnapshot.getKey().equals("free") && postsCountFree == postsSnapshot.getChildrenCount() && finishCountFree == 0) || (postsSnapshot.getKey().equals("review") && postsCountReview == postsSnapshot.getChildrenCount() && finishCountReview == 0) || (postsSnapshot.getKey().equals("tip") && postsCountTip == postsSnapshot.getChildrenCount() && finishCountTip == 0)) {
                                                            if (postsSnapshot.getKey().equals("free")) finishCountFree++;
                                                            else if (postsSnapshot.getKey().equals("review")) finishCountReview++;
                                                            else if (postsSnapshot.getKey().equals("tip")) finishCountTip++;
                                                            if (failCount + finishCountFree + finishCountReview + finishCountTip == categoryNum) {
                                                                failCount = 0;
                                                                postsRef.getRef().addChildEventListener(new ChildEventListener() { // 댓글 찾기
                                                                    @Override
                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot categorySnapshot2, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                        categorySnapshot2.getRef().addChildEventListener(new ChildEventListener() { // 내가 작성한 댓글이 있는지 게시글 하나 하나씩 가져와서 확인
                                                                            @Override
                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot commentsSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                if (!commentsSnapshot.hasChild("comments")) { // 게시글에 댓글이 없다면
                                                                                    if (categorySnapshot2.getKey().equals("free")) commentFailFree++;
                                                                                    else if (categorySnapshot2.getKey().equals("review")) commentFailReview++;
                                                                                    else if (categorySnapshot2.getKey().equals("tip")) commentFailTip++;

                                                                                    if ((categorySnapshot2.getKey().equals("free") && commentFailFree == categorySnapshot2.getChildrenCount()) || (categorySnapshot2.getKey().equals("review") && commentFailReview == categorySnapshot2.getChildrenCount()) || (categorySnapshot2.getKey().equals("tip") && commentFailTip == categorySnapshot2.getChildrenCount())) { // 모든 카테고리에 댓글이 없다면
                                                                                        if (categorySnapshot2.getKey().equals("free")) finishCount++;
                                                                                        else if (categorySnapshot2.getKey().equals("review")) finishCount++;
                                                                                        else if (categorySnapshot2.getKey().equals("tip")) finishCount++;
                                                                                        if (finishCount == categoryNum) {
                                                                                            finishCount = 0;
                                                                                            // 내가 작성한 매칭 게시물 찾기
                                                                                            db.getReference("Matching/userRequests").orderByChild("userId").equalTo(UserInfo.userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                                                                                    Map<String, Object> userData = new HashMap<>(); // 업데이트 할 데이터 미리 만들기
                                                                                                    if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.profileImg);
                                                                                                    if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);

                                                                                                    if (task.isSuccessful()) {
                                                                                                        DataSnapshot matchingSnapshot = task.getResult();
                                                                                                        if (!matchingSnapshot.exists()) { // 내가 쓴 매칭 게시물이 없다면 (전문가는 무조건 이곳에 해당)
                                                                                                            // 내 채팅 찾기
                                                                                                            db.getReference("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                    if (!chatSnapshot.exists()) { // 나의 채팅이 존재하지 않으면
                                                                                                                        Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                        getActivity().finish();
                                                                                                                    } else {
                                                                                                                        chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                            @Override
                                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                // 채팅방 한개 업데이트하기
                                                                                                                                chatCount++;
                                                                                                                                myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                    @Override
                                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                                        if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                                            // 업뎃하고 끝내기
                                                                                                                                            if (MyInfoFragment.post != null) MyInfoFragment.post.adapter.notifyDataSetChanged();
                                                                                                                                            if (MyInfoFragment.comment != null) MyInfoFragment.comment.adapter.notifyDataSetChanged();
                                                                                                                                            Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                            getActivity().finish();
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                        } else { // 내가 쓴 매칭 게시물이 있다면
                                                                                                            matchingSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 내가 쓴 매칭 게시물 하나씩 가져오기
                                                                                                                @Override
                                                                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot myMatchingSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                    // 글 하나 하나 정보 새로 업데이트
                                                                                                                    matchingSuccessCount++;
                                                                                                                    myMatchingSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(Void unused) {
                                                                                                                            if (matchingSuccessCount == matchingSnapshot.getChildrenCount()) { // 매칭 글 다 업데이트하였다면
                                                                                                                                // 나의 채팅방 업데이트 하기
                                                                                                                                db.getReference().child("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                    @Override
                                                                                                                                    public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                        if (!chatSnapshot.exists()) { // 나의 채팅방이 존재하지 않으면
                                                                                                                                            Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                            getActivity().finish();
                                                                                                                                        } else { // 나의 채팅방이 존재하면
                                                                                                                                            chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 채팅방 하나 하나 가져오기
                                                                                                                                                @Override
                                                                                                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                    // 채팅방 한개 업데이트하기
                                                                                                                                                    chatCount++;
                                                                                                                                                    myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onSuccess(Void unused) {
                                                                                                                                                            if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                                                                // 끝내기
                                                                                                                                                                Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                                                getActivity().finish();
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    });
                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                                            }
                                                                                                                        }
                                                                                                                    });
                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                }
                                                                                else { // 게시글에 댓글이 포함되어 있다면
                                                                                    commentsSnapshot.getRef().child("comments").orderByChild("userId").equalTo(UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() { // 내가 쓴게 있는지 확인
                                                                                        @Override
                                                                                        public void onSuccess(DataSnapshot myCommentSnapshot) {
                                                                                            if (!myCommentSnapshot.exists()) { // 현재 게시글에 내가 단 댓글이 없으면
                                                                                                if (categorySnapshot2.getKey().equals("free")) commentFailFree++;
                                                                                                else if (categorySnapshot2.getKey().equals("review")) commentFailReview++;
                                                                                                else if (categorySnapshot2.getKey().equals("tip")) commentFailTip++;

                                                                                                if ((categorySnapshot.getKey().equals("free") && commentFailFree + commentSuccessFree == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("review") && commentFailReview + commentSuccessReview == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("tip") && commentFailTip + commentSuccessTip == categorySnapshot.getChildrenCount())) { // 내가 단 댓글이 하나도 없다면
                                                                                                    finishCount++;
                                                                                                    if (finishCount == categoryNum) {
                                                                                                        // 내가 작성한 매칭 게시물 찾기
                                                                                                        db.getReference("Matching/userRequests").orderByChild("userId").equalTo(UserInfo.userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                                                                                                Map<String, Object> userData = new HashMap<>(); // 업데이트 할 데이터 미리 만들기
                                                                                                                if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.profileImg);
                                                                                                                if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);

                                                                                                                if (task.isSuccessful()) {
                                                                                                                    DataSnapshot matchingSnapshot = task.getResult();
                                                                                                                    if (!matchingSnapshot.exists()) { // 내가 쓴 매칭 게시물이 없다면 (전문가는 무조건 이곳에 해당)
                                                                                                                        // 내 채팅 찾기
                                                                                                                        db.getReference("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                if (!chatSnapshot.exists()) { // 나의 채팅이 존재하지 않으면
                                                                                                                                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                    getActivity().finish();
                                                                                                                                } else {
                                                                                                                                    chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                                        @Override
                                                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                            // 채팅방 한개 업데이트하기
                                                                                                                                            chatCount++;
                                                                                                                                            myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                @Override
                                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                                    if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                                                        // 끝내기
                                                                                                                                                        Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                                        getActivity().finish();
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                                    } else { // 내가 쓴 매칭 게시물이 있다면
                                                                                                                        matchingSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 내가 쓴 매칭 게시물 하나씩 가져오기
                                                                                                                            @Override
                                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myMatchingSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                // 글 하나 하나 정보 새로 업데이트
                                                                                                                                matchingSuccessCount++;
                                                                                                                                myMatchingSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                    @Override
                                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                                        if (matchingSuccessCount == matchingSnapshot.getChildrenCount()) { // 매칭 글 다 업데이트하였다면
                                                                                                                                            // 나의 채팅방 업데이트 하기
                                                                                                                                            db.getReference().child("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                                @Override
                                                                                                                                                public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                                    if (!chatSnapshot.exists()) { // 나의 채팅방이 존재하지 않으면
                                                                                                                                                        Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                                        getActivity().finish();
                                                                                                                                                    } else { // 나의 채팅방이 존재하면
                                                                                                                                                        chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 채팅방 하나 하나 가져오기
                                                                                                                                                            @Override
                                                                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                                // 채팅방 한개 업데이트하기
                                                                                                                                                                chatCount++;
                                                                                                                                                                myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                    @Override
                                                                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                                                                        if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                                                                            // 끝내기
                                                                                                                                                                            Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                                                            getActivity().finish();
                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                                });
                                                                                                                                                            }

                                                                                                                                                            @Override
                                                                                                                                                            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                                            }

                                                                                                                                                            @Override
                                                                                                                                                            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                                                            }

                                                                                                                                                            @Override
                                                                                                                                                            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                                            }

                                                                                                                                                            @Override
                                                                                                                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                                                            }
                                                                                                                                                        });
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                }
                                                                                            } else { // 내가 단 댓글이 있으면
                                                                                                Map<String, Object> userData = new HashMap<>(); // 업데이트 할 데이터 만들기
                                                                                                if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.profileImg);
                                                                                                if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);

                                                                                                if (categorySnapshot2.getKey().equals("free")) commentSuccessFree++;
                                                                                                else if (categorySnapshot2.getKey().equals("review")) commentSuccessReview++;
                                                                                                else if (categorySnapshot2.getKey().equals("tip")) commentSuccessTip++;

                                                                                                // 댓글 업데이트하기
                                                                                                myCommentSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void unused) {
                                                                                                        if ((categorySnapshot2.getKey().equals("free") && (commentFailFree + commentSuccessFree) == categorySnapshot2.getChildrenCount() && commentFinishFree == 0) || (categorySnapshot2.getKey().equals("review") && (commentFailReview + commentSuccessReview) == categorySnapshot2.getChildrenCount() && commentFinishReview == 0) || (categorySnapshot2.getKey().equals("tip") && (commentFailTip + commentSuccessTip) == categorySnapshot2.getChildrenCount() && commentFinishTip == 0)) { // 댓글이 있는 모든 게시물을 다 확인하였으면
                                                                                                            if (categorySnapshot2.getKey().equals("free")) commentFinishFree++;
                                                                                                            else if (categorySnapshot2.getKey().equals("review")) commentFinishReview++;
                                                                                                            else if (categorySnapshot2.getKey().equals("tip")) commentFinishTip++;
                                                                                                            if (commentFinishFree + commentFinishReview + commentFinishTip == categoryNum) {
                                                                                                                // 내가 작성한 매칭 게시물 찾기
                                                                                                                db.getReference("Matching/userRequests").orderByChild("userId").equalTo(UserInfo.userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                                                                                                        if (task.isSuccessful()) {
                                                                                                                            DataSnapshot matchingSnapshot = task.getResult();
                                                                                                                            if (!matchingSnapshot.exists()) { // 내가 쓴 매칭 게시물이 없다면 (전문가는 무조건 이곳에 해당)
                                                                                                                                // 내 채팅 찾기
                                                                                                                                db.getReference("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                    @Override
                                                                                                                                    public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                        if (!chatSnapshot.exists()) { // 나의 채팅이 존재하지 않으면
                                                                                                                                            Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                            getActivity().finish();
                                                                                                                                        } else {
                                                                                                                                            chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                                                @Override
                                                                                                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                    // 채팅방 한개 업데이트하기
                                                                                                                                                    chatCount++;
                                                                                                                                                    myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onSuccess(Void unused) {
                                                                                                                                                            if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                                                                // 끝내기
                                                                                                                                                                Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                                                getActivity().finish();
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    });
                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                                            } else { // 내가 쓴 매칭 게시물이 있다면
                                                                                                                                matchingSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 내가 쓴 매칭 게시물 하나씩 가져오기
                                                                                                                                    @Override
                                                                                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                        // 글 하나 하나 정보 새로 업데이트
                                                                                                                                        matchingSuccessCount++;
                                                                                                                                        snapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                            @Override
                                                                                                                                            public void onSuccess(Void unused) {
                                                                                                                                                if (matchingSuccessCount == matchingSnapshot.getChildrenCount()) { // 매칭 글 다 업데이트하였다면
                                                                                                                                                    // 나의 채팅방 업데이트 하기
                                                                                                                                                    db.getReference().child("Chat").orderByChild("users/" + UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                                            if (!chatSnapshot.exists()) { // 나의 채팅방이 존재하지 않으면
                                                                                                                                                                Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                                                getActivity().finish();
                                                                                                                                                            } else { // 나의 채팅방이 존재하면
                                                                                                                                                                chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // 채팅방 하나 하나 가져오기
                                                                                                                                                                    @Override
                                                                                                                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                                        // 채팅방 한개 업데이트하기
                                                                                                                                                                        chatCount++;
                                                                                                                                                                        myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                            @Override
                                                                                                                                                                            public void onSuccess(Void unused) {
                                                                                                                                                                                if (chatCount == chatSnapshot.getChildrenCount()) { // 업데이트 다 하였다면
                                                                                                                                                                                    // 끝내기
                                                                                                                                                                                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                                                                                                    getActivity().finish();
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                        });
                                                                                                                                                                    }

                                                                                                                                                                    @Override
                                                                                                                                                                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                                                    }

                                                                                                                                                                    @Override
                                                                                                                                                                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                                                                    }

                                                                                                                                                                    @Override
                                                                                                                                                                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                                                    }

                                                                                                                                                                    @Override
                                                                                                                                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                                                                    }
                                                                                                                                                                });
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    });
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        });
                                                                                                                                    }

                                                                                                                                    @Override
                                                                                                                                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                    }

                                                                                                                                    @Override
                                                                                                                                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                                    }

                                                                                                                                    @Override
                                                                                                                                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                                                                                    }

                                                                                                                                    @Override
                                                                                                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                                                                                    }
                                                                                                                                });
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                            }

                                                                            @Override
                                                                            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                            }

                                                                            @Override
                                                                            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                            }
                                                                        });
                                                                    }

                                                                    @Override
                                                                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                    }

                                                                    @Override
                                                                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                                                    }

                                                                    @Override
                                                                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                            }

                                            @Override
                                            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            });
                        }

                        @Override
                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}
