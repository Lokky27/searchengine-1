package searchengine.models;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "page",
        indexes = @Index(columnList = "path", unique = true))
@Data
public class PageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE,
               fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id",
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
