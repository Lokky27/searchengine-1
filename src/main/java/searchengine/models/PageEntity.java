package searchengine.models;

import javax.persistence.*;

@Entity
@Table(name = "page",
        indexes = @Index(columnList = "path", unique = true))
public class PageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
