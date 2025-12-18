public interface DiversityClassificationRepository extends JpaRepository<DiversityClassification, Long> {
List<DiversityClassification> findByActiveTrue();
}