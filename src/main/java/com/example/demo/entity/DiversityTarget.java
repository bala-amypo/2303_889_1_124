@Entity
public class DiversityTarget {
@Id @GeneratedValue
private Long id;


private Integer targetYear;


@ManyToOne
private DiversityClassification classification;


private Double targetPercentage;
private Boolean active = true;
}   