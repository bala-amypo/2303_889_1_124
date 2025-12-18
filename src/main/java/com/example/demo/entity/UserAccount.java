@Entity
public class UserAccount {
@Id @GeneratedValue
private Long id;


private String fullName;


@Column(unique = true)
private String email;


private String password;
private String role = "USER";
private Instant createdAt;


@PrePersist
public void onCreate() {
this.createdAt = Instant.now();
}
}