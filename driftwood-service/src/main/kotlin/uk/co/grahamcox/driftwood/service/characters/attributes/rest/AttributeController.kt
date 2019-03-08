package uk.co.grahamcox.driftwood.service.characters.attributes.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeData
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeId
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeNotFoundException
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeRetriever
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.rest.problem.Problem
import java.net.URI
import java.util.*

/**
 * Controller class for working with attributes
 */
@RestController
@RequestMapping("/api/attributes")
class AttributeController(private val attributeRetriever: AttributeRetriever) {
    /**
     * Get a Attribute by the unique ID
     * @param id The ID of the attribute
     * @return the attribute details
     */
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun getAttributeById(@PathVariable("id") id: UUID) : IdentifiedAttributeModel {
        val attributeId = AttributeId(id)

        val attribute = attributeRetriever.getById(attributeId)

        return buildAttributeResponse(attribute)
    }

    /**
     * Build the REST API response representing a single attribute
     * @param attribute The attribute to build the response for
     * @return the REST API response
     */
    private fun buildAttributeResponse(attribute: Resource<AttributeId, AttributeData>): IdentifiedAttributeModel {
        return IdentifiedAttributeModel(
                id = attribute.identity.id.id.toString(),
                attribute = AttributeModel(
                        name = attribute.data.name,
                        description = attribute.data.description
                )
        )
    }

    /**
     * Handle when the requested attribute was not found
     */
    @ExceptionHandler(AttributeNotFoundException::class)
    fun unknownAttribute() = Problem(
            type = URI("tag:2018,grahamcox.co.uk:attributes/problems/not-found"),
            title = "Requested attribute was not found",
            statusCode = HttpStatus.NOT_FOUND
    )
}
