package com.example.madfinalproject;

public class Users {
    private String firstname;
    private String lastname;
    private String email;
    private String contactnumber;
    private String password;

    public Users(String firstname, String lastname, String email, String contactnumber, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contactnumber = contactnumber;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
