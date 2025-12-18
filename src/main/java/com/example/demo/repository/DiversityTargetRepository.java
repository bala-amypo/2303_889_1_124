public interface DiversityTargetRepository extends JpaRepository<DiversityTarget, Long> {
List<DiversityTarget> findByTargetYear(Integer year);
}