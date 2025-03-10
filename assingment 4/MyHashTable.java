//Author Reece Wilson
//UCF ID: 5270572
package assignment4;

public class MyHashTable {
    private ArrayListAlt hash[];

    //Constructors
    public MyHashTable(){
        hash = new ArrayListAlt[11];
        for(int i =0;i<11;i++)
            hash[i] = new ArrayListAlt();
    }
    public MyHashTable(int size){
        hash = new ArrayListAlt[size];
        for(int i =0;i<size;i++)
            hash[i] = new ArrayListAlt();
    }

    //The hash function to get an index point
    private int hashFunc(int pos){
        return pos%hash.length;
    }

    //adds a record to the hash at the index point
    public void add(Record rec){
        int position = hashFunc(rec.getNumber());
        hash[position].add(rec);
    }

    //removes a number from the hash at an index point
    public void remove(int num) throws Exception {
        int position = hashFunc(num);
        hash[position].remove(num);
    }

    //unused but gets the string (like Test) from the hash table
    public String get(int num){
        int position = hashFunc(num);
        return hash[position].toString();
    }

    @Override
    public String toString(){
        for (int i=0;i<hash.length;i++){
            hash[i].toString(i);
        }
        return null;
    }

}
