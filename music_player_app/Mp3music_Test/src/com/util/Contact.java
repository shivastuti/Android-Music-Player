package com.util;

public class Contact {
	
	//private variables
	int _id;
	String _name;
	String _filename;
	String time;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	// Empty constructor
	public Contact(){
		
	}
	// constructor
	public Contact(int id, String name, String _filename){
		this._id = id;
		this._name = name;
		this._filename = _filename;
	}
	
	// constructor
	public Contact(String name, String _filename, String time){
		this._name = name;
		this._filename = _filename;
		this.time = time;
	}
	// getting ID
	public int getID(){
		return this._id;
	}
	
	// setting id
	public void setID(int id){
		this._id = id;
	}
	
	// getting name
	public String getName(){
		return this._name;
	}
	
	// setting name
	public void setName(String name){
		this._name = name;
	}
	
	// getting phone number
	public String getFilename(){
		return this._filename;
	}
	
	// setting phone number
	public void setFilename(String filename){
		this._filename = filename;
	}
}
