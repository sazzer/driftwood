package database

// CheckHealth checks that the database connection is healthy
func (db DB) CheckHealth() error {
	_, err := db.conn.Exec("SELECT 1")
	return err
}
