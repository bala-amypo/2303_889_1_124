@Entity
public class DiversityTarget {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer targetYear;

    @ManyToOne
    private DiversityClassification classification;

    private Double targetPercentage;
    private Boolean active = true;
}
