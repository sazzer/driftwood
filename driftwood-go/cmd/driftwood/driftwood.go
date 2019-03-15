package main

func main() {
	cfg := loadConfig()

	configureLogging(cfg)

	server := buildServer()

	server.Start(cfg.Port)
}
