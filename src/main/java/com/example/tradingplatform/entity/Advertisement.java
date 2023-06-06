package com.example.tradingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

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

    @Column
    private String title;

    @Column
    private Double price;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @Column
    private String status;

    @Column
    private String photoUrl;

    @Column
    @Length(max = 1500)
    private String description;

    @Column
    private String type;

    @Column
    private Boolean isActive = true;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", updatable = false)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auction_id")
    @JsonIgnore
    private Auction auction;

    @Transient
    private int orderCount;

    public Advertisement(String title, Double price, Category category, Subcategory subcategory, String status, String description, String type, String fileName, User user, Auction auction) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.subcategory = subcategory;
        this.status = status;
        this.description = description;
        this.type = type;
        this.photoUrl = fileName;
        this.user = user;
        this.auction= auction;
    }

    public Advertisement(String title, Double price, Category category, Subcategory subcategory, String status, String description, String type, String fileName, User user) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.subcategory = subcategory;
        this.status = status;
        this.description = description;
        this.type = type;
        this.photoUrl = fileName;
        this.user = user;
    }

    public String getCategoryName() {
        return this.category.getName();
    }

    public String getSubcategoryName() {
        return this.subcategory.getName();
    }

}
