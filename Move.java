/**
 * @(#)Move.java
 * Cameron Beneteau
 * 
 * This class is used to store the properties of each attack of 
 * each Pokemon object from the Pokemon class. It hold the name,
 * energy cost, damage and ability of the move. This class can 
 * returns all or the specific properties of the move.
 * 
 * January 8th, 2017
 * @version 1.00 2017/12/6
 */

import java.io.*;
import java.util.*;

public class Move {
	private String name;
	private int energy;
	private int damage;
	private String ability;
	
    public Move(String name , int energy, int damage, String ability) { //called in the Pokemon class
    	this.name = name;
    	this.energy = energy;
    	this.damage = damage;
    	this.ability = ability;
    }
    
    public String moveInfo(){
    	if (ability.equals(" ")){
    		return name + " (" + energy + " energy | " + damage + " damage | No ability)";
    	}
    	else{
    		return name + " (" + energy + " energy | " + damage + " damage | " + ability +")";
    	}
    }
    
    public int getEnergy(){
    	return energy;
    }
    
    public int getDamage(){
    	return damage;
    }
    
    public String getAbility(){
    	return ability;
    }
    
    public String getName(){
    	return name;
    }
}