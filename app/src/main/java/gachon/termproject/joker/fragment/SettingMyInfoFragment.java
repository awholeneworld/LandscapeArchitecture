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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import gachon.termproject.joker.activity.CheckPasswordActivity;

public class SettingMyInfoFragment extends Fragment {
    private View view;
    private CheckBox SU, IC, DJ, GJ, DG, US, BS, JJ, GG, GW, CB, CN, GB, GN, JB, JN, SJ;
    private StorageReference storageReference;
    private ArrayList<String> locationSelected;
    private ArrayList<String> location;
    private ImageView profileImg;
    private ImageView changeProfileImageBack;
    private EditText email;
    private EditText nickname;
    private EditText introMsg;
    private Button checkNickname;
    private Button save;
    private Button resetPwd;
    private Uri file;
    private String nicknameEdited;
    private String messageEdited;
    private String userId = UserInfo.getUserId();
    private String userEmail = UserInfo.getEmail();
    private String userNickname = UserInfo.getNickname();
    private String userProfileImg = UserInfo.getProfileImg();
    private String userIntro = UserInfo.getIntroduction();
    private ArrayList<String> userLocation = UserInfo.getLocation();
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
    private int requestsCount = 0;
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
    private int commentsNumFree = 0;
    private int commentsNumReview = 0;
    private int commentsNumTip = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myinfo_setting, container, false);
        
        // ???????????? ????????????
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

        // ?????? ??????
        profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        profileImg.setClipToOutline(true);
        if (!userProfileImg.equals("None"))
            Glide.with(getContext()).load(userProfileImg).into(profileImg);

        changeProfileImageBack.setBackground(new ShapeDrawable(new OvalShape()));
        changeProfileImageBack.setClipToOutline(true);

        // ????????? ??????
        email.setText(userEmail);
        email.setTextColor(Color.parseColor("#000000"));

        // ????????? ??????
        nicknameEdited = userNickname;
        nickname.setText(nicknameEdited);

        // ?????? ????????? ??????
        introMsg.setText(userIntro);

        // ?????? ??????
        setLocation();

        // ?????? ????????? ??? ????????? ?????? ?????? ????????? ??????
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "????????? ???????????????."), 0);
            }
        });

        changeProfileImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "????????? ???????????????."), 0);
            }
        });

        // ????????? ???????????? ?????? ????????? ???
        checkNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nicknameCheck();
            }
        });

        // ?????? ????????? ???
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                save.setEnabled(false);
                saveChanges();
            }
        });

        // ???????????? ?????? ????????? ???
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
            case android.R.id.home:{ //toolbar??? back??? ????????? ??? ??????
                getActivity().finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data != null && data.getData() != null) { // ????????? ?????? ??????????????????
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
        //???????????? ????????????
        String temp = nickname.getText().toString();

        if (temp.length() == 0) {
            nickname.setText(userNickname);
            nickname.setEnabled(false);
            checkNickname.setEnabled(false);
            Toast.makeText(getContext(), "????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        }
        else if (temp.length() > 6) {
            nickname.setText(userNickname);
            nickname.setEnabled(false);
            checkNickname.setEnabled(false);
            Toast.makeText(getContext(), "???????????? 6??? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        }
        else if (temp.equals(userNickname)) {
            nickname.setEnabled(false);
            checkNickname.setEnabled(false);
            Toast.makeText(getContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        }
        else { //???????????????????????? ???????????? ????????? ????????? ??????!!!
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
                                Toast.makeText(getContext(), "????????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                                break;
                            } else if (i == list.size() - 1) {
                                flag_nickname_check++;
                                nicknameEdited = temp;
                                nickname.setEnabled(false);
                                checkNickname.setEnabled(false);
                                Toast.makeText(getContext(), "?????? ????????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }
    }

    public void setLocation() {
        location = userLocation;

        if (location.contains("??????")) SU.setChecked(true);
        if (location.contains("??????")) IC.setChecked(true);
        if (location.contains("??????")) DJ.setChecked(true);
        if (location.contains("??????")) GJ.setChecked(true);
        if (location.contains("??????")) DG.setChecked(true);
        if (location.contains("??????")) US.setChecked(true);
        if (location.contains("??????")) BS.setChecked(true);
        if (location.contains("??????")) JJ.setChecked(true);
        if (location.contains("??????")) GG.setChecked(true);
        if (location.contains("??????")) GW.setChecked(true);
        if (location.contains("??????")) CB.setChecked(true);
        if (location.contains("??????")) CN.setChecked(true);
        if (location.contains("??????")) GB.setChecked(true);
        if (location.contains("??????")) GN.setChecked(true);
        if (location.contains("??????")) JB.setChecked(true);
        if (location.contains("??????")) JN.setChecked(true);
        if (location.contains("??????")) SJ.setChecked(true);
    }

    public ArrayList<String> checkLocation() {
        //????????? ????????? ????????? ?????????
        ArrayList<String> location = new ArrayList<>();

        if(SU.isChecked()) location.add("??????");
        if(IC.isChecked()) location.add("??????");
        if(DJ.isChecked()) location.add("??????");
        if(GJ.isChecked()) location.add("??????");
        if(DG.isChecked()) location.add("??????");
        if(US.isChecked()) location.add("??????");
        if(BS.isChecked()) location.add("??????");
        if(JJ.isChecked()) location.add("??????");
        if(GG.isChecked()) location.add("??????");
        if(GW.isChecked()) location.add("??????");
        if(CB.isChecked()) location.add("??????");
        if(CN.isChecked()) location.add("??????");
        if(GB.isChecked()) location.add("??????");
        if(GN.isChecked()) location.add("??????");
        if(JB.isChecked()) location.add("??????");
        if(JN.isChecked()) location.add("??????");
        if(SJ.isChecked()) location.add("??????");

        return location;
    }

    public void saveChanges() {
        int finishCount = 0;

        // ?????? ??????????
        if (flag_profileImg_change == 1) finishCount++;

        // ????????? ?????? ??????????
        if (flag_nickname_check == 0  && nicknameEdited.equals(userNickname)) {
            flag_nickname_check = 0;
        } else if (flag_nickname_check == 0) {
            save.setEnabled(true);
            Toast.makeText(getContext(), "????????? ?????? ????????? ????????????", Toast.LENGTH_SHORT).show();
            return;
        }

        finishCount++;

        // ?????? ?????? ??????????
        locationSelected = checkLocation();
        if (locationSelected.isEmpty()) {
            save.setEnabled(true);
            Toast.makeText(getContext(), "????????? ?????? ?????? ??????????????????", Toast.LENGTH_SHORT).show();
            return;
        } else if (!locationSelected.isEmpty()) // ?????? ???????????????
            if (!location.equals(locationSelected)) // ?????????
                flag_location++;

        finishCount++;

        // ?????? ????????? ?????? ??????????
        messageEdited = introMsg.getText().toString();
        if (messageEdited.length() > 30) {
            save.setEnabled(true);
            Toast.makeText(getContext(), "?????? ???????????? 30??? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!messageEdited.equals(userIntro))
            flag_message++;

        finishCount++;

        // ?????? ????????? ??? ??? 16?????? = 8??????(?????? ??????X) + 8??????(?????? ??????O)
        if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 0 && flag_message == 0) {
            Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 0 && flag_message == 1) { // ?????? ????????? ????????? ????????? ??????
            FirebaseFirestore.getInstance().collection("users").document(userId).update("introduction", messageEdited).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.setIntroduction(messageEdited);
                    MyInfoFragment.intro.setText(messageEdited);
                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 1 && flag_message == 0) { // ????????? ????????? ??????
            FirebaseFirestore.getInstance().collection("users").document(userId).update("location", locationSelected).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.setLocation(locationSelected);

                    String locationStr = "";
                    for (String item : locationSelected) {
                        locationStr += item + " ";
                    }
                    MyInfoFragment.location.setText(locationStr);

                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 0 && flag_message == 0) { // ???????????? ????????? ??????
            FirebaseFirestore.getInstance().collection("users").document(userId).update("nickname", nicknameEdited).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.setNickname(nicknameEdited);
                    MyInfoFragment.nickname.setText(nicknameEdited);
                    dbUpdate();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 1 && flag_message == 1) { // ????????? ?????? ????????? ????????? ??????
            Map<String, Object> dataToUpdate = new HashMap<>();
            dataToUpdate.put("location", locationSelected);
            dataToUpdate.put("introduction", messageEdited);

            FirebaseFirestore.getInstance().collection("users").document(userId).update(dataToUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.setLocation(locationSelected);
                    UserInfo.setIntroduction(messageEdited);

                    String locationStr = "";
                    for (String item : locationSelected) {
                        locationStr += item + " ";
                    }
                    MyInfoFragment.location.setText(locationStr);
                    MyInfoFragment.intro.setText(messageEdited);

                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 0 && flag_message == 1) { // ???????????? ?????? ????????? ????????? ??????
            Map<String, Object> dataToUpdate = new HashMap<>();
            dataToUpdate.put("nickname", nicknameEdited);
            dataToUpdate.put("introduction", messageEdited);

            FirebaseFirestore.getInstance().collection("users").document(userId).update(dataToUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.setNickname(nicknameEdited);
                    UserInfo.setIntroduction(messageEdited);
                    MyInfoFragment.nickname.setText(nicknameEdited);
                    MyInfoFragment.intro.setText(messageEdited);
                    dbUpdate();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 1 && flag_message == 0) { // ???????????? ?????? ????????? ????????? ??????
            Map<String, Object> dataToUpdate = new HashMap<>();
            dataToUpdate.put("nickname", nicknameEdited);
            dataToUpdate.put("location", locationSelected);

            FirebaseFirestore.getInstance().collection("users").document(userId).update(dataToUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.setNickname(nicknameEdited);
                    UserInfo.setLocation(locationSelected);

                    String locationStr = "";
                    for (String item : locationSelected) {
                        locationStr += item + " ";
                    }
                    MyInfoFragment.nickname.setText(nicknameEdited);
                    MyInfoFragment.location.setText(locationStr);

                    dbUpdate();
                }
            });
        } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 1 && flag_message == 1) { // ???????????? ??????, ?????? 3?????? ????????? ??????
            Map<String, Object> dataToUpdate = new HashMap<>();
            dataToUpdate.put("nickname", nicknameEdited);
            dataToUpdate.put("location", locationSelected);
            dataToUpdate.put("introduction", messageEdited);

            FirebaseFirestore.getInstance().collection("users").document(userId).update(dataToUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    UserInfo.setNickname(nicknameEdited);
                    UserInfo.setLocation(locationSelected);
                    UserInfo.setIntroduction(messageEdited);

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

    // ????????? ?????????
    private void updateChangesWithImg(){
        storageReference = FirebaseStorage.getInstance().getReference().child("profileImages/" + userId);
        storageReference.putFile(file).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return storageReference.getDownloadUrl(); // URL??? ????????? ????????? ??? ??????????????? ?????? ??????
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) { // URL ?????? ?????? ???
                    Uri downloadUrl = task.getResult();
                    String url = downloadUrl.toString();

                    UserInfo.setProfileImg(url);
                    Glide.with(getActivity()).load(url).override(1000).thumbnail(0.5f).into(MyInfoFragment.profileImg);
                    if (flag_nickname_check == 1) {
                        UserInfo.setNickname(nicknameEdited);
                        MyInfoFragment.nickname.setText(nicknameEdited);
                    }
                    if (flag_location == 1) {
                        UserInfo.setLocation(locationSelected);
                        String locationStr = "";
                        for (String item : UserInfo.getLocation()) {
                            locationStr += item + " ";
                        }
                        MyInfoFragment.location.setText(locationStr);
                    }
                    if (flag_message == 1 ) {
                        UserInfo.setIntroduction(messageEdited);
                        MyInfoFragment.intro.setText(messageEdited);
                    }

                    Map<String, Object> dataToUpdate = new HashMap<>();
                    dataToUpdate.put("profileImg", url);
                    if (flag_nickname_check == 1) dataToUpdate.put("nickname", nickname.getText().toString());
                    if (flag_location == 1) dataToUpdate.put("location", locationSelected);
                    if (flag_message == 1) dataToUpdate.put("introduction", messageEdited);

                    // ?????? ?????? ???????????? DB ????????????
                    FirebaseFirestore.getInstance().collection("users").document(userId).update(dataToUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            dbUpdate();
                        }
                    });
                }
            }
        });
    }

    // ????????? ?????????, ??????, ?????????, ?????? ??????, ????????? ??????, ?????? ??????, ????????? ??? ?????? ????????????
    public void dbUpdate() {
        finishCount = 0;
        FirebaseDatabase db = FirebaseDatabase.getInstance(); // DB
        DatabaseReference postsRef = db.getReference("Posts"); // ????????? DB

        postsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() { // ????????? DB ??????
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) { // DB??? ???????????? ???????????? ????????? ?????? ??????
                    Map<String, Object> userData = new HashMap<>(); // ???????????? ??? ????????? ?????????
                    if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.getProfileImg());
                    if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);
                    if (flag_location == 1) userData.put("location", locationSelected);
                    if (flag_message == 1) userData.put("intro", introMsg);

                    // ?????? ????????? ?????? ????????? ??????
                    db.getReference("Matching/userRequests").addChildEventListener(new ChildEventListener() { // ?????? ????????? ??????????????? ????????????
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot matchSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                            requestsCount++;
                            if (matchSnapshot.hasChild("requests/" + userId)) {
                                successCount++;
                                matchSnapshot.child("requests/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (failCount + successCount == requestsCount) {
                                            // ??? ?????? ??????
                                            db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                @Override
                                                public void onSuccess(DataSnapshot chatSnapshot) {
                                                    if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                        getActivity().finish();
                                                    } else {
                                                        chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                            @Override
                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                // ????????? ?????? ?????? ??????????????????

                                                                if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                    chatCount++;
                                                                    myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                            if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                // ?????????
                                                                                Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                getActivity().finish();
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
                                                }
                                            });
                                        }
                                    }
                                });
                            } else {
                                failCount++;
                                if (failCount + successCount == requestsCount) {
                                    db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                        @Override
                                        public void onSuccess(DataSnapshot chatSnapshot) {
                                            if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                getActivity().finish();
                                            } else {
                                                chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                        // ????????? ?????? ?????? ??????????????????

                                                        if (myChatSnapshot.hasChild("users/" + userId)) {
                                                            chatCount++;
                                                            myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                    if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                        // ?????????
                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                        getActivity().finish();
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
                                        }
                                    });
                                }
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

                    // ????????? ????????? ????????? ???????????? ?????? ?????? ????????? ????????????
                    db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot matchSnapshot) {
                            if (!matchSnapshot.exists()) { // ???????????? ????????? ??????

                            } else { // ???????????? ????????? ??????
                                matchSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ?????? ??? ?????? ????????? ????????? ????????????
                                    @Override
                                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                        // ??? ?????? ?????? ?????? ?????? ????????????
                                        matchingSuccessCount++;
                                        snapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                if (matchingSuccessCount == matchSnapshot.getChildrenCount()) { // ?????? ??? ??? ????????????????????????
                                                    // ?????? ????????? ???????????? ??????
                                                    db.getReference().child("Chat").orderByChild("users/" + userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DataSnapshot chatSnapshot) {
                                                            if (!chatSnapshot.exists()) { // ?????? ???????????? ???????????? ?????????
                                                                Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                getActivity().finish();
                                                            } else { // ?????? ???????????? ????????????
                                                                chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ????????? ?????? ?????? ????????????
                                                                    @Override
                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                        // ????????? ?????? ??????????????????
                                                                        chatCount++;
                                                                        myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                    // ?????????
                                                                                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
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

                                                                // ????????? ????????? ????????? ???????????? ?????? ?????? ?????????
                                                                db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DataSnapshot matchSnapshot) {
                                                                        if (!matchSnapshot.exists()) {

                                                                        } else {

                                                                        }
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
                    });
                } else { // DB??? ???????????? ???????????? ???????????? ?????? ??????
                    Map<String, Object> userData = new HashMap<>(); // ???????????? ??? ????????? ?????????
                    if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.getProfileImg());
                    if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);
                    if (flag_location == 1) userData.put("location", locationSelected);
                    if (flag_message == 1) userData.put("intro", introMsg);

                    final long categoryNum = dataSnapshot.getChildrenCount(); // ?????? ?????? ?????? ????????? ???????????? ??? ?????????

                    dataSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ??? ?????? ????????? DB??? ???????????? ???????????? ??? ???????????? ???????????? ????????? ?????????
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot categorySnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                            // ?????? ??? ???????????? ????????? ???????????? ????????????. ??? ???????????? ??????->??????->?????? ????????? ????????????.
                            categorySnapshot.getRef().orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() { // ??? ?????????????????? ?????? ??? ?????? ????????? ??????
                                @Override
                                public void onSuccess(DataSnapshot postsSnapshot) {
                                    if (!postsSnapshot.exists()) { // ?????? ??? ?????? ?????? ??????????????? ????????? ???????????? ????????? fail count ?????? ??????
                                        failCount++;
                                        if (failCount == categoryNum) { // ?????? ?????? ?????? ??????????????? ?????? ?????? ????????? ?????????
                                            failCount = 0;
                                            postsRef.getRef().addChildEventListener(new ChildEventListener() { // ?????? ??????
                                                @Override
                                                public void onChildAdded(@NonNull @NotNull DataSnapshot categorySnapshot2, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                    categorySnapshot2.getRef().addChildEventListener(new ChildEventListener() { // ?????? ????????? ????????? ????????? ????????? ?????? ????????? ???????????? ??????
                                                        @Override
                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot commentsSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                            if (!commentsSnapshot.hasChild("comments")) { // ???????????? ????????? ?????????
                                                                if (categorySnapshot2.getKey().equals("free")) commentFailFree++;
                                                                else if (categorySnapshot2.getKey().equals("review")) commentFailReview++;
                                                                else if (categorySnapshot2.getKey().equals("tip")) commentFailTip++;

                                                                if ((categorySnapshot2.getKey().equals("free") && commentFailFree + commentSuccessFree == categorySnapshot2.getChildrenCount()) || (categorySnapshot2.getKey().equals("review") && commentFailReview + commentSuccessReview == categorySnapshot2.getChildrenCount()) || (categorySnapshot2.getKey().equals("tip") && commentFailTip + commentSuccessTip == categorySnapshot2.getChildrenCount())) { // ?????? ??????????????? ????????? ?????????
                                                                    if (categorySnapshot2.getKey().equals("free")) finishCount++;
                                                                    else if (categorySnapshot2.getKey().equals("review")) finishCount++;
                                                                    else if (categorySnapshot2.getKey().equals("tip")) finishCount++;
                                                                    if (finishCount == categoryNum) {
                                                                        finishCount = 0;
                                                                        // ?????? ????????? ?????? ????????? ??????
                                                                        db.getReference("Matching/userRequests").addChildEventListener(new ChildEventListener() { // ?????? ????????? ??????????????? ????????????
                                                                            @Override
                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot matchSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                requestsCount++;
                                                                                if (matchSnapshot.hasChild("requests/" + userId)) {
                                                                                    successCount++;
                                                                                    matchSnapshot.child("requests/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                            if (failCount + successCount == requestsCount) {
                                                                                                // ??? ?????? ??????
                                                                                                db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                        if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                            Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                            getActivity().finish();
                                                                                                        } else {
                                                                                                            chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                @Override
                                                                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                    // ????????? ?????? ?????? ??????????????????

                                                                                                                    if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                        chatCount++;
                                                                                                                        myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                    // ?????????
                                                                                                                                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                    getActivity().finish();
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
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    failCount++;
                                                                                    if (failCount + successCount == requestsCount) {
                                                                                        db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                            @Override
                                                                                            public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                    getActivity().finish();
                                                                                                } else {
                                                                                                    chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                        @Override
                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                            // ????????? ?????? ?????? ??????????????????

                                                                                                            if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                chatCount++;
                                                                                                                myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                        if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                            // ?????????
                                                                                                                            Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                            getActivity().finish();
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
                                                                                            }
                                                                                        });
                                                                                    }
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

                                                                        // ????????? ????????? ????????? ???????????? ?????? ?????? ????????? ????????????
                                                                        db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                if (!matchSnapshot.exists()) { // ???????????? ????????? ??????

                                                                                } else { // ???????????? ????????? ??????
                                                                                    matchSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ?????? ??? ?????? ????????? ????????? ????????????
                                                                                        @Override
                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                            // ??? ?????? ?????? ?????? ?????? ????????????
                                                                                            matchingSuccessCount++;
                                                                                            snapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    if (matchingSuccessCount == matchSnapshot.getChildrenCount()) { // ?????? ??? ??? ????????????????????????
                                                                                                        // ?????? ????????? ???????????? ??????
                                                                                                        db.getReference().child("Chat").orderByChild("users/" + userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                if (!chatSnapshot.exists()) { // ?????? ???????????? ???????????? ?????????
                                                                                                                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                    getActivity().finish();
                                                                                                                } else { // ?????? ???????????? ????????????
                                                                                                                    chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ????????? ?????? ?????? ????????????
                                                                                                                        @Override
                                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                            // ????????? ?????? ??????????????????
                                                                                                                            chatCount++;
                                                                                                                            myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                    if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                        // ?????????
                                                                                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
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

                                                                                                                    // ????????? ????????? ????????? ???????????? ?????? ?????? ?????????
                                                                                                                    db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                                                            if (!matchSnapshot.exists()) {

                                                                                                                            } else {

                                                                                                                            }
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
                                                                        });
                                                                    }
                                                                }
                                                            }
                                                            else { // ???????????? ????????? ???????????? ?????????
                                                                Map<String, Object> userData = new HashMap<>(); // ???????????? ??? ????????? ?????????
                                                                if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.getProfileImg());
                                                                if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);
                                                                if (flag_location == 1) userData.put("location", locationSelected);
                                                                if (flag_message == 1) userData.put("intro", introMsg);

                                                                commentsSnapshot.getRef().child("comments").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() { // ?????? ?????? ????????? ??????
                                                                    @Override
                                                                    public void onSuccess(DataSnapshot myCommentSnapshot) {
                                                                        if (!myCommentSnapshot.exists()) { // ?????? ???????????? ?????? ??? ????????? ?????????
                                                                            if (categorySnapshot2.getKey().equals("free")) commentFailFree++;
                                                                            else if (categorySnapshot2.getKey().equals("review")) commentFailReview++;
                                                                            else if (categorySnapshot2.getKey().equals("tip")) commentFailTip++;

                                                                            if ((categorySnapshot2.getKey().equals("free") && commentFailFree + commentSuccessFree == categorySnapshot2.getChildrenCount()) || (categorySnapshot2.getKey().equals("review") && commentFailReview + commentSuccessReview == categorySnapshot2.getChildrenCount()) || (categorySnapshot2.getKey().equals("tip") && commentFailTip + commentSuccessTip == categorySnapshot2.getChildrenCount())) { // ?????? ??? ????????? ????????? ?????????
                                                                                finishCount++;
                                                                                if (finishCount == categoryNum) {
                                                                                    // ?????? ????????? ?????? ????????? ??????
                                                                                    db.getReference("Matching/userRequests").addChildEventListener(new ChildEventListener() { // ?????? ????????? ??????????????? ????????????
                                                                                        @Override
                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot matchSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                            requestsCount++;
                                                                                            if (matchSnapshot.hasChild("requests/" + userId)) {
                                                                                                successCount++;
                                                                                                matchSnapshot.child("requests/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                        if (failCount + successCount == requestsCount) {
                                                                                                            // ??? ?????? ??????
                                                                                                            db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                    if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                        getActivity().finish();
                                                                                                                    } else {
                                                                                                                        chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                            @Override
                                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                // ????????? ?????? ?????? ??????????????????

                                                                                                                                if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                                    chatCount++;
                                                                                                                                    myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                        @Override
                                                                                                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                            if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                // ?????????
                                                                                                                                                Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                getActivity().finish();
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
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            } else {
                                                                                                failCount++;
                                                                                                if (failCount + successCount == requestsCount) {
                                                                                                    db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                            if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                                Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                getActivity().finish();
                                                                                                            } else {
                                                                                                                chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                    @Override
                                                                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                        // ????????? ?????? ?????? ??????????????????

                                                                                                                        if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                            chatCount++;
                                                                                                                            myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                    if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                        // ?????????
                                                                                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                        getActivity().finish();
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
                                                                                                        }
                                                                                                    });
                                                                                                }
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

                                                                                    // ????????? ????????? ????????? ???????????? ?????? ?????? ????????? ????????????
                                                                                    db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                            if (!matchSnapshot.exists()) { // ???????????? ????????? ??????

                                                                                            } else { // ???????????? ????????? ??????
                                                                                                matchSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ?????? ??? ?????? ????????? ????????? ????????????
                                                                                                    @Override
                                                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                        // ??? ?????? ?????? ?????? ?????? ????????????
                                                                                                        matchingSuccessCount++;
                                                                                                        snapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void unused) {
                                                                                                                if (matchingSuccessCount == matchSnapshot.getChildrenCount()) { // ?????? ??? ??? ????????????????????????
                                                                                                                    // ?????? ????????? ???????????? ??????
                                                                                                                    db.getReference().child("Chat").orderByChild("users/" + userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                            if (!chatSnapshot.exists()) { // ?????? ???????????? ???????????? ?????????
                                                                                                                                Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                getActivity().finish();
                                                                                                                            } else { // ?????? ???????????? ????????????
                                                                                                                                chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ????????? ?????? ?????? ????????????
                                                                                                                                    @Override
                                                                                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                        // ????????? ?????? ??????????????????
                                                                                                                                        chatCount++;
                                                                                                                                        myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                            @Override
                                                                                                                                            public void onSuccess(Void unused) {
                                                                                                                                                if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                    // ?????????
                                                                                                                                                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
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

                                                                                                                                // ????????? ????????? ????????? ???????????? ?????? ?????? ?????????
                                                                                                                                db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                    @Override
                                                                                                                                    public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                                                                        if (!matchSnapshot.exists()) {

                                                                                                                                        } else {

                                                                                                                                        }
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
                                                                                    });
                                                                                }
                                                                            }
                                                                        } else if (myCommentSnapshot.child("userId").getValue().equals(userId)){ // ?????? ??? ????????? ?????????
                                                                            // ?????? ??????????????????
                                                                            myCommentSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    if ((categorySnapshot2.getKey().equals("free") && (commentFailFree + commentSuccessFree) == categorySnapshot2.getChildrenCount() && commentFinishFree == 0) || (categorySnapshot2.getKey().equals("review") && (commentFailReview + commentSuccessReview) == categorySnapshot2.getChildrenCount() && commentFinishReview == 0) || (categorySnapshot2.getKey().equals("tip") && (commentFailTip + commentSuccessTip) == categorySnapshot2.getChildrenCount() && commentFinishTip == 0)) { // ????????? ?????? ?????? ???????????? ??? ??????????????????
                                                                                        if (categorySnapshot2.getKey().equals("free")) commentFinishFree++;
                                                                                        else if (categorySnapshot2.getKey().equals("review")) commentFinishReview++;
                                                                                        else if (categorySnapshot2.getKey().equals("tip")) commentFinishTip++;
                                                                                        if (commentFinishFree + commentFinishReview + commentFinishTip == categoryNum) {
                                                                                            // ?????? ????????? ?????? ????????? ??????
                                                                                            db.getReference("Matching/userRequests").addChildEventListener(new ChildEventListener() { // ?????? ????????? ??????????????? ????????????
                                                                                                @Override
                                                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot matchSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                    requestsCount++;
                                                                                                    if (matchSnapshot.hasChild("requests/" + userId)) {
                                                                                                        successCount++;
                                                                                                        matchSnapshot.child("requests/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                if (failCount + successCount == requestsCount) {
                                                                                                                    // ??? ?????? ??????
                                                                                                                    db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                            if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                                                Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                getActivity().finish();
                                                                                                                            } else {
                                                                                                                                chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                        // ????????? ?????? ?????? ??????????????????

                                                                                                                                        if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                                            chatCount++;
                                                                                                                                            myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                @Override
                                                                                                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                                    if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                        // ?????????
                                                                                                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                        getActivity().finish();
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
                                                                                                                        }
                                                                                                                    });
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                    } else {
                                                                                                        failCount++;
                                                                                                        if (failCount + successCount == requestsCount) {
                                                                                                            db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                    if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                        getActivity().finish();
                                                                                                                    } else {
                                                                                                                        chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                            @Override
                                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                // ????????? ?????? ?????? ??????????????????

                                                                                                                                if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                                    chatCount++;
                                                                                                                                    myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                        @Override
                                                                                                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                            if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                // ?????????
                                                                                                                                                Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                getActivity().finish();
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
                                                                                                                }
                                                                                                            });
                                                                                                        }
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

                                                                                            // ????????? ????????? ????????? ???????????? ?????? ?????? ????????? ????????????
                                                                                            db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                @Override
                                                                                                public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                                    if (!matchSnapshot.exists()) { // ???????????? ????????? ??????

                                                                                                    } else { // ???????????? ????????? ??????
                                                                                                        matchSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ?????? ??? ?????? ????????? ????????? ????????????
                                                                                                            @Override
                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                // ??? ?????? ?????? ?????? ?????? ????????????
                                                                                                                matchingSuccessCount++;
                                                                                                                snapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                        if (matchingSuccessCount == matchSnapshot.getChildrenCount()) { // ?????? ??? ??? ????????????????????????
                                                                                                                            // ?????? ????????? ???????????? ??????
                                                                                                                            db.getReference().child("Chat").orderByChild("users/" + userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                    if (!chatSnapshot.exists()) { // ?????? ???????????? ???????????? ?????????
                                                                                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                        getActivity().finish();
                                                                                                                                    } else { // ?????? ???????????? ????????????
                                                                                                                                        chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ????????? ?????? ?????? ????????????
                                                                                                                                            @Override
                                                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                // ????????? ?????? ??????????????????
                                                                                                                                                chatCount++;
                                                                                                                                                myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                                                        if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                            // ?????????
                                                                                                                                                            Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
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

                                                                                                                                        // ????????? ????????? ????????? ???????????? ?????? ?????? ?????????
                                                                                                                                        db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                            @Override
                                                                                                                                            public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                                                                                if (!matchSnapshot.exists()) {

                                                                                                                                                } else {

                                                                                                                                                }
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
// ????????? ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ?????????//
                                    else { // ?????? ??? ?????? ?????? ??????????????? ???????????? ????????? ??????
                                        Map<String, Object> userData = new HashMap<>(); // ???????????? ??? ????????? ?????????
                                        if (flag_profileImg_change == 1) userData.put("profileImg", UserInfo.getProfileImg());
                                        if (flag_nickname_check == 1) userData.put("nickname", nicknameEdited);
                                        if (flag_location == 1) userData.put("location", locationSelected);
                                        if (flag_message == 1) userData.put("intro", introMsg);

                                        postsSnapshot.getRef().orderByChild("userId").equalTo(userId).addChildEventListener(new ChildEventListener() { // ??? ??????????????? ?????? ??? ?????? ????????? ????????????
                                            @Override
                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myPostSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                // ??? ?????? ??????????????????
                                                if (postsSnapshot.getKey().equals("free") && myPostSnapshot.child("userId").getValue().toString().equals(userId)) postsCountFree++;
                                                else if (postsSnapshot.getKey().equals("review") && myPostSnapshot.child("userId").getValue().toString().equals(userId)) postsCountReview++;
                                                else if (postsSnapshot.getKey().equals("tip") && myPostSnapshot.child("userId").getValue().toString().equals(userId)) postsCountTip++;
                                                myPostSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        if ((postsSnapshot.getKey().equals("free") && postsCountFree == postsSnapshot.getChildrenCount() && finishCountFree == 0) || (postsSnapshot.getKey().equals("review") && postsCountReview == postsSnapshot.getChildrenCount() && finishCountReview == 0) || (postsSnapshot.getKey().equals("tip") && postsCountTip == postsSnapshot.getChildrenCount() && finishCountTip == 0)) {
                                                            if (postsSnapshot.getKey().equals("free")) finishCountFree++;
                                                            else if (postsSnapshot.getKey().equals("review")) finishCountReview++;
                                                            else if (postsSnapshot.getKey().equals("tip")) finishCountTip++;
                                                            if (failCount + finishCountFree + finishCountReview + finishCountTip == categoryNum) {
                                                                failCount = 0;
                                                                postsRef.getRef().addChildEventListener(new ChildEventListener() { // ???????????? ????????? ?????? ???????????? ?????? ?????? ??????
                                                                    @Override
                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot categorySnapshot2, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                        categorySnapshot2.getRef().addChildEventListener(new ChildEventListener() { // ????????? ????????? ????????? ?????? ????????? ???????????? ??????
                                                                            @Override
                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot postSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                if (categorySnapshot2.getKey().equals("free")) commentsNumFree++;
                                                                                else if (categorySnapshot2.getKey().equals("review")) commentsNumReview++;
                                                                                else if (categorySnapshot2.getKey().equals("tip")) commentsNumTip++;

                                                                                if (!postSnapshot.hasChild("comments")) { // ???????????? ????????? ?????????
                                                                                    if (categorySnapshot2.getKey().equals("free")) commentFailFree++;
                                                                                    else if (categorySnapshot2.getKey().equals("review")) commentFailReview++;
                                                                                    else if (categorySnapshot2.getKey().equals("tip")) commentFailTip++;

                                                                                    if ((categorySnapshot2.getKey().equals("free") && commentFailFree + commentSuccessFree == categorySnapshot2.getChildrenCount()) || (categorySnapshot2.getKey().equals("review") && commentFailReview + commentSuccessReview == categorySnapshot2.getChildrenCount()) || (categorySnapshot2.getKey().equals("tip") && commentFailTip + commentSuccessTip == categorySnapshot2.getChildrenCount())) { // ?????? ??????????????? ????????? ?????????
                                                                                        if (categorySnapshot2.getKey().equals("free")) commentFinishFree++;
                                                                                        else if (categorySnapshot2.getKey().equals("review")) commentFinishReview++;
                                                                                        else if (categorySnapshot2.getKey().equals("tip")) commentFinishTip++;

                                                                                        if (commentFinishFree + commentFinishReview + commentFinishTip == categoryNum) { // ?????? ?????? ???
                                                                                            // ?????? ????????? ?????? ????????? ??????
                                                                                            db.getReference("Matching/userRequests").addChildEventListener(new ChildEventListener() { // ?????? ????????? ??????????????? ????????????
                                                                                                @Override
                                                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot matchSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                    requestsCount++;
                                                                                                    if (matchSnapshot.hasChild("requests/" + userId)) {
                                                                                                        successCount++;
                                                                                                        matchSnapshot.child("requests/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                if (failCount + successCount == requestsCount) {
                                                                                                                    // ??? ?????? ??????
                                                                                                                    db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                            if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                                                Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                getActivity().finish();
                                                                                                                            } else {
                                                                                                                                chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                        // ????????? ?????? ?????? ??????????????????

                                                                                                                                        if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                                            chatCount++;
                                                                                                                                            myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                @Override
                                                                                                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                                    if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                        // ?????????
                                                                                                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                        getActivity().finish();
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
                                                                                                                        }
                                                                                                                    });
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                    } else {
                                                                                                        failCount++;
                                                                                                        if (failCount + successCount == requestsCount) {
                                                                                                            db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                    if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                        getActivity().finish();
                                                                                                                    } else {
                                                                                                                        chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                            @Override
                                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                // ????????? ?????? ?????? ??????????????????

                                                                                                                                if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                                    chatCount++;
                                                                                                                                    myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                        @Override
                                                                                                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                            if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                // ?????????
                                                                                                                                                Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                getActivity().finish();
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
                                                                                                                }
                                                                                                            });
                                                                                                        }
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

                                                                                            // ????????? ????????? ????????? ???????????? ?????? ?????? ????????? ????????????
                                                                                            db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                @Override
                                                                                                public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                                    if (!matchSnapshot.exists()) { // ???????????? ????????? ??????

                                                                                                    } else { // ???????????? ????????? ??????
                                                                                                        matchSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ?????? ??? ?????? ????????? ????????? ????????????
                                                                                                            @Override
                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                // ??? ?????? ?????? ?????? ?????? ????????????
                                                                                                                matchingSuccessCount++;
                                                                                                                snapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                        if (matchingSuccessCount == matchSnapshot.getChildrenCount()) { // ?????? ??? ??? ????????????????????????
                                                                                                                            // ?????? ????????? ???????????? ??????
                                                                                                                            db.getReference().child("Chat").orderByChild("users/" + userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                    if (!chatSnapshot.exists()) { // ?????? ???????????? ???????????? ?????????
                                                                                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                        getActivity().finish();
                                                                                                                                    } else { // ?????? ???????????? ????????????
                                                                                                                                        chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ????????? ?????? ?????? ????????????
                                                                                                                                            @Override
                                                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                // ????????? ?????? ??????????????????
                                                                                                                                                chatCount++;
                                                                                                                                                myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                                                        if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                            // ?????????
                                                                                                                                                            Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
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

                                                                                                                                        // ????????? ????????? ????????? ???????????? ?????? ?????? ?????????
                                                                                                                                        db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                            @Override
                                                                                                                                            public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                                                                                if (!matchSnapshot.exists()) {

                                                                                                                                                } else {

                                                                                                                                                }
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
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                } else { // ???????????? ????????? ???????????? ?????????
                                                                                    postSnapshot.getRef().child("comments").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() { // ?????? ?????? ????????? ??????
                                                                                        @Override
                                                                                        public void onSuccess(DataSnapshot commentSnapshot) {
                                                                                            if (!commentSnapshot.exists()) { // ?????? ???????????? ?????? ??? ????????? ?????????
                                                                                                if (categorySnapshot2.getKey().equals("free")) commentFailFree++;
                                                                                                else if (categorySnapshot2.getKey().equals("review")) commentFailReview++;
                                                                                                else if (categorySnapshot2.getKey().equals("tip")) commentFailTip++;

                                                                                                if ((categorySnapshot2.getKey().equals("free") && commentFailFree + commentSuccessFree == commentsNumFree && commentFinishFree == 0) || (categorySnapshot2.getKey().equals("review") && commentFailReview + commentSuccessReview == commentsNumReview && commentFinishReview == 0) || (categorySnapshot2.getKey().equals("tip") && commentFailTip + commentSuccessTip == commentsNumTip && commentFinishTip == 0)) { // ?????? ??? ????????? ????????? ?????????
                                                                                                    if (categorySnapshot2.getKey().equals("free")) commentFinishFree++;
                                                                                                    else if (categorySnapshot2.getKey().equals("review")) commentFinishReview++;
                                                                                                    else if (categorySnapshot2.getKey().equals("tip")) commentFinishTip++;
                                                                                                    if (commentFinishFree + commentFinishReview + commentFinishTip == categoryNum) {
                                                                                                        // ?????? ????????? ?????? ????????? ??????
                                                                                                        db.getReference("Matching/userRequests").addChildEventListener(new ChildEventListener() { // ?????? ????????? ??????????????? ????????????
                                                                                                            @Override
                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot matchSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                requestsCount++;
                                                                                                                if (matchSnapshot.hasChild("requests/" + userId)) {
                                                                                                                    successCount++;
                                                                                                                    matchSnapshot.child("requests/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                            if (failCount + successCount == requestsCount) {
                                                                                                                                // ??? ?????? ??????
                                                                                                                                db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                    @Override
                                                                                                                                    public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                        if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                                                            Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                            getActivity().finish();
                                                                                                                                        } else {
                                                                                                                                            chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                                                @Override
                                                                                                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                    // ????????? ?????? ?????? ??????????????????

                                                                                                                                                    if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                                                        chatCount++;
                                                                                                                                                        myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                            @Override
                                                                                                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                                                if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                                    // ?????????
                                                                                                                                                                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                                    getActivity().finish();
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
                                                                                                                                    }
                                                                                                                                });
                                                                                                                            }
                                                                                                                        }
                                                                                                                    });
                                                                                                                } else {
                                                                                                                    failCount++;
                                                                                                                    if (failCount + successCount == requestsCount) {
                                                                                                                        db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                                                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                    getActivity().finish();
                                                                                                                                } else {
                                                                                                                                    chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                                        @Override
                                                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                            // ????????? ?????? ?????? ??????????????????

                                                                                                                                            if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                                                chatCount++;
                                                                                                                                                myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                                        if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                            // ?????????
                                                                                                                                                            Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                            getActivity().finish();
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
                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
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

                                                                                                        // ????????? ????????? ????????? ???????????? ?????? ?????? ????????? ????????????
                                                                                                        db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                                                if (!matchSnapshot.exists()) { // ???????????? ????????? ??????

                                                                                                                } else { // ???????????? ????????? ??????
                                                                                                                    matchSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ?????? ??? ?????? ????????? ????????? ????????????
                                                                                                                        @Override
                                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                            // ??? ?????? ?????? ?????? ?????? ????????????
                                                                                                                            matchingSuccessCount++;
                                                                                                                            snapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                    if (matchingSuccessCount == matchSnapshot.getChildrenCount()) { // ?????? ??? ??? ????????????????????????
                                                                                                                                        // ?????? ????????? ???????????? ??????
                                                                                                                                        db.getReference().child("Chat").orderByChild("users/" + userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                            @Override
                                                                                                                                            public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                                if (!chatSnapshot.exists()) { // ?????? ???????????? ???????????? ?????????
                                                                                                                                                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                    getActivity().finish();
                                                                                                                                                } else { // ?????? ???????????? ????????????
                                                                                                                                                    chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ????????? ?????? ?????? ????????????
                                                                                                                                                        @Override
                                                                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                            // ????????? ?????? ??????????????????
                                                                                                                                                            chatCount++;
                                                                                                                                                            myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                @Override
                                                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                                                    if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                                        // ?????????
                                                                                                                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
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

                                                                                                                                                    // ????????? ????????? ????????? ???????????? ?????? ?????? ?????????
                                                                                                                                                    db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                                                                                            if (!matchSnapshot.exists()) {

                                                                                                                                                            } else {

                                                                                                                                                            }
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
                                                                                                        });
                                                                                                    }
                                                                                                }
                                                                                            } else { // ?????? ??? ????????? ?????????
                                                                                                // ?????? ??????????????????
                                                                                                commentSnapshot.getRef().orderByChild("userId").equalTo(userId).addChildEventListener(new ChildEventListener() {
                                                                                                    @Override
                                                                                                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                        snapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void unused) {
                                                                                                                if (categorySnapshot2.getKey().equals("free")) commentSuccessFree++;
                                                                                                                else if (categorySnapshot2.getKey().equals("review")) commentSuccessReview++;
                                                                                                                else if (categorySnapshot2.getKey().equals("tip")) commentSuccessTip++;

                                                                                                                if ((categorySnapshot2.getKey().equals("free") && commentFailFree + commentSuccessFree == commentsNumFree && commentFinishFree == 0) || (categorySnapshot2.getKey().equals("review") && commentFailReview + commentSuccessReview == commentsNumReview && commentFinishReview == 0) || (categorySnapshot2.getKey().equals("tip") && commentFailTip + commentSuccessTip == commentsNumTip && commentFinishTip == 0)) { // ????????? ?????? ?????? ???????????? ??? ??????????????????
                                                                                                                    if (categorySnapshot2.getKey().equals("free")) commentFinishFree++;
                                                                                                                    else if (categorySnapshot2.getKey().equals("review")) commentFinishReview++;
                                                                                                                    else if (categorySnapshot2.getKey().equals("tip")) commentFinishTip++;
                                                                                                                    if (commentFinishFree + commentFinishReview + commentFinishTip == categoryNum) { // ?????? ?????? ??????????????????
                                                                                                                        // ?????? ????????? ?????? ????????? ??????
                                                                                                                        db.getReference("Matching/userRequests").addChildEventListener(new ChildEventListener() { // ?????? ????????? ??????????????? ????????????
                                                                                                                            @Override
                                                                                                                            public void onChildAdded(@NonNull @NotNull DataSnapshot matchSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                requestsCount++;
                                                                                                                                if (matchSnapshot.hasChild("requests/" + userId)) {
                                                                                                                                    successCount++;
                                                                                                                                    matchSnapshot.child("requests/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                        @Override
                                                                                                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                            if (failCount + successCount == requestsCount) {
                                                                                                                                                // ??? ?????? ??????
                                                                                                                                                db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                                        if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                                                                            Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                            getActivity().finish();
                                                                                                                                                        } else {
                                                                                                                                                            chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                                                                @Override
                                                                                                                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                                    // ????????? ?????? ?????? ??????????????????

                                                                                                                                                                    if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                                                                        chatCount++;
                                                                                                                                                                        myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                            @Override
                                                                                                                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                                                                if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                                                    // ?????????
                                                                                                                                                                                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                                                    getActivity().finish();
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
                                                                                                                                                    }
                                                                                                                                                });
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                } else {
                                                                                                                                    failCount++;
                                                                                                                                    if (failCount + successCount == requestsCount) {
                                                                                                                                        db.getReference("Chat").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                            @Override
                                                                                                                                            public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                                if (!chatSnapshot.exists()) { // ????????? ???????????? ?????????
                                                                                                                                                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                    getActivity().finish();
                                                                                                                                                } else {
                                                                                                                                                    chatSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                            // ????????? ?????? ?????? ??????????????????

                                                                                                                                                            if (myChatSnapshot.hasChild("users/" + userId)) {
                                                                                                                                                                chatCount++;
                                                                                                                                                                myChatSnapshot.child("users/" + userId).getRef().updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                    @Override
                                                                                                                                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                                                                        if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                                            // ?????????
                                                                                                                                                                            Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                                            getActivity().finish();
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
                                                                                                                                            }
                                                                                                                                        });
                                                                                                                                    }
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

                                                                                                                        // ????????? ????????? ????????? ???????????? ?????? ?????? ????????? ????????????
                                                                                                                        db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                                                                if (!matchSnapshot.exists()) { // ???????????? ????????? ??????

                                                                                                                                } else { // ???????????? ????????? ??????
                                                                                                                                    matchSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ?????? ??? ?????? ????????? ????????? ????????????
                                                                                                                                        @Override
                                                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                            // ??? ?????? ?????? ?????? ?????? ????????????
                                                                                                                                            matchingSuccessCount++;
                                                                                                                                            snapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                @Override
                                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                                    if (matchingSuccessCount == matchSnapshot.getChildrenCount()) { // ?????? ??? ??? ????????????????????????
                                                                                                                                                        // ?????? ????????? ???????????? ??????
                                                                                                                                                        db.getReference().child("Chat").orderByChild("users/" + userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                                            @Override
                                                                                                                                                            public void onSuccess(DataSnapshot chatSnapshot) {
                                                                                                                                                                if (!chatSnapshot.exists()) { // ?????? ???????????? ???????????? ?????????
                                                                                                                                                                    Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                                                                                                                                                    getActivity().finish();
                                                                                                                                                                } else { // ?????? ???????????? ????????????
                                                                                                                                                                    chatSnapshot.getRef().addChildEventListener(new ChildEventListener() { // ????????? ?????? ?????? ????????????
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot myChatSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                                                                                                            // ????????? ?????? ??????????????????
                                                                                                                                                                            chatCount++;
                                                                                                                                                                            myChatSnapshot.getRef().updateChildren(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                                @Override
                                                                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                                                                    if (chatCount == chatSnapshot.getChildrenCount()) { // ???????????? ??? ????????????
                                                                                                                                                                                        // ?????????
                                                                                                                                                                                        Toast.makeText(getContext(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
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

                                                                                                                                                                    // ????????? ????????? ????????? ???????????? ?????? ?????? ?????????
                                                                                                                                                                    db.getReference("Matching/userRequests").orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onSuccess(DataSnapshot matchSnapshot) {
                                                                                                                                                                            if (!matchSnapshot.exists()) {

                                                                                                                                                                            } else {

                                                                                                                                                                            }
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
