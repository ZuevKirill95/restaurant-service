package ru.sber.model;

import lombok.Data;

@Data
public class UpdateUserData {
    private String firstName;
    private String lastName;
    private String email;
    private Attributes attributes;
}
