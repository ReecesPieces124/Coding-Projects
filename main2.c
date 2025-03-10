/* COP 3502C Assignment 3
This program is written by: Reece Wilson */
#include <stdio.h>
#include <string.h>
#define MAXPEOPLE 10
#define MAXLEN 20

void printPermutArray(char names[MAXPEOPLE][MAXLEN], int perm[MAXPEOPLE],int n){
  
    for(int i=0; i<n; i++)
      printf("%s ", names[perm[i]]);
    printf("\n");

}
//check if person without popcorn are sitting next to someone with popcorn
//if person is not sitting next to someone with popcorn and they also do not have popcorn
//return 1
int checkPopcorn(int perm[MAXPEOPLE], int popcorn[MAXPEOPLE], int numPeople){
  //Loop through Permutation array
  for(int i=0; i<numPeople-1; i++){
    
    if( i==0 && popcorn[perm[i]] == 0 && popcorn[perm[i+1]] == 0){//check first 2 positions to see if they don't have popcorn
      return 1;
    }
    else if (i== numPeople-2 && popcorn[perm[numPeople-1]] == 0 && popcorn[perm[numPeople-2]] == 0){//Then check last 2 positinos to see if they don't have popcorn
      return 1;
    }
    else if(popcorn[perm[i]] == 0 && popcorn[perm[i-1]] == 0 && popcorn[perm[i+1]] == 0){//Finally check if your position, before your position, and after your position to see if all of you don't have popcorn.
      return 1;
    }
    
  }
  return 0;
}

//check if perm has a conflict
//If there is a conflict return 1 
int checkPermute(int perm[MAXPEOPLE], int conArr[MAXPEOPLE][MAXPEOPLE], char name[MAXPEOPLE][MAXLEN], int numPeople){
  //we do up to numPeople -1 because ew are checking for the current position and the next position. So we don't have to go all the way to the last guy to check if he has a conflict with person b
  for(int i=0;i<numPeople-1;i++){//Loop through the permutation seeing if the current position and the next position are good sitting with eachother.
    if(conArr[perm[i]][perm[i+1]] == 1){//This checks if person A and person B Can't sit with eachother. If so we have a conflict and return 1.
      return 1;
    }
  }
  return 0;
}

//the used array keep track which number is used and which number to be transferred to the perm
void printperms(int perm[MAXPEOPLE], int* used, int k, int n, char names[MAXPEOPLE][MAXLEN],int conArr[MAXPEOPLE][MAXPEOPLE], int *count, int popcorn[MAXPEOPLE], int numPeople) {
  if (k == n)//if k reaches the length print the result
  {
    if(checkPermute(perm, conArr, names, numPeople)==0 && checkPopcorn(perm, popcorn, numPeople) == 0 ){//if we have already printed the first permutation we don't print anymore.
      if((*count) == 0)
        printPermutArray(names, perm, n);
      (*count)++;
    }
    return; //return to the most recent call
  }
  int i;
  for (i=0; i<n; i++) {
    if (!used[i]) { //if i was not used
      used[i] = 1; //mark that it is used
      perm[k] = i; //transfer i to the the perm array at kth position
      printperms(perm, used, k+1, n, names, conArr, count, popcorn, numPeople); //increase k and grow further
      used[i] = 0; //unmark i for next process.
    }
  }
}

int main(void){
  int numPeople=0, numRestrictions=0;
  //initialize the permutation, used, and popcorn arrays.
  int perm[MAXPEOPLE] = {0}, used[MAXPEOPLE] = {0}, popcorn[MAXPEOPLE] = {0};
  int conArr[MAXPEOPLE][MAXPEOPLE] = {0};//2D array for telling us who can't sit with who
  int i=0, count = 0;
  char names[MAXPEOPLE][MAXLEN];//2D array for People
  char nameRes1[MAXLEN], nameRes2[MAXLEN];//read in the names for people who can't sit with each other
  
  scanf("%d %d",&numPeople, &numRestrictions);
  

  for(i=0;i<numPeople;i++){
    scanf("%s %d", names[i], &popcorn[i]);
  }

  i=0;
  int save1,save2;

  //As we read in the people we fill in a 2D array that will show 2 possibilities. 
  //Person A can't sit with person B. And Person B can't sit with person A.
  while(i<numRestrictions){
    scanf("%s %s", nameRes1, nameRes2);
    for(int j=0; j<numPeople; j++){
      if(strcmp(nameRes1,names[j]) == 0){//Check if name1 matches with any name in our names Array
        save1 = j;//save position in the names array
      }
      if(strcmp(nameRes2,names[j]) == 0){//Check if name2 matches with any name in our names Array
        save2 = j;//save position in the names array
      }
    }
    conArr[save1][save2] = 1;//Person A can't sit with person B
    conArr[save2][save1] = 1;//Person B can't sit with person A
    i++;
  }

  printperms(perm, used, 0, numPeople, names, conArr, &count, popcorn, numPeople);
  printf("%d\n", count);
   
  return 0;
}