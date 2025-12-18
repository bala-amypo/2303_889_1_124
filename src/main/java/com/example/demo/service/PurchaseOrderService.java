@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository poRepo;
    private final SupplierRepository supplierRepo;
    private final SpendCategoryRepository categoryRepo;

    public PurchaseOrderService(PurchaseOrderRepository poRepo,
                                SupplierRepository supplierRepo,
                                SpendCategoryRepository categoryRepo) {
        this.poRepo = poRepo;
        this.supplierRepo = supplierRepo;
        this.categoryRepo = categoryRepo;
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrder po) {
        if (po.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be > 0");
        }
        return poRepo.save(po);
    }

    public List<PurchaseOrder> getPurchaseOrdersBySupplier(Long supplierId) {
        return poRepo.findBySupplier_Id(supplierId);
    }
}
