// +build !docker

package driftwood

// DbWrapper is a wrapper around the embedded database
type DbWrapper struct{}

// LaunchDb will launch a new database
func (DbWrapper) LaunchDb() {
	panic("No Docker Support")
}

// StopDb will stop the database running
func (DbWrapper) StopDb() {
	panic("No Docker Support")
}

// GetConnectionURL will get the connection details to connect to the database
func (DbWrapper) GetConnectionURL() string {
	panic("No Docker Support")
}
