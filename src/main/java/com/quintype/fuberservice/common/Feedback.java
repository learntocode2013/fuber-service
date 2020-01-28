package com.quintype.fuberservice.common;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class Feedback {
    public enum Rating {
        One_Star,
        Two_Star,
        Three_Star,
        Four_Star,
        Five_Star
    }

    private UUID trackingId;
    private User reviewFrom;
    private User reviewFor;
    private Rating rating;
    private LocalDateTime dateTime;

    private String description;

    public Feedback() {
    }

    public Feedback(User from, User recipient, Rating rating) {
        this.reviewFrom = from;
        this.reviewFor = recipient;
        this.rating = rating;
        this.dateTime = LocalDateTime.now();
    }

    public User getReviewFrom() {
        return reviewFrom;
    }

    public User getReviewFor() {
        return reviewFor;
    }

    public Rating getRating() {
        return rating;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<UUID> getTrackingId() {
        return Optional.ofNullable(trackingId);
    }

    public Feedback setDescription(String description) {
        this.description = description;
        return this;
    }

    public Feedback setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
        return this;
    }

    public void setReviewFrom(User reviewFrom) {
        this.reviewFrom = reviewFrom;
    }

    public void setReviewFor(User reviewFor) {
        this.reviewFor = reviewFor;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
