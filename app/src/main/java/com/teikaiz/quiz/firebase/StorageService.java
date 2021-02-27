package com.teikaiz.quiz.firebase;

import android.net.Uri;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageService {

    private StorageService() {}
    private static class SingletonHelper{
        private static final StorageService instance = new StorageService();
    }
    public static StorageService getInstance() {
        return StorageService.SingletonHelper.instance;
    }

    public UploadTask storage(String folder, Uri uri, String key, String suffix) {
        final StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child(folder);
        StorageReference path = storageReference.child(uri.getLastPathSegment() + key + suffix);
        return  path.putFile(uri);
    }
}
