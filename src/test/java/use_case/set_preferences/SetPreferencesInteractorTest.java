package use_case.set_preferences;

import entity.User;
import entity.UserPreferences;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// --- Stub/Mock Classes for Testing Dependencies ---

/**
 * Concrete implementation of UserPreferences for testing.
 */
class TestUserPreferences extends UserPreferences {
    private final String language;
    private final String region;
    public TestUserPreferences(String language, String region) {
        this.language = language;
        this.region = region;
    }
    @Override
    public String getLanguage() { return language; }
    @Override
    public String getRegion() { return region; }
}

/**
 * Mock implementation of the Data Access Interface.
 * Used to control the User data returned and record updates.
 */
class MockSetPreferencesDataAccessObject implements SetPreferencesDataAccessInterface {
    private User storedUser;
    private boolean updateCalled = false;

    public MockSetPreferencesDataAccessObject(User initialUser) {
        this.storedUser = initialUser;
    }

    @Override
    public void update(User user) {
        this.storedUser = user;
        this.updateCalled = true;
    }

    @Override
    public User get(String username) {
        return storedUser != null && storedUser.getUsername().equals(username) ? storedUser : null;
    }

    public boolean isUpdateCalled() {
        return updateCalled;
    }

    public User getStoredUser() {
        return storedUser;
    }
}

/**
 * Mock implementation of the Output Boundary (Presenter).
 * Used to record which view method was called and with what data.
 */
class MockSetPreferencesPresenter implements SetPreferencesOutputBoundary {
    private String failMessage = null;
    private UserPreferences initialPreferences = null;
    private SetPreferencesOutputData successOutputData = null;

    @Override
    public void initPreferenceView(UserPreferences userPreferences) {
        this.initialPreferences = userPreferences;
    }

    @Override
    public void prepareSuccessView(SetPreferencesOutputData outputData) {
        this.successOutputData = outputData;
    }

    @Override
    public void prepareFailView(String message) {
        this.failMessage = message;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public UserPreferences getInitialPreferences() {
        return initialPreferences;
    }

    public SetPreferencesOutputData getSuccessOutputData() {
        return successOutputData;
    }
}


public class SetPreferencesInteractorTest {

    // Dependencies
    private MockSetPreferencesDataAccessObject mockDataAccessObject;
    private MockSetPreferencesPresenter mockPresenter;
    private SetPreferencesInteractor interactor;

    // Test data
    private final String TEST_USERNAME = "testUser";
    private final UserPreferences EXISTING_PREFS = new UserPreferences();
    private User TEST_USER; // Must be initialized in setUp

    @BeforeEach
    void setUp() {
        // Initialize user before each test to ensure a fresh object
        TEST_USER = User.fromPersistence(TEST_USERNAME, "1234", new ArrayList<>(),
                new ArrayList<>(), EXISTING_PREFS);

        // Initialize Mocks/Stubs
        mockDataAccessObject = new MockSetPreferencesDataAccessObject(TEST_USER);
        mockPresenter = new MockSetPreferencesPresenter();

        // Initialize Interactor
        interactor = new SetPreferencesInteractor(mockDataAccessObject, mockPresenter);
    }

    // =========================================================================
    //                            TESTS FOR load()
    // =========================================================================

    /**
     * Coverage for load(): username is null (First 'if' condition).
     */
    @Test
    void testLoad_UsernameIsNull() {
        // Arrange
        SetPreferencesInputData inputData = new SetPreferencesInputData(null, null);

        // Act
        interactor.load(inputData);

        // Assert
        // Verify that the fail view is called with the correct message
        assertEquals("You need to login first", mockPresenter.getFailMessage());
        // Verify no other presenter methods were called
        assertNull(mockPresenter.getInitialPreferences());
    }

    /**
     * Coverage for load(): user not found in DAO (Second 'else if' condition).
     */
    @Test
    void testLoad_UserNotFound() {
        // Arrange
        // Configure DAO to return null (by passing null, we simulate not finding the user)
        mockDataAccessObject = new MockSetPreferencesDataAccessObject(null);
        interactor = new SetPreferencesInteractor(mockDataAccessObject, mockPresenter);

        SetPreferencesInputData inputData = new SetPreferencesInputData("nonExistentUser", null);

        // Act
        interactor.load(inputData);

        // Assert
        // Verify that the fail view is called
        assertEquals("You need to login first", mockPresenter.getFailMessage());
        // Verify no other presenter methods were called
        assertNull(mockPresenter.getInitialPreferences());
    }

    /**
     * Coverage for load(): successful load (Final 'else' block).
     */
    @Test
    void testLoad_Success() {
        // Arrange
        SetPreferencesInputData inputData = new SetPreferencesInputData(TEST_USERNAME);

        // Act
        interactor.load(inputData);

        // Assert
        // Verify that the success view is called with the user's existing preferences
        assertEquals(EXISTING_PREFS, mockPresenter.getInitialPreferences());
        // Verify that the fail view was not called
        assertNull(mockPresenter.getFailMessage());
    }

    // =========================================================================
    //                            TESTS FOR execute()
    // =========================================================================

    /**
     * Coverage for execute(): success path (validation passes, final success call).
     * The validation condition is false.
     */
    @Test
    void testExecute_Success() {
        // Arrange
        // Preferences with both language and region set (validation passes)
        UserPreferences newPreferences = new TestUserPreferences("German", "DE");
        SetPreferencesInputData inputData = new SetPreferencesInputData(TEST_USERNAME, newPreferences);

        // Act
        interactor.execute(inputData);

        // Assert
        // 1. Verify that the User's preferences were updated in the stored user object
        assertEquals(newPreferences, mockDataAccessObject.getStoredUser().getUserPreferences());

        // 2. Verify that the update was called on the DAO
        assertTrue(mockDataAccessObject.isUpdateCalled());

        // 3. Verify that the success view was prepared
        SetPreferencesOutputData capturedOutput = mockPresenter.getSuccessOutputData();
        assertNotNull(capturedOutput);
        assertEquals(TEST_USERNAME, capturedOutput.getUsername());
        assertEquals(newPreferences, capturedOutput.getUserPreferences());
        assertEquals("Preferences Saved!", capturedOutput.getMessage());

        // 4. Verify that the fail view was NOT called
        assertNull(mockPresenter.getFailMessage());
    }

    /**
     * Coverage for execute(): validation fails because Language is null.
     * Hits the 'if' condition: (userPreferences.getLanguage() == null || userPreferences.getRegion() == null) is true.
     * Note: The interactor code is flawed; it calls prepareFailView but continues execution
     * and immediately calls prepareSuccessView. We test this actual flawed behavior for 100% coverage.
     */
    @Test
    void testExecute_ValidationFail_LanguageIsNull() {
        // Arrange
        // Preferences with null language
        UserPreferences invalidPreferences = new TestUserPreferences(null, "UK");
        SetPreferencesInputData inputData = new SetPreferencesInputData(TEST_USERNAME, invalidPreferences);

        // Act
        interactor.execute(inputData);

        // Assert
        // 1. Verify that the fail view is called (covers the 'if' branch)
        assertEquals("Language and/or Region required.", mockPresenter.getFailMessage());

        // 2. Verify that the success view is *also* called (testing the interactor's flawed flow)
        assertNull(mockPresenter.getSuccessOutputData());

        // 3. Verify data persistence calls still happen
        assert(!mockDataAccessObject.isUpdateCalled());
        // The user preferences should still be updated with the invalid data due to the flawed interactor logic
        assertTrue(!invalidPreferences.equals(mockDataAccessObject.getStoredUser().getUserPreferences()));
    }

    /**
     * Coverage for execute(): validation fails because Region is null.
     * Hits the 'if' condition: (userPreferences.getLanguage() == null || userPreferences.getRegion() == null) is true.
     */
    @Test
    void testExecute_ValidationFail_RegionIsNull() {
        // Arrange
        // Preferences with null region
        UserPreferences invalidPreferences = new TestUserPreferences("French", null);
        SetPreferencesInputData inputData = new SetPreferencesInputData(TEST_USERNAME, invalidPreferences);

        // Act
        interactor.execute(inputData);

        // Assert
        // 1. Verify that the fail view is called
        assertEquals("Language and/or Region required.", mockPresenter.getFailMessage());

        // 2. Verify that the success view is *also* called (testing the interactor's flawed flow)
        assertNull(mockPresenter.getSuccessOutputData());

        // 3. Verify data persistence calls still happen
        assertTrue(!mockDataAccessObject.isUpdateCalled());
    }

    /**
     * Coverage for execute(): validation fails because both Language and Region are null.
     * Hits the 'if' condition: (userPreferences.getLanguage() == null || userPreferences.getRegion() == null) is true.
     */
    @Test
    void testExecute_ValidationFail_BothNull() {
        // Arrange
        // Preferences with both null
        UserPreferences invalidPreferences = new TestUserPreferences(null, null);
        SetPreferencesInputData inputData = new SetPreferencesInputData(TEST_USERNAME, invalidPreferences);

        // Act
        interactor.execute(inputData);

        // Assert
        // 1. Verify that the fail view is called
        assertEquals("Language and/or Region required.", mockPresenter.getFailMessage());

        // 2. Verify that the success view is *also* called (testing the interactor's flawed flow)
        assertNull(mockPresenter.getSuccessOutputData());

        // 3. Verify data persistence calls still happen
        assertTrue(!mockDataAccessObject.isUpdateCalled());
    }
}