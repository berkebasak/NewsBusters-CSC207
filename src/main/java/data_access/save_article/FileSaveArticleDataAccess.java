package data_access.save_article;

import entity.Article;
import use_case.save_article.SaveArticleDataAccessInterface;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class FileSaveArticleDataAccess implements SaveArticleDataAccessInterface {

    private final File storageFile;
    private final Set<String> ids = new HashSet<>();
    private final Set<String> urls = new HashSet<>();

    public FileSaveArticleDataAccess(String filePath) throws IOException{
        this.storageFile = new File(filePath);
        try {
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

                if (parts.length >= 1 && !parts[0].isBlank()) {
                    ids.add(parts[0]);
                }
                if (parts.length >= 3 && !parts[2].isBlank()) {
                    urls.add(parts[2]);
                }
            }
        }
    }

    @Override
    public boolean existsById(String id){
        return id != null && ids.contains(id);
    }

    @Override
    public boolean existsByUrl(String url){
        return url != null && urls.contains(url);
    }

    @Override
    public void save(Article article) throws Exception{
        try (FileWriter fw = new FileWriter(storageFile, true);
             BufferedWriter bw = new BufferedWriter(fw)){

            bw.write(article.getId() + "|" + article.getTitle() + "|" + article.getUrl());
            bw.newLine();
        }

        if (article.getId() != null){
            ids.add(article.getId());
        }
        if (article.getUrl() != null){
            urls.add(article.getUrl());
        }

    }
}
