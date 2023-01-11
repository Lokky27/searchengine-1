package searchengine.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "site")
@Data
public class SiteEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status",
            columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED')",
            nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "status_time",
            columnDefinition = "DATETIME",
            nullable = false)
    private LocalDateTime statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "url",
            columnDefinition = "VARCHAR(255)",
            nullable = false)
    private String url;

    @Column(name = "name",
            columnDefinition = "VARCHAR(255)",
            nullable = false)
    private String name;
}
