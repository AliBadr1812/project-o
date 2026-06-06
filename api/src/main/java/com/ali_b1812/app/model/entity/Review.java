package com.ali_b1812.app.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"comment"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String userName;

    @ElementCollection
    @CollectionTable(name = "review_likes",
                    joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "user_identifier")
    private List<String> likes;

    @ElementCollection
    @CollectionTable(name = "review_dislikes",
                    joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "user_identifier")
    private List<String> dislikes;

    @Transient
    public Double getRating() {
        int likeCount = (likes != null) ? likes.size() : 0;
        int dislikeCount = (dislikes != null) ? dislikes.size() : 0;
        int total = likeCount + dislikeCount;
        if (total == 0) return 0.0;
        return (double) likeCount / total * 5;
    }

    @Column(length = 2000)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
