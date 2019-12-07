/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TempleateRDB.TempleatePostgreSQL;

import GeneradorSQL.GeneradorPostgreSQL;
import TempleateRDB.GenericTempleate.Attribute;

/**
 *
 * @author wilmer-PC
 */
public class AttributePostgreSQL extends Attribute{
    
    public AttributePostgreSQL(String nombre, String type) {
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
    
    public AttributePostgreSQL(String nombre, String type, String defaultValue, String length, boolean nullable) {
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
    
    public String toSql(GeneradorPostgreSQL gen) {
        return gen.SingleAddColumnTable(type, name, length, defaultValue, !nullable);
    }
}
