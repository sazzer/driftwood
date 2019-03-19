package driftwood

import (
	"database/sql"
	"fmt"
	"os/exec"
	"time"

	"github.com/sirupsen/logrus"

	"github.com/gobuffalo/packr"
	"github.com/rubenv/sql-migrate"
)

func migrateDb(db *sql.DB) error {
	migrations := &migrate.PackrMigrationSource{
		Box: packr.NewBox("../../../migrations"),
	}

	logrus.Info("Waiting...")
	time.Sleep(60 * time.Second)

	out, err := exec.Command("docker", "ps").Output()
	if err != nil {
		logrus.WithError(err).Error("Command failed")
	}
	fmt.Printf("The output is %s\n", out)

	n, err := migrate.Exec(db, "postgres", migrations, migrate.Up)
	if err != nil {
		logrus.WithError(err).Error("Failed to migrate database")
		return err
	}
	logrus.WithField("migrations", n).Debug("Migrated databas")
	return nil
}
