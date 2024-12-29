package org.example.lib.models;

import java.time.LocalDate;

public class Reader {
    private String id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private LocalDate dob;

    public Reader() {
    }

    public Reader(String id, String fullName, String address, String phoneNumber, String email, LocalDate dob) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dob = dob;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                '}';
    }

    public static Reader mapToReader(String[] data) {
        return new Reader(data[0], data[1], data[2], data[3], data[4], LocalDate.parse(data[5]));
    }

    public static String mapToString(Reader reader) {
        return String.join(",",
                reader.getId(),
                reader.getFullName(),
                reader.getAddress(),
                reader.getPhoneNumber(),
                reader.getEmail(),
                reader.getDob().toString()
        );
    }
}
