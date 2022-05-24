package com.learning.restaurantrater;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //declaring variables to be used throughout the program
    myRestaurantTableHelper myRestaurantTableHelper;
    String restaurantName, restaurantAddress, city, state, zipcode, entreeName, appetizerName,
            dessertName, restaurantId;
    double entreeRating, appetizerRating, dessertRating;
    EditText editTextRestaurantName, editTextRestaurantAddress, editTextCity, editTextState,
            editTextZipCode, editTextEntreeName, editTextAppetizerName, editTextDessertName;
    Button buttonSave, buttonRate, buttonDoRating;
    RatingBar entreeRatingBar,  appetizerRatingBar, dessertRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapWidgetsMainActivity();

    }

    //helper function that makes sure restaurant information was entered correctly and that it is in
    // lowercase so that all text in the db is uniform. returns true if details were satisfactory
    public boolean getLocationDetails(EditText editTextRestaurantName, EditText editTextRestaurantAddress,
                                      EditText editTextCity, EditText editTextState, EditText editTextZipCode){

        if (!editTextRestaurantName.getText().toString().equals("") &&
                !editTextRestaurantAddress.getText().toString().equals("") ){

            restaurantName = editTextRestaurantName.getText().toString().toLowerCase();
            restaurantAddress = editTextRestaurantAddress.getText().toString().toLowerCase();
            city = editTextCity.getText().toString().toLowerCase();
            state = editTextState.getText().toString().toLowerCase();
            zipcode = editTextZipCode.getText().toString().toLowerCase();
            return true;
        }
        return false;
    }

    public void mapWidgetsMainActivity(){

        //mapping variables to widgets in main activity
        editTextRestaurantName = findViewById(R.id.editTextLocationName);
        editTextRestaurantAddress = findViewById(R.id.editTextStreetAddress);
        editTextCity = findViewById(R.id.editTextCity);
        editTextState = findViewById(R.id.editTextState);
        editTextZipCode = findViewById(R.id.editTextZipcode);
        buttonRate = findViewById(R.id.buttonRate);
        buttonSave = findViewById(R.id.buttonSaveLocationDetails);
        //initializing dbhelper variable
        myRestaurantTableHelper = new myRestaurantTableHelper(this, null,null, 1);

    }

    public void mapWidgetsRatingLayout(){

        //mapping variables to widgets in rating activity
        appetizerRatingBar = findViewById(R.id.ratingBarAppetizer);
        dessertRatingBar = findViewById(R.id.ratingBarDessert);
        entreeRatingBar = findViewById(R.id.ratingBarEntree);
        buttonDoRating = findViewById(R.id.buttonDoRating);
        editTextEntreeName = findViewById(R.id.editTextEntree);
        editTextAppetizerName = findViewById(R.id.editTextAppetizer);
        editTextDessertName = findViewById(R.id.editTextDessert);

    }

    public void changeLayoutToRatingLayout(View view){
        //changing the content view to the rating layout.

        setContentView(R.layout.rating_layout);

        //mapping variables to the widgets in the rating layout.
        mapWidgetsRatingLayout();

    }


    public void doRating(View view){
        //checking if store details were entered correctly.
        if (getLocationDetails(editTextRestaurantName, editTextRestaurantAddress, editTextCity,
                editTextState, editTextZipCode)){

            //storing the ratings in variables
            dessertRating = dessertRatingBar.getRating();
            appetizerRating = appetizerRatingBar.getRating();
            entreeRating = entreeRatingBar.getRating();
            entreeName = editTextEntreeName.getText().toString().toLowerCase();
            appetizerName = editTextAppetizerName.getText().toString().toLowerCase();
            dessertName = editTextDessertName.getText().toString().toLowerCase();
            restaurantId = myRestaurantTableHelper.getId(restaurantName,restaurantAddress);

            System.out.println(appetizerName);
            System.out.println(dessertName);
            System.out.println(entreeName);

            //using our db handler to update the database.
            myRestaurantTableHelper.addDishToDB(entreeName, appetizerName, dessertName, entreeRating,
                    appetizerRating, dessertRating, restaurantId);

            setContentView(R.layout.activity_main);

            mapWidgetsMainActivity();


            //outputting a message to let the user know it was a success
            Toast.makeText(getApplicationContext(),
                    "Your ratings have been saved to the database",
                    Toast.LENGTH_LONG).show();
        }

        //location details werent set so the user needs to let us know which store to rate.
        else {
            Toast.makeText(getApplicationContext(),
                    "Please enter valid location information",
                    Toast.LENGTH_LONG).show();
        }
    }


    public void saveLocation(View view){

        //calling a helper which checks if store details have been entered correctly
        if(getLocationDetails(editTextRestaurantName, editTextRestaurantAddress, editTextCity,
                editTextState, editTextZipCode)){

            //store details entered correct, now checking if the store is actually in the db
            if(myRestaurantTableHelper.findHandler(restaurantName, restaurantAddress)) {

                //case details are in the db, update the db using update helper function
                myRestaurantTableHelper.updateHandler(restaurantName, restaurantAddress, city,
                        state, zipcode);
                //store details are not in the db. Add to the db
            } else {

                //using db helper class method to add to the db
                myRestaurantTableHelper.addToDB(restaurantName, restaurantAddress, city,
                        state, zipcode);

            }

            //db has been altered, letting the user know details are saved.
            Toast.makeText(getApplicationContext(),
                    "You have successfully saved your location details",
                    Toast.LENGTH_LONG).show();

            //store details have been entered incorrectly, notifying the user to try again.
        } else {
            //showing user a message.
            Toast.makeText(getApplicationContext(),
                    "You have not yet entered any location details, please try again",
                    Toast.LENGTH_LONG).show();

        }

    }


}
