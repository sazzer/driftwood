package service

import (
	"github.com/sazzer/driftwood/internal/characters/attributes"
)

// GetByID represents a way that we can retrieve an Attribute by it's unique ID
func (a AttributeService) GetByID(id attributes.AttributeID) *attributes.Attribute {
	return a.dao.GetByID(id)
}
