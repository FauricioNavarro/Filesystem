/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Fauricio
 */
public class file {
    private String name;
    private String type;
    private String conten;

    public file(String name, String type, String conten) {
        this.name = name;
        this.type = type;
        this.conten = conten;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConten() {
        return conten;
    }

    public void setConten(String conten) {
        this.conten = conten;
    }

    @Override
    public String toString() {
        return "file{" + "name=" + name + ", type=" + type + ", conten=" + conten + '}';
    }
    
    
}
