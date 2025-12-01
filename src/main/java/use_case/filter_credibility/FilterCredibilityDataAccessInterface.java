package use_case.filter_credibility;

/**
 * Data access interface for the filter credibility use case.
 * Currently not needed as filtering is done in-memory,
 * but included for consistency with the architecture pattern.
 * 
 * If future requirements need to persist filter preferences or
 * retrieve filtered articles from a database, this interface can be implemented.
 */
public interface FilterCredibilityDataAccessInterface {
    // No methods needed for in-memory filtering
    // Can be extended in the future if needed
}
