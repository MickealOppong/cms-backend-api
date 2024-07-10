package opp.mic.cms.model;

import java.time.LocalDateTime;

public record CategoryDT0(Long id, String name, String description, Long quantity, Long sale, LocalDateTime createAt) {
}
