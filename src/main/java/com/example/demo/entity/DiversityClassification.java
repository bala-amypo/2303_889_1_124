@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class DiversityClassification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String description;
    private Boolean active = true;
}
