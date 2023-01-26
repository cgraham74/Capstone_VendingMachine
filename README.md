# Capstone_VendingMachine
Capstone pair programming project.
A vending machine application that dispenses beverages, candy, chips, and gum. 
Each item has a name and price.
Main menu displays when the software runs. It is stocked via an input file and automatically restocked each time the application runs.
Customers can display items and quantity remaining including if a product has sold out. 
Customers can feed money and receive change in the smallest amount of coins possible and the balance is updated.
We used the greedy Algorithm to return change. 
We also incuded the optional sales output feature with unique file names. 



#Run VendingMachineCLI


You will be given 3 choices.

  1.) Display Vending Machine Items
  
    Will display all items loaded from vendingmachine.csv into a List datastructure located within the Inventory class
    
  2.) Purchase
  
    Will display the purchace menu that consists of 3 options.
    
      1.) Feed Money - Prompts users to feed money. Only accepts 1, 2, 5, 10 values until they exit by hitting 0. Displays a running total after each entry.
      
      2.) Select Product - Displays a list of products, Their ID, price, type, and amount left. Can purchase until funds are depleted. Only accepts valid input.
      
      3.) Finish transaction - Creates or updates sales and security logs. Calls the greedy coin algorithm to dispense change. Updates balance and returns to main menu.
      
  3.) Exits the program.
  
  There is a hidden Option on this menu.  If you select 4 - It generates a custom sales report for the life of the machine with a unique timestamped filename.
      
    
![greedyCoin](https://user-images.githubusercontent.com/18030411/214900727-042a5506-c381-4599-a035-15e8c6921901.png)
