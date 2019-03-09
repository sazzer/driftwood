package uk.co.grahamcox.driftwood.service.rest.problem

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.HttpStatus
import java.net.URI

/**
 * Representation of a problem in a REST call
 * @property type The type code for the problem
 * @property title The english description of the problem
 * @property statusCode The status code for the problem
 * @property instance The instance URI for this request
 * @property detail The detail of the problem
 * @property extraData Any extra data for the problem
 * @property status The status code for the problem
 */
data class Problem(
        val type: URI,
        val title: String,
        @JsonIgnore val statusCode: HttpStatus,
        val instance: URI? = null,
        val detail: String? = null,
        @get:JsonAnyGetter val extraData: Map<String, Any?> = emptyMap()
) {
    val status = statusCode.value()
}
