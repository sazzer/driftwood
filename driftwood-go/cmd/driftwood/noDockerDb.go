// +build !docker

package main

type dbWrapper struct{}

func (dbWrapper) launchDb() {
	panic("No Docker Support")
}

func (dbWrapper) stopDb() {
	panic("No Docker Support")
}
