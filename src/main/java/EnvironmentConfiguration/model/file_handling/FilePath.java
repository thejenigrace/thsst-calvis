package EnvironmentConfiguration.model.file_handling;

/**
 * Created by Ivan on 1/27/2016.
 */
public class FilePath {
    private String extension;
    private String name;
    private String location;

    public FilePath(String location, String name, String extension){
        this.location = location;
        this.name = name;
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
