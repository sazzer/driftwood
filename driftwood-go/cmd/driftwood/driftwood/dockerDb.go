// +build docker

package driftwood

import (
	"fmt"

	"github.com/sazzer/driftwood/utils/docker"
)

// DbWrapper is a wrapper around the embedded database
type DbWrapper struct {
	wrapper docker.Wrapper
}

// LaunchDb will launch a new database
func (d *DbWrapper) LaunchDb() {
	d.wrapper = docker.New("postgres:10.6-alpine", []uint16{5432})
	err := d.wrapper.Start()
	if err != nil {
		panic(err)
	}
}

// StopDb will stop the database running
func (d *DbWrapper) StopDb() {
	err := d.wrapper.Stop()
	if err != nil {
		panic(err)
	}
}

// GetConnectionURL will get the connection details to connect to the database
func (d *DbWrapper) GetConnectionURL() string {
	return fmt.Sprintf("host=127.0.0.1 port=%d user=postgres password=postgres dbname=postgres sslmode=disable",
		d.wrapper.GetPortMapping(5432))
}
