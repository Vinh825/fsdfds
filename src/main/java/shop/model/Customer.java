/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author CE190449 - Le Anh Khoa
 */
public class Customer {

    private int customerId;
    private String fullName;
    private String username;
    private String email;
    private String passwordHash;
    private String phone;
    private String gender;
    private String address;
    private String avatarUrl;
    private Date dateOfBirth;
    private boolean isDeactivated;
    private Timestamp lastLogin;
    private String googleId;
    private String rememberMeToken;
    private String resetToken;
    private Timestamp resetTokenExpiry;
    private boolean emailVerified;
    private String emailVerificationToken;
    private Timestamp emailVerificationExpiry;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Customer(int customerId, String fullName, String username, String email, String passwordHash, String phone, String gender, String address, String avatarUrl, Date dateOfBirth, boolean isDeactivated, Timestamp lastLogin, String googleId, String rememberMeToken, String resetToken, Timestamp resetTokenExpiry, boolean emailVerified, String emailVerificationToken, Timestamp emailVerificationExpiry, Timestamp createdAt, Timestamp updatedAt) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.dateOfBirth = dateOfBirth;
        this.isDeactivated = isDeactivated;
        this.lastLogin = lastLogin;
        this.googleId = googleId;
        this.rememberMeToken = rememberMeToken;
        this.resetToken = resetToken;
        this.resetTokenExpiry = resetTokenExpiry;
        this.emailVerified = emailVerified;
        this.emailVerificationToken = emailVerificationToken;
        this.emailVerificationExpiry = emailVerificationExpiry;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Customer(int customerId, String fullName, String username, String email, String phone, String gender, String address, String avatarUrl, Date dateOfBirth) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.dateOfBirth = dateOfBirth;
    }

    public Customer(String fullName, String username, String email, String passwordHash, String avatarUrl) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.avatarUrl = avatarUrl;
    }

    public Customer(int customerId, String email, String passwordHash, boolean isDeactivated) {
        this.customerId = customerId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isDeactivated = isDeactivated;
    }

    public Customer(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Customer() {
    }

    public Customer(int customerId, String username, String email, String fullName, String phone, String address) {
        this.customerId = customerId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean isIsDeactivated() {
        return isDeactivated;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getRememberMeToken() {
        return rememberMeToken;
    }

    public String getResetToken() {
        return resetToken;
    }

    public Timestamp getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public Timestamp getEmailVerificationExpiry() {
        return emailVerificationExpiry;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAvatarUri(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setIsDeactivated(boolean isDeactivated) {
        this.isDeactivated = isDeactivated;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public void setRememberMeToken(String rememberMeToken) {
        this.rememberMeToken = rememberMeToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public void setResetTokenExpiry(Timestamp resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public void setEmailVerificationExpiry(Timestamp emailVerificationExpiry) {
        this.emailVerificationExpiry = emailVerificationExpiry;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
