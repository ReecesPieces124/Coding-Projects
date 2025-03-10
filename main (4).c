#include <stdio.h>
#include <math.h>
#include <stdlib.h>

#define PI 3.14159265358979323846
#define EPSILON 0.0001

typedef struct group{
  double angle_D;
  double angle_R;
  int num;
}group;

typedef struct Result{
  int firstGroup;
  int secondGroup;
  double angle_D;
}Result;

double convertAngle(double angle_R){
  double angle_D = angle_R * 180 / PI;
  return angle_D;
}

//Calculates the angle in radians and returns a double
double angleCalc(int x, int y){
  double angle_R;
  if(y == 0){
    if(x > 0)
      return 0.0000000;
    else if(x<0)
      return PI;
  }
  else if(x == 0){
    if(y > 0)
      return PI/2;
    else if(y<0)
      return 3*PI/2;
  }
  else if (x>0 && y>0){
    angle_R = atan(1.000000 * y/x);
  }
  else if(x<0 && y>0){
    angle_R = PI + atan(1.000000 * y/x);//arctan will spit out a negative number since x is negative
  }
  else if(x<0 && y<0){
    angle_R = PI + atan(1.000000 * y/x);//arctan will spit out a positive number since x is negative and y is negative
  }
  else if(x>0 && y<0){
    angle_R = 2*PI + atan(1.000000 * y/x);//arctan will spit out a negative number since y is negative
  }
  return angle_R;
}


//change for stuct use
//Sort for Groups array


void merge(group arr[], int l, int m, int r){
  int i, j, k;
  int n1 = m - l + 1;
  int n2 = r - m;
  /* create temp arrays */
  group *L = malloc(n1*sizeof(group));
  group *R = malloc(n2*sizeof(group));
  /* Copy data to temp arrays L[] and R[] */
  for (i = 0; i < n1; i++)
    L[i] = arr[l + i];
  for (j = 0; j < n2; j++)
    R[j] = arr[m + 1+ j];
  /* Merge the temp arrays back into arr[l..r]*/
  i = 0; // Initial index of first subarray
  j = 0; // Initial index of second subarray
  k = l; // Initial index of merged subarray
  while (i < n1 && j < n2){
    if (L[i].angle_D <= R[j].angle_D){
      arr[k] = L[i];
      i++;
    }
    else{
      arr[k] = R[j];
      j++;
    }
    k++;
  }
  /* Copy the remaining elements of L[], if there
  are any */
  while (i < n1){
    arr[k] = L[i];
    i++;
    k++;
  }
  /* Copy the remaining elements of R[], if there
  are any */
  while (j < n2){
    arr[k] = R[j];
    j++;
    k++;
  }
  free(L);
  free(R);

}

void mergeSort(group arr[], int l, int r){
  if (r>l){
    int mid = (r+l)/2;
    mergeSort(arr, mid+1, r);
    mergeSort(arr, l, mid);
    merge(arr, l, mid, r);
  }
}

void intSwap(int *a, int *b){
  int temp = *a;
  *a = *b;
  *b = temp;
}

//Quick sort for result array
void swap(Result *a, Result *b){
  Result temp = *a;
  *a = *b;
  *b = temp;
}

int partition(Result* vals, int low, int high){
  int temp;
  int i, lowpos;
  // Pick a random partition element and swap it into index low.
  i = low + rand()%(high-low+1);
  swap(&vals[low], &vals[i]);
  // Store the index of the partition element.
  lowpos = low;
  // Update our low pointer.
  low++;
  // Run the partition so long as the low and high counters don't cross.
  while (low <= high) {
    // Move the low pointer until we find a value too large for this side.
    while (low <= high && vals[low].angle_D <= vals[lowpos].angle_D)
      low++;
    // Move the high pointer until we find a value too small for this side.
    while (high >= low && vals[high].angle_D > vals[lowpos].angle_D)
      high--;
    // Now that we've identified two values on the wrong side, swap them.
    if (low < high)
      swap(&vals[low], &vals[high]);
  }
  // Swap the partition element into it's correct location.
  swap(&vals[lowpos], &vals[high]);
  return high; // Return the index of the partition element

}

void quickSort(Result* numbers, int low, int high){
  int k;
  if (low < high){
    k = partition(numbers, low, high);
    quickSort(numbers, low, k - 1); // Before k
    quickSort(numbers, k + 1, high); // After k
  }

}

//print group arrays
void printArray(group arr[], int size){
  int i;
  for (i=0; i < size; i++)
    printf("Group %d is at angle %lf \n", arr[i].num, arr[i].angle_D);

  printf("\n");
}
void printResult(Result arr[], int size){
  int i;
  for (i=0; i < size; i++)
    printf("Groups %d and %d have angle %lf \n", arr[i].firstGroup,arr[i].secondGroup, arr[i].angle_D);

  printf("\n");
}
//Returns 1 if a and b are the same.
int compareDouble(double a, double b){
  if(fabs(a-b)<EPSILON)
    return 1;
  return 0;
}

int main(void) {

  int numGroups, pAngle, xPosition, yPosition, numPeople;
  
  scanf("%d %d", &numGroups, &pAngle);
  group *groups = malloc(numGroups * sizeof(group));;

  for(int i=0; i<numGroups;i++){
    scanf("%d %d %d", &xPosition, &yPosition, &numPeople);
    if(xPosition!= 0 || yPosition != 0){
  
      groups[i].angle_R = angleCalc(xPosition, yPosition);
      groups[i].angle_D = convertAngle(groups[i].angle_R);
      groups[i].num = i;
      
    }
    
  }

  //sort the Groups Array
  mergeSort(groups, 0, numGroups-1);

  Result *results = malloc(numGroups * sizeof(Result));

  //make result array
  int counter =0;
  //loop through groups array and put each pair into the result array with their difference in angle
  for(int i=0; i<numGroups; i++){

    //Find angle in between 2 groups
    double compare = groups[i].angle_D - groups[i+1].angle_D;
    compare = fabs(compare);

    //Once we get to the last group, we compare its angle with the first group
    if(i==numGroups-1){

      //find the area from the last group to 360 degrees and then add the degrees of the first group
      compare = fabs(groups[i].angle_D-360.000000) + groups[0].angle_D;
      results[counter].angle_D = compare;
      results[counter].firstGroup = groups[0].num;
      results[counter].secondGroup = groups[i].num;
    }
    else{
      results[counter].angle_D = compare;
      results[counter].firstGroup = groups[i].num;
      results[counter].secondGroup = groups[i+1].num;
    }
    counter++;
  }

  //sort the Results array
  quickSort(results, 0, numGroups-1);

  //to keep track of the highest angle we can use front and rear pointers.
  int rear=numGroups-1;
  int front=rear;//front is == rear because of we have multiple highest angles we need to start at that first one and print to the last one. so We'll just increment down
  for(int i=0; i<numGroups;i++){
    if(compareDouble(results[rear].angle_D, results[front].angle_D)){
      front--;
    }
  }
  
  //print result array.
  printf("The max projection without harming is %.4lf\n" , results[front+1].angle_D);
  printf("The closest possible group pairs in order:\n");
  for(int i=front+1; i<=rear; i++){//Because front started at rear we start the for loop at front+1

    //swap the results if they aren't in order
    if(i != rear){
      if(results[i].secondGroup > results[i+1].secondGroup)
        swap(&results[i], &results[i+1]);
      if(results[i].firstGroup > results[i+1].firstGroup)
        swap(&results[i], &results[i+1]);
    }
    //swaps the order of groups within the result array if they aren't in order
    if(results[i].firstGroup > results[i].secondGroup){
      intSwap(&results[i].firstGroup, &results[i].secondGroup);
    }
    //prints only 1 output if there are multiple of the same outputs. If so we skip the next iteration.
    if(results[i].firstGroup == results[i+1].firstGroup && results[i].secondGroup == results[i+1].secondGroup){
      printf("%d %d\n", results[i].firstGroup, results[i].secondGroup);
      i++;
    }
    else
      printf("%d %d\n", results[i].firstGroup, results[i].secondGroup);
  }

  //freedom alas
  free(results);
  free(groups);
  return 0;
}
