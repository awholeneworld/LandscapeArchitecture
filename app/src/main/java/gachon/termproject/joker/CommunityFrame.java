package gachon.termproject.joker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class  CommunityFrame extends Fragment {
    private View view;
    private FragmentManager fm;
    private FloatingActionButton button1;
    private FloatingActionButton button2;
    private CommunityListStyle communityList;
    private CommunityAlbumStyle communityAlbum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_frame, container, false);

        fm = getChildFragmentManager();
        if (communityList == null) {
            communityList = new CommunityListStyle();
            fm.beginTransaction().add(R.id.community_frame, communityList).commit();
            fm.beginTransaction().show(communityList).commit();
        }

        // <현재 테스트용 코드>
        // button1 = 리스트 & button2 = 앨범형
        // 추후 탭 버튼이랑 연계하면 됨
        button1 = view.findViewById(R.id.list);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (communityAlbum != null) {
                    fm.beginTransaction().hide(communityAlbum).commit();
                    fm.beginTransaction().show(communityList).commit();
                }
            }
        });

        button2 = view.findViewById(R.id.album);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (communityAlbum == null) {
                    communityAlbum = new CommunityAlbumStyle();
                    fm.beginTransaction().add(R.id.community_frame, communityAlbum).commit();
                }
                fm.beginTransaction().show(communityAlbum).commit();
                fm.beginTransaction().hide(communityList).commit();
            }
        });

        return view;
    }
}