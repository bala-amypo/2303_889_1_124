@Entity
public class DiversityClassification {
@Id @GeneratedValue
private Long id;


@Column(unique = true)
private String code;


private String description;
private Boolean active = true;
}