package com.example.project_prm.Repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm.Dao.UserDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.User;
import com.example.project_prm.Models.LoginResult;
import com.example.project_prm.Models.PasswordChangeResult;
import com.example.project_prm.Models.ProfileResult;
import com.example.project_prm.Models.RegisterResult;
import com.example.project_prm.Models.ResetPasswordResult;
import com.example.project_prm.ViewModel.User.UserProfile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;
    private final MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> googleLoginResult = new MutableLiveData<>(); // For Google Sign-In

    public UserRepository(Context context) {
        ClothingDatabase database = ClothingDatabase.getInstance(context);
        userDao = database.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public LiveData<List<User>> getAllUserAdmin() {
        return userDao.getAllUsersAdmin();
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public MutableLiveData<LoginResult> getGoogleLoginResult() {
        return googleLoginResult;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void softDeleteUser(int userId) {
        executorService.execute(() -> userDao.softDeleteUser(userId));
    }
    public void restoreUser(int userId) {
        executorService.execute(() -> userDao.restoreUser(userId));
    }
    public void register(String username, String password, String gmail) {
        executorService.execute(() -> {
            try {
                if (!isValidEmail(gmail)) {
                    onRegisterFailure("Invalid email format");
                    return;
                }
                User existingUser = userDao.getUserByUsername(username);
                if (existingUser != null) {
                    onRegisterFailure("Username already exists");
                    return;
                }
                User existingEmail = userDao.getUserByEmail(gmail);
                if (existingEmail != null) {
                    onRegisterFailure("Email already exists");
                    return;
                }

                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setGmail(gmail);
                newUser.setRole_id(2);
                newUser.setCreated_at(currentTime);
                newUser.setUpdated_at(currentTime);
                newUser.setIsDelete(0);
                newUser.setGoogleUser(false);

                userDao.insert(newUser);

                User insertedUser = userDao.getUserByUsername(username);
                if (insertedUser != null) {
                    Log.d("UserRepository", "User inserted: " + insertedUser.getGmail());
                    onRegisterSuccess(insertedUser.getId());
                } else {
                    onRegisterFailure("Failed to create user account");
                }
            } catch (Exception e) {
                onRegisterFailure("Registration error: " + e.getMessage());
            }
        });
    }

    public void googleLogin(String username, String email, MutableLiveData<LoginResult> googleLoginResult) {
        executorService.execute(() -> {
            try {
                User existingUser = userDao.googleLogin(email);
                if (existingUser != null) {
                    if (existingUser.getIsDelete() == 1) {
                        onGoogleLoginFailure("This account has been deactivated", googleLoginResult);
                    } else {
                        onGoogleLoginSuccess(existingUser, googleLoginResult);
                    }
                    return;
                }

                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword("");
                newUser.setGmail(email);
                newUser.setRole_id(2);
                newUser.setCreated_at(currentTime);
                newUser.setUpdated_at(currentTime);
                newUser.setIsDelete(0);
                newUser.setGoogleUser(true);

                userDao.insert(newUser);

                User insertedUser = userDao.googleLogin(email);
                if (insertedUser != null) {
                    onGoogleLoginSuccess(insertedUser, googleLoginResult);
                } else {
                    onGoogleLoginFailure("Failed to create Google user account", googleLoginResult);
                }
            } catch (Exception e) {
                onGoogleLoginFailure("Google login error: " + e.getMessage(), googleLoginResult);
            }
        });
    }

    public void login(String username, String password) {
        executorService.execute(() -> {
            try {
                User user = userDao.login(username, password);

                if (user != null) {
                    if (user.getIsDelete() == 1) {
                        onLoginFailure("This account has been deactivated");
                    } else {
                        onLoginSuccess(user);
                    }

                } else {
                    onLoginFailure("Invalid username or password");
                }
            } catch (Exception e) {
                onLoginFailure("Login error: " + e.getMessage());
            }
        });
    }

    public void updateProfile(int userId, String username, String email, String phone, String zipCode, String profilePicturePath, MutableLiveData<ProfileResult> profileResult) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                User currentUser = userDao.getUserByIdSync(userId);
                if (currentUser == null) {
                    profileResult.postValue(new ProfileResult(false, "User not found"));
                    return;
                }

                String updatedUsername = username.isEmpty() ? currentUser.getUsername() : username;
                String updatedEmail = email.isEmpty() ? currentUser.getGmail() : email;
                String updatedPhone = phone.isEmpty() ? currentUser.getPhone() : phone;
                String updatedAddress = zipCode.isEmpty() ? currentUser.getAddress() : zipCode;
                String updatedImage = profilePicturePath.isEmpty() ? currentUser.getImage() : profilePicturePath;

                String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                Log.d("UserRepository", "Updating user profile: " +
                        "userId=" + userId +
                        ", username=" + updatedUsername +
                        ", email=" + updatedEmail +
                        ", phone=" + updatedPhone +
                        ", address=" + updatedAddress +
                        ", image=" + updatedImage);

                userDao.updateUserProfile(
                        userId,
                        updatedUsername,
                        currentUser.getPassword(),
                        updatedEmail,
                        updatedPhone,
                        updatedAddress,
                        updatedImage,
                        updatedAt
                );

                profileResult.postValue(new ProfileResult(true, "Profile updated successfully"));
            } catch (Exception e) {
                Log.e("UserRepository", "Error updating profile: " + e.getMessage());
                profileResult.postValue(new ProfileResult(false, "Failed to update profile: " + e.getMessage()));
            }
        });
    }

    public void getUserProfile(int userId, MutableLiveData<UserProfile> userProfileLiveData) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                User user = userDao.getUserByIdSync(userId);
                if (user != null) {
                    UserProfile profile = mapUserToUserProfile(user);
                    userProfileLiveData.postValue(profile);
                } else {
                    Log.e("UserRepository", "User not found with ID: " + userId);
                    userProfileLiveData.postValue(null);
                }
            } catch (Exception e) {
                Log.e("UserRepository", "Error fetching user profile: " + e.getMessage());
                userProfileLiveData.postValue(null);
            }
        });
    }

    private UserProfile mapUserToUserProfile(User user) {
        if (user == null) return null;
        return new UserProfile(
                user.getUsername(),
                user.getGmail(),
                user.getPhone(),
                user.getAddress(),
                user.getImage()
        );
    }

    public void logAllUsers() {
        executorService.execute(() -> {
            List<User> users = userDao.getAllUsers();
            for (User user : users) {
                Log.d("UserRepository", "User: id=" + user.getId() + ", username=" + user.getUsername() + ", password=" + user.getPassword() + ", gmail=" + user.getGmail() + ", isDelete=" + user.getIsDelete() + ", isGoogleUser=" + user.isGoogleUser());
            }
        });
    }

    private void onRegisterSuccess(int userId) {
        RegisterResult result = new RegisterResult(true, "Registration successful", userId);
        registerResult.postValue(result);
    }

    private void onRegisterFailure(String errorMessage) {
        RegisterResult result = new RegisterResult(false, errorMessage);
        registerResult.postValue(result);
    }

    private void onLoginSuccess(User user) {
        LoginResult result = new LoginResult(true, "Login successful", user);
        loginResult.postValue(result);
    }

    private void onLoginFailure(String errorMessage) {
        LoginResult result = new LoginResult(false, errorMessage);
        loginResult.postValue(result);
    }

    private void onGoogleLoginSuccess(User user, MutableLiveData<LoginResult> googleLoginResult) {
        LoginResult result = new LoginResult(true, "Google login successful", user);
        googleLoginResult.postValue(result);
    }

    private void onGoogleLoginFailure(String errorMessage, MutableLiveData<LoginResult> googleLoginResult) {
        LoginResult result = new LoginResult(false, errorMessage);
        googleLoginResult.postValue(result);
    }

    public void changePassword(int userId, String email, String oldPassword, String newPassword, MutableLiveData<PasswordChangeResult> result) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserByEmail(email);
                if (user == null || user.getId() != userId) {
                    result.postValue(new PasswordChangeResult(false, "Invalid email address"));
                    return;
                }
                if (user.isGoogleUser()) {
                    result.postValue(new PasswordChangeResult(false, "Google users cannot change passwords here"));
                    return;
                }

                String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                int rowsAffected = userDao.changePassword(userId, oldPassword, newPassword, updatedAt);

                if (rowsAffected > 0) {
                    result.postValue(new PasswordChangeResult(true, "Password changed successfully"));
                } else {
                    result.postValue(new PasswordChangeResult(false, "Incorrect current password"));
                }
            } catch (Exception e) {
                Log.e("UserRepository", "Error changing password: " + e.getMessage());
                result.postValue(new PasswordChangeResult(false, "Error changing password: " + e.getMessage()));
            }
        });
    }

    public void verifyUserForPasswordReset(String email, MutableLiveData<ResetPasswordResult> result) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserForPasswordReset( email);
                if (user != null) {
                    if (user.isGoogleUser()) {
                        result.postValue(new ResetPasswordResult(false, "Google users must reset passwords via Google"));
                    } else {
                        result.postValue(new ResetPasswordResult(true, "Verification successful", user.getId()));
                    }
                } else {
                    result.postValue(new ResetPasswordResult(false, "User not found. Please check your username and email."));
                }
            } catch (Exception e) {
                Log.e("UserRepository", "Error verifying user for password reset: " + e.getMessage());
                result.postValue(new ResetPasswordResult(false, "Error verifying user: " + e.getMessage()));
            }
        });
    }

    public void completePasswordReset(int userId, String newPassword, MutableLiveData<ResetPasswordResult> result) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserByIdSync(userId);
                if (user == null) {
                    result.postValue(new ResetPasswordResult(false, "User not found"));
                    return;
                }
                if (user.isGoogleUser()) {
                    result.postValue(new ResetPasswordResult(false, "Google users cannot reset passwords here"));
                    return;
                }

                String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                userDao.updatePassword(userId, newPassword, updatedAt);

                result.postValue(new ResetPasswordResult(true, "Password has been reset successfully"));
            } catch (Exception e) {
                Log.e("UserRepository", "Error resetting password: " + e.getMessage());
                result.postValue(new ResetPasswordResult(false, "Error resetting password: " + e.getMessage()));
            }
        });
    }
    public void insert(User user) {
        executorService.execute(() -> userDao.insert(user));
    }

    public void update(User user) {
        executorService.execute(() -> userDao.update(user));
    }

    public void delete(User user) {
        executorService.execute(() -> userDao.delete(user));
    }
    public LiveData<Integer> getRoleById(int userId) {
        return userDao.getRoleById(userId);
    }

}