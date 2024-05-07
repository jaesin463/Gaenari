package com.gaenari.backend.domain.recordDetail.dto;

import com.gaenari.backend.domain.record.dto.enumType.ChallengeType;
import com.gaenari.backend.domain.record.dto.enumType.ExerciseType;
import com.gaenari.backend.domain.record.dto.enumType.ProgramType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordDetailDto {
    private Long exerciseId;
    private LocalDateTime date;
    private ExerciseType exerciseType;

    private ProgramType programType;
    private ProgramInfoDto program;

    private RecordDto record;
    private PaceDto paces;
    private HeartrateDto heartrates;

    private List<TrophyDto> trophies;
    private List<MissionDto> missions;

    private int attainableCoin;
    private int attainableHeart;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordDto {
        private Double distance;
        private Double time;
        private Double cal;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaceDto {
        private Double average;  // 평균 페이스
        private List<Integer> arr; // 분당 페이스
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HeartrateDto {
        private Double average;
        private Integer max;
        private Integer min;
        private List<Integer> arr; // 분당 심박수
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrophyDto {
        private Integer id;
        private ChallengeType type;  // enum: D(거리), T(시간)
        private Integer coin;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MissionDto {
        private Integer id;
        private ChallengeType type;  // enum: D(거리), T(시간)
        private Integer value;
        private Integer coin;
        private Integer heart;
    }
}
