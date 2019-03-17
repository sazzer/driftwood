package database

import (
	"database/sql"
)

// DB represents a wrapper around the database connection
type DB struct {
	conn *sql.DB
}

// New creates a new Database wrapper
func New(conn *sql.DB) DB {
	return DB{
		conn: conn,
	}
}
