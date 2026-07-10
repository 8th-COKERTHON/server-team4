package com.team4.hackerton.global.init;

import com.team4.hackerton.domain.track.entity.Track;
import com.team4.hackerton.domain.track.entity.TrackType;
import com.team4.hackerton.domain.track.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final TrackRepository trackRepository;

    @Override
    public void run(ApplicationArguments args) {
        Arrays.stream(TrackType.values())
                .filter(type -> trackRepository.findByTrackType(type).isEmpty())
                .forEach(type -> trackRepository.save(switch (type) {
                    case SELF_CARE -> new Track(type, "매일 몸을 조금씩 움직여 보세요", 21);
                    case GO_OUTSIDE -> new Track(type, "하루 한 번 밖으로 나가 보세요", 21);
                    case CONNECT_PEOPLE -> new Track(type, "한 사람에게 먼저 말을 걸어 보세요", 21);
                }));
    }
}
