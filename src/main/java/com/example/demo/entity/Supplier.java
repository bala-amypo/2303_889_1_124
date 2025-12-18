@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = "name"),
    @UniqueConstraint(columnNames = "email")
})
public class Supplier {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String registrationNumber;

    @Column(nullable = false)
    private String email;

    private String phone;
    private String address;

    @ManyToMany
    private List<DiversityClassification> diversityClassifications = new ArrayList<>();

    private Boolean isActive = true;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }
}
