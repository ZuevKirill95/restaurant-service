package ru.sber.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.entities.enums.EStatusEmployee;

/**
 * Сотрудник
 */
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "employees")
public class User {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "branchs_office_id", nullable = false)
    private BranchOffice branchOffice;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EStatusEmployee status;

    private String resetPasswordToken;

    public User(String id) {
        this.id = id;
    }

    public User(String id, BranchOffice branchOffice) {
        this.id = id;
        this.branchOffice = branchOffice;
    }
}