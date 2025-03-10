//Author Reece Wilson
//UCF ID: 5270572
package assignment2;
import java.util.Random;

//creates a Die and can roll said die
public class Die {
    private int faceVal;
    public Die(){
        faceVal=1;//initialize die to 1
    }

    //self explanitory
    public int getFaceValue(){
        return faceVal;
    }

    //reset faceval to what the dice rolled
    public void setFaceValue(int val){
       this.faceVal = val;
    }

    //Gambling time
    //rolls die
    public void rollDie(){
        Random rand = new Random();
        setFaceValue(rand.nextInt(1,7));
    }
}
