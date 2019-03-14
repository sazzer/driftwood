package attributes

import "time"

// AttributeID represents the ID of an attribute
type AttributeID string

// Attribute represents an attribute that a character has
type Attribute struct {
	ID          AttributeID
	Created     time.Time
	Updated     time.Time
	Version     string
	Name        string
	Description string
}

// UnknownAttributeError is an error to indicate that the requested attribute does not exist
type UnknownAttributeError struct{}

// Error returns the error message for an Unknown Attribute error
func (e UnknownAttributeError) Error() string {
	return "The requested attribute does not exist"
}
