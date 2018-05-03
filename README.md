# Inventory-App
Inventory App for  Android Basics by Google Nanodegree Program

**The app contains activities for the user to:**

  - Add Inventory
  
  - See Product Details
  
  - Edit Product Details
  
  - See a list of all inventory from a Main Activity
  
 ## List Item Layout in the Main Activity

  - In the Main Activity, each list item displays the Product Name, Price, and Quantity.

  - Each list item also contains a SaleButton that reduces the total quantity of that particular product by one (include logic so that no negative quantities are displayed).

 ## Product Detail Layout

  - The Product Detail Layout displays the Product Name, Price, Quantity, Supplier Name, and Supplier Phone Number that's stored in the database.

  - The Product Detail Layout also contains buttons that increase and decrease the available quantity displayed.

  - Add a check in the code to ensure that no negative quantities display (zero is the lowest amount).

  - The Product Detail Layout contains a button to delete the product record entirely.

  - The Product Detail Layout contains a button to order from the supplier. In other words, there exists a button to contains a button for the user to contact the supplier via an intent to a phone app using the Supplier Phone Number stored in the database.

 ## Default Textview

  - When there is no information to display in the database, the layout displays a TextView with instructions on how to populate the database (e.g. what should be entered in the field, which fields are required).

 ## ListView Population

  - The Main Activity displaying the list of current inventory contains a ListView that populates with the current products stored in the table.
  
  ![screenshot_20180503_150931](https://user-images.githubusercontent.com/35422422/39577574-62424b2e-4eea-11e8-81fe-333f0c02d5ce.png)
  ![screenshot_20180503_151336](https://user-images.githubusercontent.com/35422422/39577579-66081c02-4eea-11e8-8459-ca0e888af176.png)

 ## Add Product Button

  - The Main Activity contains an Add Product Button prompts the user for product information and supplier information which are then properly stored in the table.

  - Before the information is added to the table, it must be validated -In particular, empty product information is not accepted. If user inputs invalid product information (name, price, quantity, supplier name, supplier phone number), instead of erroring out, the app includes logic to validate that no null values are accepted. If a null value is inputted, add a Toast that prompts the user to input the correct information before they can continue.
  
  ![screenshot_20180503_150943](https://user-images.githubusercontent.com/35422422/39577639-8bb6f842-4eea-11e8-8f62-794d32d2f408.png)


 ## Input Validation

  - In the Edit Product Activity, user input is validated. In particular, empty product information is not accepted. If user inputs invalid product information (name, price, quantity, supplier name, supplier phone number), instead of erroring out, the app includes logic to validate that no null values are accepted. If a null value is inputted, add a Toast that prompts the user to input the correct information before they can continue.

 ## Sale Button

  - In the Main Activity that displays a list of all available inventory, each List Item contains a Sale Button which reduces the available quantity for that particular product by one (include logic so that no negative quantities are displayed).

 ## Detail View Intent

  When a user clicks on a List Item from the Main Activity, it opens up the detail screen for the correct product.
  
  ![screenshot_20180503_150959](https://user-images.githubusercontent.com/35422422/39577655-978cc4a8-4eea-11e8-8e03-424ed478ad1a.png)


 ## Modify Quantity Buttons

  - In the Detail View for each item, there are Buttons that correctly increase or decrease the quantity for the correct product.

  - Add a check in the code to ensure that no negative quantities display (zero is the lowest amount).

  - The student may also add input for how much to increase or decrease the quantity by if not using the default of 1.

 ## Order Button

  - The Detail Layout contains a button for the user to contact the supplier via an intent to a phone app using the Supplier Phone Number stored in the database.
  
  ![screenshot_20180503_151040](https://user-images.githubusercontent.com/35422422/39577711-b9503e62-4eea-11e8-8756-458f481e8112.png)

 ## Delete Button

  - In the Detail Layout, there is a Delete Button that prompts the user for confirmation and, if confirmed, deletes the product record entirely and sends the user back to the main activity.
  
  ![screenshot_20180503_151321](https://user-images.githubusercontent.com/35422422/39577739-d08efe38-4eea-11e8-960b-024c8208bb4d.png)



