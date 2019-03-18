package driftwood

// Config represents the environment configuration for the application
type Config struct {
	// Debug represents whether we are executing in Debug mode or not
	Debug    bool   `config:"debug"`
	Port     int    `config:"port"`
	Database string `config:"DB_URL"`
}
