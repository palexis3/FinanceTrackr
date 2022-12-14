import com.patrickpie12345.helper.TimeToSearch
import com.patrickpie12345.models.product.Product
import com.patrickpie12345.models.store.Store
import java.time.OffsetDateTime
import java.util.*

data class ProductStoresAnalyticsRequest(
    val timeToSearch: TimeToSearch? = null,
    val store: String? = null
)

data class ProductStoresDBAnalyticsRequest(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val store: String?
)

data class ProductStoresAnalyticsResponse(
    val startDate: String,
    val endDate: String,
    val items: List<StoreAndProducts>
)

data class StoreAndProducts(
    val store: Store?,
    val products: List<Product>?,
    val total: String
)

data class ProductStoreSum(
    val storeId: UUID,
    val total: Float
)
