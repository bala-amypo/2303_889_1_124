@Service
public class SpendCategoryService {

    private final SpendCategoryRepository repository;

    public SpendCategoryService(SpendCategoryRepository repository) {
        this.repository = repository;
    }

    public SpendCategory createCategory(SpendCategory category) {
        return repository.save(category);
    }

    public SpendCategory updateCategory(Long id, SpendCategory category) {
        SpendCategory existing = getCategoryById(id);
        category.setId(existing.getId());
        return repository.save(category);
    }

    public SpendCategory getCategoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public List<SpendCategory> getAllCategories() {
        return repository.findByActiveTrue();
    }

    public void deactivateCategory(Long id) {
        SpendCategory category = getCategoryById(id);
        category.setActive(false);
        repository.save(category);
    }
}
