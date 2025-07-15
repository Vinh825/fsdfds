/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.sql.Date;

/**
 *
 * @author Victus
 */
public class Staff {

    private int Id;
    private String fullName;
    private String email;
    private String passwordHash;
    private String gender;
    private String phone;
    private String role;
    private Date dateOfBirth;
    private String avatarUrl;

    public Staff(int Id, String fullName, String email, String passwordHash, String gender, String phone, String role, Date dateOfBirth, String avatarUrl) {
        this.Id = Id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.gender = gender;
        this.phone = phone;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.avatarUrl = avatarUrl;
    }

    public Staff(String fullName, String email, String passwordHash, String gender, String phone, String role, Date dateOfBirth, String avatarUrl) {

        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.gender = gender;
        this.phone = phone;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.avatarUrl = avatarUrl;
    }

    public Staff(int Id, String fullName, String email, String gender, String phone, Date dateOfBirth, String avatarUrl) {
        this.Id = Id;
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.avatarUrl = avatarUrl;
    }

    public Staff(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Staff{" + "Id=" + Id + ", fullName=" + fullName + ", email=" + email + ", passwordHash=" + passwordHash + ", phone=" + phone + ", role=" + role + ", dateOfBirth=" + dateOfBirth + ", avatarUrl=" + avatarUrl + '}';
    }

}
