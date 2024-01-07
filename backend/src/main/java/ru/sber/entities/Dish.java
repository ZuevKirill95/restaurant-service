package ru.sber.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Блюдо
 */

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "dishes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 30)
    private String name;

    @Column(nullable = false)
    @Size(max = 200)
    private String description;

    private String urlImage;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private double weight;

    public Dish(String name, String urlImage, Category category, BigDecimal price, double weight) {
        this.name = name;
        this.urlImage = urlImage;
        this.category = category;
        this.price = price;
        this.weight = weight;
    }
}
