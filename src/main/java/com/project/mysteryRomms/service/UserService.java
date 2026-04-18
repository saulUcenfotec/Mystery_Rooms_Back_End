package com.project.mysteryRomms.service;

import com.project.mysteryRomms.dto.request.UserSessionStatsRequest;
import com.project.mysteryRomms.dto.response.UserStatsSummaryResponse;
import com.project.mysteryRomms.model.entity.PasswordResetToken;
import com.project.mysteryRomms.model.entity.User;
import com.project.mysteryRomms.repository.PasswordResetTokenRepository;
import com.project.mysteryRomms.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String createPasswordResetToken(User getterUser) {
        Optional<User> user = repositoryUser.findByEmail(getterUser.getEmail());
        if (user.isEmpty()) {
            return null;
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user.get());
        tokenRepository.save(resetToken);
        return token;
    }

    public boolean resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            return false;
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        repositoryUser.save(user);
        tokenRepository.delete(resetToken);
        return true;
    }

    public UserStatsSummaryResponse recordSessionStats(Long userId, UserSessionStatsRequest request) {
        User user = repositoryUser.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        long nextSessionsPlayed = safeLong(user.getSessionsPlayed()) + 1L;
        long nextTotalSessionSeconds = safeLong(user.getTotalSessionSeconds()) + safeLong(request.getSessionElapsedSeconds());
        long nextTotalPuzzleSolveSeconds = safeLong(user.getTotalPuzzleSolveSeconds()) + safeLong(request.getTotalSolveSeconds());
        long nextTotalSuccesses = safeLong(user.getTotalSuccesses()) + safeInteger(request.getSuccesses());
        long nextTotalFailures = safeLong(user.getTotalFailures()) + safeInteger(request.getFailures());
        long nextTotalPuzzlesSolved = safeLong(user.getTotalPuzzlesSolved()) + safeInteger(request.getPuzzlesSolved());

        user.setSessionsPlayed(nextSessionsPlayed);
        user.setTotalSessionSeconds(nextTotalSessionSeconds);
        user.setAverageSessionSeconds(calculateAverage(nextTotalSessionSeconds, nextSessionsPlayed));
        user.setTotalPuzzleSolveSeconds(nextTotalPuzzleSolveSeconds);
        user.setAveragePuzzleSolveSeconds(calculateAverage(nextTotalPuzzleSolveSeconds, nextSessionsPlayed));
        user.setTotalSuccesses(nextTotalSuccesses);
        user.setAverageSuccesses(calculateAverage(nextTotalSuccesses, nextSessionsPlayed));
        user.setTotalFailures(nextTotalFailures);
        user.setAverageFailures(calculateAverage(nextTotalFailures, nextSessionsPlayed));
        user.setTotalPuzzlesSolved(nextTotalPuzzlesSolved);
        user.setAveragePuzzlesSolved(calculateAverage(nextTotalPuzzlesSolved, nextSessionsPlayed));

        User savedUser = repositoryUser.save(user);
        return toStatsSummary(savedUser);
    }

    public UserStatsSummaryResponse getUserStatsSummary(Long userId) {
        User user = repositoryUser.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toStatsSummary(user);
    }

    private UserStatsSummaryResponse toStatsSummary(User user) {
        UserStatsSummaryResponse response = new UserStatsSummaryResponse();
        response.setUserId(user.getId());
        response.setSessionsPlayed(safeLong(user.getSessionsPlayed()));
        response.setTotalSessionSeconds(safeLong(user.getTotalSessionSeconds()));
        response.setAverageSessionSeconds(safeDouble(user.getAverageSessionSeconds()));
        response.setTotalPuzzleSolveSeconds(safeLong(user.getTotalPuzzleSolveSeconds()));
        response.setAveragePuzzleSolveSeconds(safeDouble(user.getAveragePuzzleSolveSeconds()));
        response.setTotalSuccesses(safeLong(user.getTotalSuccesses()));
        response.setAverageSuccesses(safeDouble(user.getAverageSuccesses()));
        response.setTotalFailures(safeLong(user.getTotalFailures()));
        response.setAverageFailures(safeDouble(user.getAverageFailures()));
        response.setTotalPuzzlesSolved(safeLong(user.getTotalPuzzlesSolved()));
        response.setAveragePuzzlesSolved(safeDouble(user.getAveragePuzzlesSolved()));
        return response;
    }

    private long safeLong(Long value) {
        return value == null ? 0L : value;
    }

    private long safeInteger(Integer value) {
        return value == null ? 0L : value.longValue();
    }

    private double safeDouble(Double value) {
        return value == null ? 0.0 : value;
    }

    private double calculateAverage(long total, long count) {
        if (count <= 0) {
            return 0.0;
        }
        return (double) total / count;
    }
}
