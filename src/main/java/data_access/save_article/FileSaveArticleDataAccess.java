package data_access.save_article;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import entity.Article;
import use_case.save_article.SaveArticleDataAccessInterface;

public class FileSaveArticleDataAccess implements SaveArticleDataAccessInterface {

    private static final int PARTS_LIMIT = 3;
    private final File storageFile;
    private final Map<String, Set<String>> urlsByUser = new HashMap<>();

    public FileSaveArticleDataAccess(String filePath) throws IOException {
        this.storageFile = new File(filePath);
        try {
            final File parent = storageFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
                // ensure directory exists
            }
            if (!storageFile.exists()) {
                storageFile.createNewFile();
            }
            loadExisting();
        }
        catch (IOException ex) {
            throw new RuntimeException("Could not create/access storage file:" + filePath, ex);
        }

    }

    private void loadExisting() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                final String[] parts = line.split("\\|", PARTS_LIMIT);

                if (parts.length < PARTS_LIMIT) {
                    // ignore malformed lines
                    continue;
                }
                final String username = parts[0].trim();
                final String url = parts[2].trim();

                if (username.isEmpty() || url.isEmpty()) {
                    continue;
                }
                urlsByUser
                        .computeIfAbsent(username, key -> new HashSet<>())
                        .add(url);
            }
        }
    }

    @Override
    public boolean existsByUserandUrl(String username, String url) {
        boolean exists = false;

        if (username != null && url != null) {
            final Set<String> urls = urlsByUser.get(username);
            exists = urls != null && urls.contains(url);
        }

        return exists;
    }

    @Override
    public void saveForUser(String username, Article article) throws Exception {
        if (username != null && !username.isBlank() && article != null) {

            try (FileWriter fw = new FileWriter(storageFile, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {

                bw.write(username + "|" + article.getTitle() + "|" + article.getUrl());
                bw.newLine();
            }

            if (article.getUrl() != null) {
                urlsByUser
                        .computeIfAbsent(username, key -> new HashSet<>())
                        .add(article.getUrl());
            }
        }
    }

}
