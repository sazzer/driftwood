package dao

import (
	"github.com/sazzer/driftwood/internal/characters/attributes"
)

// GetByID represents a way that we can retrieve an Attribute by it's unique ID
func (dao AttributeDaoImpl) GetByID(id attributes.AttributeID) (attributes.Attribute, error) {
	//rows, err := dao.db.Query("SELECT * FROM attributes WHERE attribute_id = ?", id)

	return attributes.Attribute{}, nil
}
