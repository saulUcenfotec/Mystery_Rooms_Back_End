package com.project.mysteryRomms.dto.response;

public class UserStatsSummaryResponse {
    private Long userId;
    private Long sessionsPlayed;
    private Long totalSessionSeconds;
    private Double averageSessionSeconds;
    private Long totalPuzzleSolveSeconds;
    private Double averagePuzzleSolveSeconds;
    private Long totalSuccesses;
    private Double averageSuccesses;
    private Long totalFailures;
    private Double averageFailures;
    private Long totalPuzzlesSolved;
    private Double averagePuzzlesSolved;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSessionsPlayed() {
        return sessionsPlayed;
    }

    public void setSessionsPlayed(Long sessionsPlayed) {
        this.sessionsPlayed = sessionsPlayed;
    }

    public Long getTotalSessionSeconds() {
        return totalSessionSeconds;
    }

    public void setTotalSessionSeconds(Long totalSessionSeconds) {
        this.totalSessionSeconds = totalSessionSeconds;
    }

    public Double getAverageSessionSeconds() {
        return averageSessionSeconds;
    }

    public void setAverageSessionSeconds(Double averageSessionSeconds) {
        this.averageSessionSeconds = averageSessionSeconds;
    }

    public Long getTotalPuzzleSolveSeconds() {
        return totalPuzzleSolveSeconds;
    }

    public void setTotalPuzzleSolveSeconds(Long totalPuzzleSolveSeconds) {
        this.totalPuzzleSolveSeconds = totalPuzzleSolveSeconds;
    }

    public Double getAveragePuzzleSolveSeconds() {
        return averagePuzzleSolveSeconds;
    }

    public void setAveragePuzzleSolveSeconds(Double averagePuzzleSolveSeconds) {
        this.averagePuzzleSolveSeconds = averagePuzzleSolveSeconds;
    }

    public Long getTotalSuccesses() {
        return totalSuccesses;
    }

    public void setTotalSuccesses(Long totalSuccesses) {
        this.totalSuccesses = totalSuccesses;
    }

    public Double getAverageSuccesses() {
        return averageSuccesses;
    }

    public void setAverageSuccesses(Double averageSuccesses) {
        this.averageSuccesses = averageSuccesses;
    }

    public Long getTotalFailures() {
        return totalFailures;
    }

    public void setTotalFailures(Long totalFailures) {
        this.totalFailures = totalFailures;
    }

    public Double getAverageFailures() {
        return averageFailures;
    }

    public void setAverageFailures(Double averageFailures) {
        this.averageFailures = averageFailures;
    }

    public Long getTotalPuzzlesSolved() {
        return totalPuzzlesSolved;
    }

    public void setTotalPuzzlesSolved(Long totalPuzzlesSolved) {
        this.totalPuzzlesSolved = totalPuzzlesSolved;
    }

    public Double getAveragePuzzlesSolved() {
        return averagePuzzlesSolved;
    }

    public void setAveragePuzzlesSolved(Double averagePuzzlesSolved) {
        this.averagePuzzlesSolved = averagePuzzlesSolved;
    }
}
