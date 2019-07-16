package com.dsoccer1980.repository;

import com.dsoccer1980.domain.ShortMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortMessageRepository extends JpaRepository<ShortMessage, Integer> {
}
