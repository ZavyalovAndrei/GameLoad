package com.zavyalov;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Main {
    private static final String ZIP_FILE = "D://Games/savegames/FF8/Packed_saves.zip";
    private static final String SAVE_DIRECTORY = "D://Games/savegames/FF8";
    static GameProgress gameProgress = null;

    public static void main(String[] args) {
        String saveFile = choseSave(openZip(ZIP_FILE, SAVE_DIRECTORY));
        System.out.println(loadGame(saveFile, SAVE_DIRECTORY));
    }

    private static List<String> openZip(String zip, String directory) {
        List<String> listSaves = new ArrayList<>();
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zip))) {
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                listSaves.add(entry.getName());
                FileOutputStream fout = new FileOutputStream(directory + "/" + entry.getName());
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return listSaves;
    }

    private static String choseSave(List<String> listSaves) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите номер сохранения для загрузки.");
        for (int i = 0; i < listSaves.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + listSaves.get(i));
        }
        String message = "Введен неправильный номер. Попробуйте снова.";
        while (true) {
            try {
                int enter = Integer.parseInt(scanner.nextLine()) - 1;
                if (enter >= 0 && enter < listSaves.size()) {
                    return listSaves.get(enter);
                } else {
                    System.out.println(message);
                }
            } catch (NumberFormatException err) {
                System.out.println(message);
            }
        }
    }

    private static String loadGame(String saveFile, String saveDirectory) {
        try (FileInputStream fis = new FileInputStream(saveDirectory + "/" + saveFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress.toString();
    }
}


