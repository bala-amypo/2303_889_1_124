@Entity
public class SpendCategory {
@Id @GeneratedValue
private Long id;


@Column(unique = true)
private String name;


private String description;
private Boolean active = true;
}