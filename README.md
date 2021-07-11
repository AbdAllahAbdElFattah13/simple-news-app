# simple-news-app
A simple news app allows searhcing for news given a search term.

# Setup
To be able to build this app locally, you need to have API key from [newsapi.org](https://newsapi.org/). To include the API key in the project follow the steps:

1. Go to [newsapi.org](https://newsapi.org/).
2. Click [Get API Key](https://newsapi.org/register)
3. Register a free/developer account to obtain the API key.
4. Copy the provided API key.
5. In app root directly, open `local.properties` file. If it's not there, create one.
6. Paste the provided API key into the file in the following form: `newsApiKey="xxxxxxxxxxxxxx"`
7. Now you should be able to build and run the app normally.

# App Architecture overview
App follows [Google's recommended app architecture](https://developer.android.com/jetpack/guide#recommended-app-arch), which consists mainly of the following layers:
* UI. *Single Activity with multiple Fragments*
* Presentation. *ViewModel/LiveData*
* Repository to abstruct data sources.
* Remote. *[Unofficial Java News client](https://github.com/KwabenBerko/News-API-Java)*

[Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) in conjunction with [Rxjava 2.x](https://github.com/ReactiveX/RxJava/tree/2.x) was used to handle the news pagination.

[Android Navigation Component](https://developer.android.com/guide/navigation) was used to handle navigation between different screens and pass data between screens.

Dependencies are bing injected manually which works fine for now.

[JUint 4](https://junit.org/junit4/), [mockito-kotlin](https://github.com/mockito/mockito-kotlin) along side some android core testing libraries were used for unit tests.

# Communication
- To contribute: check the [issues](https://github.com/AbdAllahAbdElFattah13/simple-news-app/issues) for the issue or the feature request first, if it's not there please create one. Once discussed feel free to fork and create a PR for review.
- For questions: Open an [issue](https://github.com/AbdAllahAbdElFattah13/simple-news-app/issues) and add the `question` label to it.
