package com.example.tradingplatform.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "auction")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Date endDate;

    @Column
    private Double startingBid;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Bid> bids;

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
