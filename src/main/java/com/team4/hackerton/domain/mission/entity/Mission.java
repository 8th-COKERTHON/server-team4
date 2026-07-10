package com.team4.hackerton.domain.mission.entity;

import com.team4.hackerton.domain.track.entity.Track;
import com.team4.hackerton.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "mission")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionType type;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static Mission ofTrackDefault(Track track, String title) {
        Mission mission = new Mission();
        mission.track = track;
        mission.title = title;
        mission.type = MissionType.TRACK_DEFAULT;
        return mission;
    }

    public static Mission ofUserCustom(User user, Track track, String title) {
        Mission mission = new Mission();
        mission.user = user;
        mission.track = track;
        mission.title = title;
        mission.type = MissionType.USER_CUSTOM;
        return mission;
    }
}
