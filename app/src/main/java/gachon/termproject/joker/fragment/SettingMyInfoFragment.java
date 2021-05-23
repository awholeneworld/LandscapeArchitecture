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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
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
import gachon.termproject.joker.activity.CheckPasswordActivity;
import gachon.termproject.joker.activity.MainActivity;
import gachon.termproject.joker.activity.WritePostActivity;
import gachon.termproject.joker.activity.WriteReviewPostExpertListActivity;
import gachon.termproject.joker.adapter.CommunityPagerAdapter;

public class SettingMyInfoFragment extends Fragment {

    private View view;
    private CheckBox SU, IC, DJ, GJ, DG, US, BS, JJ, GG, GW, CB, CN, GB, GN, JB, JN, SJ;
    private StorageReference storageReference;
    private ArrayList<String> locationSelected;
    private ArrayList<String> location;
    private ImageView profileImg;
    private TextView profileChangeImage;
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
    private int count = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_myinfo_fragment, container, false);
        // 레이아웃 가져오기
        profileChangeImage = view.findViewById(R.id.profileChangeImage);
        profileImg = view.findViewById(R.id.profileImage);
        EditText email = view.findViewById(R.id.setting_email);
        nickname = view.findViewById(R.id.setting_nickname);
        introMsg = view.findViewById(R.id.setting_message);
        checkNickname = view.findViewById(R.id.duplicateCheck);
        save = view.findViewById(R.id.setting_save_button);
        resetPwd = view.findViewById(R.id.setting_password_button);
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

        // 프사 설정
        profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        profileImg.setClipToOutline(true);
        if (!UserInfo.profileImg.equals("None"))
            Glide.with(getContext()).load(UserInfo.profileImg).into(profileImg);

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
        profileChangeImage.setOnClickListener(new View.OnClickListener() {
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
        }).addOnCompleteListener(new OnCompleteListener<Uri>() { // URL 다운 성공 시
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) { // URL을 DB에 있는 계정 정보에 업데이트
                    Uri downloadUrl = task.getResult();
                    String url = downloadUrl.toString();

                    Map<String, Object> dataToUpdate = new HashMap<>();
                    dataToUpdate.put("profileImg", url);
                    if (flag_nickname_check == 1) dataToUpdate.put("nickname", nickname.getText().toString());
                    if (flag_location == 1) dataToUpdate.put("location", locationSelected);
                    if (flag_message == 1) dataToUpdate.put("introduction", messageEdited);

                    FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update(dataToUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            UserInfo.profileImg = url;
                            Glide.with(getContext()).load(UserInfo.profileImg).into(MyInfoFragment.profileImg);

                            if (flag_nickname_check == 1) {
                                UserInfo.nickname = nicknameEdited;
                                MyInfoFragment.nickname.setText(nicknameEdited);

                            }
                            if (flag_location == 1) {
                                UserInfo.location = locationSelected;

                                String locationStr = "";
                                for (String item : locationSelected) {
                                    locationStr += item + " ";
                                }
                                MyInfoFragment.location.setText(locationStr);
                            }
                            if (flag_message == 1 ) {
                                UserInfo.introduction = messageEdited;
                                MyInfoFragment.intro.setText(messageEdited);
                            }

                            if (MainActivity.userPostsIdList.size() == 0) {
                                Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            } else {
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference dbRef = db.getReference("Posts");

                                dbRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        final long categoryNum = dataSnapshot.getChildrenCount();
                                        dbRef.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                snapshot.getRef().orderByChild("userId").equalTo(UserInfo.userId).addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                        Map<String, Object> userData = new HashMap<>();
                                                        userData.put("profileImg", url);
                                                        if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);

                                                        snapshot.getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                count++;
                                                                if (count == categoryNum) {
                                                                    count = 0;

                                                                    db.getReference("Matching/userRequests").orderByChild("userId").equalTo(UserInfo.userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                DataSnapshot snapshot = task.getResult();
                                                                                if (snapshot.getValue() != null) {
                                                                                    snapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                        @Override
                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                            Map<String, Object> userData = new HashMap<>();
                                                                                            userData.put("profileImg", url);
                                                                                            if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);

                                                                                            snapshot.getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                                    getActivity().finish();
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
                                                                                } else {
                                                                                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                                                    getActivity().finish();
                                                                                }
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
                                });
                            }
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
                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
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
                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
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

                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
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

                    Toast.makeText(getContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        } else
            updateChangesWithImg();
    }
}
