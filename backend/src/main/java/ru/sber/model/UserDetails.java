package ru.sber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.entities.BranchOffice;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDetails {
    private String username;
    private String email;
    private String phoneNumber;
    private BranchOffice idBranchOffice;
    private String status;
}
