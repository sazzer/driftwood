package attributes

// Retriever represents a means to retrieve information about attributes
type Retriever interface {
	// GetByID represents a way that we can retrieve an Attribute by it's unique ID
	GetByID(AttributeID) *Attribute
}
