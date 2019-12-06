package TempleateRDB.TempleateMySQL;

import GeneradorSQL.GeneradorMySQL;
import TempleateRDB.GenericTempleate.Attribute;

/**
 *
 * @author wilmer-PC
 */
public class AttributeMySQL extends Attribute {

    public AttributeMySQL(String nombre, String type) {
        super.name = nombre;
        super.type = type;
        super.defaultValue = null;
        super.superType = "String";
        super.length = "0";
        nullable = true;
        primaryKey = null;
        uniqueKey = null;
        foreignKey = null;
    }

    public AttributeMySQL(String nombre, String type, String defaultValue, String length, boolean nullable) {
        super.name = nombre;
        super.type = type;
        super.defaultValue = defaultValue;
        super.length = length;
        super.nullable = nullable;
        primaryKey = null;
        uniqueKey = null;
        foreignKey = null;

        super.superType = type;
    }

    public String toSql(GeneradorMySQL gen) {
        return gen.SingleAddColumnTable(type, name, length, defaultValue, !nullable);
    }
}
