package com.example.project_prm.ViewModel.User;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm.Models.LoginResult;
import com.example.project_prm.Models.PasswordChangeResult;
import com.example.project_prm.Models.ProfileResult;
import com.example.project_prm.Models.RegisterResult;
import com.example.project_prm.Models.ResetPasswordResult;
import com.example.project_prm.Repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final LiveData<RegisterResult> registerResult;
    private final LiveData<LoginResult> loginResult;
    private final MutableLiveData<ProfileResult> profileResult = new MutableLiveData<>();
    private final MutableLiveData<UserProfile> userProfile = new MutableLiveData<>();
    private final MutableLiveData<PasswordChangeResult> passwordChangeResult = new MutableLiveData<>();
    private final MutableLiveData<ResetPasswordResult> resetPasswordResult = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> googleLoginResult = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        registerResult = userRepository.getRegisterResult();
        loginResult = userRepository.getLoginResult();
    }

    public void updateProfile(int userId, String username, String email, String phone, String zipCode, String profilePicturePath) {
        userRepository.updateProfile(userId, username, email, phone, zipCode, profilePicturePath, profileResult);
    }

    public void fetchUserProfile(int userId) {
        userRepository.getUserProfile(userId, userProfile);
    }

    public void register(String username, String password, String gmail) {
        userRepository.register(username,password,gmail);
    }

    public void login(String username, String password) {
        userRepository.login(username, password);
    }

    public LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public LiveData<ProfileResult> getProfileResult() {
        return profileResult;
    }

    public MutableLiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public void changePassword(int userId, String email, String oldPassword, String newPassword) {
        userRepository.changePassword(userId, email, oldPassword, newPassword, passwordChangeResult);
    }

    public LiveData<PasswordChangeResult> getPasswordChangeResult() {
        return passwordChangeResult;
    }

    public void resetPassword(String email) {
        userRepository.verifyUserForPasswordReset(email, resetPasswordResult);
    }

    public void completePasswordReset(int userId, String newPassword) {
        userRepository.completePasswordReset(userId, newPassword, resetPasswordResult);
    }

    public LiveData<ResetPasswordResult> getResetPasswordResult() {
        return resetPasswordResult;
    }

    public void googleLogin(String username, String email) {
        userRepository.googleLogin(username, email, googleLoginResult);
    }

    public LiveData<LoginResult> getGoogleLoginResult() {
        return googleLoginResult;
    }
    public LiveData<Integer> getRoleById(int userId) {
        return userRepository.getRoleById(userId);
    }
}

