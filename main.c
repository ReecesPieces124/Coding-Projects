/* COP 3502C Assignment 5
This program is written by: Reece Wilson */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAXLEN 19

typedef struct customer {
  char name[MAXLEN+1];
  int points;
}customer;

typedef struct treenode {
  customer* cPtr;
  int size;
  struct treenode* left;
  struct treenode* right;
}treenode;

customer* createCust(char* name, int points);
treenode* createNode(customer* cPtr);
treenode* insert(treenode* root, treenode* item);
treenode* Delete(treenode* root, char* name);
treenode* search(treenode* root, char* name);
void inOrder(treenode* root);
void preOrder(treenode* root);
void freePostOrder(treenode* root);
int balanceFactor(treenode* root);
int height(treenode* root, char*name, int count);
treenode* parent(treenode *root, treenode *node);
int isLeaf(treenode *node);
int hasOnlyLeftChild(treenode *node);
int hasOnlyRightChild(treenode *node);
treenode* maxVal(treenode *root);
int countSmaller(treenode* root, char* name);

int main(void) {

  int nCommands, data, depth;
  char name[MAXLEN];
  char command[MAXLEN];

  customer *person;
  treenode *node, *root;
  root = NULL;

  
  scanf("%d", &nCommands);

  for(int i=0; i<nCommands;i++){
    scanf("%s", command);

    //adds points to an existing customer or creates a new customer and adds points to them
    if(strcmp(command, "add")==0){
      scanf("%s %d", name, &data);

      if(root!= NULL){
        node = search(root, name);
        if(node != NULL){
          node->cPtr->points += data;
        }
        else{
          person = createCust(name, data);
          node = createNode(person);
          root = insert(root, node);
        }
      }
      
      else{
        person = createCust(name, data);
        node = createNode(person);
        root = insert(root, node);   
      }

      printf("%s %d\n",node->cPtr->name, node->cPtr->points);
            
    }

    //subtract points
    else if(strcmp(command, "sub")==0){
      scanf("%s %d", name, &data);

      if(root!= NULL){
        node = search(root, name);
        if(node != NULL){
          if(node->cPtr->points < data)
            node->cPtr->points = 0;
          else
            node->cPtr->points -= data;
          printf("%s %d\n",node->cPtr->name, node->cPtr->points);
        }
        else
          printf("%s not found\n", name);
        
      }
      else
        printf("%s not found\n", name);

    }

    //delete a person
    else if(strcmp(command, "del")==0){
      scanf("%s", name);

      if(root!= NULL){
        node = Delete(root, name);
        if(node != NULL){
          printf("%s deleted\n", name);
        }
        else
          printf("%s not found\n", name);
      }
      
    }

    //search for a person
    else if(strcmp(command, "search")==0){
      scanf("%s", name);
      
      if(root!= NULL){
        node = search(root, name);
                
        if(node != NULL){
          depth = height(root, name, 0);
          printf("%s %d %d\n",node->cPtr->name, node->cPtr->points, depth);
        }

        else
          printf("%s not found\n", name);
      }
        
      else
        printf("%s not found\n", name);
    }

    //count total number of people who are alphabetically before a given person
    else if(strcmp(command, "count_smaller")==0){
      scanf("%s ", name);
      
      if(root!= NULL){
        node = search(root, name);
        if(node != NULL){
          printf("%d\n ", countSmaller(root, name));
        }
        else
          printf("%s not found\n", name);
      }
      else
        printf("%s not found\n", name);
    }
    else
      i--;
    
  }
  

  
  
  //Frees all the memory using post order method
  if(root != NULL)
    freePostOrder(root);
  
  
  return 0;
}

// creates a Customer
customer* createCust(char* name, int points){
  customer *person;
  person = (customer*)malloc(sizeof(customer));
  
  person->points = points;
  strcpy(person->name, name);
  return person;
}

// creates a Tree node
treenode * createNode(customer* cPtr){
  treenode *node;
  node = malloc(sizeof(treenode));
  node->cPtr = cPtr;
  node->size = 1;
  node->left = NULL;
  node->right = NULL;
  return node;
}

// search customers within the Tree
treenode* search(treenode* root, char* name){
  if(root == NULL){
    return NULL;
  }
  
  if(strcmp(root->cPtr->name, name)==0)
    return root;
  
  else if(strcmp(root->cPtr->name, name)>0)
    return search(root->left, name);
  
  else
    return search(root->right, name);
  
}

//count smaller people in BST
int countSmaller(treenode* root, char* name){
  //base case
  if(root == NULL)
    return 0;
  //we found our node, return the size of the left subtree
  if(strcmp(root->cPtr->name, name)==0){
    if(root->left !=NULL)
      return root->left->size;
    else 
      return 0;
  }
  //our node is on the right subtree so we need to add the size of the left subtree plus the root node 
  if(strcmp(root->cPtr->name, name)<0){
    return 1 + root->left->size + countSmaller(root->right, name);
  }
  //else our name is in the left subtree
  else
    return countSmaller(root->left, name);
    
}

// insert a node into the Tree
treenode* insert(treenode* root, treenode* item){
  if(root==NULL)
    return item;
    
  else{
    //if strcmp returns a negative value, the item should be inserted to the right
    //if root is "Greg"  and item is "Reece" we'll get a negative value, because R > G. Thus we need to go right of root.
    if(strcmp(root->cPtr->name, item->cPtr->name) < 0){
      if(root->right != NULL){
        root->size ++;
        root->right = insert(root->right, item);
      }
      else{
        root->right = item;
        root->size ++;
      }
    }
    else{
      if(root->left != NULL){
        root->size ++;
        root->left = insert(root->left, item);
      }
      else{
        root->left = item;
        root->size ++;
      }
    }
    return root;
  }
}

//Deleting a node from the Tree
treenode* Delete(treenode* root, char* name){
  treenode *delnode, *new_del_node, *save_node;
  treenode *par;
  char save_name[MAXLEN];

  delnode = root;
  while(1){
    if(delnode==NULL)
      break;
    if(strcmp(name, delnode->cPtr->name) < 0){
      delnode->size--;
      delnode = delnode->left;
    }
    else if(strcmp(name, delnode->cPtr->name) > 0){
      delnode->size--;
      delnode = delnode->right;
    }
    else
      break;
    
  }
  par = parent(root, delnode);

  if(isLeaf(delnode) == 1){
    if(par == NULL){
      free(root);
      return NULL;
    }
    if(strcmp(name, par->cPtr->name) < 0){
      free(par->left);
      par->left = NULL;
    }
    else{
      free(par->right);
      par->right = NULL;
    }
    return root;
  }
  else if(hasOnlyLeftChild(delnode) == 1){

    //deletes node if its the root of the tree
    if(par == NULL){
      save_node = delnode->left;
      free(delnode);
      return save_node;
    }

    //deletes node if its a left child
    if(strcmp(name, par->cPtr->name) < 0){
      save_node = par->left;
      par->left = par->left->left;
      free(save_node);
    }
      
    //deletes node if its a right child
    else{
      save_node = par->right;
      par->right = par->right->left;
      free(save_node);
    }
    return root;
  }
  else if(hasOnlyRightChild(delnode) == 1){
    //deletes node if its the root of the tree
    if(par == NULL){
      save_node = delnode->right;
      free(delnode);
      return save_node;
    }
    
    //deletes node if its a left child
    if(strcmp(name, par->cPtr->name) < 0){
      save_node = par->left;
      par->left = par->left->right;
      free(save_node);
    }
      
    //deletes node if its a right child
    else{
      save_node = par->right;
      par->right = par->right->right;
      free(save_node);
    }
    return root;
  }

  else{
    //find the new node to delete    
    new_del_node = maxVal(delnode->left);
    delnode->cPtr->points = new_del_node->cPtr->points;
    
    strcpy(save_name, new_del_node->cPtr->name);
    Delete(root, save_name);
    
    strcpy(delnode->cPtr->name, save_name);
    
    return root;
  }
  
}

//returns the max value in the left subtree
treenode* maxVal(treenode *root) {
  
  if (root->right == NULL)
    return root;
    
  else
    return maxVal(root->right);
}

//Find parent of a node
treenode* parent(treenode *root, treenode *node) {
  // Take care of NULL cases.
  if (root == NULL || root == node)
    return NULL;
  
  // The root is the direct parent of node.
  if (root->left == node || root->right == node)
    return root;
  
  // Look for node's parent in the left side of the tree.
  if (strcmp(node->cPtr->name, root->cPtr->name)<0)
    return parent(root->left, node);
    
  // Look for node's parent in the right side of the tree.
  else if (strcmp(node->cPtr->name, root->cPtr->name)>0)
    return parent(root->right, node);
  
  return NULL; // Catch any other extraneous cases.
}

//Checks if the node is a leaf node
int isLeaf(treenode *node) {
return (node->left == NULL && node->right == NULL);
}

// Returns 1 if node has a left child and no right child.
int hasOnlyLeftChild(treenode *node) {
return (node->left!= NULL && node->right == NULL);
}

// Returns 1 if node has a right child and no left child.
int hasOnlyRightChild(treenode *node) {
return (node->left== NULL && node->right != NULL);
}

//Calculates the height of the current node
int height(treenode* root, char* name, int count){
  if(root == NULL){
    return 0;
  }

  if(strcmp(root->cPtr->name, name)==0)
    return count;

  else if(strcmp(root->cPtr->name, name)>0)
    return height(root->left, name, count+1);

  else
    return height(root->right, name, count+1);

}

// prints the Tree in order
void inOrder(treenode* root){
  if(root!=NULL){
    inOrder(root->left);
    printf("%s %d__", root->cPtr->name, root->cPtr->points);
    inOrder(root->right);
  }
}

//Free all nodes within the Tree
void freePostOrder(treenode* root){
  if(root!=NULL){
    freePostOrder(root->left);
    freePostOrder(root->right);
    free(root->cPtr);
    free(root);
  }
}