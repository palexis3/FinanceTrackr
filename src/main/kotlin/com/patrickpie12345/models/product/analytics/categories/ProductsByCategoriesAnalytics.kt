import com.patrickpie12345.helper.TimeToSearch
import com.patrickpie12345.models.product.Product
import com.patrickpie12345.models.product.ProductCategory
import java.time.OffsetDateTime

data class ProductCategoryAnalyticsRequest(
    val timeToSearch: TimeToSearch? = null,
    val category: ProductCategory? = null
)

data class ProductCategoryDBAnalyticsRequest(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val category: String?
)

data class ProductCategoryAnalyticsResponse(
    val startDate: String,
    val endDate: String,
    val items: List<CategoryAndProducts>
)

data class CategoryAndProducts(
    val productCategory: String,
    val products: List<Product>,
    val total: String
)

data class ProductCategorySum(
    val productCategory: String,
    val total: Float
)
