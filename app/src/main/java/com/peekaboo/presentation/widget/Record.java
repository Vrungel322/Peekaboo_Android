package com.peekaboo.presentation.widget;

public class Record {

//    public enum Type {RED, GREEN, YELLOW}

    private String name;
//    private Type type;
    int resourceId;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

//    public Type getType() {
//        return type;
//    }

//    public void setType(Type type) {
//        this.type = type;
//    }

    public void Record (String name, int resourceId) {
        this.name = name;
        this.resourceId = resourceId;
    }

//    public Record copy(){
//        Record copy = new Record();
////        copy.setType(type);
//        copy.setName(name);
//        return copy;
//    }
}
