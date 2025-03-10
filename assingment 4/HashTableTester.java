//Author Reece Wilson
//UCF ID: 5270572
package assignment4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

public class HashTableTester {
    public static void main(String[] args) throws Exception {
        String data = readFile("data.txt");//args[0]

        String[] lines = data.split("\n"); // Split the data into individual lines

        StringTokenizer st = new StringTokenizer(data);//Tokenize the file
        String token;
        token = st.nextToken();
        MyHashTable HashTable = new MyHashTable(Integer.parseInt(token));//Initialize the Hash Table based on first input
        while( st.hasMoreTokens() ) {
            token = st.nextToken();

            //add operation
            if(token.compareTo("add")==0){
                token = st.nextToken();
                String[] parts = token.split(":");//need to split the number and value. Like 12:Test into 12 Test

                String number = parts[0]; // "number"
                String value = parts[1]; // "value"

                int num = Integer.parseInt(number);

                //create a record
                Record rec=new Record(num,value);

                //add record to hash table
                HashTable.add(rec);
            }
            //if not add the only other operation is to remove
            else{
                token = st.nextToken();//gets the number
                int number = Integer.parseInt(token);//parses number
                HashTable.remove(number);//removes number from hash table
            }
        }//end of while

        //print
        HashTable.toString();
    }

    //reads in the whole file
    public static String readFile(String fname) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(fname))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}