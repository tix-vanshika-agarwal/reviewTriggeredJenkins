package com.tiket.pojo.member;

public class SmartProfilePojo {
    public String firstName;
    public String lastName;
    public String nationality;
    public String nationalId;
    public long profileId;
    public String salutation;

    public SmartProfilePojo(String firstName, String lastName, String nationality, String nationalId, long profileId,String salutation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.nationalId = nationalId;
        this.profileId = profileId;
        this.salutation = salutation;
    }

    //toString method
    @Override
    public String toString() {
        return "SmartProfilePojo{" +
                "firstName = '" + firstName + '\'' +
                ", lastName = '" + lastName + '\'' +
                ", nationality = " + nationality + '\'' +
                ", nationalId = " + nationalId + '\'' +
                ", profileId = " + profileId + '\'' +
                ", salutation = " + salutation + '\'' +
                '}';
    }
}
