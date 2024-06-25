package com.example.dataprocessing.common;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class TarFileProcessor {

    public void extractTarFile(String tarFilePath, String destDirPath) throws IOException {
        File destDir = new File(destDirPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        try (TarArchiveInputStream tarInput = new TarArchiveInputStream(new FileInputStream(tarFilePath))) {
            TarArchiveEntry entry;

            while ((entry = tarInput.getNextTarEntry()) != null) {
                File destPath = new File(destDir, entry.getName());

                if (entry.isDirectory()) {
                    destPath.mkdirs();
                } else {
                    destPath.getParentFile().mkdirs();
                    try (FileOutputStream out = new FileOutputStream(destPath)) {
                        IOUtils.copy(tarInput, out);
                    }
                }
            }
        }
    }

    public void cleanUpExtractedFiles(String extractedDirPath) throws IOException {
        File extractedDir = new File(extractedDirPath);
        if (extractedDir.exists()) {
            for (File file : extractedDir.listFiles()) {
                if (file.isDirectory()) {
                    if (file.getName().endsWith("-SUPP")) {
                        deleteDirectory(file);
                    } else {
                        for (File innerFile : file.listFiles()) {
                            if (!innerFile.getName().equals("DESIGN")) {
                                deleteDirectory(innerFile);
                            }
                        }

                        // DESIGN 폴더 내 ZIP 파일들을 모두 해제합니다.
                        File designDir = new File(file, "DESIGN");
                        if (designDir.exists() && designDir.isDirectory()) {
                            for (File zipFile : designDir.listFiles((dir, name) -> name.endsWith(".zip"))) {
                                unzipFile(zipFile.getAbsolutePath(), designDir.getAbsolutePath());
                            }
                        }
                    }
                }
            }
        }
    }

    private void unzipFile(String zipFilePath, String destDirPath) throws IOException {
        try (ZipInputStream zipInput = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipInput.getNextEntry()) != null) {
                File destPath = new File(destDirPath, entry.getName());
                if (entry.isDirectory()) {
                    destPath.mkdirs();
                } else {
                    destPath.getParentFile().mkdirs();
                    try (FileOutputStream out = new FileOutputStream(destPath)) {
                        IOUtils.copy(zipInput, out);
                    }
                }
            }
        }
    }

    private void deleteDirectory(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                deleteDirectory(subFile);
            }
        }
        file.delete();
    }
}