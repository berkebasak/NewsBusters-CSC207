package data_access.save_article;

import entity.Article;
import use_case.save_article.SaveArticleDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileSaveArticleDataAccess implements SaveArticleDataAccessInterface {

    private final File storageFile;
    private final Map<String, Set<String>> urlsByUser = new HashMap<>();

    public FileSaveArticleDataAccess(String filePath) throws IOException{
        this.storageFile = new File(filePath);
        try {
            File parent = storageFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs(); // ensure directory exists
            }
            if (!storageFile.exists()){
                storageFile.createNewFile();
            }
            loadExisting();
        } catch (IOException e) {
            throw new RuntimeException("Could not create/access storage file:" + filePath, e);
        }

    }

    private void loadExisting() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if ((line.isBlank())) continue;

                String[] parts = line.split("\\|", 3);
                if (parts.length < 3) {
                    // ignore malformed lines
                    continue;
                }
                String username = parts[0].trim();
                String url = parts[2].trim();

                if (username.isEmpty() || url.isEmpty()) {
                    continue;
                }

                urlsByUser
                        .computeIfAbsent(username, k -> new HashSet<>())
                        .add(url);
            }
        }
    }

    @Override
    public boolean existsByUserandUrl(String username, String url){
        if (username == null || url == null) {
            return false;
        }
        Set<String> urls = urlsByUser.get(username);
        return urls != null && urls.contains(url);
    }

    @Override
    public void saveForUser(String username, Article article) throws Exception {
        if (username == null || username.isBlank() || article == null) {
            return;
        }

        try (FileWriter fw = new FileWriter(storageFile, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            // username|title|url
            bw.write(username + "|" + article.getTitle() + "|" + article.getUrl());
            bw.newLine();
        }

        if (article.getUrl() != null) {
            urlsByUser
                    .computeIfAbsent(username, k -> new HashSet<>())
                    .add(article.getUrl());
        }
    }
}
