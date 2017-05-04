package com.mulesoft.tools.migration;

import org.jdom2.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mulesoft.tools.migration.helper.DocumentHelper.getDocument;
import static com.mulesoft.tools.migration.helper.DocumentHelper.restoreTestDocument;

public class MigrationJobHttpTest {
    private MigrationJob migrationJob;
    private Document docRestoreFile;
    private String USE_CASE_FILE_PATH = "src/test/resources/mule/examples/http/http-all-use-case.xml";
    private String TASKS_FILE_DIR = "src/test/resources/mule/tasks/http";

    @Before
    public void setUp() throws Exception {
        ArrayList<String> filePath1 = new ArrayList<String>();
        filePath1.add(USE_CASE_FILE_PATH);

        migrationJob = new MigrationJob();
        migrationJob.setDocuments(filePath1);
        migrationJob.setBackUpProfile(Boolean.FALSE);
        docRestoreFile = getDocument(filePath1.get(0));
    }

    @Test
    public void jobWithTasksOnConfigFile() throws Exception {
        ArrayList<String> files = new ArrayList<>(Arrays.asList(USE_CASE_FILE_PATH));

        migrationJob.setDocuments(files);
        migrationJob.setConfigFileDir(TASKS_FILE_DIR);
        migrationJob.execute();
    }

    @After
    public void restoreFileState() throws Exception {
        restoreTestDocument(docRestoreFile,USE_CASE_FILE_PATH);
    }
}
