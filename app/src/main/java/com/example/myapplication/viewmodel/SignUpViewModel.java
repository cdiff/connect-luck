package com.example.myapplication.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private MutableLiveData<String> profileImageUrl = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public MutableLiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.setValue(phoneNumber);
    }

    public MutableLiveData<String> getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl.setValue(profileImageUrl);
    }
}
