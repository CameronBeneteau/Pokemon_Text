/**
 * @(#)Pokemon.java
 * Cameron Beneteau
 * 
 * This class is used to hold all the information of a Pokemon from the
 * Pokemon Arena file by creating a Pokemon object. It stores the name,
 * energy, max health, health, type, weakness, resistance, if stunned,
 * if disabled and move(s). This class returns all or the specific
 * properties of the Pokemon. It also deals with attacking (dealing
 * damage and special abilities) and increasing the energy and health
 * of the Pokemon.
 * 
 * January 8th, 2017
 * @version 1.00 2017/12/3
 */

import java.io.*;
import java.util.*;

public class Pokemon {
	private String name;
	private int energy = 50;
	private int maxHp;
	private int hp;
	private String type;
	private String resistance;
	private String weakness;
	private boolean stun = false;
	private int disable = 0; //rotates between 0 and 10 (10 means disabled and just subtract it from damage during the damage phase)
	private ArrayList<Move>moves = new ArrayList<Move>(); //move(s) for a Pokemon object
	
    public Pokemon(String[] pokeInfo) { //constructor method
		name = pokeInfo[0];
		maxHp = Integer.parseInt(pokeInfo[1]);
		hp = Integer.parseInt(pokeInfo[1]);
		type = pokeInfo[2];
		resistance = pokeInfo[3];
		weakness = pokeInfo[4];
		for (int i = 0; i < Integer.parseInt(pokeInfo[5]); i++){ //running through each move a Pokemon has and creating an object with its properties
			moves.add(new Move(pokeInfo[6 + i * 4], Integer.parseInt(pokeInfo[6 + i * 4 + 1]), Integer.parseInt(pokeInfo[6 + i * 4 + 2]), pokeInfo[6 + i * 4 + 3]));
		}
    }
    
    public String getName(){
    	return name;
    }
    
    public int getHp(){
    	return hp;
    }
    
    public int getMaxHp(){
    	return maxHp;
    }
    
    public int getEnergy(){
    	return energy;
    }
    
    public String getType(){
    	return type;
    }
    
    public String getWeakness(){
    	return weakness;
    }
    
    public String getResistance(){
    	return resistance;
    }
    public String getInformation(){
    	return name + "\nHp: " + hp + "\nEnergy:" + energy + "\n";
    }
    
    public int getNumMoves(){
     	int num = 0;
     	for (int i = 0; i < moves.size(); i++){
     		num += 1;
     	}
     	return num;
    }
    
    public String getMove(int num){
    	return moves.get(num).moveInfo();
    }
    
    public String getMoveName(int num){
    	return moves.get(num).getName();
    }
    
    public int getMoveDamage(int num){
    	return moves.get(num).getDamage();
    }
    
    public int getMoveEnergy(int num){
    	return moves.get(num).getEnergy();
    }
    
    public String getMoveAbility(int num){
    	return moves.get(num).getAbility();
    }
    
    public boolean getStun(){
    	return stun;
    }
    
    public void resetDisable(){
    	disable = 0;
    }
    
    public void resetStun(){
    	stun = false;
    }
    
    public void attackOne(Pokemon enemy, int choice){  //ability check of attack phase (will call attackTwo method if an attack is needed/allowed)
    	Random rand = new Random();
    	if (!getMoveAbility(choice).equals("wild storm")){
    		System.out.println("\n" + name + " used " + getMoveName(choice));
    	}
    	if (getMoveAbility(choice).equals("recharge")){ //recharge ability
    		System.out.println(name + " gained 20 energy");
    		energy += 20;
    		if (energy > 50){ //cannot exceed max energy
    			energy = 50;
    		}
    		attackTwo(enemy, choice); //call attack
    	}
    	else if (getMoveAbility(choice).equals("disable")){ //disable ability
    		if (enemy.disable == 10){
    			System.out.println(enemy.getName() + " has already been disabled");
    		}
    		else{
    			System.out.println(name + " disabled " + enemy.getName());
    			enemy.disable = 10; //subtract this variable from the damage
    		}
    		attackTwo(enemy, choice);
    	}
    	else if (getMoveAbility(choice).equals("stun")){ //stun ability
    		int chance = rand.nextInt(101);
    		if (chance < 50){ //stun is passed
    			System.out.println(name + " stunned " + enemy.getName());
    			enemy.stun = true;
    		}
    		attackTwo(enemy, choice);
    	}
    	else if (getMoveAbility(choice).equals("wild card")){ //wild card ability
    		int chance = rand.nextInt(101);
    		if (chance < 50){ //attack is passed
    			attackTwo(enemy, choice);
    		}
    		else{
    			energy -= moves.get(choice).getEnergy(); //attacker loses energy (even if the attack is a miss)
    			System.out.println(name + "'s attack missed\n");
    		}
    	}
    	else if (getMoveAbility(choice).equals("wild storm")){
    		int chance = 0;
    		chance = rand.nextInt(101);
    		energy -= moves.get(choice).getEnergy(); //attacker loses energy (even if the attack is a miss)
    		while (true){
    			if (chance < 50 && enemy.getHp() > 0){ //attack is passed and enemy is still alive
    				System.out.println("\n" + name + " used " + getMoveName(choice));
    				attackTwo(enemy, choice);
    				//System.out.println("\n" + name + " used " + getMoveName(choice));
    			}
    			else if (enemy.getHp() <= 0){ //enemy is already dead
    				break;
    			}
    			else{ //attack missed
    				System.out.println("\n" + name + " used " + getMoveName(choice));
    				System.out.println(name + "'s wild storm attack missed\n");
    				break;
    			}
    			chance = rand.nextInt(101);
    		}
    	}
    	else{ //no special ability
    		attackTwo(enemy, choice);
    	}
    }
    public void attackTwo(Pokemon enemy, int choice){
    	if (type.equals(enemy.getWeakness())){ //checking if half, double, or regular damage is needed
    		if (getMoveDamage(choice) >= 10){
    			enemy.hp -= (moves.get(choice).getDamage() - disable) * 2; //enemy takes two times damage
    			System.out.println(enemy.getName() + " took " + (getMoveDamage(choice) - disable) * 2 + " damage from " + name);
    		}
    		else if (disable == 10 && getMoveDamage(choice) < 10){
    			System.out.println(enemy.getName() + " took 0 damage from " + name + "\n"); //disable brings the damage to 0 if the damage less than 10
    		}
    		else{
    			enemy.hp -= moves.get(choice).getDamage() * 2; //enemy takes two times damage
    			System.out.println(enemy.getName() + " took " + getMoveDamage(choice) * 2 + " damage from " + name);
    		}
    		System.out.println("Super effective! (2x damage)\n");
    	}
    	else if (type.equals(enemy.getResistance())){
    		if (getMoveDamage(choice) >= 10){
    			enemy.hp -= (moves.get(choice).getDamage() - disable) / 2; //enemy takes half damage
    			System.out.println(enemy.getName() + " took " + (getMoveDamage(choice) - disable) / 2 + " damage from " + name);
    		}
    		else if (disable == 10 && getMoveDamage(choice) < 10){
    			System.out.println(enemy.getName() + " took 0 damage from " + name + "\n"); //disable brings the damage to 0 if the damage less than 10
    		}
    		else{
    			enemy.hp -= moves.get(choice).getDamage() / 2; //enemy takes half damage
    			System.out.println(enemy.getName() + " took " + getMoveDamage(choice) / 2 + " damage from " + name);
    		}
    		System.out.println("Not very effective! (0.5x damage)\n");
    	}
    	else{
    		if (getMoveDamage(choice) >= 10){
    			enemy.hp -= (moves.get(choice).getDamage() - disable); //enemy takes regular damage
    			System.out.println(enemy.getName() + " took " + (getMoveDamage(choice) - disable) + " damage from " + name + "\n");
    		}
    		else if (disable == 10 && getMoveDamage(choice) < 10){
    			System.out.println(enemy.getName() + " took 0 damage from " + name + "\n"); //disable brings the damage to 0 if the damage less than 10
    		}
    		else{
    			enemy.hp -= moves.get(choice).getDamage(); //enemy takes regular damage
    			System.out.println(enemy.getName() + " took " + getMoveDamage(choice) + " damage from " + name + "\n");
    		}
    	}
    	if (enemy.hp < 0){ //cannot be less than 0
    		enemy.hp = 0;
    	}
    	
    	if (!getMoveAbility(choice).equals("wild storm")){ //energy for this ability was already subtracted in the attackOne method
    		energy -= moves.get(choice).getEnergy(); //attacker loses energy
	    	if (energy < 0){ //energy cannot be less than 0
	    		energy = 0;
	    	}
    	}
    }
    
    public void regenEnergy(){ //increase energy (used after each turn)
    	energy += 10;
    	if (energy > 50){ //cannot exceed max energy
    		energy = 50;
    	}
    }
    
    public void resetEnergy(){ //increase energy (used after each battle)
    	energy = 50;
    }
    
    public void regenHealth(){ //heal pokemon (used after each battle)
    	hp += 20;
    	if (hp > maxHp){ //cannot exceed max health
    		hp = maxHp;
    	}
    }
    
    public ArrayList<Move> getMoves(){
    	return moves;
    }
}