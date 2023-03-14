package com.example.tradingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Advertisements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Users user;

    @Column
    private String category;

    @Column
    private String subcategory;

    @Column
    private String status;

    @Column
    private String photoUrl;

    @Column
    private String description;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", updatable = false)
    private Date createdAt;

    public Advertisements(String title, double price, String category, String subcategory, String status, String description, String fileName, Users user) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.subcategory = subcategory;
        this.status = status;
        this.description = description;
        this.photoUrl = fileName;
        this.user = user;
    }
}
