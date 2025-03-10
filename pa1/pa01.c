/*============================================================================
| Assignment: pa01 - Encrypting a plaintext file using the Hill cipher
|
| Author: Reece Wilson
| Language: c
| To Compile: gcc -o pa01 pa01.c
|
| To Execute: c -> ./pa01 kX.txt pX.txt
|
|       where kX.txt is the keytext file
|       and pX.txt is plaintext file
|
|   Note:
|       All input files are simple 8 bit ASCII input
|       All execute commands above have been tested on Eustis
|
| Class: CIS3360 - Security in Computing - Spring 2025
| Instructor: McAlpin
| Due Date: per assignment
+===========================================================================*/
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAXTEXT 10000

void alphabetize(char *text);
char matrixMult(int **key, int row, char *characters, int col);

//this will track where we are in the plaintext array later in the code
int tracker =0;

int main(int argc, char **argv) {

    // read in the 2 files from command line
    char *fname1 = argv[1];
    char *fname2 = argv[2];

    // open's the key file
    FILE *fp = fopen(fname1, "r");
    if (fp == NULL) {
        perror("Error in opening the Key file" );
        return 1;
    }
    int size;
    fscanf(fp, "%d\n", &size);
    int **keyMatrix = malloc(size * sizeof(int*));
    for (int i = 0; i < size; i++) {
        keyMatrix[i] = malloc(size * sizeof(int));
    }

    // Store the key matrix inside a Matrix
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            fscanf(fp, "%d", &keyMatrix[i][j]);
        }
    }
    fclose(fp);

    // open the Plaintext File
    fp = fopen(fname2, "r");
    if (fp == NULL) {
        perror("Error in opening the Plaintext file" );
    }

    //puts plaintext file into the text array
    char text[MAXTEXT];
    char plaintext[MAXTEXT] = "", charPtr;
    //initialize the plaintext array
    for(int i = 0; i < MAXTEXT; i++) {
        text[i] = '\0';
    }
    char *eofChecker = "";
    /*while(eofChecker!=NULL) {
        eofChecker = fgets(text, MAXTEXT, fp);
        text[strcspn(text,  "\n")] = '\0';
    }

    while (fgets(text + strlen(text), MAXTEXT - strlen(text), fp) != NULL) {
        text[strcspn(text, "\n")] = '\0'; // Remove newlines
    }
 */

    //printf("\nProcessed Plaintext (Before Encryption):\n%s\n", text);


    //convert text in plaintext to lowercase
    //alphabetize(text);

    while((charPtr = fgetc(fp)) != EOF) {
        charPtr = tolower(charPtr);

        if(charPtr - 'a' >=0 && charPtr - 'a' < 26) {
            //add letter to plaintext
            strncat(plaintext, &charPtr, 1);
        }
    }
    fclose(fp);
    //Now we do the Hill Cipher
    int cipherSize = strlen(plaintext);
    while(cipherSize%size!=0) {
        //text[cipherSize] = 'x';
        //cipherSize++;
        strcat(plaintext, "x");
        cipherSize++;
    }
    char cipherText[cipherSize+1];
    cipherText[cipherSize]='\0';

    for(int i=0; i<cipherSize;i++){
        cipherText[i]= matrixMult(keyMatrix,i%size,plaintext,size);
        if(!(i%size==0) && i !=0)
            tracker+=size;
    }
    //Print key matrix
    printf("\nKey matrix:\n");
    for(int i=0; i<size;i++) {
        for(int j=0; j<size;j++) {
            printf("%4d",keyMatrix[i][j]);
        }
        printf("\n");
    }

    //print Plaintext
    printf("\nPlaintext: \n");

    int counter = 0;
    while(plaintext[counter]!='\0') {
        int j;
        for(j=0;j<80 && plaintext[counter]!='\0' ;j++) {
            printf("%c",plaintext[counter++]);
        }
        printf("\n");
    }
    printf("\n");

    //print ciphertext
    printf("Ciphertext: \n");
    counter =0;
    while(cipherText[counter]!='\0') {
        int j;
        for(j=0; j<80 && cipherText[counter]!='\0' ;j++) {
            printf("%c",cipherText[counter++]);
        }
        printf("\n");
    }
    //printf("\n");
    //printf("Ciphertext: %s\n",cipherText);


    //free memory
    for(int i=0; i<size; i++) {
        free(keyMatrix[i]);
    }
    free(keyMatrix);

    return 0;
}

void alphabetize(char *text) {
    //takes in the plaintext and converts the characters into lowercase and "wipes out" the special characters
    int j = 0;
    for (int i = 0; text[i] != '\0'; i++) {
        if (isalpha(text[i])) {
            text[j++] = tolower(text[i]); // Convert to lowercase immediately
        }
    }
    text[j] = '\0'; // Null-terminate the string after filtering
}

char matrixMult(int **key, int row, char *characters, int col) {

    int save=0;
    int temp =0;
    int j=tracker;
    for (int i = 0; i < col; i++) {
        for(j;j<MAXTEXT && i<col;j++) {
            temp+=key[row][i]*(characters[j] - 'a');
            i++;
        }
    }
    save = temp % 26;//alphabetize it then add it to a to get its corresponding letter

    return 'a' +save;
}

/*=============================================================================
| I Reece Wilson (re854936) affirm that this program is
| entirely my own work and that I have neither developed my code together with
| any another person, nor copied any code from any other person, nor permitted
| my code to be copied or otherwise used by any other person, nor have I
| copied, modified, or otherwise used programs created by others. I acknowledge
| that any violation of the above terms will be treated as academic dishonesty.
+=============================================================================*/