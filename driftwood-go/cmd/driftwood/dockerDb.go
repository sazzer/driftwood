// +build docker

package main

import (
	"github.com/sazzer/driftwood/utils/docker"
)

type dbWrapper struct {
	wrapper docker.Wrapper
}

func (d *dbWrapper) launchDb() {
	d.wrapper = docker.New("postgres:10.6-alpine", []uint16{5432})
	err := d.wrapper.Start()
	if err != nil {
		panic(err)
	}
}

func (d *dbWrapper) stopDb() {
	err := d.wrapper.Stop()
	if err != nil {
		panic(err)
	}
}
