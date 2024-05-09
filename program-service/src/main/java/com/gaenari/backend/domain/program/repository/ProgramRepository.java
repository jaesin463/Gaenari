package com.gaenari.backend.domain.program.repository;

import com.gaenari.backend.domain.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;
import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> findByMemberIdOrderByIsFavoriteDescUsageCountDesc(String programId);


}