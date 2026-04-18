package com.project.mysteryRomms.dto.request;

public class UserSessionStatsRequest {
    private Long sessionElapsedSeconds;
    private Long totalSolveSeconds;
    private Integer successes;
    private Integer failures;
    private Integer puzzlesSolved;

    public Long getSessionElapsedSeconds() {
        return sessionElapsedSeconds;
    }

    public void setSessionElapsedSeconds(Long sessionElapsedSeconds) {
        this.sessionElapsedSeconds = sessionElapsedSeconds;
    }

    public Long getTotalSolveSeconds() {
        return totalSolveSeconds;
    }

    public void setTotalSolveSeconds(Long totalSolveSeconds) {
        this.totalSolveSeconds = totalSolveSeconds;
    }

    public Integer getSuccesses() {
        return successes;
    }

    public void setSuccesses(Integer successes) {
        this.successes = successes;
    }

    public Integer getFailures() {
        return failures;
    }

    public void setFailures(Integer failures) {
        this.failures = failures;
    }

    public Integer getPuzzlesSolved() {
        return puzzlesSolved;
    }

    public void setPuzzlesSolved(Integer puzzlesSolved) {
        this.puzzlesSolved = puzzlesSolved;
    }
}
