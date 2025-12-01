# NewsBusters 📰🚫

**Smart News Aggregation & Fake News Detection**

**Domain:** News Credibility/Smart News

### 📖 About The Project

NewsBusters is a desktop application designed to combat misinformation in the digital age. It allows users to browse current headlines while providing real-time credibility assessments. By combining powerful API's, NewsBusters assigns a "Trust Score" to every article, empowering users to consume information confidently.

### ✨ Key Features

**User Experience**

  * **Smart Feed:** Browse top headlines in the app and read full articles on the web.
  * **Personalization:** Tailor your feed by setting language and region preferences.
  * **Search & Discovery:** Filter news by topic, search by specific keywords, or use the "Discover" tab for recommendations based on reading history.
  * **Trust Scores:** Every article receives a calculated credibility score which you can then filter by to specify your reading experience.
  * **Detailed Breakdown"** Click view details on article cards to view specific signals (Subscores, Domain Reputation Score, AI analysis score, etc.).

### 🛠️ Tech Stack & APIs

NewsBusters integrates three distinct external APIs to aggregate news and analyze its veracity. The application is built using **Java** and handles networking via **okhttp3**.

| API Name | Service Provided |
| :--- | :--- |
| **[NewsData.io](https://newsdata.io/)** | Retrieves global news headlines and supports topic filtering. 
| **[TextRazor](https://www.textrazor.com/)** | Performs NLP (sentiment analysis, entity extraction) to determine content confidence. 
| **[OpenPageRank](https://www.domcop.com/openpagerank/)** | Analyzes domain authority and popularity to determine source reliability. 


### 🚀 Getting Started

#### Prerequisites

  * Java Development Kit (JDK) 11 or higher
  * Maven
  * Internet connection for API access

#### Installation

1.  **Clone the repo**
    ```sh
    git clone https://github.com/berkebasak/NewsBusters.git
    ```
2.  **Navigate to the project directory**
    ```sh
    cd NewsBusters
    ```
3.  **Install dependencies**
    ```sh
    mvn install
    ```
4.  **Configure API Keys**
      * Existing API keys exsist in the .env_example file for immediate program usage
      * Create your own .env file to use your personal keys.

### 🕹️ Usage Guide

1.  **Login:** Enter your credentials to access your personalized profile.
2.  **Set Preferences:** Select your region and language in the Profile menu.
3.  **Browse & Filter:** Use the main dashboard to view top headlines. Sort by **"Highest Trust"** to prioritize reliable news.
4.  **Save:** Click the "Save" icon on any article to store it for later viewing in your Profile.

### 🧩 System Design (Use Cases)

  * **Authentication:** Secure login validation against the database.
  * **Data Aggregation:** Fetches data via NewsAPI; caches for offline connectivity failures.
  * **Scoring Logic:** Aggregates data from TextRazor (Content) and OpenPageRank (Source) to generate a composite score.
  * **Local Storage:** File handling for saving user "Favorite" articles.

### 👥 The Team

  * **Team Name:** NewsBusters
  * **Members:** Berke, Ishaaq, Lana, Ruby, Muhammad, Ava

-----
