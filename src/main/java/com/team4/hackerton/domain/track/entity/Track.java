package com.team4.hackerton.domain.track.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "track")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TrackType trackType;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private int requiredDays;

    public Track(TrackType trackType, String goal, int requiredDays) {
        this.trackType = trackType;
        this.goal = goal;
        this.requiredDays = requiredDays;
    }
}
