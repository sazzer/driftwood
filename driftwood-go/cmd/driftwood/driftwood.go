package main

func main() {
	cfg := loadConfig()

	configureLogging(cfg)

	dbWrapper := dbWrapper{}
	dbWrapper.launchDb()
	defer dbWrapper.stopDb()

	server := buildServer()

	server.Start(cfg.Port)

}
