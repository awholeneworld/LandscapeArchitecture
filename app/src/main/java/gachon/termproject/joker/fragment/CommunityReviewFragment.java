package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gachon.termproject.joker.OnPostListener;
import gachon.termproject.joker.adapter.PostAdapter;
import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.PostAlbumAdapter;

public class CommunityReviewFragment extends Fragment {
    private View view;
    private SwipeRefreshLayout refresher;
    private static RecyclerView contents;
    private ArrayList<PostContent> postContentList;
    private PostContent postContent;
    private static PostAdapter postAdapter;
    private static PostAlbumAdapter postAlbumAdapter;
    private static boolean clicked = false;
    public static DatabaseReference databaseReference;
    public static ValueEventListener postsListener;
    Boolean topScrolled;
    Boolean doUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_community, container, false);

        // 레이아웃 가져오기
        contents = view.findViewById(R.id.communityContent);
        refresher = view.findViewById(R.id.refresh_layout);

        // 게시판 글 목록 내용 넣어줄 어레이 리스트와 어댑터 지정
        postContentList = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), postContentList);
        postAlbumAdapter = new PostAlbumAdapter(getActivity(), postContentList);
        // postAdapter.setOnPostListener(onPostListener);

        // 레이아웃 설정
        contents.setLayoutManager(new LinearLayoutManager(getActivity()));
        contents.setHasFixedSize(true);
        contents.setAdapter(postAdapter);
        /*
        contents.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();

                if(newState == 1 && firstVisibleItemPosition == 0){
                    topScrolled = true;
                }
                if(newState == 0 && topScrolled){
                    loadPosts(true);
                    topScrolled = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

                if(totalItemCount - 3 <= lastVisibleItemPosition && ! doUpdate){
                    loadPosts(false);
                }

                if(0 < firstVisibleItemPosition){
                    topScrolled = false;
                }
            }
        });
        */

        // 리스너 설정
        postsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postContentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    postContent = snapshot.getValue(PostContent.class);
                    postContentList.add(0, postContent);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        // DB에 리스너 설정
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts/review");
        databaseReference.addListenerForSingleValueEvent(postsListener);

        // 새로고침 리스너 설정
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                databaseReference.addListenerForSingleValueEvent(postsListener);
                refresher.setRefreshing(false);
            }
        });

        return view;
    }

    // 앨범형으로 보기 눌렀을 때 실행되는 함수
    public static void changeMode() {
        if (!clicked) {
            contents.setLayoutManager(new GridLayoutManager(contents.getContext(), 3));
            contents.setAdapter(postAlbumAdapter);
            clicked = true;
        } else {
            contents.setLayoutManager(new LinearLayoutManager(contents.getContext()));
            contents.setAdapter(postAdapter);
            clicked = false;
        }
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onPost() {

        }

        @Override
        public void onDelete(PostContent postContent) {
            postContentList.remove(postContent);
            postAdapter.notifyDataSetChanged();
        }

        @Override
        public void onModify() {
        }
    };

    /*
    private void loadPosts(final boolean clear) {
        doUpdate = true;
        Date date = postContentList.size() == 0 || clear ? new Date() : postContentList.get(postContentList.size() - 1).getCreatedAt();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("posts");
        collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).whereLessThan("createdAt", date).limit(10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(clear){
                                postContentList.clear();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                postContentList.add(new PostContent(
                                        document.getData().get("title").toString(),
                                        (ArrayList<String>) document.getData().get("contents"),
                                        (ArrayList<String>) document.getData().get("formats"),
                                        document.getData().get("publisher").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getId()));
                            }
                            postAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        doUpdate = false;
                    }
                });
    }
    */
}
