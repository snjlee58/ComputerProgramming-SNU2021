package cpta;

import cpta.environment.Compiler;
import cpta.environment.Executer;
import cpta.exam.ExamSpec;
import cpta.exam.Problem;
import cpta.exam.Student;
import cpta.exam.TestCase;
import cpta.exceptions.CompileErrorException;
import cpta.exceptions.FileSystemRelatedException;
import cpta.exceptions.InvalidFileTypeException;
import cpta.exceptions.RunTimeErrorException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Grader {
    Compiler compiler;
    Executer executer;

    public Grader(Compiler compiler, Executer executer) {
        this.compiler = compiler;
        this.executer = executer;
    }


    public Map<String,Map<String, List<Double>>> gradeSimple(ExamSpec examSpec, String submissionDirPath) {
        // TODO Problem 1-1
        Map<String, Map<String, List<Double>>> scoreMap = new HashMap<>();

        List<Problem> problems = examSpec.problems;
        List<Student> students = examSpec.students;

        ListIterator<Student> studentsIter = students.listIterator();

        // iterate over list of Students
        while (studentsIter.hasNext()){
            // add student to scoreMap
            Student currStudent = studentsIter.next();
            scoreMap.put(currStudent.id, new HashMap<>());

            // iterate over list of Problems
            ListIterator<Problem> problemIter = problems.listIterator();
            while (problemIter.hasNext()) {
                Problem currProblem = problemIter.next();
                scoreMap.get(currStudent.id).put(currProblem.id, new ArrayList<Double>());

                try{
                    // compile submitted .sugo file (assume only 1 for every problem)
                    compiler.compile(submissionDirPath + currStudent.id + File.separator + currProblem.id + File.separator + currProblem.targetFileName);

                        // get filename of .yo file to execute
                    int idxFileExtension = currProblem.targetFileName.indexOf(".sugo");
                    String executeFileName = currProblem.targetFileName.substring(0, idxFileExtension) + ".yo";

                    // iterate over TestCases
                    currProblem.testCases.sort(new sortById());
                    ListIterator<TestCase> testCaseIter = currProblem.testCases.listIterator();
                    while(testCaseIter.hasNext()) {
                        TestCase currTestCase = testCaseIter.next();

                        // execute compiled file
                        String targetFilePath = submissionDirPath + currStudent.id + File.separator + currProblem.id + File.separator + executeFileName;
                        String inputFilePath = currProblem.testCasesDirPath + currTestCase.inputFileName;
                        String outputFilePath = submissionDirPath + currStudent.id + File.separator + currProblem.id + File.separator + currTestCase.outputFileName;
                        executer.execute(targetFilePath, inputFilePath, outputFilePath);

                        // Compare the Student's submitted output file & the Testcase output file
                        File submissionOutputFile = new File(outputFilePath);
                        File testCaseOutputFile = new File(currProblem.testCasesDirPath + currTestCase.outputFileName);

                        Scanner outputLines = new Scanner(submissionOutputFile);
                        String submissionOutputString = "";
                        while (outputLines.hasNext()) {
                                String nextLine = outputLines.nextLine();
                                submissionOutputString = submissionOutputString + nextLine + "\n";
                            }

                        outputLines = new Scanner(testCaseOutputFile);
                        String testCaseOutputString = "";
                        while (outputLines.hasNext()) {
                                String nextLine = outputLines.nextLine();
                                testCaseOutputString = testCaseOutputString + nextLine + "\n";
                            }
                        outputLines.close();


                        // strict grading
                        if (testExactEquality(submissionOutputString, testCaseOutputString))
                            scoreMap.get(currStudent.id).get(currProblem.id).add(currTestCase.score);

                        else scoreMap.get(currStudent.id).get(currProblem.id).add(0.0);

                    }
                } catch(Exception e){

                }
            }
        }
        return scoreMap;
    }

    public Map<String,Map<String, List<Double>>> gradeRobust(ExamSpec examSpec, String submissionDirPath) {
        // TODO Problem 1-2
        Map<String, Map<String, List<Double>>> scoreMap = new HashMap<>();

        List<Problem> problems = examSpec.problems;
        List<Student> students = examSpec.students;

        ListIterator<Student> studentsIter = students.listIterator();

        // iterate over list of Students
        while (studentsIter.hasNext()){
            // add student to scoreMap (regardless of whether they submitted)
            Student currStudent = studentsIter.next();
            scoreMap.put(currStudent.id, new HashMap<>());

            // Group 4: Student Submission Format Errors
            Boolean submissionExists = false;
            File studSubmissionDir = new File(submissionDirPath);
            File[] submittedStudDirs = studSubmissionDir.listFiles(); // array of submitted student directories
            String submittedDirName = currStudent.id;

            // Check if Submission exists + Wrong directory name (replace with submitted directory name if it contains the student ID)
            for (File submittedStudDir : submittedStudDirs){
                if (submittedStudDir.getName().contains(currStudent.id)) {
                    submittedDirName = submittedStudDir.getName();
                    submissionExists = true;
                    break;
                }
            }

            // give 0.0 points for all problems and testcases if there's no submission for the student
            if (!submissionExists){
                ListIterator<Problem> problemIter = problems.listIterator();
                while (problemIter.hasNext()){
                    Problem currProblem = problemIter.next();
                    scoreMap.get(currStudent.id).put(currProblem.id, new ArrayList<Double>());
                    zeroPointsForProblem(currProblem, currStudent, scoreMap);
                }
                continue;
            }

            // iterate over list of Problems
            ListIterator<Problem> problemIter = problems.listIterator();
            while (problemIter.hasNext()) {
                // add problem to scoreMap
                Problem currProblem = problemIter.next();
                scoreMap.get(currStudent.id).put(currProblem.id, new ArrayList<Double>());

                try{
                    // Submitted directory doesn't contain the problem directory: give 0 points for all testCases
                    if (!containsNecessaryDirOrFile(submissionDirPath + submittedDirName + File.separator, currProblem.id)){
                        zeroPointsForProblem(currProblem, currStudent, scoreMap);
                        continue;
                    }

                    // Group 4 : Additional Directory inside the problem directory: move the files out and overwrite any overlapping files (no penalty)
                    File problemDir = new File(submissionDirPath + submittedDirName + File.separator + currProblem.id);
                    File[] problemDirFiles = problemDir.listFiles();
                    for (File problemDirFile_ : problemDirFiles){

                        // Check if the path is a directory or not
                        File problemDirFile = new File(submissionDirPath + submittedDirName + File.separator + currProblem.id + File.separator + problemDirFile_.getName() + File.separator);
                        if (problemDirFile.isDirectory()) {
                            String sourceDirPath = submissionDirPath + submittedDirName + File.separator + currProblem.id + File.separator + problemDirFile.getName() + File.separator;
                            String destDirPath = submissionDirPath + submittedDirName + File.separator + currProblem.id + File.separator;

                            // copy files from additional directory then delete it
                            copyFiles(sourceDirPath, destDirPath);
                            deleteDir(new File(sourceDirPath));

                            break;
                        }
                    }

                    // Group 3: Copy wrapper codes from wrapperDirPath
                    if (currProblem.wrappersDirPath != null){
                        File wrapperDir = new File(currProblem.wrappersDirPath);
                        File[] wrapperCodes_ = wrapperDir.listFiles();
                        LinkedList<File> wrapperCodes = new LinkedList<>();

                        // collect only the .sugo files with correct format
                        for (File wrapperCode: wrapperCodes_) {
                            if (isValidFileType(wrapperCode, ".sugo"))
                                wrapperCodes.add(wrapperCode);
                        }

                        ListIterator<File> wrapperCodesIterator = wrapperCodes.listIterator();
                        while (wrapperCodesIterator.hasNext()){
                            File wrapperCode = wrapperCodesIterator.next();

                            String fileWriterPath = submissionDirPath + submittedDirName + File.separator + currProblem.id + File.separator + wrapperCode.getName();
                            copyFile(wrapperCode, fileWriterPath);
                        }
                    }

                    // COMPILE submitted .sugo file(s)
                    File submissionDir = new File(submissionDirPath + submittedDirName + File.separator + currProblem.id + File.separator);
                    File[] submissionFiles = submissionDir.listFiles();

                    for (File submissionFile : submissionFiles){
                        if (isValidFileType(submissionFile, ".sugo"))
                            compiler.compile(submissionDirPath + submittedDirName + File.separator + currProblem.id + File.separator + submissionFile.getName());
                    }

                    // get filename of .yo file to execute
                    int idxFileExtension = currProblem.targetFileName.indexOf(".sugo");
                    String executeFileName = currProblem.targetFileName.substring(0, idxFileExtension) + ".yo";

                     // update file array after compilation
                    submissionDir = new File(submissionDirPath + submittedDirName + File.separator + currProblem.id + File.separator);
                    submissionFiles = submissionDir.listFiles();


                    // Group 4: Submitted .yo file without corresponding .sugo file (execute, but cut scores by 1/2 for all test cases)
                    int sugoFileCount = 0;
                    int yoFileCount = 0;
                    for (File submissionFile : submissionFiles){
                        if (isValidFileType(submissionFile, ".sugo")) sugoFileCount++;
                        else if (isValidFileType(submissionFile, ".yo")) yoFileCount++;
                    }

                    double submissionPenalty = 1.0;
                    if (sugoFileCount < yoFileCount) submissionPenalty = 0.5;


                    // iterate over TestCases
                    currProblem.testCases.sort(new sortById());
                    ListIterator<TestCase> testCaseIter = currProblem.testCases.listIterator();
                    while(testCaseIter.hasNext()) {
                        TestCase currTestCase = testCaseIter.next();

                        // execute compiled file
                        String targetFilePath = submissionDirPath + submittedDirName + File.separator + currProblem.id + File.separator + executeFileName;
                        String inputFilePath = currProblem.testCasesDirPath + currTestCase.inputFileName;
                        String outputFilePath = submissionDirPath + submittedDirName + File.separator + currProblem.id + File.separator + currTestCase.outputFileName;

                        try {
                            executer.execute(targetFilePath, inputFilePath, outputFilePath);

                            File submissionOutputFile = new File(outputFilePath);
                            File desiredOutputFile = new File(currProblem.testCasesDirPath + currTestCase.outputFileName);

                                // fetch content of submitted output file
                            Scanner outputLines = new Scanner(submissionOutputFile);
                            String submissionOutputString = "";
                            while (outputLines.hasNext()) {
                                String nextLine = outputLines.nextLine();
                                submissionOutputString += nextLine + "\n";
                            }
                            outputLines.close();

                                // fetch content of desired output file
                            outputLines = new Scanner(desiredOutputFile);
                            String desiredOutputString = "";
                            while (outputLines.hasNext()) {
                                String nextLine = outputLines.nextLine();
                                desiredOutputString += nextLine + "\n";
                            }
                            outputLines.close();

                            // Compare the Student's submitted output file & the Testcase output file
                            Boolean noMistakes = true;
                                // Group 2: if judgingTypes set is null or empty, do not tolerate any generous cases
                            if (currProblem.judgingTypes == null || currProblem.judgingTypes.isEmpty()) {
                                noMistakes = testExactEquality(submissionOutputString, desiredOutputString);
                            }

                                // Group 2: if judgingTypes contains at least 1 generous case, iterate through them
                            else {
                                List<String> judgingTypes = getJudgingTypesList(currProblem.judgingTypes);
                                ListIterator<String> judgingTypesIter = judgingTypes.listIterator();

                                while (judgingTypesIter.hasNext()) {
                                    String currJudgingType = judgingTypesIter.next();

                                    if (currJudgingType.equals(Problem.LEADING_WHITESPACES)) {
                                        submissionOutputString = submissionOutputString.stripLeading();
                                        desiredOutputString = desiredOutputString.stripLeading();
                                    }
                                    else if (currJudgingType.equals(Problem.IGNORE_WHITESPACES)) {
                                        submissionOutputString = submissionOutputString.replaceAll("\\s+","");
                                        desiredOutputString = desiredOutputString.replaceAll("\\s+","");
                                    }
                                    else if (currJudgingType.equals(Problem.CASE_INSENSITIVE)) {
                                        submissionOutputString = submissionOutputString.toLowerCase();
                                        desiredOutputString = desiredOutputString.toLowerCase();
                                    }
                                    else if (currJudgingType.equals(Problem.IGNORE_SPECIAL_CHARACTERS)) {
                                        submissionOutputString = submissionOutputString.replaceAll("[^a-zA-Z0-9\\s]", "");
                                        desiredOutputString = desiredOutputString.replaceAll("[^a-zA-Z0-9\\s]", "");
                                    }
                                }
                                noMistakes = testExactEquality(submissionOutputString, desiredOutputString);
                            }
                            // if Group 2 mistake was found, add score 0.0 and move to next TestCase
                            if (!noMistakes) scoreMap.get(currStudent.id).get(currProblem.id).add(0.0);
                            // when no mistakes were found in all corner cases (Group 1~4)
                            else scoreMap.get(currStudent.id).get(currProblem.id).add(currTestCase.score * submissionPenalty);

                        } catch (RunTimeErrorException | InvalidFileTypeException | FileSystemRelatedException e){
                            scoreMap.get(currStudent.id).get(currProblem.id).add(0.0);
                        }
                    }
                } catch (CompileErrorException | InvalidFileTypeException | FileSystemRelatedException e){
                    zeroPointsForProblem(currProblem, currStudent, scoreMap);
                }
                catch (Exception e){
                    zeroPointsForProblem(currProblem, currStudent, scoreMap);
                }
            }
        }
        return scoreMap;
    }

    private List<String> getJudgingTypesList (Set<String> judgingTypes){
        String[] arrayJudgingTypes = judgingTypes.toArray(new String[0]);
        List<String> listJudgingTypes = new LinkedList<>();
        for (String judgingType : arrayJudgingTypes) listJudgingTypes.add(judgingType);

        return listJudgingTypes;
    }

    private void copyFiles(String sourceDirPath, String destDirPath){
        File sourceDir = new File(sourceDirPath);
        File[] sourceDirFiles = sourceDir.listFiles();

        for (File file : sourceDirFiles){
            try{
                // collect the lines to copy, create list
                Scanner input = new Scanner(file);
                List<String> linesToCopy = new LinkedList<>();

                while (input.hasNext()){
                    linesToCopy.add(input.nextLine());
                }
                input.close();

                // iterate through lines and paste into new file in the desired destination
                FileWriter fileWriter = new FileWriter(destDirPath + file.getName());
                for (int line = 0; line < linesToCopy.size(); line++) {
                    if (line == linesToCopy.size()-1){
                        fileWriter.write(linesToCopy.get(line));
                        continue;
                    }

                    fileWriter.write(linesToCopy.get(line) + "\n");
                }
                fileWriter.close();

            } catch (IOException e){

            }
        }
    }

    private void copyFile(File fileToCopy, String fileWriterPath){
        // collect the lines to copy, create list
        try {
            Scanner input = new Scanner(fileToCopy);
            List<String> linesToCopy = new LinkedList<>();

            while (input.hasNext()) {
                linesToCopy.add(input.nextLine());
            }
            input.close();

            // iterate through lines and paste into new file in the desired destination
            FileWriter fileWriter = new FileWriter(fileWriterPath);
            for (int line = 0; line < linesToCopy.size(); line++) {
                if (line == linesToCopy.size() - 1) {
                    fileWriter.write(linesToCopy.get(line));
                    continue;
                }

                fileWriter.write(linesToCopy.get(line) + "\n");
            }
            fileWriter.close();
        }catch(IOException e){

        }
    }


    private List<File> fileArrayToList (File[] fileArray){
        List<File> fileList = new LinkedList<>();
        for (File file : fileArray){
            fileList.add(file);
        }
        return fileList;
    }

    private void deleteDir(File dirToDelete){
        File[] filesInDir = dirToDelete.listFiles();
        for (File file : filesInDir){
            file.delete();
        }
        dirToDelete.delete();
    }

    private boolean containsNecessaryDirOrFile(String targetDirectory_, String targetFileName){
        File targetDirectory = new File(targetDirectory_);
        File[] fileList = targetDirectory.listFiles();
        for (File file : fileList) {
            if (file.getName().equals(targetFileName)) return true;
        }
        return false;
    }

    private boolean isValidFileType(File file, String fileExtention){
        int filenameStartIdx = file.getName().indexOf(".");

        // count occurences of zero in filename
        int zeroOccurence = 0;
        char[] charArray = file.getName().toCharArray();
        for (char c : charArray){
            if (c == '.') zeroOccurence++;
        }

        if (file.getName().substring(filenameStartIdx).equals(fileExtention) && zeroOccurence == 1)
            return true;
        else return false;
    }

    private void zeroPointsForProblem(Problem currProblem, Student currStudent, Map<String, Map<String, List<Double>>> scoreMap){
        ListIterator<TestCase> testCaseIterator = currProblem.testCases.listIterator();
        while (testCaseIterator.hasNext()){
            TestCase currTestCase = testCaseIterator.next();
            scoreMap.get(currStudent.id).get(currProblem.id).add(0.0);
        }
    }

    private boolean testExactEquality(String submissionOutputString, String testCaseOutputString){
        if (submissionOutputString.equals(testCaseOutputString)) return true;
        else return false;
    }
}

class sortById implements Comparator<TestCase>{
    @Override
    public int compare(TestCase tc1, TestCase tc2){
        String id1 = tc1.id;
        String id2 = tc2.id;
        return id1.compareTo(id2);
    }
}

