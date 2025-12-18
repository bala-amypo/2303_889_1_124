public interface SupplierRepository extends JpaRepository<Supplier, Long> {
List<Supplier> findByIsActiveTrue();
}