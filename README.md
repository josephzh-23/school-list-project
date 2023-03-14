# school-list-project


Main features of this projects are summarized as below:

0. User is able to try loading school list after losing and restoring internet connection 

1. If user doesn't have connection upon first opening the app, user can load list after restoring internet connection

2. Upon encountering network error when loading school data, user can retry sending network requests.

3. Project uses paging3 library to achieve paginagion to fetch school lists, MVVM architecture pattern (with repository + service), dependency injection(Hilt dagger)
, flow and coroutine for asynchoronous programming technique. 

4. Test cases are also included to test viewmodel live data and mock network requests 

