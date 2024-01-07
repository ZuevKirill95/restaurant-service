package ru.sber.entities.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String idBranchOffice;
    private String firstName;
    private String lastName;
}