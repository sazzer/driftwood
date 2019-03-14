package http

// Attribute represents the HTTP Response for a single Attribute
type Attribute struct {
	ID          string `json:"id"`
	Name        string `json:"name"`
	Description string `json:"description"`
}
