package com.team4.hackerton.domain.cheer.repository;

import com.team4.hackerton.domain.cheer.entity.Cheer;
import com.team4.hackerton.domain.track.entity.Track;
import com.team4.hackerton.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CheerRepository extends JpaRepository<Cheer, Long> {

    boolean existsBySenderAndTargetDate(User sender, LocalDate targetDate);

    List<Cheer> findTop10ByTrackOrderByCreatedAtDesc(Track track);
}
