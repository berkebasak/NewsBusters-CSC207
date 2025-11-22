package use_case.top_headlines;

import entity.Article;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TopHeadlinesInteractorTest {

    @Test
    void limitsToTwentyArticles() {
        TopHeadlinesUserDataAccessInterface fakeDao = new TopHeadlinesUserDataAccessInterface() {
            @Override
            public List<Article> fetchTopHeadlines() {
                List<Article> list = new ArrayList<>();
                for (int i = 0; i < 25; i++) {
                    Article a = new Article();
                    a.setTitle("Headline " + i);
                    a.setSource("example-source");
                    a.setUrl("https://example.com/article-" + i);
                    list.add(a);
                }
                return list;
            }
        };

        class CapturingPresenter implements TopHeadlinesOutputBoundary {
            TopHeadlinesOutputData lastData;

            @Override
            public void present(TopHeadlinesOutputData outputData) {
                this.lastData = outputData;
            }
        }

        CapturingPresenter presenter = new CapturingPresenter();
        TopHeadlinesInputBoundary interactor = new TopHeadlinesInteractor(fakeDao, presenter);

        TopHeadlinesInputData inputData = new TopHeadlinesInputData("top");
        interactor.execute(inputData);

        assertNotNull(presenter.lastData);
        List<Article> result = presenter.lastData.getArticles();
        assertNotNull(result);
        assertEquals(20, result.size());
        assertEquals("Headline 0", result.get(0).getTitle());
        assertEquals("Headline 19", result.get(19).getTitle());
    }
}
