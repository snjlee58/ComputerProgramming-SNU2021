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

        return null;
    }

    public Map<String,Map<String, List<Double>>> gradeRobust(ExamSpec examSpec, String submissionDirPath) {
        // TODO Problem 1-2

        return null;
    }

}


