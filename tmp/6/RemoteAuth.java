//package com.vismus.saftooshare.data;
//
//import android.app.Activity;
//
//import android.support.annotation.NonNull;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class RemoteAuth {
//
//    static RemoteAuth _instance = null;
//
//    public static RemoteAuth getInstance(Activity activity){
//        if(_instance == null){
//            _instance = new RemoteAuth(activity);
//        }
//        return _instance;
//    }
//
//    Activity _activity;
//    OnAuthCompleteListener _onAuthCompleteListener;
//
//    RemoteAuth(Activity activity){
//        _activity = activity;
//    }
//
//    public void setOnAuthCompleteListener(OnAuthCompleteListener listener){
//        _onAuthCompleteListener = listener;
//    }
//
//    public void authenticate(String username, String password){
//        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
//                .addOnCompleteListener(_activity, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(_onAuthCompleteListener != null) {
//                            _onAuthCompleteListener.onAuthComplete(task.isSuccessful());
//                        }
//                    }
//                });
//    }
//
//    public interface OnAuthCompleteListener {
//        void onAuthComplete(boolean isSuccessful);
//    }
//
//}
