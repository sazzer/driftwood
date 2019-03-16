package server

import (
	"fmt"

	"github.com/facebookgo/grace/gracehttp"

	"github.com/labstack/echo"
	"github.com/labstack/echo/middleware"

	log "github.com/sirupsen/logrus"
)

// HandlerRegistrationFunc represents a mechanism to register handlers with the server
type HandlerRegistrationFunc = func(*echo.Echo)

// Server represents the actual HTTP Server that we are using
type Server struct {
	server *echo.Echo
}

// New creates a new HTTP Server
func New() Server {
	e := echo.New()
	e.Use(middleware.RequestID())
	e.Pre(middleware.RemoveTrailingSlash())
	e.Use(middleware.CORSWithConfig(middleware.CORSConfig{}))
	e.Use(middleware.GzipWithConfig(middleware.GzipConfig{
		Level: 5,
	}))
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())

	return Server{
		server: e,
	}
}

// RegisterHandlers will allow the provided HandlerRegistrationFunc to register handlers with the server
func (s *Server) RegisterHandlers(f HandlerRegistrationFunc) {
	f(s.server)
}

// Start will start the server listening on the given port
func (s *Server) Start(port int) error {
	address := fmt.Sprintf(":%d", port)
	log.WithFields(log.Fields{
		"address": address,
	}).Info("Starting server")

	s.server.Server.Addr = address
	err := gracehttp.Serve(s.server.Server)
	if err != nil {
		log.WithError(err).Error("Failed to start server")
	}

	log.Info("Stopping Server")
	return err
}
