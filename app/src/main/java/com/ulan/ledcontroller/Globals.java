package com.ulan.ledcontroller;

class Globals{
    private static Globals instance;

    // Global variable
    private int data;

    // Restrict the constructor from being instantiated
    protected Globals(){}

    void setData(int d){
        this.data=d;
    }
    int getData(){
        return this.data;
    }

    static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}