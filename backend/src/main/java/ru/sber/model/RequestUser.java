package ru.sber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RequestUser {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private boolean enabled;
    private List<Credential> credentials;
    private Attributes attributes;
}