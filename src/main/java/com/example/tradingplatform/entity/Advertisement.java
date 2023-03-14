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
@Table(name = "advertisement")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private Double price;

    @Column
    private String category;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;


    public Advertisement(String title, double price, String category, String subcategory, String status, String description, String fileName, User user, Auction auction) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.status = status;
        this.description = description;
        this.photoUrl = fileName;
        this.user = user;
        this.auction= auction;
    }

    public Advertisement(String title, double price, String category, String subcategory, String status, String description, String fileName, User user) {
    }
}
