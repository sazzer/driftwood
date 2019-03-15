package http

// Health represents the current health of the system
type Health struct {
	Status string            `json:"status"`
	Errors map[string]string `json:"errors"`
}
