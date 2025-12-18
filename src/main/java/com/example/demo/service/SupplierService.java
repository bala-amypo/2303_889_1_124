@Service
public class SupplierService {
private final SupplierRepository repo;


public SupplierService(SupplierRepository repo) {
this.repo = repo;
}
}