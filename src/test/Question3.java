package test;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class Question3 {
        public static void main(String[] args) {
                String filename = args[0];
                File file = new File(filename);
                boolean display = true;
                List<String> filesList = new ArrayList<String>();
                if (file.isFile()) {
                        filesList.add(file.getAbsolutePath());
                } else if (file.isDirectory()) {
                        getAllFilesPaths(file, filesList);
                } else {
                        System.out.println("Given file doesn't exist!");
                        display = false;
                }
                Set<String> uniqueFiles = new HashSet<String>();
                List<String> uniqueFilePaths = new ArrayList<String>();
                int blanklines = 0, comments = 0, codelines = 0;
                String line;
                for (String filepath : filesList) {
                        MessageDigest md = null;
                        try {
                                md = MessageDigest.getInstance("MD5");
                        } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                        }
                        try {
                                md.update(Files.readAllBytes(Paths.get(filepath)));
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        byte[] digest = md.digest();
                        //String checkSum = DatatypeConverter.printHexBinary(digest).toUpperCase();
                        BigInteger bi = new BigInteger(1, digest);
                        String checkSum = bi.toString(16);
                        if (checkSum.length() < 32) {
                                int count = 32 - checkSum.length();
                                StringBuilder builder = new StringBuilder();
                                while (count > 0) {
                                        builder.append("0");
                                        count--;
                                }
                                checkSum = builder.toString() + checkSum;
                        }
                        if (!uniqueFiles.contains(checkSum)) {
                                uniqueFiles.add(checkSum);
                                uniqueFilePaths.add(filepath);
                                file = new File(filepath);
                                BufferedReader br = null;
                                try {
                                        br = new BufferedReader(new FileReader(file));
                                } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                }
                                try {
                                        while ((line = br.readLine()) != null) {
                                                line = line.trim();
                                                if (line.length() == 0)
                                                        blanklines++;
                                                else if (line.startsWith("//"))
                                                        comments++;
                                                else if (line.startsWith("/*")) {
                                                        if (line.endsWith("*/"))
                                                                comments++;
                                                        else {
                                                                comments++;
                                                                while ((line = br.readLine()) != null) {
                                                                        line = line.trim();
                                                                        if (line.length() == 0) {
                                                                                blanklines++;
                                                                        } else if (line.endsWith("*/")) {
                                                                                comments++;
                                                                                break;
                                                                        } else {
                                                                                comments++;
                                                                        }
                                                                }
                                                        }
                                                } else
                                                        codelines++;
                                        }
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
                }
                if(display)
                System.out.println(filesList.size() + "-" + uniqueFiles.size() + "-" + blanklines + "-" + comments + "-" + codelines);
        }

        public static void getAllFilesPaths(File dir, List<String> list) {
                for (File f : dir.listFiles()) {
                        if (f.isFile()) {
                                if (f.getName().endsWith(".java"))
                                        list.add(f.getAbsolutePath());
                        }
                        if (f.isDirectory()) {
                                getAllFilesPaths(f, list);
                        }
                }
        }
}