//Author Reece Wilson
//UCF ID: 5270572
package assignment4;

import java.util.ArrayList;

public class ArrayListAlt {

    private ArrayList<Record> arrList;
    public ArrayListAlt(){
        arrList = new ArrayList<>();
    }

    //adds a record to the arrayList or edits a current one depending if its already in the arrayList.
    public void add(Record r){

        int place =find(r.getNumber());

        if(place==-1) {
            for (int i = 0; i <= arrList.size()-1; i++) {
                if (r.getNumber() > arrList.get(i).getNumber()) {
                    place = i;
                }
            }
            //System.out.println(arrList);
            arrList.add(place+1, r);
        }
        else{
            arrList.get(place).setValue(r.getValue());
        }
    }

    //returns the index of a Record that you are searching for. If we don't find it we return -1.
    //uses binary search to find the location
    private int find(int number){
        int low = 0;
        int high = arrList.size()-1;
        int mid = (low+high)/2;//midpoint

        while(low<=high){
            //check if we are at our number
            if(number == arrList.get(mid).getNumber()){
                return mid;
            }
            //check if our number is in the top half of the array
            else if(number > arrList.get(mid).getNumber()){
                low = mid+1;
                mid = (low+high)/2;
            }
            //our number is in the bottom half of the array
            else{
                high = mid-1;
                mid = (low+high)/2;
            }
        }

        //no number found, Thus we have a new number to add to the array
        return -1;
    }

    //removes a record and returns true if so.
    public boolean remove(int num) throws Exception{

        int elem = find(num);//locates record

        //if we have a record, remove it
        if(elem !=-1) {
            arrList.remove(elem);
            return true;
        }

        //we don't have it, so no removal occurs :(
        else {
            System.out.println("No such element");
            return false;
            //throw new Exception("No such element");
        }
    }

    //returns size of the arrayList
    public int size(){
        return arrList.size();
    }

    //returns the name of a record in the arrayList
    public String get(int num){
        int save = find(num);
        if(save == -1)
            return null;

        else
            return arrList.get(save).getValue();
    }

    //converts the array list into a String
    public String toString(int num) {

        if(!arrList.isEmpty()) {
            System.out.println("" + num + ": " + arrList);
            return arrList.toString();
        }
        else {
            System.out.println("" + num + ": ");
            return arrList.toString();
        }
    }
}
