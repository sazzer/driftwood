package main

import (
	"database/sql"
	"time"

	"github.com/sirupsen/logrus"

	"github.com/gobuffalo/packr"
	"github.com/rubenv/sql-migrate"
)

func migrateDb(db *sql.DB) error {
	migrations := &migrate.PackrMigrationSource{
		Box: packr.NewBox("../../migrations"),
	}

	time.Sleep(10 * time.Second)
	n, err := migrate.Exec(db, "postgres", migrations, migrate.Up)
	if err != nil {
		logrus.WithError(err).Error("Failed to migrate database")
		return err
	}
	logrus.WithField("migrations", n).Debug("Migrated databas")
	return nil
}
