package main

import "errors"

type simpleHealth struct{}

func (s simpleHealth) CheckHealth() error {
	return errors.New("Oops")
}
