package health

//go:generate mockgen -destination=mocks/mock_healthchecker.go -package=mocks github.com/sazzer/driftwood/internal/health Healthchecker

// Healthchecker represents an interface that can check the health of some part of the system
type Healthchecker interface {
	CheckHealth() error
}
