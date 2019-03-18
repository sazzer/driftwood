package dao

import (
	"time"

	"github.com/sazzer/driftwood/internal/characters/attributes"
	"github.com/sirupsen/logrus"
)

type dbAttribute struct {
	ID          string    `db:"attribute_id"`
	Created     time.Time `db:"created"`
	Updated     time.Time `db:"updated"`
	Version     string    `db:"version"`
	Name        string    `db:"name"`
	Description string    `db:"description"`
}

// GetByID represents a way that we can retrieve an Attribute by it's unique ID
func (dao AttributeDaoImpl) GetByID(id attributes.AttributeID) (attributes.Attribute, error) {
	rows, err := dao.db.Query("SELECT * FROM attributes WHERE attribute_id = :id",
		map[string]interface{}{"id": string(id)})

	if err != nil {
		logrus.WithField("id", id).WithError(err).Error("Failed to load attribute")
		return attributes.Attribute{}, err
	}
	defer rows.Close()

	if !rows.Next() {
		logrus.WithField("id", id).Debug("No matching attributes found")
		return attributes.Attribute{}, nil
	}

	resultRow := dbAttribute{}
	err = rows.StructScan(&resultRow)
	if err != nil {
		logrus.WithField("id", id).WithError(err).Error("Failed to parse attribute")
		return attributes.Attribute{}, err
	}

	logrus.WithField("id", id).WithField("row", resultRow).Debug("Loaded attribute data")

	return attributes.Attribute{
		ID:          attributes.AttributeID(resultRow.ID),
		Version:     resultRow.Version,
		Created:     resultRow.Created,
		Updated:     resultRow.Updated,
		Name:        resultRow.Name,
		Description: resultRow.Description,
	}, nil
}
