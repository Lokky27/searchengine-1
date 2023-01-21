package searchengine.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "page",
        indexes = @Index(columnList = "path", unique = true))
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "site_id",
            insertable = false,
            updatable = false,
            nullable = false)
    private SiteEntity site;

    @Column(name = "path",
            columnDefinition = "TEXT",
            nullable = false)
    private String path;

    @Column(name = "code",
            nullable = false)
    private int statusCode;

    @Column(name = "content",
            columnDefinition = "MEDIUMTEXT",
            nullable = false)
    private String content;
}
