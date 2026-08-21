package com.msvc.huesped.repository;

import com.msvc.huesped.entity.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HuespedRepository extends JpaRepository<Huesped, String> {
}
