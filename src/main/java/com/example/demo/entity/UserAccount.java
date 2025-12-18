@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserAccount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String password;
    private String role = "USER";

    private Timestamp createdAt;

    @PrePersist
    void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }
}
