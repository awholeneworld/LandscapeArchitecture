package gachon.termproject.joker;

import static gachon.termproject.joker.Util.showToast;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import gachon.termproject.joker.Content.PostContent;

public class FirebaseHelper {
    private Activity activity;
    private OnPostListener onPostListener;
    private int successCount;

    public FirebaseHelper(Activity activity) {
        this.activity = activity;
    }

    public void setOnPostListener(OnPostListener onPostListener) {
        this.onPostListener = onPostListener;
    }

    /*
    public void storageDelete(final PostContent postContent){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // final String id = postContent.getId();
        ArrayList<String> contentsList = postContent.getContent();
        for (int i = 0; i < contentsList.size(); i++) {
            String contents = contentsList.get(i);
            if (isStorageUrl(contents)) {
                successCount++;
                StorageReference desertRef = storageRef.child("posts/" + id + "/" + storageUrlToName(contents));
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        successCount--;
                        storeDelete(id, postContent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showToast(activity, "Error");
                    }
                });
            }
        }
        storeDelete(id, postContent);
    }
    */
    public static void storeDelete(Activity activity, final String Bigcategory,  final String category, final String id, final ArrayList<String> imgs) {
        //Bigcategory => Matching, Posts
        //category => free, review, tip, userRequests...

        //사진있따면 사진도 지워져야함 =-> Storage 사진을 어떻게 지울것인가??? 그것이 문제로다,,,
        //postContent; => 이걸 이용히서 Storage 삭제해야함
        FirebaseStorage storage = FirebaseStorage.getInstance();



        if(imgs != null){
            String uri = imgs.get(0);

            String uripath = uri.substring(uri.indexOf("/o/") + 3, uri.indexOf("%2Fimage"));
            uripath = uripath.replace("%2F", "/");

            StorageReference storageRef = storage.getReference().child(uripath);
//
            storageRef.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            for (StorageReference item : listResult.getItems()) {
                                // All the items under listRef.
                                item.delete();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast(activity, "사진 삭제 에러.");
                        }
                    });


        }


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Bigcategory + "/").child(category).child(id);
        ref.removeValue();
        showToast(activity, "게시글이 삭제되었습니다.");


    }
}
