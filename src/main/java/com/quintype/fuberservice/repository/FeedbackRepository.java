package com.quintype.fuberservice.repository;

import com.quintype.fuberservice.common.Feedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class FeedbackRepository {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackRepository.class.getSimpleName());
    @Inject
    private RiderRepository riderRepository;
    @Inject
    private DriverRepository driverRepository;

    private final Map<UUID, Feedback> feedbackById = new HashMap<>();

    public FeedbackRepository() {
    }

    public List<Feedback> getAllSubmittedFeedback() {
        populateCache();
        return feedbackById.values()
                .stream()
                .collect(Collectors.toList());
    }

    private void populateCache() {
        if (feedbackById.isEmpty()) {
            // Cache all the reviews in memory
            fetchAllFeedback()
                    .stream()
                    .forEach(feedback -> feedbackById.put(feedback.getTrackingId().get(), feedback));
        }
    }

    public Optional<Feedback> getById(UUID id) {
        populateCache();
        return Optional.ofNullable(feedbackById.get(id));
    }

    public UUID saveFeedback(Feedback newFeedback) {
        UUID reviewId = newFeedback.getTrackingId().orElseGet(() -> UUID.randomUUID());
        feedbackById.put(reviewId, newFeedback);
        logger.info("Saved feedback with id: {}", reviewId);
        return reviewId;
    }

    public void deleteFeedback(UUID id) {
        Optional.ofNullable(feedbackById.remove(id))
                .ifPresent(feedback -> logger.info("Feedback from user: {} with review id: {} was deleted",
                        feedback.getReviewFrom().getUniqueId(),
                        feedback.getTrackingId()));
    }

    private List<Feedback> fetchAllFeedback() {
        return Arrays.asList(Feedback.Rating.values())
                .stream()
                .map(rating -> new Feedback(
                        riderRepository.getRandomRider(),
                        driverRepository.getRandomDriver(),
                        rating))
                .map(rating -> rating.setTrackingId(UUID.randomUUID()))
                .collect(Collectors.toList());
    }
}
