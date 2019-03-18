package database

import (
	"database/sql"

	"github.com/jmoiron/sqlx"
)

// DB represents a wrapper around the database connection
type DB struct {
	conn *sqlx.DB
}

// New creates a new Database wrapper
func New(conn *sql.DB) DB {
	return DB{
		conn: sqlx.NewDb(conn, "postgres"),
	}
}

// Query will execute a query against the database and return the resultset
func (db DB) Query(sql string, args interface{}) (*sqlx.Rows, error) {
	return db.conn.NamedQuery(sql, args)
}

// Exec will execute a query and return a result counter instead of a resultset
func (db DB) Exec(sql string, args interface{}) (sql.Result, error) {
	return db.conn.NamedExec(sql, args)
}
