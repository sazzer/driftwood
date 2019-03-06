package uk.co.grahamcox.driftwood.e2e.populator

import org.junit.Assert
import kotlin.reflect.KMutableProperty1

/**
 * Mechanism to populate a single bean to the provided data
 */
class BeanPopulator<T>(private val fields: Map<String, FieldDefinition<T>>) {
    /** Representation of a field that can be matched against */
    data class FieldDefinition<T>(
            val populator: KMutableProperty1<T, String>
    )

    /**
     * Match the expected and actual values to see if they differ
     * @param expected The expected values
     * @param actual The actual values
     */
    fun populate(expected: Map<String, String>, actual: T) {
        val unexpected = expected.keys
                .filterNot(fields::containsKey)
        Assert.assertTrue("Unexpected fields: $unexpected", unexpected.isEmpty())

        fields.entries
                .filter { expected.containsKey(it.key) }
                .forEach { (field, definition) ->
                    val converted = expected.getValue(field)
                    definition.populator.set(actual, converted)
                }
    }
}
