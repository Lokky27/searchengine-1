package searchengine.dto.sites;

import lombok.Builder;
import lombok.Data;
import searchengine.models.Status;

import java.util.Date;
@Data
@Builder
public class SiteDto {
    private Integer id;
    private Status status;
    private Date statusTime;
    private String lastError;
    private String url;
    private String name;

}
