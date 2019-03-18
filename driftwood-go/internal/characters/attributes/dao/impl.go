package dao

import (
	"github.com/sazzer/driftwood/internal/database"
)

// AttributeDaoImpl represents the actual implementationof AttributeDao
type AttributeDaoImpl struct {
	db *database.DB
}

// New creates a new instance of the Attribute DAO
func New(db *database.DB) AttributeDao {
	return AttributeDaoImpl{db}
}
