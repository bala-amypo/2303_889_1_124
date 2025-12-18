@Service
public class UserAccountService {

    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepository repository,
                              PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount register(RegisterRequest req) {
        if (repository.existsByEmail(req.email)) {
            throw new BadRequestException("Email already exists");
        }

        UserAccount user = new UserAccount();
        user.setFullName(req.fullName);
        user.setEmail(req.email);
        user.setPassword(passwordEncoder.encode(req.password) + "_ENC");
        user.setRole(req.role != null ? req.role : "USER");

        return repository.save(user);
    }

    public UserAccount findByEmailOrThrow(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
