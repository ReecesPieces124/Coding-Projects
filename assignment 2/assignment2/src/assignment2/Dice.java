package assignment2;
import java.util.Random;
//Author Reece Wilson
//UCF ID: 5270572

//creates 2 dice and rolls them
public class Dice {

    private Die newDice;
    private Die newDice2;

    //Initialize the dice
    public Dice(){
        newDice = new Die();
        newDice2 = new Die();

    }

    //roll the dice (Gambling time)
    public void roll(){
        newDice.rollDie();
        newDice2.rollDie();
    }

    //returns the combined total of the two dice
    public int getDice(){
        return newDice.getFaceValue() + newDice2.getFaceValue();
    }
}
