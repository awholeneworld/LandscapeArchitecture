package gachon.termproject.joker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.MainActivity;
import gachon.termproject.joker.activity.SeePostActivity;
import gachon.termproject.joker.Content.PostContent;

public class MyInfoTabPostAdapter extends RecyclerView.Adapter<MyInfoTabPostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostContent> myInfoPostList;
    private int finishCount = 0;

    public MyInfoTabPostAdapter(Context context) {
        this.context = context;
        myInfoPostList = new ArrayList<>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        dbRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                final long categoryNum = dataSnapshot.getChildrenCount();
                dbRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                        snapshot.getRef().orderByChild("userId").equalTo(UserInfo.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot item : snapshot.getChildren()) {
                                    PostContent myInfoPostContent = item.getValue(PostContent.class);
                                    myInfoPostList.add(0, myInfoPostContent);
                                    MainActivity.userPostsIdList.add(0, myInfoPostContent.getPostId());
                                }

                                finishCount++;
                                if (finishCount == categoryNum) {
                                    myInfoPostList.sort(new Comparator<PostContent>() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public int compare(PostContent o1, PostContent o2) {
                                            long o1Id = Long.parseUnsignedLong(o1.getPostId());
                                            long o2Id = Long.parseUnsignedLong(o2.getPostId());

                                            if (o1Id < o2Id) return 1;
                                            else return -1;
                                        }
                                    });

                                    notifyDataSetChanged();
                                    finishCount = 0;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                        myInfoPostList.clear();
                        snapshot.getRef().orderByChild("userId").equalTo(UserInfo.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot item : snapshot.getChildren()) {
                                    PostContent myInfoPostContent = item.getValue(PostContent.class);
                                    myInfoPostList.add(0, myInfoPostContent);
                                    MainActivity.userPostsIdList.add(0, myInfoPostContent.getPostId());
                                }

                                finishCount++;
                                if (finishCount == categoryNum) {
                                    myInfoPostList.sort(new Comparator<PostContent>() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public int compare(PostContent o1, PostContent o2) {
                                            long o1Id = Long.parseUnsignedLong(o1.getPostId());
                                            long o2Id = Long.parseUnsignedLong(o2.getPostId());

                                            if (o1Id < o2Id) return 1;
                                            else return -1;
                                        }
                                    });

                                    notifyDataSetChanged();
                                    finishCount = 0;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                        myInfoPostList.clear();
                        snapshot.getRef().orderByChild("userId").equalTo(UserInfo.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot item : snapshot.getChildren()) {
                                    PostContent myInfoPostContent = item.getValue(PostContent.class);
                                    myInfoPostList.add(0, myInfoPostContent);
                                    MainActivity.userPostsIdList.add(0, myInfoPostContent.getPostId());
                                }

                                finishCount++;
                                if (finishCount == categoryNum) {
                                    myInfoPostList.sort(new Comparator<PostContent>() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public int compare(PostContent o1, PostContent o2) {
                                            long o1Id = Long.parseUnsignedLong(o1.getPostId());
                                            long o2Id = Long.parseUnsignedLong(o2.getPostId());

                                            if (o1Id < o2Id) return 1;
                                            else return -1;
                                        }
                                    });

                                    notifyDataSetChanged();
                                    finishCount = 0;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
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

        /* 이 코드는 추후 개선 목적으로 작성 중에 있는 코드입니다.
        // 로그인 할 때 자기가 쓴 포스트 아이디 리스트를 MainActivity로 보내게 되는데 그 아이디 리스트가지고 게시물 불러오는게 좀 더 효율적이지 않을까 싶어서
        for (String myPostId : MainActivity.userPostsIdList) {
            FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild(myPostId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                    if (snapshot.hasChild(myPostId)) {
                        int i = 0;
                    }
                    PostContent myInfoPostContent = snapshot.getValue(PostContent.class);
                    myInfoPostList.add(0, myInfoPostContent);
                    notifyDataSetChanged();
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
        */
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        String categoryOfPost;
        String userIdInPost;
        String profileImgInPost;
        String titleInPost;
        String nicknameInPost;
        String timeInPost;
        String postIdInPost;
        ArrayList<String> contentInPost;
        ArrayList<String> imagesInPost;
        ArrayList<Integer> orderInPost;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SeePostActivity.class);
                    intent.putExtra("category", categoryOfPost);
                    intent.putExtra("userId", userIdInPost);
                    intent.putExtra("profileImg", profileImgInPost);
                    intent.putExtra("title", titleInPost);
                    intent.putExtra("nickname", nicknameInPost);
                    intent.putExtra("time", timeInPost);
                    intent.putExtra("postId", postIdInPost);
                    intent.putStringArrayListExtra("content", contentInPost);
                    intent.putStringArrayListExtra("images", imagesInPost);
                    intent.putIntegerArrayListExtra("order", orderInPost);
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_myinfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostContent content = myInfoPostList.get(position);

        String contentTitle = content.getTitle();
        String contentNickname = content.getNickname();
        String contentTime = content.getPostTime();
        ArrayList<String> contentsList = content.getContent();
        ArrayList<String> imagesList = content.getImages();

        // 뷰홀더 클래스의 전역 변수 설정
        holder.categoryOfPost = content.getCategory();
        holder.userIdInPost = content.getUserId();
        holder.profileImgInPost = content.getProfileImg();
        holder.titleInPost = contentTitle;
        holder.nicknameInPost = contentNickname;
        holder.timeInPost = contentTime;
        holder.postIdInPost = content.getPostId();
        holder.contentInPost = contentsList;
        holder.imagesInPost = imagesList;

        if (imagesList != null)
            Glide.with(context).load(imagesList.get(0)).override(1000).thumbnail(0.1f).into(holder.imageView);
        else
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() { return myInfoPostList.size(); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
