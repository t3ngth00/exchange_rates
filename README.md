
# Exchange Rate Calculator App (Simple Euro to Vietnam Dong Converter) 
This guide explains a user-friendly Android application that converts Euros to Vietnam Dong (VND) using live exchange rates. 

## What it Does:

Converts Euro amounts to VND based on the latest exchange rates.
Fetches exchange rates from an external API (https://api.fxratesapi.com/).

## Getting Started:

1. Grab the Code: Clone the project from its GitHub repository (link not provided).
2. Open in Android Studio: Import the project into Android Studio for development.
3. Configure API:
- Locate the ExchangeRatesApi.kt file.
- Inside this file, find the BASE_URL variable. Replace it with the actual base URL of your chosen currency API (likely with an API key).
- Note: Some APIs require an API key. If yours does, add it to the BASE_URL within ExchangeRatesApi.kt.
4. Run the App: Launch the app on your Android emulator or a physical device.

## Using the App:

The app features two input fields:
- Enter the Euro amount you want to convert.
- The converted VND amount will be displayed (initially 0).
The exchange rate is automatically retrieved upon app launch.
To convert, simply enter a Euro value and tap "Calculate" The corresponding VND amount will appear in the second field.

### Technical Details:

Dependencies:
- Retrofit: Handles network communication with the currency API.
- Gson: Parses JSON data received from the API.
- ViewModel & LiveData: Manages UI state and data effectively.
  
API Source: The app utilizes https://api.fxratesapi.com/ to fetch live currency rates.

