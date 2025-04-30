package com.example.project_prm.ViewModel.User;

public class UserProfile {
    private final String username;
    private final String email;
    private final String phone;
    private final String address;
    private final String image;

    public UserProfile(String username, String email, String phone, String address, String image) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.image = image;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getImage() { return image; }
}
