public interface SpendCategoryRepository extends JpaRepository<SpendCategory, Long> {
    List<SpendCategory> findByActiveTrue();
}
