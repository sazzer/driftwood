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
