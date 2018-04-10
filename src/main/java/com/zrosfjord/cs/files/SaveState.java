package com.zrosfjord.cs.files;

import com.zrosfjord.cs.Company;
import com.zrosfjord.cs.Movie;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SaveState implements Serializable {

    private static final transient File dir;

    static {
        dir = new File("/saves");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private Company company;
    private List<Movie> registry;

    public SaveState(Company company) {
        this.company = company;
        this.registry = Movie.REGISTRY;
    }

    public File recordState() throws IOException {
        File result = new File(dir, company.getName().toUpperCase() + ".cts");
        if (!result.exists() || result.isDirectory()) {
            result.delete();
            result.createNewFile();
        }

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(result));

        oos.writeObject(this);
        oos.close();

        return result;
    }

    public static SaveState getState(String company) throws IOException, ClassNotFoundException {
        File f = new File(dir, company.toUpperCase() + ".cts");
        if (!f.exists())
            return null;

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        SaveState result = (SaveState) ois.readObject();
        ois.close();

        return result;
    }

    public static boolean delete(String name) {
        File f = new File(dir, name.toUpperCase() + ".cts");
        return f.delete();
    }

    public static List<String> getSaves() {
        return Arrays.stream(dir.listFiles())
                .filter(f -> f.getName().endsWith(".cts"))
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public Company getCompany() {
        return company;
    }

    public List<Movie> getRegistry() {
        return registry;
    }

}
