package co.edu.uptc.services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.Getter;

import co.edu.uptc.models.PersonModel;

@Service
public class PeopleManagerService {

    @Getter
    private final ObjectMapper mapper;
    private Path pathDirectory;

    @Value("${source.file.people}")
    String pathPeopleFile;

    @Value("${source.file.idPeople}")
    String pathIdPeople;

    public PeopleManagerService(){
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        pathDirectory = Paths.get(System.getProperty("user.dir"));
    }    

    public void addPerson(PersonModel person) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(getAbsPathPersons().toString(), "rw");
        long fileLength = raf.length();
        int id = getAndUpdateLastId();
        person.setId((long) id);

        if (fileLength == 0) {
            // Archivo vacío: se inicia el array JSON
            raf.writeBytes("[\n");
            raf.writeBytes(chainObjectJson(person) + "\n");
            raf.writeBytes("]");
        } else {
            raf.seek(fileLength - 2);
            raf.writeBytes(",\n");
            raf.writeBytes(chainObjectJson(person) + "\n");
            raf.writeBytes("]");
        }
        raf.close();
    }


    private int getAndUpdateLastId() throws IOException {
        int lastIdPerson = 0;
        File fileInfo = new File(getAbsPathInfoPersons().toString());
        JsonNode rootNode = mapper.readTree(fileInfo);
        lastIdPerson = rootNode.get("lastId").asInt();
        ((ObjectNode) rootNode).put("lastId", lastIdPerson + 1);
        mapper.writeValue(fileInfo, rootNode);
        return lastIdPerson;
    }

    private String chainObjectJson(PersonModel person) {
        return "{\n" +
                "  \"id\": " + person.getId() + ",\n" +
                "  \"name\": \"" + person.getName() + "\",\n" +
                "  \"lastName\": \"" + person.getLastName() + "\",\n" +
                "  \"age\": " + person.getAge() + ",\n" +
                "  \"gender\": \"" + person.getGender().toString() + "\"\n" +
                "}";
    }

    public String getFilePersons() {
        String contentFile = "";
        try {
            contentFile = Files.readString(getAbsPathPersons());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return contentFile;
    }

    private Path getAbsPathPersons() {
        return Paths.get(pathDirectory.toString(), pathPeopleFile);
    }

    private Path getAbsPathInfoPersons() {
        return Paths.get(pathDirectory.toString(), pathIdPeople);
    }
}