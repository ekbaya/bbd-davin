package com.example.hbddavin.apis;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.hbddavin.modals.ModelPost;
import com.example.hbddavin.services.PostListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PostAPI {
    private PostListener postListener;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    List<ModelPost> postList;

    public PostAPI() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        databaseReference.keepSynced(true);
        postList = new ArrayList<>();
    }

    public void publishPost(final String name, byte[] data, final String message){
        //for post image name, post-id, post-publish-time
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" + timeStamp;

        storageReference = FirebaseStorage.getInstance().getReference().child(filePathAndName);

        storageReference.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image is uploaded to firebase storage now get its url
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());

                        String downloadUri = Objects.requireNonNull(uriTask.getResult()).toString();

                        if (uriTask.isSuccessful()){
                            //url is received upload post to Firebase database
                            HashMap<Object, String> hashMap = new HashMap<>();
                            //put post info
                            hashMap.put("Id",timeStamp);
                            hashMap.put("name",name);
                            hashMap.put("image",downloadUri);
                            hashMap.put("message",message);
                            hashMap.put("time",timeStamp);

                            databaseReference.child(timeStamp).setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            postListener.onPostUploaded();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                           postListener.onPostUploadFailure(e);
                                        }
                                    });
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       postListener.onImageUploadFailure(e);
                    }
                });

    }

    public void loadPosts(){
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                }
                postListener.onPostDataReceived(postList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                postListener.onDatabaseCancelled(error);

            }
        });
    }

    public void searchPosts(final String query){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    if (modelPost.getName().toLowerCase().contains(query.toLowerCase()) ||
                            modelPost.getMessage().toLowerCase().contains(query.toLowerCase()))
                    {
                        postList.add(modelPost);
                    }
                    postListener.onPostDataReceived(postList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                postListener.onDatabaseCancelled(error);

            }
        });
    }

    public PostListener getPostListener() {
        return postListener;
    }

    public void setPostListener(PostListener postListener) {
        this.postListener = postListener;
    }
}
