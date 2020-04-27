# WeatherLogger
Project for weather log
This project is developed in Kotlin to get weather details based on current location.
API call retrives weather data from "https://openweathermap.org"
If user don't give access for device location, than Riga city's locatio is used default to check application performance.

  -Room database used to store location data
 
 -Retrofit with repository pattern used for API call
 
 -MVVM pattern followed for app structure.
 
 -Widget created day and night mode. for test purpose, database field count is used to switch widged mode.
 
 Ex: If database total record count is ODD number , than app widged will be shown in night mode. If database total record count is EVEN number, than day mode is used for App widged.

All Animations in peoject are implemented using xml files.

## Application Widget
![Widget Screen](/screenshot/screen1.jpg)

## Application Screenshots
![](/screenshot/screen2.jpg)![](/screenshot/screen3.jpg)![](/screenshot/screen4.jpg)



![](/screenshot/screen5.jpg) 



![](/screenshot/screen6.jpg)![](/screenshot/screen7.jpg)

