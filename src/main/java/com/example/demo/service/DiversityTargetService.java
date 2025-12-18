@Service
public class DiversityTargetService {

    private final DiversityTargetRepository repository;

    public DiversityTargetService(DiversityTargetRepository repository) {
        this.repository = repository;
    }

    public DiversityTarget createTarget(DiversityTarget target) {
        if (target.getTargetPercentage() < 0 || target.getTargetPercentage() > 100) {
            throw new BadRequestException("Target percentage must be between 0 and 100");
        }
        return repository.save(target);
    }

    public DiversityTarget updateTarget(Long id, DiversityTarget target) {
        DiversityTarget existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Target not found"));
        target.setId(existing.getId());
        return repository.save(target);
    }

    public List<DiversityTarget> getTargetsByYear(Integer year) {
        return repository.findByTargetYear(year);
    }

    public List<DiversityTarget> getAllTargets() {
        return repository.findAll();
    }

    public void deactivateTarget(Long id) {
        DiversityTarget target = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Target not found"));
        target.setActive(false);
        repository.save(target);
    }
}
