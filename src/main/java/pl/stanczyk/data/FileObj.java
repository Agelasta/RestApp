package pl.stanczyk.data;

public class FileObj extends Response {

    private String name;
    private String content;

    public FileObj(String name, String info, String content) {
        this.name = name;
        this.info = info;
        this.content = content;
    }

    public FileObj() {}

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getInfo() {
        return info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
