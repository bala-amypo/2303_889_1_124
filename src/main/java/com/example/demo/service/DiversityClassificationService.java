@Service
public class DiversityClassificationService {

    private final DiversityClassificationRepository repository;

    public DiversityClassificationService(DiversityClassificationRepository repository) {
        this.repository = repository;
    }

    public DiversityClassification createClassification(DiversityClassification c) {
        return repository.save(c);
    }

    public DiversityClassification updateClassification(Long id, DiversityClassification c) {
        DiversityClassification existing = getById(id);
        c.setId(existing.getId());
        return repository.save(c);
    }

    public List<DiversityClassification> getAllClassifications() {
        return repository.findByActiveTrue();
    }

    public DiversityClassification getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classification not found"));
    }

    public void deactivateClassification(Long id) {
        DiversityClassification c = getById(id);
        c.setActive(false);
        repository.save(c);
    }
}
