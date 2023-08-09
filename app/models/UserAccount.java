package models;

import lombok.Data;

@Data
public class UserAccount {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}