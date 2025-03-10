//Author Reece Wilson
//UCF ID: 5270572
package assignment2;

//rolls the combination of 2 6-sided die and plots a histogram of the ocurrance of dice rolls
public class Wilson_Reece {
    static int[] numArr = new int[13];//array will be the possible numbers rolled and will store the number of rolls at their respective index
                                    //for instance lets say we roll a 7 167 times. the number 167 will be stored at the index of 7
    public static void main(String[] args) {

        Dice dice =new Dice();//make new Dice

        //Roll said new dice
        for(int i=0; i<1000; i++){
            dice.roll();
            numArr[dice.getDice()]++;//put rolled dice values inside the array
        }

        //print out how many times we get a number
        for(int i=2; i<13;i++){
            System.out.println( "Number of " + i +"'s is: " + numArr[i]);
        }

        //Histogram time :)
        System.out.println("Graph ");
        makeHistogram();
    }
    //Making the histogram graph
    public static void makeHistogram(){

        int y = 175;
        for(int i=0;i<7;i++){

            for(int j=1;j<13;j++){

                //structure the y-axis
                if(j==1){
                    y-=25;
                    if(y<100 && y>0) {
                        System.out.print(" ");
                    }
                    if(y==0) {
                        System.out.print("  ");
                    }
                    System.out.print(y + "|");

                }
                //print out stars for 0
                else if(i==6 && numArr[j] > 0){
                    System.out.print("*  ");
                }

                //print out stars
                else {
                    if(numArr[j]<y){
                        System.out.print("   ");
                    }
                    else{
                        System.out.print("*  ");
                    }
                }
            }
            System.out.println();
        }

        //formatting the x-axis
        System.out.println("    --------------------------------");
        System.out.print("    2  3  4  5  6  7  8  9  10 11 12 ");
    }
}