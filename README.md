# To-Do List App

## Spec.

- Users can see all to-do tasks sorted by due date with descending order in his/her to-do list.
- Show a CTA for empty to-do list to guide the user add his/her first to-do task.
- Users can add/update/remove to-do tasks.
- The following information is presented at each to-do task in the to-do list:
  - title: ex: See a doctor. Please add a placeholder to guide users to input.
  - description: ex: Take the Bus 123. Please add a placeholder to guide users to input.
  - created date: ex: 2023/03/27. The default value is now.
  - due date: ex: 2023/03/28. The default value is now + 24 hours.
  - location coordinate where the task is added: ex: 25.0174719, 121.3662922. The default location is the user's current location acquired by device GPS.
  
  These data can be input by manual in the editor and the default values is filled when the user open the editor to add a to-do task.
  
- Users can add and browse unlimited to-do tasks in his/her to-do list.
- All added to-do tasks are stored in the app. In other words, all added tasks can be displayed whenever users open this app.
- To-do list UI keeps up-to-date in realtime after a to-do task is added/updated/removed. In other words, a user can see the UI changes without refreshing UI manually.
- Display a random quote fetched by the free and open source quotations API in the To-do list: [Quotable](https://github.com/lukePeavey/quotable). To-do list shows errors if the API failed or there is no Internet.
- (Bonus) Offline quote: Get a random quote with `DailyQuote::Get()` in daily_quote.cpp. In other words, the To-do list shows 2 quotes: one is from [Quotable](https://github.com/lukePeavey/quotable) and another one is from `DailyQuote`.

## Platform

Choose one platform to implement according to the job you applied:

- iOS
- Android

Please follow the requirement for your choice:

- iOS:

  - IDE: Xcode 14.3

  - Language: Swift 5.8

  - Minimum iOS version support: iOS 14
  
  - UI framework: SwiftUI
  
- Android

  - IDE: Android Studio Electric Eel
  
  - Language: Kotlin 1.7.20
  
  - compileSdkVersion: 24
  
  - minSdkVersion: 31
  
  - targetSdkVersion: 33

  - UI framework: Jetpack Compose

## Submit

Submit your project by replying the interview email with the following information before deadline:

- The GitHub repo link to your implementation.

- Summary your implementation including but not limited to:
  
  - What's your app architecture? Why do you use this app architecture?
  
  - Did you use any 3rd-party libraries? If yes, why choose them? If not, why not use them?
  
  - Which part is the hardest? Why? How to approach this?

  - How long did it take? If you could start over, how would you speed up your implementation?

Let us know if you have any questions.
