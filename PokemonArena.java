/**
 * @(#)PokemonTest.java
 * Cameron Beneteau
 * 
 * This class runs the game by using its own methods along
 * with the Pokemon and Move classes. It reads in the text
 * file with the information of Pokemon. It sorts the Pokemon
 * into user and enemy based on the users inputs and runs
 * through battles with enemy Pokemon one by one. It allows
 * the user to attack, retreat or pass and controls the enemy
 * Pokemon. It is responsible for healing the Pokemons health
 * and energy. It check to see if Pokekon can attack with the 
 * amount of energy they have and calls methods from the Pokemon
 * class to deal damage and display the Pokemons stats. It also
 * checks for fainted Pokemon and if the user has beaten all 
 * enemy Pokemon or the enemy Pokemon have defeated all the
 * users Pokemon.
 * 
 * January 8th, 2017
 * @version 1.00 2017/12/3
 */

import java.io.*;
import java.util.*;

public class PokemonArena {
	private static ArrayList<Pokemon>pokes; //array list of all Pokemon objects
	private static ArrayList<Pokemon>playerPokes; //array list of Pokemon objects the player has chosen
	private static ArrayList<Pokemon>enemyPokes; //array list of all enemy (remaining) Pokemon
	private static ArrayList<Pokemon>currentPoke; //array list of the currentPokemon battling for the user
	private static int n; //total number of Pokemon
	
    public static void main(String[]args) { //main method
    	Scanner kb = new Scanner(System.in);
    	readFile(); //read in pokemon
    	System.out.println("             _                       _                        ");
    	System.out.println("            |_) _  |  _ __  _ __    |_| __ _ __  _            ");
    	System.out.println("            |  (_) |<(/_|||(_)| |   | | | (/_| |(_|           \n");
    	System.out.println("===============================================================\n");
    	System.out.println("Welcome to The Pulse Pokemon Challenge. You will be asked to");
    	System.out.println("select 4 Pokemon and will then be sent into a series of");
    	System.out.println("battles. Your goal is to defeat each and every enemy Pokemon");
    	System.out.println("to acheive the status 'Trainer Supreme.' Fight hard, be smart,");
    	System.out.println("but most of all, have fun! Good luck trainer!\n");
    	System.out.println("=====================PRESS ENTER TO START!====================");
    	kb.nextLine();
    	System.out.println("LET THE CHALLENGE BEGIN...\n");
    	setPlayerPokemon(); //user picks pokemon
    	setEnemyPokemon(); //enemy pokemon are randomized
    	while (true){ //runs until all enemy pokemon or user pokemon have fainted 
		    System.out.println("\nA NEW BATTLE BEGINS...\n");
		    choseBattlePoke(); //user choses which pokemon will battle
		    System.out.println("You are battling " + enemyPokes.get(0).getName() + " (" + enemyPokes.get(0).getType() + ")\n" );
		    if (whoStarts() == 1){
		    	System.out.println("YOU START\n");
		    	while (validBattle()){ //runs until a battle is won by user or enemy
		    		pokeStats(); //display pokemon stats
		    		userTurn(); //attack, retreat, pass
		    		if (validBattle() == false){ //check for status of enemy pokemon (alive or fainted)
		    			break;
		    		}
		    		pokeStats();
		    		enemyTurn(); //attack, pass
		    		if (validBattle() == false){ //check for status of current user pokemon (alive or fainted)
		    			break;
		    		}
		    		regenEnergy(); //give 10 energy to each pokemon
		    	}
		    }
			else{
				System.out.println("ENEMY STARTS\n");
				while (validBattle()){
					pokeStats();
					enemyTurn();
					if (validBattle() == false){
		    			break;
		    		}
					pokeStats();
					userTurn();
					if (validBattle() == false){
		    			break;
		    		}
					regenEnergy();
				}
			}
			if (validGame() == false){ //check if all user pokemon are dead or all enemy pokemon are dead
				break;
			}
			else{
				healAfterBattle(); //give each Pokemon 20 health after battle
			}
    	}
    }
    
    public static void readFile(){ //reads the pokemon.txt file
    	pokes = new ArrayList<Pokemon>(); 
    	
    	try{
	    	Scanner inFile = new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
	    	n = inFile.nextInt();
	    	inFile.nextLine(); //dummy next line
	    	for (int i = 0; i < n; i++){
	    		String[] pokeInfo = inFile.nextLine().split(","); //creating string array that the Pokemon class constructor will read from
	    		pokes.add(new Pokemon(pokeInfo)); //creating and adding pokemon to the ArrayList
	    	}	
	    }
    	catch(IOException ex){
    		System.out.println("Misplaced File");
		}
    }
    
    public static void setPlayerPokemon(){ //allows the user to chose their pokemon
		playerPokes = new ArrayList<Pokemon>();
    	enemyPokes = new ArrayList<Pokemon>();
    	Scanner kb = new Scanner(System.in);
    	int[] playerPokesIndex = new int[4]; //index of the players selected Pokemon (used for the pokes Array
	    for (int i = 0; i < n; i++){
	    	System.out.println(i + 1 + ". " + pokes.get(i).getName() + " (" + pokes.get(i).getType() + ")"); //showing each Pokemon to the user
	    }
	    System.out.println("\nChose your 4 Pokemon (Enter the corresponding number one at a time and press ENTER)");
	    int pos = 0;
	    while (playerPokes.size() < 4){ //keep asking the user to enter number until 4 valid and different pokemon have been chosen
	    	System.out.printf("Pokemon %d: ", pos + 1);
	    	int userChoice = kb.nextInt() - 1;
	    	if (userChoice < n && userChoice > -1){ //checking if the input is valid 
	    		playerPokesIndex[pos] = userChoice;
	    		if (!playerPokes.contains(pokes.get(playerPokesIndex[pos]))) {
		    		playerPokes.add(pokes.get(playerPokesIndex[pos++])); //adding the chosen Pokemon to the players pokemon if its not a duplicate
		    		System.out.println("You chose " + playerPokes.get(playerPokes.size() - 1).getName());
		    	}
		    	else{
		    		System.out.print("You already selected this Pokemon: " + pokes.get(playerPokesIndex[pos]).getName() + "\n");
		    	}
	    	}
	    	else{
	    		System.out.println("Invalid entry");
	    	}
	    		
	    }
    }
    
	public static void setEnemyPokemon(){ //sets enemy pokemon based on users choices
	    for (int i = 0; i < pokes.size(); i++){
			if (!playerPokes.contains(pokes.get(i))){
	    		enemyPokes.add(pokes.get(i)); //adding all remaining pokemon to the enemies
	    	}	
	    }
	    Collections.shuffle(enemyPokes); //randomizing the order of the enemy pokemon
    }
    
    public static void choseBattlePoke(){ //ask user to pick a starting pokemon
    	currentPoke = new ArrayList<Pokemon>(); //always empty the list when choosing a new pokemon to battle
    	Scanner kb = new Scanner(System.in);
    	System.out.println("Your Pokemon: ");
    	for (int i = 0; i < playerPokes.size(); i++){
    		System.out.println(i + 1 + ". " + playerPokes.get(i).getName() + " (" + playerPokes.get(i).getType() + ")"); //showing users chosen pokemon
    	}
    	while (true){ //keep asking until valid input
    		System.out.print("Chose a Pokemon to battle: ");
	    	int userChoice = kb.nextInt() - 1; //take user input
	    	if (userChoice < playerPokes.size() && userChoice > -1){
	    		currentPoke.add(playerPokes.get(userChoice)); //if its valid, set that as the current battling pokemon
	    		System.out.println("\n" + playerPokes.get(userChoice).getName() + " I chose you!\n");
	    		break;
	    	}
	 		else{ //have the user chose a valid pokemon 
	 			System.out.println("Invalid entry\n");
	 		}
 		}
    }	

	public static void pokeStats(){ //display pokemon stats
		System.out.println("Your Pokemon: " + currentPoke.get(0).getName() + " (" + currentPoke.get(0).getType() + ")" + 
			"    Health: " + currentPoke.get(0).getHp() + "/" + currentPoke.get(0).getMaxHp()
			+ "    Energy: " + currentPoke.get(0).getEnergy() + "/50");
		System.out.println("Enemy Pokemon: " + enemyPokes.get(0).getName() + " (" + enemyPokes.get(0).getType() + ")" + 
			"    Health: " + enemyPokes.get(0).getHp() + "/" + enemyPokes.get(0).getMaxHp()
			+ "    Energy: " + enemyPokes.get(0).getEnergy() + "/50\n");
	}
	
	public static void userTurn(){ //ask user what they want to do (attack, retreat, pass) and call that method
		Scanner kb = new Scanner(System.in);
		int userChoice = 0;
		if (currentPoke.get(0).getStun() == false){
			System.out.println("Your options: ");
			System.out.println("1. Attack \n2. Retreat \n3. Pass");
			while (true){ //getting valid input from user
			System.out.print("What will " + currentPoke.get(0).getName() + " do? ");
				userChoice = kb.nextInt();
				if (userChoice > 0 && userChoice < 4){
					break;
				}
				else{
					System.out.println("Invalid entry\n");
				}
			}
			if (userChoice == 1 && energyToAttack(currentPoke.get(0)) == false){ //if the pokemon cannot use any moves
				System.out.println("\nYou do not have enough energy to attack\n");
				userTurn();
			}
			else if (userChoice == 1){ //pokemon can attack
				userAttack();
			}
			else if (userChoice == 2 && playerPokes.size() > 1){ //only call the retreat method if the user has a pokemon to switch in
				userRetreat();
			}
			else if (userChoice == 2){ //no pokemon to switch in
				System.out.println("\nYou do not have any Pokemon to switch in for " + currentPoke.get(0).getName() + "\n");
				userTurn(); //ask user to perform a different action
			}
			else{
				System.out.println("\n" + currentPoke.get(0).getName() + " passed\n"); //input is 3 and user passed
			}
				}
		else{ //only other thing that can stop a pokemon from attacking or retreating
			System.out.println(currentPoke.get(0).getName() + " is stunned and cannot move\n");
			currentPoke.get(0).resetStun();
		}
	}
	public static boolean energyToAttack(Pokemon attacker){ //check if the pokemon has enough energy to use at least 1 attack
		for (int i = 0; i < attacker.getNumMoves(); i++){
			if (attacker.getEnergy() >= attacker.getMoveEnergy(i)){ //check if one move can be used
				return true;
			}
		}
		return false; //none of the moves can be used
	}
	public static boolean energyForMove(Pokemon attacker, int userChoice){ //check to see if the pokemon has enough energy to use the selected move
		if (attacker.getEnergy() < attacker.getMoveEnergy(userChoice)){
			return false;
			}
		else{
			return true;
		}
	}
	
	public static void userAttack(){ //user attacks, loses energy and deals damage to enemy
		Scanner kb = new Scanner(System.in);
		int userChoice = 0;
		while (true){ //getting valid input from user
			System.out.println("\nYour attacks: ");
			for (int i = 0; i < currentPoke.get(0).getNumMoves(); i++){
				System.out.println(1 + i + ". " + currentPoke.get(0).getMove(i)); //display moves to the user
			}
			System.out.print("Which attack will " + currentPoke.get(0).getName() + " use? ");
			userChoice = kb.nextInt() - 1;
			if (userChoice > -1 && userChoice < currentPoke.get(0).getNumMoves()){
				if (energyForMove(currentPoke.get(0), userChoice)){ //check if user has enough energy to use the chosen move
					break;
				}
				else{
					System.out.println("\nYou do not have enough energy to use this move");
				}
			}
			else{
				System.out.println("Invalid entry");
			}
		}
		currentPoke.get(0).attackOne(enemyPokes.get(0), userChoice); //user attacks
	}
	
	public static void userRetreat(){ //user switches current pokemon with one from reserves
		Scanner kb = new Scanner(System.in);
		System.out.println("\nYour Pokemon: ");
		for (int i = 0; i < playerPokes.size(); i++){ //show pokemon that can be switched in
			System.out.println(i + 1 + ". " + playerPokes.get(i).getName());
		}
		int userChoice = 0;
		while (true){ //keep asking until valid input
			System.out.print("Which Pokemon will you switch in for " + currentPoke.get(0).getName() + "? ");
			userChoice = kb.nextInt() - 1;
			if (userChoice > -1 && userChoice < playerPokes.size()){
				break;
			}
			else{
				System.out.println("Invalid entry\n");
			}
		}
		currentPoke.remove(0); //take out current pokemon
		currentPoke.add(playerPokes.get(userChoice)); //switching in new pokemon
		System.out.println("\n" + currentPoke.get(0).getName() + ", I chose you!\n");
	}
	
	public static void enemyTurn(){ //enemy attacks, loses energy and deal damage to user
		ArrayList<Integer> movePicks = new ArrayList<Integer>();
		Random rand = new Random();
		//int pos = 0;
		if (enemyPokes.get(0).getStun() == false){
			if (energyToAttack(enemyPokes.get(0))){ //enemy attack will always be forced if it has enough energy
				while (true){
					int moveNum = rand.nextInt(enemyPokes.get(0).getNumMoves());
					if (energyForMove(enemyPokes.get(0), moveNum) && !movePicks.contains(moveNum)){
						enemyPokes.get(0).attackOne(currentPoke.get(0), moveNum); //after so the (super effective/not very effective) prints after the attack
						break;
					}
					else{
						movePicks.add(moveNum);
					}
				}
			}
			else{
				System.out.println(enemyPokes.get(0).getName() + " passed (not enough energy to attack)\n");
			}
		}
		else{
			System.out.println(enemyPokes.get(0).getName() + " is stunned and cannot attack\n");
			enemyPokes.get(0).resetStun();
		}
	}
	
	public static void regenEnergy(){ //giving both sides 10 energy after each round
		for (int i = 0; i < playerPokes.size(); i++){
			playerPokes.get(i).regenEnergy();
		}
		enemyPokes.get(0).regenEnergy();
	}
	
	public static void healAfterBattle(){ //set all energy back to 50, heal all pokemon after a battle and reset the stun and disabled values
		for (int i = 0; i < playerPokes.size(); i++){
			playerPokes.get(i).resetEnergy();
			playerPokes.get(i).regenHealth();
			playerPokes.get(i).resetDisable();
			playerPokes.get(i).resetStun();
		}
	}

	public static int whoStarts(){ //choosing who goes first for the battle (1 = player, 2 = enemy)
		Random rand = new Random();
		int turn = rand.nextInt(2) + 1;
		return turn;
	}
	
	public static boolean validBattle(){ //check if the pokemon can still battle (in terms of health)
		if (playerPokes.size() == 0 || currentPoke.get(0).getHp() == 0){
			System.out.println(currentPoke.get(0).getName() + " fainted\n");
			playerPokes.remove(playerPokes.indexOf(currentPoke.get(0))); //remove that pokemon from the player
			currentPoke.remove(0); //no longer current pokemon
			if (playerPokes.size() == 0){
				return false;
			}
			else{
				choseBattlePoke(); //ask user to put another pokemon into battle
				enemyTurn(); //enemy turn (user cannot switch in and perform a move)
				return validBattle(); //re-check for a valid battle off a switch of a player pokemon (enemy could have killed the new user pokemon)
			}
		}
		else if (enemyPokes.get(0).getHp() == 0){
			System.out.println(enemyPokes.get(0).getName() + " fainted");
			System.out.println(currentPoke.get(0).getName() + " defeated " + enemyPokes.get(0).getName() + "\n");
			enemyPokes.remove(0); //remove enemy pokemon from the list
			if (enemyPokes.size() != 0){
				System.out.print("Good job! You won!\nNext Battle\n" + "(" + enemyPokes.size() + " Enemy Pokemon Left)\n");
			}
			return false;
		}
		else{
			return true;
		}
	}
	
	public static boolean validGame(){ //checking to see if either side has pokemon to battle
		if (playerPokes.size() == 0){ //no more user pokemon left
			System.out.println("ALL YOUR POKEMON HAVE FAINTED\nYOU HAVE BEEN DEFEATED BY THE ENEMY POKEMON\nBETTER LUCK NEXT TIME");
			return false;
		}
		else if (enemyPokes.size() == 0){ //no more enemy pokemon left
			System.out.print("CONGRATULATIONS, YOU HAVE DEFEATED ALL ENEMY POKEMON AND ARE NOW TRAINER SUPREME!");
			return false;
		}
		else{
			return true;
		}
	}
    
}