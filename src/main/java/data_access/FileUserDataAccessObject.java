package data_access;

import entity.Article;
import entity.User;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileUserDataAccessObject implements
        UserDataAccessInterface,
        LoginUserDataAccessInterface,
        SignupUserDataAccessInterface {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final File storage;
    private final Map<String, User> users = new HashMap<>();

    public FileUserDataAccessObject(String filePath) throws IOException {
        this.storage = new File(filePath);
        initializeStorage();
        loadUsers();
    }

    private void initializeStorage() throws IOException {
        File parent = storage.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new IOException("Could not create user storage directory: " + parent);
            }
        }
        if (!storage.exists()) {
            if (!storage.createNewFile()) {
                throw new IOException("Could not create user storage file: " + storage);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(storage))) {
                writer.write(new JSONObject().put("users", new JSONArray()).toString());
            }
        }
    }

    private void loadUsers() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(storage))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line.trim());
            }
            if (builder.length() == 0) {
                return;
            }
            JSONObject root = new JSONObject(builder.toString());
            JSONArray jsonUsers = root.optJSONArray("users");
            if (jsonUsers == null) {
                return;
            }
            for (int i = 0; i < jsonUsers.length(); i++) {
                JSONObject userObj = jsonUsers.getJSONObject(i);
                String username = userObj.getString("username");
                String password = userObj.has("password")
                        ? userObj.getString("password")
                        : userObj.optString("passwordHash", "");
                List<Article> savedArticles = parseArticles(userObj.optJSONArray("savedArticles"));
                User user = User.fromPersistence(username, password, savedArticles);
                users.put(username.toLowerCase(), user);
            }
        }
    }

    @Override
    public boolean existsByName(String username) {
        if (username == null) {
            return false;
        }
        return users.containsKey(username.toLowerCase());
    }

    @Override
    public void save(User user) {
        Objects.requireNonNull(user, "user");
        String key = user.getUsername().toLowerCase();
        if (users.containsKey(key)) {
            throw new IllegalArgumentException("User already exists: " + user.getUsername());
        }
        users.put(key, user);
        persist();
    }

    @Override
    public void update(User user) {
        Objects.requireNonNull(user, "user");
        users.put(user.getUsername().toLowerCase(), user);
        persist();
    }

    @Override
    public User get(String username) {
        if (username == null) {
            return null;
        }
        return users.get(username.toLowerCase());
    }

    @Override
    public Collection<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private void persist() {
        JSONObject root = new JSONObject();
        JSONArray jsonUsers = new JSONArray();
        for (User user : users.values()) {
            jsonUsers.put(serializeUser(user));
        }
        root.put("users", jsonUsers);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storage))) {
            writer.write(root.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist users", e);
        }
    }

    private JSONObject serializeUser(User user) {
        JSONObject json = new JSONObject();
        json.put("username", user.getUsername());
        json.put("password", user.getPassword());

        JSONArray saved = new JSONArray();
        for (Article article : user.getSavedArticles()) {
            saved.put(serializeArticle(article));
        }
        json.put("savedArticles", saved);
        return json;
    }

    private JSONObject serializeArticle(Article article) {
        JSONObject json = new JSONObject();
        json.put("id", article.getId());
        json.put("title", article.getTitle());
        json.put("description", article.getDescription());
        json.put("url", article.getUrl());
        json.put("imageUrl", article.getImageUrl());
        json.put("source", article.getSource());
        if (article.getContent() != null) {
            json.put("content", article.getContent());
        }
        if (article.getPublicationDate() != null) {
            json.put("publicationDate", DATE_FORMATTER.format(article.getPublicationDate()));
        }
        return json;
    }

    private List<Article> parseArticles(JSONArray jsonArray) {
        List<Article> articles = new ArrayList<>();
        if (jsonArray == null) {
            return articles;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            String publicationDate = json.optString("publicationDate", null);
            LocalDateTime date = publicationDate == null || publicationDate.isBlank()
                    ? null
                    : LocalDateTime.parse(publicationDate, DATE_FORMATTER);
            Article article = new Article(
                    json.optString("id", null),
                    json.optString("title", ""),
                    json.optString("description", ""),
                    json.optString("url", ""),
                    json.optString("imageUrl", ""),
                    json.optString("source", "Unknown"),
                    json.optString("content", null),
                    date,
                    null,
                    null,
                    0.0,
                    "Unknown",
                    true
            );
            articles.add(article);
        }
        return articles;
    }
}
