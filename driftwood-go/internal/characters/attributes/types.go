package attributes

import "time"

// AttributeID represents the ID of an attribute
type AttributeID string

// Attribute represents an attribute that a character has
type Attribute struct {
	id          AttributeID
	created     time.Time
	updated     time.Time
	version     string
	name        string
	description string
}
