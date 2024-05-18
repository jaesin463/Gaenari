package com.gaenari.backend.domain.client;

import com.gaenari.backend.domain.program.dto.responseDto.ProgramDetailDto;
import com.gaenari.backend.global.format.code.ResponseCode;
import com.gaenari.backend.global.format.response.GenericResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "exercise-record-service", url = "${feign.url.exercise-record-service}")
public interface RecordServiceClient {

    @GetMapping("/record/feign/{programId}")
    @CircuitBreaker(name = "exercise-record-service-circuit-breaker", fallbackMethod = "fallbackForGetUsageLog")
    ResponseEntity<GenericResponse<List<ProgramDetailDto.UsageLogDto>>> getUsageLog(@PathVariable(name = "programId") Long programId);

    default ResponseEntity<GenericResponse<List<ProgramDetailDto.UsageLogDto>>> fallbackForGetUsageLog(Long programId, Throwable t) {
        // 사용자 친화적인 메시지를 포함한 기본값을 반환
        ProgramDetailDto.UsageLogDto fallbackLog = ProgramDetailDto.UsageLogDto.builder()
                .recordId(0L)  // 기본값 설정
                .distance(0.0)
                .averagePace(0.0)
                .time(0.0)
                .date(null)
                .cal(0.0)
                .isFinished(false)
                .build();
        GenericResponse<List<ProgramDetailDto.UsageLogDto>> response = new GenericResponse<>();
        response.setStatus("FALLBACK");
        response.setMessage(ResponseCode.FALLBACK_SUCCESS.getMessage());
        response.setData(Collections.singletonList(fallbackLog));
        return ResponseEntity.ok(response);
    }
}
