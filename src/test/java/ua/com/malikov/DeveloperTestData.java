package ua.com.malikov;

public class DeveloperTestData {
    private static DeveloperTestData ourInstance = new DeveloperTestData();

    public static DeveloperTestData getInstance() {
        return ourInstance;
    }

    private DeveloperTestData() {
    }
}
