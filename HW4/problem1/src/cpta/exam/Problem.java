package cpta.exam;

import java.util.List;
import java.util.Set;

// added by me
import java.util.LinkedList;
import java.util.ListIterator;

public class Problem {
    public String id;
    public String testCasesDirPath;
    public List<TestCase> testCases;
    public String targetFileName;
    public String wrappersDirPath;
    public Set<String> judgingTypes;

    public static String LEADING_WHITESPACES = "leading-whitespaces";
    public static String IGNORE_WHITESPACES = "ignore-whitespaces";
    public static String CASE_INSENSITIVE = "case-insensitive";
    public static String IGNORE_SPECIAL_CHARACTERS = "ignore-special-characters";

    public Problem(
            String id, String testCasesDirPath, List<TestCase> testCases,
            String targetFileName, String wrappersDirPath, Set<String> judgingTypes
    ) {
        this.id = id;
        this.testCasesDirPath = testCasesDirPath;
        this.testCases = testCases;
        this.targetFileName = targetFileName;
        this.wrappersDirPath = wrappersDirPath;
        this.judgingTypes = judgingTypes;
    }

}

