package com.team4.hackerton.domain.track.entity;

import com.team4.hackerton.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "user_track")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @Column(nullable = false)
    private LocalDate joinedAt;

    @Column
    private LocalDate completedAt;

    @Column(nullable = false)
    private boolean isCurrent;

    public UserTrack(User user, Track track, LocalDate joinedAt) {
        this.user = user;
        this.track = track;
        this.joinedAt = joinedAt;
        this.isCurrent = true;
    }

    public void complete(LocalDate completedAt) {
        this.completedAt = completedAt;
        this.isCurrent = false;
    }
}
