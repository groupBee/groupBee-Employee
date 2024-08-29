package groupbee.employee.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
@Table(name = RankEntity.TABLE_NAME, schema = "public")
public class RankEntity implements Serializable {
    public static final String TABLE_NAME = "rank";
    public static final String COLUMN_ID_NAME = "id";
    public static final String COLUMN_RANK_NAME = "rank";
    private static final long serialVersionUID = -2860881984130740281L;

    private Long id;

    private String rank;

    @Id
    @Column(name = COLUMN_ID_NAME, nullable = false)
    public Long getId() {
        return id;
    }

    @Column(name = COLUMN_RANK_NAME, length = Integer.MAX_VALUE)
    public String getRank() {
        return rank;
    }

}