package com.project.mysteryRomms.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table(name = "user")
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(nullable = false)
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());
        return List.of(authority);
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "sessions_played", nullable = false, columnDefinition = "bigint default 0")
    private Long sessionsPlayed = 0L;

    @Column(name = "total_session_seconds", nullable = false, columnDefinition = "bigint default 0")
    private Long totalSessionSeconds = 0L;

    @Column(name = "average_session_seconds", nullable = false, columnDefinition = "double precision default 0")
    private Double averageSessionSeconds = 0.0;

    @Column(name = "total_puzzle_solve_seconds", nullable = false, columnDefinition = "bigint default 0")
    private Long totalPuzzleSolveSeconds = 0L;

    @Column(name = "average_puzzle_solve_seconds", nullable = false, columnDefinition = "double precision default 0")
    private Double averagePuzzleSolveSeconds = 0.0;

    @Column(name = "total_successes", nullable = false, columnDefinition = "bigint default 0")
    private Long totalSuccesses = 0L;

    @Column(name = "average_successes", nullable = false, columnDefinition = "double precision default 0")
    private Double averageSuccesses = 0.0;

    @Column(name = "total_failures", nullable = false, columnDefinition = "bigint default 0")
    private Long totalFailures = 0L;

    @Column(name = "average_failures", nullable = false, columnDefinition = "double precision default 0")
    private Double averageFailures = 0.0;

    @Column(name = "total_puzzles_solved", nullable = false, columnDefinition = "bigint default 0")
    private Long totalPuzzlesSolved = 0L;

    @Column(name = "average_puzzles_solved", nullable = false, columnDefinition = "double precision default 0")
    private Double averagePuzzlesSolved = 0.0;

    // Constructors
    public User() {}


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    public boolean isEnabledField() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
