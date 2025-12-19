public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    boolean existsByEmail(String email);
    Optional<UserAccount> findByEmail(String email);
}
