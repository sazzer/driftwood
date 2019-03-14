package dao

// AttributeDaoImpl represents the actual implementationof AttributeDao
type AttributeDaoImpl struct {
}

// New creates a new instance of the Attribute DAO
func New() AttributeDao {
	return AttributeDaoImpl{}
}
