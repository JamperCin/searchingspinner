[![](https://jitpack.io/v/JamperCin/searchingspinner.svg)](https://jitpack.io/#JamperCin/searchingspinner)
# searchingspinner
add search functionality to a spinner and even allow/disallow users to enter their own values to the spinner.
Also have the functionality of adding user's own entered values to the list already loaded in the spinner


**Step 1: Add this to your root build.gradle(Project level) at the end of repositories:**

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  **Step 2: Add the dependency to your dependencies in the build.gradle (Module:App level)**

	dependencies {
	       compile 'com.github.JamperCin:searchingspinner:1.0.9'
	}
  
  
  **Step 3: Just include in it in your xml as a normal view like:**
  ```
  <com.jamper.searchspinner.SearchingSpinner
        android:id="@+id/me"
        android:entries="@array/CountryCodes"
        android:textSize="16sp"
        android:layout_width="match_parent"
        android:layout_height="50dp"/>
```

**and call it from the Activity like:**

```
  SearchingSpinner  spinner = (SearchingSpinner)findViewById(R.id.me);
  spinner.addEntries(getList());   **you can add your list string data here programmatically** 
  spinner.setTitle("Search here");  **Add a title to the searching dialog that pops up**
  spinner.setAcceptLocalEntries(false); **Allow user to enter a value that is not found in the list available** 
  spinner.setLocalEntriesAddable(false);  **Add user's own entries that are not in the list to the already existing list**
  spinner.setItemOnClickDismissDialog(false);   **Dismiss the dialog when user selects an item from the list**
```

**Register an onItemSelectedListener to the spinner and get the item selected and its position from the list**
  ```
  spinner.setOnItemSelectedListener(new OnItemSelected() {
        @Override
        public void onItemSelected(String item, int itemPosition) {
            //Get the selected item and its position here
        }

        @Override
        public void onNothingSelected(String item) {
           //Get the selected item here 
        }
    });
