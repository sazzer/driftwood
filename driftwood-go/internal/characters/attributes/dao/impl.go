package dao

import "database/sql"

// AttributeDaoImpl represents the actual implementationof AttributeDao
type AttributeDaoImpl struct {
	db *sql.DB
}

// New creates a new instance of the Attribute DAO
func New(db *sql.DB) AttributeDao {
	return AttributeDaoImpl{db}
}
