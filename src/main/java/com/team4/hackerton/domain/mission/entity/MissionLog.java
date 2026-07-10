package com.team4.hackerton.domain.mission.entity;

import com.team4.hackerton.domain.track.entity.Track;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "mission_log",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "mission_id", "track_id", "performed_date"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @Column(nullable = false)
    private LocalDate performedDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public MissionLog(User user, Mission mission, Track track, LocalDate performedDate) {
        this.user = user;
        this.mission = mission;
        this.track = track;
        this.performedDate = performedDate;
    }
}
