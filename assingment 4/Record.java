//Author Reece Wilson
//UCF ID: 5270572
package assignment4;

public class Record {
    private int number;
    private String value;

    //Constructors
    public Record(){
        number=0;
        value = null;
    }
    public Record(int num, String val){
        number = num;
        value = val;
    }

    //returns number
    public int getNumber() {
        return number;
    }

    //sets number, unused
    public void setNumber(int num) {
        number = num;
    }

    //returns value
    public String getValue(){
        return value;
    }

    //sets value to new value
    public void setValue(String value) {
        this.value = value;//work more with the this.variable set up to get more familiar with it
    }

    @Override
    public String toString() {
        return "" + number +":"+ value;
    }

}
