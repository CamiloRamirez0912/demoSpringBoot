package co.edu.uptc.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import co.edu.uptc.models.PersonModel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PeopleManagerService {

    private final ObjectMapper mapper;
    private Path pathDirectory;

    @Value("${source.file.people}")
    private String pathPeopleFile;

    @Value("${source.file.idPeople}")
    private String pathIdPeople;

    public PeopleManagerService() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        pathDirectory = Paths.get(System.getProperty("user.dir"));
    }

    public void initFile() throws IOException {
        Path filePath = getAbsPathPersons();
        File file = filePath.toFile();

        if (!file.exists()) {
            file.createNewFile();
        }

        if (file.length() == 0) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("[]");
            }
        }
    }

    public void addPerson(PersonModel person) throws IOException {
        initFile();

        RandomAccessFile raf = new RandomAccessFile(getAbsPathPersons().toString(), "rw");
        long fileLength = raf.length();
        int id = getAndUpdateLastId();
        person.setId((long) id);

        raf.seek(fileLength - 1);

        if (fileLength > 2)
            raf.writeBytes(",\n");

        raf.writeBytes(chainObjectJson(person) + "\n]");
        raf.close();
    }

    private int getAndUpdateLastId() throws IOException {
        Path filePath = getAbsPathInfoPersons();
        File file = filePath.toFile();
        Map<String, Integer> idInfo = new HashMap<>();

        if (file.exists() && file.length() > 0) {
            idInfo = mapper.readValue(file, new TypeReference<Map<String, Integer>>() {
            });
        }

        int lastIdPerson = idInfo.getOrDefault("lastId", 0);
        idInfo.put("lastId", lastIdPerson + 1);

        mapper.writeValue(file, idInfo);
        return lastIdPerson;
    }

    private String chainObjectJson(PersonModel person) {
        try {
            return mapper.writeValueAsString(person); 
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getFilePersons() {
        String contentFile = "";
        try {
            String fullContent = Files.readString(getAbsPathPersons());
            List<PersonModel> persons = mapper.readValue(fullContent, new TypeReference<List<PersonModel>>() {
            });
            List<PersonModel> filteredPersons = new ArrayList<>();

            for (PersonModel person : persons) {
                if (!person.isDeleted()) {
                    filteredPersons.add(person);
                }
            }

            contentFile = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filteredPersons);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentFile;
    }

    public PersonModel getPersonById(Long id) throws IOException {
        initFile();
        String fullContent = Files.readString(getAbsPathPersons());
        List<PersonModel> persons = mapper.readValue(fullContent, new TypeReference<List<PersonModel>>() {
        });

        for (PersonModel person : persons) {
            if (person.getId().equals(id) && !person.isDeleted()) {
                return person;
            }
        }

        return null;
    }

    public PersonModel deletePersonById(Long id) throws IOException {
        initFile();
        String fullContent = Files.readString(getAbsPathPersons());
        List<PersonModel> persons = mapper.readValue(fullContent, new TypeReference<List<PersonModel>>() {
        });

        for (PersonModel person : persons) {
            if (person.getId().equals(id) && !person.isDeleted()) {
                person.setDeleted(true);
                mapper.writerWithDefaultPrettyPrinter().writeValue(getAbsPathPersons().toFile(), persons);
                return person;
            }
        }

        return null;
    }

    public List<PersonModel> getLowestSalary() throws IOException {
        initFile();
        String fullContent = Files.readString(getAbsPathPersons());
        List<PersonModel> persons = mapper.readValue(fullContent, new TypeReference<List<PersonModel>>() {
        });

        List<PersonModel> activePersons = new ArrayList<>();
        double lowestSalary = Double.MAX_VALUE;

        for (PersonModel person : persons) {
            if (!person.isDeleted()) {
                activePersons.add(person);
                if (person.getSalary() < lowestSalary) {
                    lowestSalary = person.getSalary();
                }
            }
        }

        List<PersonModel> personsWithLowestSalary = new ArrayList<>();
        for (PersonModel person : activePersons) {
            if (person.getSalary() == lowestSalary) {
                personsWithLowestSalary.add(person);
            }
        }

        return personsWithLowestSalary;
    }

    private Path getAbsPathPersons() {
        return Paths.get(pathDirectory.toString(), pathPeopleFile);
    }

    private Path getAbsPathInfoPersons() {
        return Paths.get(pathDirectory.toString(), pathIdPeople);
    }
}
