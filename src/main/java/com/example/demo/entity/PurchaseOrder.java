@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "poNumber"))
public class PurchaseOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String poNumber;

    @ManyToOne
    private Supplier supplier;

    @ManyToOne
    private SpendCategory category;

    private BigDecimal amount;
    private LocalDate dateIssued;
    private String approvedBy;
    private String notes;
}
