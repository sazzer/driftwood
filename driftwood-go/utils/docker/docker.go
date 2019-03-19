package docker

import (
	"context"
	"fmt"
	"io"
	"os"
	"time"

	"docker.io/go-docker"
	"docker.io/go-docker/api/types"
	"docker.io/go-docker/api/types/container"
	"github.com/docker/go-connections/nat"
	"github.com/phayes/freeport"
	"github.com/sirupsen/logrus"
)

// Wrapper represents a wrapper around a Docker container
type Wrapper struct {
	container    string
	ports        []uint16
	portMappings map[uint16]int
	containerID  string
}

// New creates a new wrapper around a Docker Container that we can control as needed
func New(container string, ports []uint16) Wrapper {
	return Wrapper{
		container:    container,
		ports:        ports,
		portMappings: make(map[uint16]int),
	}
}

// Start will start the Docker container that we are referring to
func (wrapper *Wrapper) Start() error {
	containerConfig := container.Config{
		Image: wrapper.container,
	}

	hostConfig := container.HostConfig{
		PortBindings: map[nat.Port][]nat.PortBinding{},
		AutoRemove:   true,
	}

	for _, port := range wrapper.ports {
		freePort, err := freeport.GetFreePort()
		if err != nil {
			logrus.WithError(err).WithField("container", wrapper.container).Error("Failed to find free port")
			return err
		}
		logrus.WithField("port", port).
			WithField("mapping", freePort).
			WithField("container", wrapper.container).
			Debug("Mapping port")
		wrapper.portMappings[port] = freePort

		containerPort := fmt.Sprintf("%d/tcp", port)
		localPort := fmt.Sprintf("%d/tcp", freePort)

		hostConfig.PortBindings[nat.Port(containerPort)] = []nat.PortBinding{nat.PortBinding{
			HostIP:   "127.0.0.1",
			HostPort: localPort,
		}}
	}

	cli, err := docker.NewEnvClient()
	if err != nil {
		logrus.WithError(err).WithField("container", wrapper.container).Error("Failed to create Docker Connection")
		return err
	}
	defer cli.Close()

	if closer, err := cli.ImagePull(context.Background(), wrapper.container, types.ImagePullOptions{}); err != nil {
		panic(err)
	} else {
		io.Copy(os.Stdout, closer)
	}

	container, err := cli.ContainerCreate(context.Background(), &containerConfig, &hostConfig, nil, "")
	if err != nil {
		logrus.WithError(err).WithField("container", wrapper.container).Error("Failed to create container")
		return err
	}

	err = cli.ContainerStart(context.Background(), container.ID, types.ContainerStartOptions{})
	if err != nil {
		logrus.WithError(err).WithField("container", wrapper.container).Error("Failed to start container")
		return err
	}

	logrus.WithField("container", wrapper.container).Debug("Started container")

	wrapper.containerID = container.ID
	return nil
}

// Stop will stop the Docker container that we are referring to
func (wrapper *Wrapper) Stop() error {
	logrus.WithField("container", wrapper.container).Debug("Stopping container")
	cli, err := docker.NewEnvClient()
	if err != nil {
		logrus.WithError(err).WithField("container", wrapper.container).Error("Failed to create Docker Connection")
		return err
	}
	defer cli.Close()

	timeout := 10 * time.Second
	err = cli.ContainerStop(context.Background(), wrapper.containerID, &timeout)
	if err != nil {
		logrus.WithError(err).WithField("container", wrapper.container).Error("Failed to stop container")
		return err
	}

	return nil
}

// GetPortMapping will return the local port number that refers to the specified one
func (wrapper *Wrapper) GetPortMapping(port uint16) int {
	return wrapper.portMappings[port]
}
