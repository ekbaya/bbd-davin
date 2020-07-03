package com.example.hbddavin.services;

import com.example.hbddavin.modals.ModelPost;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface PostListener {
    void onImageUploadFailure(Exception e);
    void onPostUploaded();
    void onPostUploadFailure(Exception e);
    void onPostDataReceived(List<ModelPost> postList);
    void onDatabaseCancelled(DatabaseError error);
}
